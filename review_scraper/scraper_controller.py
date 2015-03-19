import csv
import get_urls
import sys
states_selection = []
with open('states_and_provinces.csv', 'r') as csvfile:
	states_url = csv.reader(csvfile)
	for state in states_url:
		if(len(state)==3):
			states_selection.append(state)
number_of_states = len(states_selection)
count = 0
for i in states_selection:
	print i[1]+" #"+ str(count)
	count += 1
selected_state = int(raw_input("Please select a state: "))
if selected_state < 0 or selected_state >= number_of_states :
	print "Please enter a number 0-" + str(number_of_states-1)
	sys.exit()
else:
	print "You selected " + states_selection[selected_state][1]
get_urls.get_cities(states_selection[selected_state][1],states_selection[selected_state][2])
