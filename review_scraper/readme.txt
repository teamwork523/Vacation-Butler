Require scrapy 24.4, selenium 2.44, and Firefox
sudo pip install scrapy
sudo pip install selenium

Get all the cities in a province:
1) enter “python get_urls.py”. The script will launch Firefox via selenium.
Selenium will get the ajax contents.
2) output city_urls.json

Scrape attractions\activities from trip advisor:
1) open a terminal
2) cd to the directory of the scraper
3) enter "scrapy crawl myspider -o data.json". This will run the spider and save the scraped data to data.json

Remove duplicate json entries (depend on data.json)
1) enter "python remove_duplicate_json.py"
2) the script will save the result in processed_data.json

Get recommended length of visit from trip advisor (depend on processed_data.json)
1) enter "python recommended_length_of_visit.py"
2) the result is saved in recommended_length_of_visit.json

Get opening hours (depend on processed_data.json)
1) enter "python google_opening_hours.py"
2) the result is saved in opening_hours.json
