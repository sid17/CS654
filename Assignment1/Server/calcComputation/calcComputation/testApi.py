import json
import yaml
import urllib,requests
import ast
def PostQuery(operator='plus',operand1='2',operand2='3'):
		query=dict(operator=operator,operand1=operand1,operand2=operand2)
		myport=8080
		data=json.dumps(query)
		myURL = "http://127.0.0.1:%s/compute/getCachedResult/" % (myport) + "?operator='plus'&operand1='2'&operand2='3'"
		print myURL
		headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
		print data
		r = requests.get(myURL, data=data,headers=headers)
		response=yaml.safe_load(r.text)
		return response

if __name__ == '__main__':
	print PostQuery()