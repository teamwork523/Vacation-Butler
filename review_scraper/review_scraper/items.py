# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class ReviewScraperItem(scrapy.Item):
    # define the fields for your item here like:
    name = scrapy.Field()
    review_url = scrapy.Field()
    category = scrapy.Field()
    latitude = scrapy.Field()
    longitude = scrapy.Field()
    number_of_reviews = scrapy.Field()
    average_rating = scrapy.Field()
