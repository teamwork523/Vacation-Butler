import json
import urllib
import urllib2
import string
from lxml import etree
import re
from types import NoneType
from bs4 import BeautifulSoup
import time

json_data=open('processed_data.json')

data = json.load(json_data)
names = []
review_urls = []
for num in range(len(data)):
    names.append(urllib.quote_plus(data[num]["name"][0].encode("utf-8"))) #make strings URL safe
    review_urls.append(data[num]["review_url"][0])

opener = urllib2.build_opener()
opener.addheaders = [('User-Agent', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10) AppleWebKit/600.1.25 (KHTML, like Gecko) Version/8.0 Safari/600.1.25')]
final_list = []
outfile = open("attraction_info.json", "w")
for name_index in range(len(names)):
    query_url = "http://www.tripadvisor.ca" + review_urls[name_index]
    htmlparser = etree.HTMLParser()
    tree = etree.parse(opener.open(query_url), htmlparser)
    result = tree.xpath('//b[text() = "Recommended length of visit:"]/following-sibling::text()')
    place = {}
    place["name"] = data[name_index]["name"][0]
    if result == []:
        place["recommended_length_of_visit"] = "NA"
    else:
        result_str = str(result[0])
        if "hour" in result_str:
            place["recommended_length_of_visit"] = result_str
        else:
            place["recommended_length_of_visit"] = "NA"
    google_query = "https://www.google.com/search?q=" + names[name_index]
    soup = BeautifulSoup(opener.open(google_query).read())
    result = soup.find_all("span", {"class" : "loht__open-interval"})
    if result == []:
        place["hours"] = "NA"
    else:
        place["hours"] = "Sunday: " + result[0].text + " Monday: " + result[1].text + " Tuesday: " + result[2].text + " Wednesday: " + result[3].text + " Thursday: " + result[4].text + " Friday: " + result[5].text + " Saturday: " + result[6].text
    final_list.append(place)
    #time.sleep(1) #to avoid blacklist

json.dump(final_list, outfile)
json_data.close()
outfile.close()
