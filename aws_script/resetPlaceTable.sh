#!/bin/bash

# Delete Place Table
aws dynamodb delete-table --table-name PlacesTable
sleep 3

# Recreate Place Table
cd aws_script 2> /dev/null
aws dynamodb create-table --table-name PlacesTable --attribute-definitions file://json/placeTableAttr.json --key-schema AttributeName=PlaceID,KeyType=HASH --global-secondary-indexes file://json/placeTableGSI.json --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
