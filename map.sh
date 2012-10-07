#!/bin/bash

function usage {
    cat<<EOF>&2
Usage
    $0 {classname} {map-to} 

Maps
    awt          -- AWT platform classname
    platform     -- Platform class
    vector       -- vector class

Syntax

    All classnames in JPL (dot) syntax.

EOF
    exit 1
}

case "${1}" in
    java.awt.*)
        case "${2}" in
            awt)
                echo "${1}"
                ;;
            platform)
                egrep "^${1}" awt/index.map | sed "s/${1}://; s/:.*//;"
                ;;
            vector)
                egrep "^${1}" awt/index.map | sed 's/.*://;'
                ;;
            *)
                usage ;;
        esac
        ;;
    platform.*)
        case "${2}" in
            awt)
                egrep ":${1}:" awt/index.map | sed 's/:.*//'
                ;;
            platform)
                echo "${1}"
                ;;
            vector)
                egrep ":${1}:" awt/index.map | sed 's/.*://'
                ;;
            *)
                usage ;;
        esac
        ;;
    vector.*)
        case "${2}" in
            awt)
                egrep ":${1}" awt/index.map | sed 's/:.*//'
                ;;
            platform)
                egrep ":${1}" awt/index.map | sed "s/:${1}//; s/.*://"
                ;;
            vector)
                echo "${1}"
                ;;
            *)
                usage ;;
        esac
        ;;
    *)
        usage ;;
esac

