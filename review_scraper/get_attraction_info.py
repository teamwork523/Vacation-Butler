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
    place["review_url"] = review_urls[name_index]
    if length_of_visit == []:
        place["recommended_length_of_visit"] = "NA"
    else:
        length_of_visit_str = str(length_of_visit[0])
        if "hour" in length_of_visit_str:
            place["recommended_length_of_visit"] = length_of_visit_str
        else:
            place["recommended_length_of_visit"] = "NA"
    city_name = tree.xpath('//a[contains(@onclick, "ta.setEvtCookie(\'Breadcrumbs\', \'click\', \'City\'")]')
    if city_name == []:
        place["city"] = "NA"
    else:
        place["city"] = city_name[0].find('span').text
    country_name = tree.xpath('//a[contains(@onclick, "ta.setEvtCookie(\'Breadcrumbs\', \'click\', \'Country\'")]')
    if country_name == []:
        place["country"] = "NA"
    else:
        place["country"] = country_name[0].find('span').text
    region_name = tree.xpath('//a[contains(@onclick, "ta.setEvtCookie(\'Breadcrumbs\', \'click\', \'State\'")]')
    if region_name == []:
        region_name = tree.xpath('//a[contains(@onclick, "ta.setEvtCookie(\'Breadcrumbs\', \'click\', \'Province\'")]')
        if region_name == []:
            place["region"] = "NA"
        else:
            place["region"] = region_name[0].find('span').text
    else:
        place["region"] = region_name[0].find('span').text
    categories = tree.xpath('//div[@class="heading_details"]//a')
    full_cat = ''
    for category in categories:
        if full_cat != '':
            full_cat = full_cat +';' + category.text
        else:
            full_cat = category.text
    place["category"] = full_cat
    google_query = "https://www.google.com/search?q=" + names[name_index]
    soup = BeautifulSoup(opener.open(google_query).read())
    opening_hours = soup.find_all("span", {"class" : "loht__open-interval"})
    if opening_hours == []:
        place["hours"] = "NA"
    else:
        place["hours"] = "Sunday: " + opening_hours[0].text + " Monday: " + opening_hours[1].text + " Tuesday: " + opening_hours[2].text + " Wednesday: " + opening_hours[3].text + " Thursday: " + opening_hours[4].text + " Friday: " + opening_hours[5].text + " Saturday: " + opening_hours[6].text
    final_list.append(place)
    #time.sleep(1) #to avoid blacklist

json.dump(final_list, outfile)
json_data.close()
outfile.close()
