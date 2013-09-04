package vector.services;

import json.Strings;

import vector.data.DataMessage;

import java.lang.reflect.Method;

/**
 * Display operator service evaluates UI commands in the
 * {@link Cat} language.
 * 
 * @see Cat
 */
public class CatService
    extends AbstractService
{

    public final static CatService Instance = new CatService();

    /**
     * Indempotent class initialization
     */
    public final static void Init(){
    }
    /**
     * Service call
     * @param argv A sequence of name-value pairs as <code>({@link Cat}, String<)*</code>.
     * @return One message per service, may be null
     */
    public final static DataMessage[] Evaluate(Object... argv){

        return CatService.Instance.evaluate(argv);
    }

    /**
     * The operating context of any service is intended as other services.
     */
    public interface Service
        extends AbstractService.Service
    {
        /**
         * @param argv Sequence of name value pairs, for name a member of {@link Cat}
         * 
         * @return Response lines (may be null)
         */
        public DataMessage[] evaluate(Object... argv);
    }


    /**
     * Instance constructor
     */
    private CatService(){
        super(Cat.class);

        for (Class clas: this){
            try {
                CatService.Service service = (CatService.Service)clas.newInstance();

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
        Cat operator = null, prev = null;

        for (int count = argv.length, cc = 0; cc < count; cc++){

            Object arg = argv[cc];

            if (null == arg)
                throw new IllegalArgumentException("Null argument");

            else if (null == operator){

                Class argclas = arg.getClass();

                if (Cat.class.isAssignableFrom(argclas)){

                    Cat next = (Cat)arg;
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
                    throw new IllegalArgumentException(String.format("Operator (%s) not in class Cat",argclas.getName()));
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
