Require scrapy 24.4, selenium 2.44, and Firefox
sudo pip install scrapy
sudo pip install selenium

Get all the cities in a province:
1) enter "python scraper_controller.py"
2) select a state or province. The script will launch Firefox via selenium, which will get the ajax contents.
2) output city_urls.json

Scrape attractions\activities from trip advisor:
1) open a terminal
2) cd to the directory of the scraper and delete the existing data.json (Scrapy appends to this file)
3) enter "scrapy crawl myspider -o data.json". This will run the spider and save the scraped data to data.json

Remove duplicate json entries (depend on data.json)
1) enter "python remove_duplicate_json.py"
2) the script will save the result in processed_data.json

Get detailed info from trip advisor (depend on processed_data.json)
1) enter "python get_attraction_info.py"
2) the result is saved in attraction_info.json

Fields in attraction_info.json:

name, category, city, region, country, street_address, zipcode, phone_number, longitude, latitude,
rating, number_of_reviews, review_url, recommended_length_of_visit, hours

example

{"category": "Art Museums;Museums", "city": "Vancouver", "phone_number": "+1 604-682-3455", "name": "Bill Reid Gallery", "rating": "4.5 of 5 stars", "country": "Canada", "region": "British Columbia", "zipcode": "V6C 2G3", "longitude": "-123.11914", "hours": "Sunday: Closed Monday: Closed Tuesday: 11:00 am \u2013 5:00 pm Wednesday: 11:00 am \u2013 5:00 pm Thursday: 11:00 am \u2013 5:00 pm Friday: 11:00 am \u2013 5:00 pm Saturday: 11:00 am \u2013 5:00 pm", "number_of_reviews": "65", "recommended_length_of_visit": " 1-2 hours ", "latitude": "49.284576", "review_url": "/Attraction_Review-g154943-d1015074-Reviews-Bill_Reid_Gallery-Vancouver_British_Columbia.html", "street_address": "639 Hornby Street"}
