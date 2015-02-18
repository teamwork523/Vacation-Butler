import json

raw_file = open('data.json')
raw_data = raw_file.read()
# combine multiple json objects into one
json_data = raw_data.replace("][[", ", ")
json_data = json_data.replace("][", ", ")
output_file = open("processed_data.json", "w")
old_list = json.loads(json_data)

started_number = 0;
final_number = 0;
encountered = set()
newlist = []
for i in old_list:
    started_number += 1
    repr_i = repr(i)
    if repr_i in encountered:
       continue
    encountered.add(repr_i)
    newlist.append(i)
    final_number += 1

json.dump(newlist, output_file)
output_file.close()
raw_file.close()
print 'removed ' + str(started_number - final_number) + " duplicates"
print 'final number of entries: ' + str(final_number)
