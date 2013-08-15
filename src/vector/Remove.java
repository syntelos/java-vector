package vector;

import java.lang.reflect.Constructor;
import java.net.URI;

/**
 * Display operator and arguments.
 * 
 * In {@link RemoveService} an input sequence (argv) is constructed
 * for evaluating expressions defined by this class.  
 * 
 * In the evaluation input sequence, each component of this enumerated
 * set may have a corresponding value.
 * 
 * @see RemoveService
 */
public enum Remove {
    /**
     * 
     */
    REMOVE(true),
    /**
     * 
     */
    T(true);


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


    private Remove(boolean required, Class... possibleValues){
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
