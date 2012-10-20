#!/bin/bash
#
# Check class name in Android
#

tmpf=/tmp/tmp$$

classname=''
android_level='10'

if [ -n "${1}" ]
then
    classname="${1}"

    if [ -n "${2}" ]
    then
        android_level="${2}"
    fi
else
    cat<<EOF >&2
Usage

    $0 'class.name' [android.level]

Description

    Confirm presence of named class for android API level

Arguments

    class.name      Dot format class name to search class
                    file listing.  Use '.' separator for 
                    inner class names (not '$').

                    Multiple class names may be employed when
                    separated by whitespace within the first 
                    argument.

    android.level   One or more integer numbers, default "10",
                    in preference order.  The first found is
                    employed.  Space separated string in one 
                    argument.

EOF
    exit 1
fi

if jarf=$(java -cp ant AndroidLibPath ${android_level} ) &&[ -n "${jarf}" ]&&[ -f "${jarf}" ]
then
    if unzip -l $jarf | awk '{print $4}' | egrep '\.class$' > ${tmpf} 
    then
        unset blacklist
        unset whitelist

        for srcf in $(find ${tgt} -type f -name '*.java')
        do
            for cre in ${classname}
            do
                if [ -z "$(egrep ${cre} ${tmpf} )" ]
                then
                    blacklist="${blacklist} ${cre}"
                else
                    whitelist="${whitelist} ${cre}"
                fi
            done
        done
        echo "# Ok"
        for m in ${whitelist} ; do echo "${m}"; done | sort -u | sed 's/^/+ /'
        echo "# Not Ok"
        for m in ${blacklist} ; do echo "${m}"; done | sort -u | sed 's/^/- /'

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
