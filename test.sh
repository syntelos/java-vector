#!/bin/bash

jarf=$(ls vector-*.jar | tail -n 1 )
if [ -n "${jarf}" ]&&[ -f "${jarf}" ]
then
    for definition in *.json
    do
        cat<<EOF

  Running '${definition}'...

EOF
        if java -jar ${jarf} ${definition}
        then
            continue
        else
            break
        fi
    done
else
    cat<<EOF>&2
Error, vector.jar not found.  Need build.  Run 'ant'.
EOF
    exit 1
fi
