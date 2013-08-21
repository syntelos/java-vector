package vector.services;

import json.Strings;

import java.lang.reflect.Method;


/**
 * Display operator service evaluates UI commands in the
 * {@link Create} language.
 * 
 * @see Create
 */
public class CreateService
    extends AbstractService
{

    public final static CreateService Instance = new CreateService();

    /**
     * Indempotent class initialization
     */
    public final static void Init(){
    }
    /**
     * Service call
     * @param argv A sequence of name-value pairs as <code>({@link Create}, String<)*</code>.
     * @return Services combined response text lines, may be null
     */
    public final static String[] Evaluate(Object... argv){

        return CreateService.Instance.evaluate(argv);
    }

    /**
     * The operating context of any service is intended as other services.
     */
    public interface Service
        extends AbstractService.Service
    {
        /**
         * @param argv Sequence of name value pairs, for name a member of {@link Create}
         * 
         * @return Response lines (may be null)
         */
        public String[] evaluate(Object... argv);
    }


    /**
     * Instance constructor
     */
    private CreateService(){
        super(Create.class);

        for (Class clas: this){
            try {
                CreateService.Service service = (CreateService.Service)clas.newInstance();

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
        Create operator = null, prev = null;

        for (int count = argv.length, cc = 0; cc < count; cc++){

            Object arg = argv[cc];

            if (null == arg)
                throw new IllegalArgumentException("Null argument");

            else if (null == operator){

                Class argclas = arg.getClass();

                if (Create.class.isAssignableFrom(argclas)){

                    Create next = (Create)arg;
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
                    throw new IllegalArgumentException(String.format("Operator (%s) not in class Create",argclas.getName()));
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
