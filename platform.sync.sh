#!/bin/bash

wd=$(cd $(dirname $0 ); pwd)

targets=$(egrep -v '(^awt$|^#)' ${wd}/platform.txt)

function fupdate {
    src="$1"
    tgt="$2"

    if [ ${src} -nt ${tgt} ]
    then
        if cp -p ${src} ${tgt}
        then
            cat<<EOF
M ${tgt}
EOF
            return 0

        else
            cat<<EOF>&2
Error: failed to copy '${src}' to '${tgt}'
EOF
            cat<<EOF
E ${tgt}
EOF
            return 1
        fi
    else
        cat<<EOF>&2
Warning: '${src}' is not newer than '${tgt}'
EOF
        cat<<EOF
Z ${tgt}
EOF
        return 0
    fi
}


for file in $(egrep -v '^#' ${wd}/platform.sync.txt )
do
    awt=awt/${file}

    for platform in ${targets}
    do

        if fupdate "${awt}" "${platform}/${file}"
        then
            continue;
        else
            exit 1
        fi
    done
done
