import csv
import get_urls
import sys
import os.path
option1_file_path = "./state_and_province_urls/"
option2_file_path = "./attraction_urls/"
option3_file_path = "./scraped_data/"
state_and_province_csv = "states_and_provinces.csv"

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
