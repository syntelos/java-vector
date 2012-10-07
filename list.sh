#!/bin/bash

if [ -n "${1}" ]
then
    for awtclass in $(cat awt/index.map | sed 's/:.*//'); 
    do
        ./map.sh ${awtclass} ${1}
    done
else
    cat<<EOF>&2
Usage
    $0 {map-to}

    See ./map.sh for more into

EOF
    exit 1
fi
