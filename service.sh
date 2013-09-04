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
            svcterm_name=$(echo $svcterm | sed 's/:.*//')
            svcterm_type=$(echo $svcterm | sed 's/.*://')

            svc[${cc}]=${svcterm_name}
            if [ "${svcterm_name}" != "${svcterm_type}" ]
            then
                svc_args[${cc}]=${svcterm_type}
            else
                svc_args[${cc}]=''
            fi

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
        classname_impl="${cname}Impl"

        tgt_enum="services/src/vector/services/${classname_enum}.java"
        tgt_svc="services/src/vector/services/${classname_svc}.java"

        tgt_meta="services-impl/src/META-INF/services/vector.${classname_enum}"

        tgt_impl="services-impl/src/vector/services/impl/${classname_enum}Impl.java"

        package_svc="vector.services"
        package_impl="vector.services.impl"

        directory=$(dirname "${tgt_svc}")

        if [ ! -d "${directory}" ]
        then
            cat<<EOF>&2
Error, directory not found: ${directory}
EOF
        fi

        #
        # Generate enum
        #

        if [ -n "${package_svc}" ]
        then
            cat<<EOF >${tgt_enum}
package ${package_svc};

import vector.data.DataField;
import vector.data.DataMessage;
import vector.data.DataOperator;
import vector.data.DataSubfield;

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
public enum ${classname_enum}
    implements DataOperator<${classname_enum}>
{
EOF
        cc=0
        count=${#svc[*]}
        term=$(( $count -1 ))
        while [ $cc -lt $count ]
        do
            name=$(echo ${svc[${cc}]} | tr 'a-z' 'A-Z')
            cat<<EOF >>${tgt_enum}
    /**
     * 
     */
EOF
            printf "    %s(true" ${name}  >>${tgt_enum}
            if [ -n "${svc_args[${cc}]}" ]
            then
                for svcarg in $(echo "${svc_args[${cc}]}" | sed 's/,/ /g')
                do
                    printf ",%s.class" ${svcarg}  >>${tgt_enum}
                done
            fi

            if [ $cc -eq $term ]
            then
                cat<<EOF >>${tgt_enum}
);
EOF
            else
                cat<<EOF >>${tgt_enum}
),
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

    public final boolean possibleString;

    public final boolean possibleClass;
    /**
     * Zero or more possible value types should have a string constructor
     */
    private final Class[] possibleValues;

    private final Constructor[] possibleCtors;


    private ${classname_enum}(boolean required, Class... possibleValues){
        this.required = required;
        this.possibleValues = possibleValues;

        boolean possibleString = false;
        boolean possibleClass = false;

        Constructor[] ctors = new Constructor[0];
        {
            final Class StringClass = String.class;
            final Class ClassClass = Class.class;

            for (Class possibleVC: possibleValues){
                /*
                 * Special cases
                 */
                if (StringClass.equals(possibleVC))
                    possibleString = true;

                else if (ClassClass.equals(possibleVC))
                    possibleClass = true;

                else {
                    try {
                        Constructor[] list = possibleVC.getConstructors();
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
        this.possibleString = possibleString;
        this.possibleClass = possibleClass;
        this.possibleCtors = ctors;
        this.argument = (0 < possibleValues.length);
    }


    /**
     * 
     */
    public boolean isOperator(){

        return true;
    }
    public boolean isOperator(DataSubfield sub){

        return (null == sub);
    }
    public boolean isSyntactic(){
        return true;
    }
    public boolean isRequired(){
        return this.required;
    }
    public boolean hasArgument(){
        return this.argument;
    }
    public Object toObject(String uin){

        return this.toObject( (Object)uin);
    }
    /**
     * @param uin User input
     * @return Operator service argument value
     */
    public Object toObject(Object uin){
        if (this.possibleString && uin instanceof String)
            return uin;
        else if (this.possibleClass){
            if (uin instanceof Class)
                return (Class)uin;
            else if (uin instanceof String){
                try {
                    return Class.forName( (String)uin);
                }
                catch (Exception next){
                }
            }
        }

        for (Constructor ctor : this.possibleCtors){
            try {
                return ctor.newInstance(uin);
            }
            catch (Exception next){
            }
        }
        return uin;
    }
    public String toString(Object data){

        return data.toString();
    }
    public boolean hasSubfieldClass(){
        return false;
    }
    public Class<vector.data.DataSubfield> getSubfieldClass()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
    public boolean hasSubfieldDefault(){
        return false;
    }
    public vector.data.DataSubfield getSubfieldDefault()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
    public boolean hasMapping(){
        return false;
    }
    public Iterable<vector.data.DataField<${classname_enum}>> getMapping(){
        throw new UnsupportedOperationException();
    }
    public boolean hasAlternative(){
        return false;
    }
    public <DO extends vector.data.DataOperator> DO getAlternative()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
    public <DO extends vector.data.DataOperator> Class<DO> getAlternativeClass()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
    public boolean isPossibleString(){
        return this.possibleString;
    }
    public boolean isPossibleClass(){
        return this.possibleClass;
    }
    public Class[] getPossibleValues(){
        return this.possibleValues.clone();
    }
    public Constructor[] getPossibleCtors(){
        return this.possibleCtors.clone();
    }
    public DataMessage[] evaluate(Object... argv){
        return ${classname_svc}.Evaluate(argv);
    }
}
EOF

        #
        # Generate service
        #

        if [ -n "${package_svc}" ]
        then
            cat<<EOF >${tgt_svc}
package ${package_svc};

EOF
        else
            cat<<EOF >${tgt_svc}

EOF
        fi
        cat<<EOF >>${tgt_svc}
import json.Strings;

import vector.data.DataMessage;

import java.lang.reflect.Method;

/**
 * Display operator service evaluates UI commands in the
 * {@link ${classname_enum}} language.
 * 
 * @see ${classname_enum}
 */
public class ${classname_svc}
    extends AbstractService
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
     * @return One message per service, may be null
     */
    public final static DataMessage[] Evaluate(Object... argv){

        return ${classname_svc}.Instance.evaluate(argv);
    }

    /**
     * The operating context of any service is intended as other services.
     */
    public interface Service
        extends AbstractService.Service
    {
        /**
         * @param argv Sequence of name value pairs, for name a member of {@link ${classname_enum}}
         * 
         * @return Response lines (may be null)
         */
        public DataMessage[] evaluate(Object... argv);
    }


    /**
     * Instance constructor
     */
    private ${classname_svc}(){
        super(${classname_enum}.class);

        for (Class clas: this){
            try {
                ${classname_svc}.Service service = (${classname_svc}.Service)clas.newInstance();

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
    public DataMessage[] evaluate(Object... argv){

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

                argv[cc] = operator.toObject(arg);

                prev = operator;

                operator = null;
            }
            else {
                throw new IllegalArgumentException(String.format("Operator (%s) has unexpected argument",operator));
            }
        }

        return super.evaluate(argv);
    }

}
EOF

        #
        # Generate META 
        #
        echo "vector.services.impl.${classname_impl}" > ${tgt_meta}


        #
        # Generate IMPL
        #
        uname=$(echo ${cname} | tr 'a-z' 'A-Z')

        cat<<EOF > ${tgt_impl}
package ${package_impl};

import vector.data.DataMessage;

/**
 * Built-in display service function for {@link vector.${cname} ${cname}}.
 * 
 * @see vector.${classname_svc}
 */
public class ${classname_impl}
    extends Object
    implements ${package_svc}.${classname_svc}.Service
{

    public ${classname_impl}(){
        super();
    }


    public DataMessage[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (${package_svc}.${classname_enum}.${uname} == argv[0])

                return ${package_svc}.DisplayService.${cname}(argv);

            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not ${uname}",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
EOF


    done
else
    usage
fi
