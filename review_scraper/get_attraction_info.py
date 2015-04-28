import json
import urllib
import urllib2
import string
from lxml import etree
import re
from types import NoneType
from bs4 import BeautifulSoup
import time
import sys
import os

num_entries_before_sleep = 15
sleep_time = 5
output_file_name = "attraction_info.json"

json_data=open('processed_data.json')

data = json.load(json_data)
names = []
review_urls = []
for num in range(len(data)):
    names.append(urllib.quote_plus(data[num]["name"][0].encode("utf-8"))) #make strings URL safe
    review_urls.append(data[num]["review_url"][0])

opener = urllib2.build_opener()
opener.addheaders = [('User-Agent', 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0'),('Accept-Language', 'en-US,en;q=0.5'),('Accept-Encoding', 'gzip, deflate')]

existing_set_url = set() # create a set of existing url to avoid duplicate queries
count_exist = 0
final_list = [] # this will store the final result
if os.path.isfile(output_file_name): #if the file already exists
    outfile = open("attraction_info.json", "r")
    final_list = json.load(outfile) # resume downloads
    if len(final_list) > 0:
        for item in final_list:
            existing_set_url.add(item["review_url"])
            count_exist += 1
        print "number of existing entries: " + str(count_exist)
    outfile.close() #close the read file

outfile = open("attraction_info.json", "w")
sleep_count = 0;
try:
    for name_index in range(len(names)):
        if count_exist>0:
            if review_urls[name_index] in existing_set_url: # already in the set
                continue
        if sleep_count > num_entries_before_sleep:
            print "sleeping"
            time.sleep(sleep_time)
            sleep_count = 0
        else:
            sleep_count += 1
        query_url = "http://www.tripadvisor.ca" + review_urls[name_index]
        htmlparser = etree.HTMLParser()
        tree = etree.parse(opener.open(query_url), htmlparser)
        length_of_visit = tree.xpath('//b[text() = "Recommended length of visit:"]/following-sibling::text()')
        place = {}
        place["name"] = data[name_index]["name"][0]
        print place["name"]
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
        rating = tree.xpath('//span[@class="rate sprite-rating_rr rating_rr"]//img/@alt')
        if rating == []:
            place["rating"] = "NA"
        else:
            place["rating"] = rating[0]
        number_of_reviews = tree.xpath('//a[@href="#REVIEWS"]//span[@property="v:count"]')
        if number_of_reviews == []:
            place["number_of_reviews"] = "NA"
        else:
            place["number_of_reviews"] = number_of_reviews[0].text
        longitude = tree.xpath('//div[@class="mapWrap"]//div[@class="mapContainer"]/@data-lng')
        if longitude == []:
            place["longitude"] = "NA"
        else:
            place["longitude"] = longitude[0]
        latitude = tree.xpath('//div[@class="mapWrap"]//div[@class="mapContainer"]/@data-lat')
        if latitude == []:
            place["latitude"] = "NA"
        else:
            place["latitude"] = latitude[0]
        street_address = tree.xpath('//span[@class="street-address"]')
        if street_address == []:
            place["street_address"] = "NA"
        else:
            place["street_address"] = street_address[0].text
        zipcode = tree.xpath('//span[@property="v:postal-code"]')
        if zipcode == []:
            place["zipcode"] = "NA"
        else:
            place["zipcode"] = zipcode[0].text
        phone_number = tree.xpath('//div[@class="phoneNumber"]')
        if phone_number == []:
            place["phone_number"] = "NA"
        else:
            place["phone_number"] = phone_number[0].text.replace('Phone Number: ', '')
        google_query = "https://www.google.com/search?q=" + names[name_index]
        soup = BeautifulSoup(opener.open(google_query).read())
        opening_hours = soup.find_all("span", {"class" : "loht__open-interval"})
        if opening_hours == []:
            place["hours"] = "NA"
        else:
            place["hours"] = "Sunday: " + opening_hours[0].text + " Monday: " + opening_hours[1].text + " Tuesday: " + opening_hours[2].text + " Wednesday: " + opening_hours[3].text + " Thursday: " + opening_hours[4].text + " Friday: " + opening_hours[5].text + " Saturday: " + opening_hours[6].text
        final_list.append(place)
        #time.sleep(1) #to avoid blacklist
except:
    json.dump(final_list, outfile)
    json_data.close()
    outfile.close()
    print "Unexpected error:", sys.exc_info()[0]
    raise

json.dump(final_list, outfile)
json_data.close()
outfile.close()
