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

libs=$(ls lib/*.jar | tr '\n' ' ')

if [ -n "${libs}" ]
then
    jar_geda=$(ls vector-geda-[0-9]*.[0-9]*.[0-9]*.jar | tail -n 1 )
    jar_awt=$(ls vector-awt-[0-9]*.[0-9]*.[0-9]*.jar | tail -n 1 )
    jar_core=$(ls vector-[0-9]*.[0-9]*.[0-9]*.jar | tail -n 1 )

    if [ -n "${jar_geda}" ]&&[ -f "${jar_geda}" ]&&[ -n "${jar_awt}" ]&&[ -f "${jar_awt}" ]&&[ -n "${jar_core}" ]&&[ -f "${jar_core}" ]
    then
        cat<<EOF>&2
Main ${jar_geda} ${jar_awt} ${jar_core}
Lib ${libs}
EOF
        for src in geda/*.sym
        do
            name=$(basename ${src} .sym)
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

    <jar href="${jar_awt}" main="true" download="eager"/>
    <jar href="${jar_core}" download="eager"/>
    <jar href="${jar_geda}" download="eager"/>
EOF
            for libf in ${libs}
            do
                cat<<EOF>>${tgt}
    <jar href="${libf}" download="$(download_style ${libf})" />
EOF
            done
            cat<<EOF>>${tgt}
  </resources>

  <application-desc main-class="geda.Frame">
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
Error, missing required jar file.  Use 'ant' to build jar files.
EOF
        exit 1
    fi
else
    cat<<EOF>&2
Error, files not found: 'lib/*.jar'.
EOF
    exit 1
fi
