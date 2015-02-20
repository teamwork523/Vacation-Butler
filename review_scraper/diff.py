#!/usr/bin/python
import json
from argparse import ArgumentParser

# take the name of a json file
# return a set containing all the entries
def load_json_from_file(json_file_name):
	raw_file = open(json_file_name)
	raw_data = raw_file.read()
	# combine multiple json objects into one
	raw_data = raw_data.replace("][[", ", ")
	json_data = raw_data.replace("][", ", ")
	json_list = json.loads(json_data)
	encountered = set()
	for i in json_list:
		repr_i = repr(i)
		encountered.add(repr_i)
	return encountered

def main():
	parser = ArgumentParser(description='diff two json files')
	parser.add_argument('file1', help='first json file')
	parser.add_argument('file2', help='second json file')
	args = parser.parse_args()
	set1 = load_json_from_file(args.file1)
	set2 = load_json_from_file(args.file2)
	print set2 - set1

if __name__ == "__main__":
	main()
