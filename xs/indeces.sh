#!/bin/bash

map="war/WEB-INF/appengine-generated/datastore-indexes-auto.xml:web/WEB-INF/appengine-generated/datastore-indexes-auto.xml war/WEB-INF/datastore-indexes.xml:web/WEB-INF/datastore-indexes.xml"

for srctgt in ${map}
do
    src=$(echo $srctgt | sed 's/:.*//')
    tgt=$(echo $srctgt | sed 's/.*://')

    if [ -f ${src} ]&&[ $src -nt $tgt ]
    then
        if cp -p $src $tgt
        then
            ls -l $tgt
        else
            cat<<EOF>&2
Error in 'cp -p $src $tgt'
EOF
            exit 1
        fi
    fi
done
