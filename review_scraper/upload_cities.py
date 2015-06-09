import json
import urllib2
import time

url = "http://dev-env-xwauaaztim.elasticbeanstalk.com/api/city/createcity"

with open('list_of_cities.txt', 'r') as city_file:
    for line in city_file:
        city_dict = {}
        city_split_list = line.split(';',2)
        city_dict['City Name'] = city_split_list[2].strip()
        city_dict['State Name'] = city_split_list[1].strip()
        city_dict['Country Name'] = city_split_list[0].strip()
        try:
            req = urllib2.Request(url)
            req.add_header('Content-Type', 'application/json')
            response = urllib2.urlopen(req, json.dumps(city_dict)).read()
        except urllib2.HTTPError, e:
            if e.getcode() == 500 or e.getcode() == 400:
                response = e.read()
            else:
                raise
        print response
