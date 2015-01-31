import json
import urllib
import urllib2
from lxml import etree
import re
from types import NoneType

json_data=open('scraped_data_from_Dec_2014.json')

data = json.load(json_data)
names = []
review_urls = []
for num in range(len(data)):
    names.append(urllib.quote_plus(data[num]["name"][0])) #make strings URL safe
    review_urls.append(data[num]["review_url"][0])

opener = urllib2.build_opener()
opener.addheaders = [('User-Agent', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10) AppleWebKit/600.1.25 (KHTML, like Gecko) Version/8.0 Safari/600.1.25')]

with open("recommended_length_of_visit.json", "w") as outfile:
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
        json.dump(place, outfile)

json_data.close()
