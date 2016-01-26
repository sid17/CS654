from django.db import models
from djangotoolbox.fields import ListField,DictField
from django.db.models.signals import post_save
from datetime import datetime


class CachedValues(models.Model):
    inputStr = models.TextField()
    result = models.FloatField()
    created_at = models.DateTimeField(default=datetime.now())

    def to_json(self):
        return {"_id":self.id,
            "inputStr":self.inputStr,
            "result" : models.result
            }

    class Meta:
        db_table = 'calculatorCacheNew'
        get_latest_by = 'created_at'
