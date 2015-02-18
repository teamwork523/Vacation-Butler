import scrapy
import json
from scrapy.contrib.spiders import CrawlSpider, Rule
from scrapy.contrib.linkextractors import LinkExtractor
from review_scraper.items import ReviewScraperItem

class MySpider(CrawlSpider):
    name = 'myspider'
    allowed_domains = ['tripadvisor.ca']
    raw_file = open('city_urls.json')
    raw_data = raw_file.read()
    start_urls = json.loads(raw_data)
    # start_urls = ['http://www.tripadvisor.ca/Attractions-g154943-Activities-Vancouver_British_Columbia.html', 'http://www.tripadvisor.ca/Attractions-g154943-Activities-c25-Vancouver_British_Columbia.html']

    rules = (
        # Extract all links that have the word 'Attractions-' and parse them with the callback function parse_item
        Rule(LinkExtractor(allow=('Attractions-g', )), callback='parse_item'),
    )

    def parse_item(self, response):
        self.log('parsing attractions %s' % response.url)
        item = ReviewScraperItem()
        for site in response.xpath('//div[starts-with(@id, "attraction_") and starts-with(@class, "listing")]'):
            property = site.xpath('descendant::a[@dir="ltr" and @class="property_title "]')
            item['name'] = property.re(r'>\n(.*)<')
            item['review_url'] = property.xpath('@href').extract()
            item['category'] = site.xpath('descendant::div[@class="information attraction-type"]').re(r'</span> (.*) <')
            item['latitude'] = site.xpath('descendant::span[starts-with(@id, "restmap")]/@onclick').re('null, null, \'(.*?)\'')
            item['longitude'] = site.xpath('descendant::span[starts-with(@id, "restmap")]/@onclick').re('null, null, \'.*?\', \'(.*?)\'')
            item['number_of_reviews'] = site.xpath('descendant::a[starts-with(@href, "/Attraction_Review-") and contains(@href, "#REVIEWS")]').re(r'>\n(.*?)\n</a>')
            item['average_rating'] = site.xpath('descendant::img[@class="sprite-ratings"]/@content').extract()
            yield item
