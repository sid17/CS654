from django.conf.urls import patterns, url
from calcCache import views

urlpatterns = patterns('',
    url(r'getCachedResult/', views.return_cached_results, name='cached_results'),
)

