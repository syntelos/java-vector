#!/bin/bash

for definition in *.json
do
    if ./run.sh ${definition}
    then
        continue
    else
        cat<<EOF>&2
Error running '${definition}'
EOF
        exit 1
    fi
done
exit 0
