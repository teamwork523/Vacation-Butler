import json
import urllib
import urllib2
from bs4 import BeautifulSoup
import re
from types import NoneType

json_data=open('processed_data.json')

data = json.load(json_data)
names = []
for num in range(len(data)):
    names.append(urllib.quote_plus(data[num]["name"][0])) #make strings URL safe

opener = urllib2.build_opener()
opener.addheaders = [('User-Agent', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10) AppleWebKit/600.1.25 (KHTML, like Gecko) Version/8.0 Safari/600.1.25')]

with open("opening_hours.json", "w") as outfile:
    for name_index in range(len(names)):
        google_query = "https://www.google.com/search?q=" + names[name_index]
        soup = BeautifulSoup(opener.open(google_query).read())
        result = soup.find_all("span", {"class" : "loht__open-interval"})
        place = {}
        place["name"] = data[name_index]["name"][0]
        if result == []:
            place["hours"] = "NA"
        else:
            place["hours"] = "Sunday: " + result[0].text + " Monday: " + result[1].text + " Tuesday: " + result[2].text + " Wednesday: " + result[3].text + " Thursday: " + result[4].text + " Friday: " + result[5].text + " Saturday: " + result[6].text
        json.dump(place, outfile)

json_data.close()
