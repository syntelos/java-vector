#!/bin/bash
#
# Grep the class file strings in a jar 
#

tmpd=/tmp/tmp.$$

if [ -n "${1}" ]&&[ -n "${2}" ]&&[ -f "${1}" ]&&[ -n "$(echo ${1} | egrep '\.jar$')" ]
then
    if mkdir "${tmpd}"
    then
        srcd="$(pwd)"
        srcfar="${srcd}/${1}"

        if cd "${tmpd}"
        then
            if unzip -q "${srcfar}"
            then
                for class in $(find . -type f )
                do
                    strings=$(echo $class | sed 's/$/.strings/')
                    strings "${class}" > "${strings}"

                done
                
                egrep -n "${2}" $(find * -type f -name '*.strings')

                cd "${srcd}"

                rm -rf "${tmpd}"

                exit 0
            else
                cat<<EOF>&2
Error in 'unzip -q "${srcfar}"'
EOF
                exit 1
            fi
        else
            cat<<EOF>&2
Error in 'cd "${tmpd}"'
EOF
            exit 1
        fi
    else
        cat<<EOF>&2
Error in 'mkdir "${tmpd}"'
EOF
        exit 1
    fi
else
    cat<<EOF>&2
Usage

  $0 file.jar search-re

Description

  Search class files in 'file.jar' for string 're', an egrep regular
  expression over the product of "strings file > file.strings".

Purpose

  Search symbolic references in 'pkg/Class$Inner' format to prove
  compiled content.

  For example

    $0 vector-X.Y.Z.jar platform

  will list class file binary references to platform from vector.

EOF
    exit 1
fi
