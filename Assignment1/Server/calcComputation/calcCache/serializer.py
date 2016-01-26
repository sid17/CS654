from django.forms import widgets
from rest_framework import serializers
from models import CachedValues
from djangotoolbox.fields import ListField,DictField
import yaml
import drf_compound_fields.fields as drf
from datetime import datetime

	
class CachedValuesSerializer(serializers.Serializer):
	pk = serializers.Field()  # Note: `Field` is an untyped read-only field.
	inputStr=serializers.CharField(required=True)
	result = serializers.FloatField(required=True)
	created_at = serializers.DateTimeField(required=False)


	def restore_object(self, validated_data, instance=None):
		"""
		Create or update a new snippet instance, given a dictionary
		of deserialized field values.

		Note that if we don't define this method, then deserializing
		data will simply return a dictionary of items.
		"""
		if instance:
			return instance

		validated_data['created_at']=datetime.now()
		return CachedValues(**validated_data)
