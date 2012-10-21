#!/bin/bash

function usage {
    cat<<EOF>&2
Usage
    $0 {java-rt-classname}

Returns
    One of 'class', 'abstract', 'final', or 'interface'.

EOF
    exit 1
}

if [ -z "${JAVA_HOME}" ]||[ ! -d "${JAVA_HOME}/src" ]
then
    cat<<EOF>&2
Error, missing environment variable {JAVA_HOME} or directory '{JAVA_HOME}/src'.
EOF
    exit 1

elif [ -n "${1}" ]
then
    srcf="${JAVA_HOME}/src/$(echo "${1}" | sed 's%\.%/%g').java"

    basename=$(echo "${1}" | sed 's/.*\.//')

    if [ -f "${srcf}" ]
    then
        egrep "^public .* ${basename}" ${srcf} | sed "s/public//; s/${basename}.*//; s/  / /g; s/abstract class/abstract/; s/final class/final/; s/^ *//g; s/ *$//g"
    else
        cat<<EOF>&2
Error, file not found '${srcf}'.
EOF
        exit 1
    fi
else
    usage
fi
