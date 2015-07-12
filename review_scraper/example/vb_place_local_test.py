#!/usr/bin/env python

import urllib
import urllib2
import json
import sys

def Usage():
    head_space = len(sys.argv[0]) * " "
    print sys.argv[0] + " option"
    print head_space + " -c: create test places [Default]"
    print head_space + " -d: delete test places"

def queryAWS(url, data):
    HTTP = "http://"
    req = urllib2.Request(HTTP + urllib.quote(url))
    req.add_header('Content-Type', 'application/json')

    try:
        response = urllib2.urlopen(req, json.dumps(data)).read()
    except urllib2.HTTPError, e:
        if e.getcode() == 500 or e.getcode() == 400:
            response = e.read()
        else:
            raise

    return "HTTP " + str(e.getcode()) + " : " + response

def main():
    if (len(sys.argv) == 2 and sys.argv[1] == "-h"):
        Usage()
        sys.exit(1)
    
    option = "-c"
    if (len(sys.argv) == 2):
        option = sys.argv[1]

    domain = "dev-env-xwauaaztim.elasticbeanstalk.com"
    test_place_name = "Test Place "
    total_test_places_count = 10
    
    # Create test places
    if (option == "-c"):
        create_place_url = domain + "/api/place/createplace"
        data = {
            "Activities": "NA",
            "Categories" : "Speciality Museums;Museums", 
            "City Name" : "Test City",
            "City ID" : 1,
            "Hours": "Sun 13:00 - 17:00; Mon - Wed 10:00 - 17:00; Thu 10:00 - 21:00; Fri - Sat 10:00 - 17:00; ",
            "Latitude" : 49.692493,
            "Longitude" : -112.8467, 
            "Number of Reviews": 61,
            "Place Name" : test_place_name,
            "Phone Number" : "403-320.3898", 
            "Rating": 4.0,    
            "Recommended Length of Visit": "NA", 
            "Review URL": "/Attraction_Review-g154919-d1095898-Reviews-Galt_Museum_Archives-Lethbridge_Alberta.html", 
            "Street Address": "502 - 1st Street South",
            "Zip Code": "T1J 0P6" 
        }
        for i in range(total_test_places_count):
            data["Place Name"] = test_place_name + str(i)
            rs = queryAWS(create_place_url, data)
            print rs
    # Remove all the places
    elif (option == "-d"):
        request_place_url = domain + "/api/place/readplaces/keyword/"
        data = {
            "is Partial Matched": "false"
        }
        for i in range(total_test_places_count):
            rs = queryAWS(request_place_url+test_place_name+str(i), data)
            print rs

if __name__ == "__main__":
    main()
