import json
import selenium
import re
from selenium import webdriver
from selenium.webdriver.common.keys import Keys

def get_cities(country_name, state_name, state_url, file_path):
    driver = webdriver.Firefox()
    driver.get(state_url)
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
        general_url = div.get_attribute("href") # link to the city page
        attraction_url = re.sub(r'Tourism-g([0-9]+)-(.+)-Vacations.html', r'Attractions-g\1-Activities-\2.html', general_url)
        attraction_url = re.sub(r'Destinations-g([0-9]+)-(.+)-Vacations.html', r'Attractions-g\1-Activities-\2.html', attraction_url)
        city_urls.append(attraction_url)
        num_cities +=1
    if num_cities == 0:
        things_to_do_url = driver.find_element_by_xpath('//a[@data-trk="attractions_nav"]').get_attribute("href")
        city_urls.append(things_to_do_url)
        num_cities = 1
    with open(file_path + country_name + "_" + state_name + ".json", "w") as outfile:
        json.dump(city_urls, outfile)
    print country_name+"_"+state_name+".json" + " has " + str(num_cities) + " cities"
    driver.quit()
