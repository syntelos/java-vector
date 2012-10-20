#!/bin/bash
#
# Check progress on platform independence between AWT and Android
#

tmpf=/tmp/tmp$$

tgt=src/vector

android_level='10'

if [ -n "${1}" ]&&[ -d "${1}" ]
then
    tgt="${1}"
fi

if jarf=$(java -cp ant AndroidLibPath ${android_level} ) &&[ -n "${jarf}" ]&&[ -f "${jarf}" ]
then
    if unzip -l $jarf | awk '{print $4}' | egrep '\.class$' > ${tmpf} 
    then
        unset blacklist
        unset whitelist

        for srcf in $(find ${tgt} -type f -name '*.java')
        do
            for import in $(egrep '^import java' ${srcf} | sed 's/import *//; s/;//; s/ //g;' )
            do
                if [ -z "$(egrep ${import} ${tmpf} )" ]
                then
                    blacklist="${blacklist} ${import}"
                else
                    whitelist="${whitelist} ${import}"
                fi
            done
        done
        echo "# Ok"
        for m in ${whitelist} ; do echo ${m}; done | sort -u | sed 's/^/+ /'
        echo "# Not Ok"
        for m in ${blacklist} ; do echo ${m}; done | sort -u | sed 's/^/- /'

        rm -f ${tmpf} 

        exit 0
    else

        rm -f ${tmpf} 

        cat<<EOF>&2
Error, unable to list ${jarf}
EOF
        exit 1
    fi
else
    cat<<EOF>&2
Error, unable to locate 'android.jar' for API level(s): ${android_level}
EOF
    exit 1
fi
