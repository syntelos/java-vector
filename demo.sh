#!/bin/bash

codebase="http://java-vector.googlecode.com/git"

if [ -d ".git" ]
then
    use_git=true
else
    use_git=false
fi

function download_style {
    case "${1}" in
        lib/lxl-*)
            echo "eager";;
        lib/json-*)
            echo "eager";;
        *)
            echo "lazy";;
    esac
}

libs=$(ls lib/*.jar )

if [ -n "${libs}" ]
then
    jarfdemo=$(ls vector-demo-*.jar | tail -n 1 )
    jarfcore=$(ls vector-*.jar | egrep -v demo | tail -n 1 )

    if [ -n "${jarfdemo}" ]&&[ -f "${jarfdemo}" ]&&[ -n "${jarfcore}" ]&&[ -f "${jarfcore}" ]
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

    <jar href="${jarfcore}" main="true" download="eager"/>
    <jar href="${jarfdemo}" download="eager"/>
EOF
            for libf in ${libs}
            do
                cat<<EOF>>${tgt}
    <jar href="${libf}" download="$(download_style ${libf})" />
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
Error, file not found: 'vector-X.Y.Z.jar' or 'vector-demo-X.Y.Z.jar'.  Run 'ant demo' to build.
EOF
        exit 1
    fi
else
    cat<<EOF>&2
Error, files not found: 'lib/*.jar'.
EOF
    exit 1
fi
