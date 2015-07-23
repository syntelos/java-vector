#!/bin/bash

flist=$(./operators-files.sh)

for srcf in ${flist}
do
    class=$(basename ${srcf} .java)
    super=$(egrep '^[ 	]+extends' ${srcf} | head -n 1 | awk '{print $2}' | sed 's/<.*>//; s/ //g')
    unset type
    case ${super} in
        *Object)
            type="base";;
        *Canvas)
            type="base";;
        *)
            type="component"

            echo ${class} >> operators-subclasses/${super}
            cat operators-subclasses/${super} | sort -u > /tmp/tmp
            cp /tmp/tmp operators-subclasses/${super}

            git add operators-subclasses/${super}
            ;;
    esac


    len=$(wc -l ${srcf} | awk '{print $1}')

    classtn=Operators${class}
    supertn=Operators${super}
    classtf=${classtn}.wiki
    supertf=${supertn}.wiki

    cat<<EOF>${classtf}
#summary class ${class} extends ${super}

EOF

    if [ 'base' = "${type}" ]
    then
        cat<<EOF>>${classtf}
=[${classtn} ${class}] : ${super}=

EOF
    else
        cat<<EOF>>${classtf}
=[${classtn} ${class}] : [${supertn} ${super}]=

EOF
    fi

    if [ -f operators-subclasses/${class} ]
    then
        cat<<EOF>>${classtf}
 _Subclasses_
EOF
        for subclass in $(cat operators-subclasses/${class})
        do
            cat<<EOF>>${classtf}
 [Operators${subclass} ${subclass}]
EOF
        done
    else
        cat<<EOF>>${classtf}
 _Subclasses (none)_
EOF
    fi
    echo >>${classtf}

    for topn in $(egrep -n '^[ 	]+(public|protected) +void +[a-z]+\(' ${srcf} | sed 's/: .*//')
    do

        lasn=$(cat -n ${srcf} | tail -n $(( ${len} - ${topn} + 1 )) | egrep '^[ 	]+[0-9]+[	]*    }' | head -n 1 | awk '{print $1}')

        signature=$(cat ${srcf} | tail -n $(( ${len} - ${topn} + 1 )) | head -n 1)

        access=$(echo ${signature} | sed 's/ *void.*//; s/ *//' )

        name=$(echo ${signature} | sed 's/.*void //; s/(.*//' )

        msuper=$(cat ${srcf} | tail -n $(( ${len} - ${topn} + 1 )) | head -n $(( ${lasn} - ${topn} + 1 )) | egrep "super.${name}" | sed 's/ //g')

        if [ -n "${msuper}" ]
        then
            msuper='true'
        else
            msuper='false'
        fi

        echo "${class} : ${super} (${access} ${name} super:${msuper})"

        if ${msuper}
        then
            cat<<EOF>>${classtf}
==${name} : [${supertn}#${name} super]==
EOF
        else
            cat<<EOF>>${classtf}
==${name}==
EOF
        fi

            cat<<EOF>>${classtf}

<code language="java">
EOF

        cat -n ${srcf} | tail -n $(( ${len} - ${topn} + 1 )) | head -n $(( ${lasn} - ${topn} + 1 ))>>${classtf}

        cat<<EOF>>${classtf}
</code>

EOF

    done

    git add ${classtf}

done
