#!/usr/bin/env python

import urllib
import urllib2
import json

url = "http://dev-env-xwauaaztim.elasticbeanstalk.com/api/city/createcity"
data = {
    'City Name' : 'Seattle',
    'State Name' : 'Washington',
    'Country Name' : 'United States'
}

req = urllib2.Request(url)
req.add_header('Content-Type', 'application/json')

try:
    response = urllib2.urlopen(req, json.dumps(data))
except urllib2.HTTPError, e:
    if e.getcode() == 500 or e.getcode() == 400:
        response = e.read()
    else:
        raise

print response
