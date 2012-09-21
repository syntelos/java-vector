#!/bin/bash

codebase="http://java-vector.googlecode.com/git"

if [ -d ".git" ]
then
    use_git=true
else
    use_git=false
fi

libs=$(ls lib/*.jar )

if [ -n "${libs}" ]
then
    jarf=$(ls vector-*.jar | tail -n 1 )

    if [ -n "${jarf}" ]&&[ -f "${jarf}" ]
    then
        for src in *.json
        do
            name=$(basename ${src} .json)
            tgt=demo/${name}.jnlp
            cat<<EOF>${tgt}
<?xml version="1.0" encoding="utf-8"?>
<jnlp
     spec="1.5+"
 codebase="${codebase}"
     href="${tgt}">
  <information>
    <title>Vector Demo '${name}'</title>
    <vendor>The DigiVac Company</vendor>
    <homepage href="http://www.digivac.com/"/>
    <description>Vector scalable graphical user interfaces demo</description>
  </information>
  <resources>
    <j2se version="1.6+"/>

    <jar href="${jarf}" main="true"/>
EOF
            for libf in ${libs}
            do
                cat<<EOF>>${tgt}
    <jar href="${libf}" />
EOF
            done
            cat<<EOF>>${tgt}
  </resources>

  <application-desc main-class="vector.Frame">
    <argument>${codebase}/${src}</argument>
  </application-desc>
</jnlp>
EOF
            #
            if ${use_git}
            then
                git add ${tgt}
            fi
            #
            ls -l ${tgt}

        done
        #
        exit 0
        #
    else
        cat<<EOF>&2
Error, file not found: 'vector-X.Y.Z.jar'.  Run 'ant' to build.
EOF
        exit 1
    fi
else
    cat<<EOF>&2
Error, files not found: 'lib/*.jar'.
EOF
    exit 1
fi
