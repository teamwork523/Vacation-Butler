#!/bin/bash

option=$1

function Usage {
    echo "Usage:"
    echo "$(basename $0) [-h] [-r] [-s city_read city_write place_read place_write]"
    echo "      -h: help"
    echo "      -r: reset the capacity to minimum required values"
    echo "      -s: followed by table's capacity values"
    exit 1
}

function AWSUpdateTable {
    aws dynamodb update-table --table-name $1 --provisioned-throughput ReadCapacityUnits=$2,WriteCapacityUnits=$3
}

if [ "$option" == "-h" ]; then
    Usage
elif [ "$option" == "-r" ]; then
    # update city table
    AWSUpdateTable CitiesTable 1 1
    # update place table
    AWSUpdateTable PlacesTable 1 1
elif [ "$option" == "-s" ]; then
    # update city table
    AWSUpdateTable CitiesTable $2 $3
    # update place table
    AWSUpdateTable PlacesTable $4 $5
else
    echo "Invalid arguments."
    Usage
fi
