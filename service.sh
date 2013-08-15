#!/bin/bash

svctxt=$(pwd)/service.txt

function usage {
    cat<<EOF>&2

   Generate stub Enum and Service from
     ${svctxt}

EOF
    exit 1
}
function camelcase1 {
    b=$(echo $1 | sed 's/.//')
    a=$(echo $1 | sed "s/${b}\$//" | tr 'a-z' 'A-Z')
    echo "${a}${b}"
}
function camelcase {
    string=''
    for term in $(echo $1 | sed 's/-/ /g')
    do
        string="${string}$(camelcase1 ${term})"
    done
    echo "${string}"
}

if [ -f "${svctxt}" ]
then
    for svcdesc in $(egrep -v '^#' "${svctxt}" | sed 's/ /%_%/g')
    do
        unset svc
        unset svc_args
        cc=0
        for svcterm in $(echo $svcdesc | sed 's/%_%/ /g')
        do
            svterm_name=$(echo $svcterm | sed 's/:.*//')
            svterm_type=$(echo $svcterm | sed 's/.*://')

            svc[${cc}]=${svcterm_name}
            svc_args[${cc}]=${svcterm_type}

            cc=$(( ${cc} + 1 ))
        done

        cname=$(camelcase ${svc[0]})

        for name in ${svc[*]}
        do
            printf "%s " $name
        done
        echo

        classname_enum="${cname}"
        classname_svc="${cname}Service"

        tgt_enum="src/vector/${classname_enum}.java"
        tgt_svc="src/vector/${classname_svc}.java"

        directory=$(dirname "${tgt_svc}")

        if [ ! -d "${directory}" ]
        then
            cat<<EOF>&2
Error, directory not found: ${directory}
EOF
        else
            package=$(echo "${directory}" | sed 's%^\./%%; s%^src/%%; s%/%.%g;')
        fi

        #
        # Generate enum
        #

        if [ -n "${package}" ]
        then
            cat<<EOF >${tgt_enum}
package ${package};

import java.lang.reflect.Constructor;
import java.net.URI;
EOF
        else
            cat<<EOF >${tgt_enum}

EOF
        fi
        cat<<EOF >>${tgt_enum}

/**
 * Display operator and arguments.
 * 
 * In {@link ${classname_svc}} an input sequence (argv) is constructed
 * for evaluating expressions defined by this class.  
 * 
 * In the evaluation input sequence, each component of this enumerated
 * set may have a corresponding value.
 * 
 * @see ${classname_svc}
 */
public enum ${classname_enum} {
EOF
        cc=0
        count=${#svc[*]}
        term=$(( $count -1 ))
        while [ $cc -lt $count ]
        do
            name=$(echo ${svc[${cc}]} | tr 'a-z' 'A-Z')

            if [ $cc -eq $term ]
            then
            cat<<EOF >>${tgt_enum}
    /**
     * 
     */
    ${name}(true);
EOF
            else
            cat<<EOF >>${tgt_enum}
    /**
     * 
     */
    ${name}(true),
EOF
            fi
            cc=$(( ${cc} + 1 ))
        done
cat<<EOF >>${tgt_enum}


    /**
     * Is required in the evaluation input sequence
     */
    public final boolean required;
    /**
     * Has an argument in the evaluation input sequence
     */
    public final boolean argument;
    /**
     * Zero or more possible value types should have a string constructor
     */
    private final Class[] possibleValues;

    private final Constructor[] possibleCtors;


    private ${classname_enum}(boolean required, Class... possibleValues){
        this.required = required;
        this.possibleValues = possibleValues;

        Constructor[] ctors = new Constructor[0];
        {
            final Class StringClass = String.class;

            for (Class possibleClass: possibleValues){
                /*
                 * Exclude degenerate case
                 */
                if (!StringClass.equals(possibleClass)){
                    try {
                        Constructor[] list = possibleClass.getConstructors();
                        for (Constructor ctor: list){
                            Class[] parameters = ctor.getParameterTypes();
                            if (1 == parameters.length){
                                int clen = ctors.length;
                                if (0 == clen)
                                    ctors = new Constructor[]{ctor};
                                else {
                                    Constructor[] copier = new Constructor[clen+1];
                                    System.arraycopy(ctors,0,copier,0,clen);
                                    copier[clen] = ctor;
                                    ctors = copier;
                                }
                            }
                        }
                    }
                    catch (Exception debug){
                        debug.printStackTrace();
                    }
                }
            }
        }
        this.possibleCtors = ctors;
        this.argument = (0 < possibleValues.length);
    }


    /**
     * @param uin User input
     * @return Operator service argument value
     */
    public Object parse(Object uin){
        for (Constructor ctor : this.possibleCtors){
            try {
                return ctor.newInstance(uin);
            }
            catch (Exception next){
            }
        }
        return uin;
    }
    public Class[] getPossibleValues(){
        return this.possibleValues.clone();
    }
    public Constructor[] getPossibleCtors(){
        return this.possibleCtors.clone();
    }
}
EOF

        #
        # Generate service
        #

        if [ -n "${package}" ]
        then
            cat<<EOF >${tgt_svc}
package ${package};

EOF
        else
            cat<<EOF >${tgt_svc}

EOF
        fi
        cat<<EOF >>${tgt_svc}
import json.Strings;

import java.lang.reflect.Method;


/**
 * Display operator service evaluates UI commands in the
 * {@link ${classname_enum}} language.
 * 
 * @see ${classname_enum}
 */
public class ${classname_svc}
    extends services.Classes
{

    public final static ${classname_svc} Instance = new ${classname_svc}();

    /**
     * Indempotent class initialization
     */
    public final static void Init(){
    }
    /**
     * Service call
     * @param argv A sequence of name-value pairs as <code>({@link ${classname_enum}}, String<)*</code>.
     * @return Services combined response text lines, may be null
     */
    public final static String[] Evaluate(Object... argv){

        return ${classname_svc}.Instance.evaluate(argv);
    }

    /**
     * The operating context of any service is intended as other services.
     */
    public interface Service {
        /**
         * @param argv Sequence of name value pairs, for name a member of {@link ${classname_enum}}
         * 
         * @return Response lines (may be null)
         */
        public String[] evaluate(Object... argv);
    }


    private final lxl.List<Service> services = new lxl.ArrayList();

    /**
     * Instance constructor
     */
    private ${classname_svc}(){
        super(${classname_enum}.class);

        for (Class clas: this){
            try {
                Service service = (Service)clas.newInstance();

                this.services.add(service);
            }
            catch (Exception debug){

                debug.printStackTrace();
            }
        }
    }


    /**
     * @param argv Sequence of name value pairs, each name a member of
     * {@link Copy} and in enum (ordinal) order
     * @return Response lines (may be null)
     */
    public String[] evaluate(Object... argv){

        /*
         * Preprocessing normalization
         *
         *   * Adapts string and object users
         *
         *   * Performs validation
         */
        ${classname_enum} operator = null, prev = null;

        for (int count = argv.length, cc = 0; cc < count; cc++){

            Object arg = argv[cc];

            if (null == arg)
                throw new IllegalArgumentException("Null argument");

            else if (null == operator){

                Class argclas = arg.getClass();

                if (${classname_enum}.class.isAssignableFrom(argclas)){

                    ${classname_enum} next = (${classname_enum})arg;
                    if (null == prev)
                        operator = next;

                    else if (next.ordinal() > prev.ordinal())

                        operator = next;

                    else {
                        throw new IllegalArgumentException(String.format("Operator (%s:%d) is out of order, following (%s:%d)",next,next.ordinal(),prev,prev.ordinal()));
                    }

                    if (!operator.argument){

                        prev = operator;

                        operator = null;
                    }
                }
                else {
                    throw new IllegalArgumentException(String.format("Operator (%s) not in class ${classname_enum}",argclas.getName()));
                }
            }
            else if (operator.argument){

                argv[cc] = operator.parse(arg);

                prev = operator;

                operator = null;
            }
            else {
                throw new IllegalArgumentException(String.format("Operator (%s) has unexpected argument",operator));
            }
        }

        /*
         * Services evaluate input as a set of effects
         */
        String[] re = null;

        for (Service service: this.services){

            String[] r0 = service.evaluate(argv);

            re = Strings.Add(re,r0);
        }
        return re;
    }

}
EOF
    done
else
    usage
fi
