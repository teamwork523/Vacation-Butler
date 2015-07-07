import json
import urllib2
import re
import os
import time
import pickle

read_city_id_url = "http://dev-env-xwauaaztim.elasticbeanstalk.com/api/city/readcitybyname/"
upload_place_url = "http://dev-env-xwauaaztim.elasticbeanstalk.com/api/place/createplace"
option3_file_path = "./scraped_data/"
already_processed_places_file_name = "already_processed_places.txt"
already_processed_places_set = set()
if os.path.isfile(already_processed_places_file_name):#if the file exists
    already_processed_places_file = open(already_processed_places_file_name,'rb')
    place_already_processed_list = pickle.load(f)
    already_processed_places_set = set(place_already_processed_list) #this set prevents duplicate uploads. It's a set of review urls since they are unique
    already_processed_places_file.close()

if len(already_processed_places_set) > 0:
    print "number of places already uploaded: ", len(already_processed_places_set)
file_list = [f for f in os.listdir(option3_file_path) if os.path.isfile(option3_file_path + f)]
for f in file_list:
    if f.endswith('.json') == False: #if it is not a json file, skip
        continue
    json_data=open(option3_file_path + f)
    data = json.load(json_data)
    for num in range(len(data)):
        if data[num]["review_url"] in already_processed_places_set:
            continue #the place has already been uploaded, skip
        city_name = data[num]['city']
        # get city id from database
        try:
            city_id_response = urllib2.urlopen(read_city_id_url + city_name.replace(' ', '_'))
        except urllib2.HTTPError, e:#fail to get city id
            print data[num]['city'], 'failed to get city id'
            print e.read()
            raise
        city_data_json = json.load(city_id_response)
        list_of_cities = city_data_json["Cities"]
        city_id = 0
        for i in range(len(list_of_cities)):
            if list_of_cities[i]["State Name"] == data[num]['region'] and list_of_cities[i]["Country Name"] == data[num]['country']:
                city_id = list_of_cities[i]["City ID"]
        if city_id == 0:#no match
            raise ValueError('Cannot find city ID')
        place_dict = {}
        place_dict["Activities"] = data[num]["activities"]
        place_dict["Categories"] = data[num]["category"]
        place_dict["City Name"] = data[num]["city"]
        place_dict["City ID"] = city_id
        place_dict["Hours"] = data[num]["hours"]
        if data[num]["latitude"] == "NA":
            place_dict["Latitude"] = 0
        else:
            place_dict["Latitude"] = float(data[num]["latitude"])
        if data[num]["longitude"] == "NA":
            place_dict["Longitude"] = 0
        else:
            place_dict["Longitude"] = float(data[num]["longitude"])
        if data[num]["number_of_reviews"] == "NA":
            place_dict["Number of Reviews"] = 0
        else:
            place_dict["Number of Reviews"] = float(data[num]["number_of_reviews"].replace(',',''))
        place_dict["Place Name"] = data[num]["name"]
        print data[num]["name"], data[num]["city"], str(city_id)
        place_dict["Phone Number"] = data[num]["phone_number"]
        if data[num]["rating"] == "NA":
            place_dict["Rating"] = 0
        else:
            place_dict["Rating"] = float(data[num]["rating"].strip()[0:2])
        place_dict["Recommended Length of Visit"] = data[num]["recommended_length_of_visit"]
        place_dict["Review URL"] = data[num]["review_url"]
        place_dict["Street Address"] = data[num]["street_address"]
        place_dict["Zip Code"] = data[num]["zipcode"]
        try: #upload place
            req = urllib2.Request(upload_place_url)
            req.add_header('Content-Type', 'application/json')
            response = urllib2.urlopen(req, json.dumps(place_dict)).read()
            already_processed_places_set.add(data[num]["review_url"])#success, add to the set
        except urllib2.HTTPError, e:
            if e.getcode() == 500 or e.getcode() == 400:
                response = e.read()
                if 'null' not in response:
                    already_processed_places_set.add(data[num]["review_url"])#the place is already in database, add to the set
                else: #failed to upload, server returned place id: null
                    already_processed_places_file = open(already_processed_places_file_name,'wb')
                    place_already_processed_list = list(already_processed_places_set) #convert the set to list
                    pickle.dump(place_already_processed_list, already_processed_places_file)
                    already_processed_places_file.close()
                    print e.read()
                    raise
            else:
                already_processed_places_file = open(already_processed_places_file_name,'wb')
                place_already_processed_list = list(already_processed_places_set) #convert the set to list
                pickle.dump(place_already_processed_list, already_processed_places_file)
                already_processed_places_file.close()
                print e.read()
                raise
        print response
        time.sleep(0.5)

already_processed_places_file = open(already_processed_places_file_name,'wb')
place_already_processed_list = list(already_processed_places_set) #convert the set to list
pickle.dump(place_already_processed_list, already_processed_places_file)
already_processed_places_file.close()
