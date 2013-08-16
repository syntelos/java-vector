package vector.services;

import json.Strings;

import java.lang.reflect.Method;


/**
 * Display operator service evaluates UI commands in the
 * {@link Resize} language.
 * 
 * @see Resize
 */
public class ResizeService
    extends services.Classes
{

    public final static ResizeService Instance = new ResizeService();

    /**
     * Indempotent class initialization
     */
    public final static void Init(){
    }
    /**
     * Service call
     * @param argv A sequence of name-value pairs as <code>({@link Resize}, String<)*</code>.
     * @return Services combined response text lines, may be null
     */
    public final static String[] Evaluate(Object... argv){

        return ResizeService.Instance.evaluate(argv);
    }

    /**
     * The operating context of any service is intended as other services.
     */
    public interface Service {
        /**
         * @param argv Sequence of name value pairs, for name a member of {@link Resize}
         * 
         * @return Response lines (may be null)
         */
        public String[] evaluate(Object... argv);
    }


    private final lxl.List<Service> services = new lxl.ArrayList();

    /**
     * Instance constructor
     */
    private ResizeService(){
        super(Resize.class);

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
        Resize operator = null, prev = null;

        for (int count = argv.length, cc = 0; cc < count; cc++){

            Object arg = argv[cc];

            if (null == arg)
                throw new IllegalArgumentException("Null argument");

            else if (null == operator){

                Class argclas = arg.getClass();

                if (Resize.class.isAssignableFrom(argclas)){

                    Resize next = (Resize)arg;
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
                    throw new IllegalArgumentException(String.format("Operator (%s) not in class Resize",argclas.getName()));
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
