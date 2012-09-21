#!/bin/bash

if [ -n "${1}" ]&&[ -f "${1}" ]
then
    definition="${1}"

    jarfcore=$(ls vector-*.jar | egrep -v demo | tail -n 1 )
    jarfdemo=$(ls vector-demo-*.jar | tail -n 1 )
    if [ -n "${jarfdemo}" ]&&[ -f "${jarfdemo}" ]&&[ -n "${jarfcore}" ]&&[ -f "${jarfcore}" ]
    then
        cat<<EOF

  Running '${definition}'...

EOF
        if java -cp ${jarfdemo}:${jarfcore} vector.Frame ${definition}
        then
            exit 0
        else
            exit 1
        fi
    else
        cat<<EOF>&2
Error, vector.jar not found.  Need build.  Run 'ant'.
EOF
        exit 1
    fi
else
    cat<<EOF>&2
Usage 

  $0 file.json

EOF
    exit 1
fi
