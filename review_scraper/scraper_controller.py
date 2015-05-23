import csv
import get_urls
import sys
import os
import shutil
import remove_duplicate_json

option1_file_path = "./state_and_province_urls/"
option2_file_path = "./attraction_urls/"
option3_file_path = "./scraped_data/"
state_and_province_csv = "states_and_provinces.csv"
city_url_temp = "city_urls.json"
removed_duplicate = "processed_data.json"
scrapy_command = "scrapy crawl myspider -o data.json"

print "1 - get state and province urls"
print "2 - get attraction urls"
print "3 - scrape data"
selected_option = int(raw_input("Please select an option: "))
if selected_option < 1 or selected_option > 3:
    print "Please enter a number 1-3"
    sys.exit()
elif selected_option == 1:
    states_selection = []
    with open(state_and_province_csv, 'r') as csvfile:
        states_url = csv.reader(csvfile)
        for state in states_url:
            if(len(state)==3):
                states_selection.append(state)
    number_of_states = len(states_selection)
    for i in range(number_of_states):
        file_name = states_selection[i][0] + "_" + states_selection[i][1] + ".json"
        if os.path.isfile(option1_file_path + file_name):
            print file_name + " already exists"
        else:
            get_urls.get_cities(states_selection[i][0],states_selection[i][1],states_selection[i][2], option1_file_path)
elif selected_option == 2:
    file_list = [f for f in os.listdir(option1_file_path) if os.path.isfile(option1_file_path + f)]
    print "number of state/province urls: " + str(len(file_list))
    for f in file_list:
        if os.path.isfile(option2_file_path + f): # skip if the attraction url file already exists
            print "skipped " + option2_file_path + f
            continue
        if os.path.isfile(city_url_temp): #if temp files already exist, delete them
            os.remove(city_url_temp)
        if os.path.isfile(removed_duplicate):
            os.remove(removed_duplicate)
        shutil.copyfile(option1_file_path + f, city_url_temp)
        os.system(scrapy_command)
        remove_duplicate_json.remove_duplicate_lines()
        shutil.copyfile(removed_duplicate, option2_file_path+f)