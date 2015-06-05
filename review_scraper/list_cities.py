import json
import os

option3_file_path = "./scraped_data/"
file_list = [f for f in os.listdir(option3_file_path) if os.path.isfile(option3_file_path + f)]
print "number of states/provinces: " + str(len(file_list))
unique_cities = set() # a set of unique city strings
for f in file_list:
    if f.endswith('.json') == False: #if it is not a json file, skip
        continue
    json_data=open(option3_file_path + f)
    data = json.load(json_data)
    for num in range(len(data)):
        country_region_city = data[num]['country'] + ';' + data[num]['region'] + ';' + data[num]['city']
        unique_cities.add(country_region_city)
city_list = sorted(list(unique_cities)) # convert the set to a list
output_file = open('list_of_cities.txt','w')
for city in city_list:
    output_file.write(city + '\n')
output_file.close()