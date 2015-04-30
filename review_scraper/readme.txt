Require scrapy 24.4, selenium 2.45, and Firefox
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
rating, number_of_reviews, review_url, recommended_length_of_visit, hours, activities

example

{"category": "Speciality Museums;Museums", "city": "Lethbridge", "phone_number": "403-320.3898", "rating": "4.0 of 5 stars", "name": "Galt Museum &amp; Archives", "activities": "NA", "country": "Canada", "region": "Alberta", "zipcode": "T1J 0P6", "longitude": "-112.8467", "hours": "Sun 13:00 - 17:00; Mon - Wed 10:00 - 17:00; Thu 10:00 - 21:00; Fri - Sat 10:00 - 17:00; ", "number_of_reviews": "61", "recommended_length_of_visit": "NA", "latitude": "49.692493", "review_url": "/Attraction_Review-g154919-d1095898-Reviews-Galt_Museum_Archives-Lethbridge_Alberta.html", "street_address": "502 - 1st Street South"}
