#!/bin/bash

for awtclass in $(./imports-wrt-android.sh | egrep '^- ' | sed 's/- //'); do
    for srcf in $(egrep $awtclass $(find src -type f -name '*.java') | sed 's/:.*//' | sort -u)
    do
        echo $awtclass $srcf
    done
done

