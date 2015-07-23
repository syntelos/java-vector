#!/bin/bash

flist=$(./operators-files.sh)

for srcf in ${flist}
do
    class=$(basename ${srcf} .java)

    classtn=Operators${class}

    classtf=${classtn}.wiki

    cat<<EOF
  * [${classtn} ${class}]
EOF
done
