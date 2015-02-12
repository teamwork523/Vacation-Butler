import json
import selenium
from selenium import webdriver
from selenium.webdriver.common.keys import Keys

province_url = 'http://www.tripadvisor.ca/Tourism-g154922-British_Columbia-Vacations.html'
driver = webdriver.Firefox()
driver.get(province_url)

#expand the page to show all cities
expand_count = 0
while True:
    isPresent = len(driver.find_elements_by_class_name("morePopularCities"))
    if isPresent <= 0:
        break
    see_more_cities_button = driver.find_element_by_class_name("morePopularCities")
    see_more_cities_button.click()
    expand_count += 1

print "expanded " + str(expand_count) + " times"

# The page has loaded all the cities
# We can gather the urls
num_cities = 0
city_urls = []
city_divs = driver.find_elements_by_xpath('//a[@class="popularCity hoverHighlight"]')
for div in city_divs:
    city_urls.append(div.get_attribute("href"))
    num_cities +=1

with open("city_urls.json", "w") as outfile:
    json.dump(city_urls, outfile)
print "number of cities: " + str(num_cities)