# Create your views here.
from rest_framework import status
from django.http import HttpResponse
from calcCache.models import CachedValues
from rest_framework.response import Response
from serializer import CachedValuesSerializer
import json
from django.core import serializers
from django.db.transaction import commit_on_success
import yaml
from calcExpressionStr import eval_expr

def getResult(inputVal):
	result="Math Error"
	try:
		result=eval_expr(inputVal)
	except Exception, e:
		pass
	print result
	return result
		
def return_cached_results(request):
	inputVal=request.GET.get('input','')
	data=dict()
	data['inputStr']=inputVal
	print inputVal
	result=CachedValues()
	objList=result.__class__.objects.all().filter(inputStr=inputVal)
	if (len(objList)>0):
		data['result']=objList[0].result
		return HttpResponse(json.dumps(data), content_type="application/json")
	else:
		data['result']=getResult(inputVal)
		json_data=json.dumps(data)
		data=yaml.safe_load(json_data)
		if (data['result']=="Math Error"):
			return HttpResponse(json.dumps(data), content_type="application/json")	
		else:
			serializer = CachedValuesSerializer(data=data)
			if serializer.is_valid():
				serializer.save()
				return HttpResponse(json.dumps(data), content_type="application/json")	
			return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
					   