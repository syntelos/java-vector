#!/bin/bash

function orps {

    if [ -d .git ]
    then
        tmpf=/tmp/orps.$$

        git status > ${tmpf}

        lno=$(egrep -n 'Untracked files' ${tmpf} | egrep '^[0-9]' | sed 's/:.*//')

        if [ -n "${lno}" ]
        then
            nhead=$(( ${lno} + 2 ))
            lc=$(wc -l ${tmpf} | awk '{print $1}')
            ntail=$(( ${lc} - ${nhead} ))
            cat ${tmpf} | tail -n ${ntail} | awk '{print $2}'
        fi
        rm -f ${tmpf}
    fi
}

if flist=$(orps | grep esidl | egrep -v '^esidl/esidl$') && [ -n "${flist}" ]
then
    rm -rf ${flist}
    exit 0
else
    exit 1
fi
