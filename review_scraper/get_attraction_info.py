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
    length_of_visit = tree.xpath('//b[text() = "Recommended length of visit:"]/following-sibling::text()')
    place = {}
    place["name"] = data[name_index]["name"][0]
    if length_of_visit == []:
        place["recommended_length_of_visit"] = "NA"
    else:
        length_of_visit_str = str(length_of_visit[0])
        if "hour" in length_of_visit_str:
            place["recommended_length_of_visit"] = length_of_visit_str
        else:
            place["recommended_length_of_visit"] = "NA"
    city_name = tree.xpath('//b[text() = "Recommended length of visit:"]/following-sibling::text()')
    google_query = "https://www.google.com/search?q=" + names[name_index]
    soup = BeautifulSoup(opener.open(google_query).read())
    length_of_visit = soup.find_all("span", {"class" : "loht__open-interval"})
    if length_of_visit == []:
        place["hours"] = "NA"
    else:
        place["hours"] = "Sunday: " + length_of_visit[0].text + " Monday: " + length_of_visit[1].text + " Tuesday: " + length_of_visit[2].text + " Wednesday: " + length_of_visit[3].text + " Thursday: " + length_of_visit[4].text + " Friday: " + length_of_visit[5].text + " Saturday: " + length_of_visit[6].text
    final_list.append(place)
    #time.sleep(1) #to avoid blacklist

json.dump(final_list, outfile)
json_data.close()
outfile.close()
