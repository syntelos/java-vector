#!/bin/bash

awt=awt/src/platform/Document.java
prove=prove/src/platform/Document.java
android=android/src/platform/Document.java

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

if fupdate ${awt} ${prove} 
then
    if fupdate ${awt} ${android}
    then
        exit 0
    else
        exit 1
    fi
else
    exit 1
fi

