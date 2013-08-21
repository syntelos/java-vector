#!/bin/bash

wd=$(cd $(dirname $0); pwd)


function usage {
    cat<<EOF>&2
Usage

    $0 [optstr]

Description

    Run find over platform dirs with optional name search.

Optstr

    Name search character code, "-optstr"

        Character arguments in the set "jt-?" are expanded to one or more "string" for

            find -name "string"

        Character code

            d
                Verbose shell debug output (set -x)

            j
                Search for files named '*.java'

            t
                Search for files named '*.txt'

            h|?
                This message

    Other searchstring, find name "re"

        The optstr that does not begin with '-' is one string argument to 

            find -name "string"

EOF
    exit 1
}

#
# for example, "./platform-find -d 'Search*.java' "
#
if platform_dirs=$(egrep -v '^#' ${wd}/platform.txt | sed "s%^%${wd}/%") && [ -n "${platform_dirs}" ]
then
    search="*.java"
    if [ -n "${1}" ]
    then
        search="${1}"
        namesearch=''
        while [ -n "${1}" ]
        do
            if [ -z "$(echo ${1} | sed 's/-.//')" ]
            then
                #
                # parse character code for common search patterns
                #
                for token in $(echo "${1}" | sed 's/./ &/g')
                do
                    case "${token}" in
                        d)
                            if [ -n "$(echo $- | grep x )" ]
                            then
                                set +x
                            else
                                set -x
                            fi
                            ;;
                        j)
                            namesearch="${namesearch} %%%.java"
                            ;;
                        t)
                            #
                            # protecting the "for" loop consumer 
                            #
                            namesearch="${namesearch} %%%.txt"
                            ;;
                        *)
                            namesearch=''
                            ;;
                        -)
                            ;;
                        h|?)
                            usage
                            ;;
                    esac
                done
            else
                namesearch="${namesearch} $(echo ${1} | sed 's/\*/%%%/g')"
            fi
            shift
        done
    fi

    if [ -n "${namesearch}" ]
    then
        for dir in ${platform_dirs}
        do
            for search in $(echo ${namesearch} | sed 's/%%%/*/g')
            do

                find "${dir}" -type f -name "${search}"

            done
        done
    else
        
        for dir in ${platform_dirs}
        do

            find "${dir}" -type f -name "${search}"

        done
    fi

else
    cat<<EOF>&2
Error, file not found, './platform.txt'.
EOF
    exit 1
fi
