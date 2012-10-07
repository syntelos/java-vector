#!/bin/bash
function usage {
    cat<<EOF>&2
Usage
    $0 [test|drop|create]

Operators
    test         -- List target files
    drop         -- Remove target files
    create       -- Write target files
EOF
    exit 1
}


test=false
drop=false
create=false

if [ -n "${1}" ]
then
    case "${1}" in
        test)
            test=true;;
        drop)
            drop=true;;
        create)
            create=true;;
        *)
            usage ;;
    esac
else
    usage
fi

for awtclass in $(./list.sh 'awt'); 
do
    basename=$(echo ${awtclass} | sed 's%.*\.%%')
    subpackage=$(echo ${awtclass} | sed "s%java\.awt\.%%; s%${basename}%%; s%\.$%%;")

    if [ -n "${subpackage}" ]
    then
        package=platform.${subpackage}
        interface=vector.${subpackage}.${basename}

        tgtf=awt/src/platform/${subpackage}/${basename}.java
    else
        package=platform
        interface=vector.${basename}

        tgtf=awt/src/platform/${basename}.java
    fi

    tgtd=$(dirname ${tgtf})

    if ${test}
    then
        echo "T ${tgtf}"

    elif ${drop}
    then

        if 2>/dev/null git rm --quiet -f ${tgtf}
        then
            echo "D ${tgtf}"
        else
            echo "X ${tgtf}"
        fi

    elif ${create}
    then
        if [ ! -d ${tgtd} ]
        then
            mkdir -p ${tgtd}
        fi

        cp HEAD.txt ${tgtf}

        cat<<EOF>>${tgtf}
package ${package};

/**
 * 
 */
public class ${basename}
    extends ${awtclass}
    implements ${interface}
{


    public ${basename}(){
        super();
    }
}
EOF

        if git add ${tgtf}
        then
            echo "A ${tgtf}"
        else
            echo "X ${tgtf}"
        fi
    fi
done
exit 0
