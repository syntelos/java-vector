package vector.services;

import java.lang.reflect.Constructor;
import java.net.URI;

/**
 * Display operator and arguments.
 * 
 * In {@link ShowService} an input sequence (argv) is constructed
 * for evaluating expressions defined by this class.  
 * 
 * In the evaluation input sequence, each component of this enumerated
 * set may have a corresponding value.
 * 
 * @see ShowService
 */
public enum Show {
    /**
     * 
     */
    SHOW(true),
    /**
     * 
     */
    S(true,Class.class);


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


    private Show(boolean required, Class... possibleValues){
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
     * @param uin User input
     * @return Operator service argument value
     */
    public Object parse(Object uin){
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
    public Class[] getPossibleValues(){
        return this.possibleValues.clone();
    }
    public Constructor[] getPossibleCtors(){
        return this.possibleCtors.clone();
    }
}
