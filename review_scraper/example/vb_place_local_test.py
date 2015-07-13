#!/usr/bin/env python

import urllib
import urllib2
import json
import sys
import ast

def Usage():
    head_space = len(sys.argv[0]) * " "
    print sys.argv[0] + " option count[Default:10]"
    print head_space + " -c: create test places [Default]"
    print head_space + " -d: delete test places"
    print head_space + " -q: query test places"

def queryAWS(url, data, method):
    HTTP = "http://"
    opener = urllib2.build_opener(urllib2.HTTPHandler)
    request = urllib2.Request(HTTP + urllib.quote(url), data=json.dumps(data))
    request.add_header('Content-Type', 'application/json')
    request.get_method = lambda: method

    http_code = 200

    try:
        response = opener.open(request).read()
    except urllib2.HTTPError, e:
        http_code = e.getcode()
        if e.getcode() == 500 or e.getcode() == 400:
            response = e.read()
        else:
            raise
    
    return (http_code, response)

def main():
    if (len(sys.argv) == 2 and sys.argv[1] == "-h"):
        Usage()
        sys.exit(1)
    
    option = "-c"
    if (len(sys.argv) >= 2):
        option = sys.argv[1]
    total_test_places_count = 10
    if (len(sys.argv) >= 3):
        total_test_places_count = int(sys.argv[2])
    domain = "dev-env-xwauaaztim.elasticbeanstalk.com"
    test_place_name = "Test Place "

    
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
            print "#" * 80
            print "Working on place " + str(i)
            data["Place Name"] = test_place_name + str(i)
            rs = queryAWS(create_place_url, data, "POST")
            print rs
    # Query all the places
    elif (option == "-q"):
        request_place_url = domain + "/api/place/readplaces/keyword/"
        data = {
            "is Partial Matched": False
        }
        for i in range(total_test_places_count):
            print "#" * 80
            print "Working on place " + str(i)
            http_code, rs = queryAWS(request_place_url+test_place_name+str(i), data, "POST")
            print "HTTP " + str(http_code) + ": " + rs
    # Remove all the places
    elif (option == "-d"):
        request_place_url = domain + "/api/place/readplaces/keyword/"
        delete_place_url = domain + "/api/place/deleteplacebyplaceid/"
        query_data = {
            "is Partial Matched": "false"
        }
        for i in range(total_test_places_count):
            print "#" * 80
            print "Working on place " + str(i)
            query_http_code, query_rs = queryAWS(request_place_url+test_place_name+str(i), query_data, "POST")
            print "HTTP " + str(query_http_code) + ": " + query_rs
            d = json.loads(query_rs)
            delete_http_code, delete_rs = queryAWS(delete_place_url+str(d["Places"][0]["Place ID"]), "", "DELETE")
            print "HTTP " + str(delete_http_code) + ": " + delete_rs

if __name__ == "__main__":
    main()
