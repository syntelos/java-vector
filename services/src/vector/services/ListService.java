package vector.services;

import json.Strings;

import vector.data.DataMessage;

import java.lang.reflect.Method;

/**
 * Display operator service evaluates UI commands in the
 * {@link List} language.
 * 
 * @see List
 */
public class ListService
    extends AbstractService
{

    public final static ListService Instance = new ListService();

    /**
     * Indempotent class initialization
     */
    public final static void Init(){
    }
    /**
     * Service call
     * @param argv A sequence of name-value pairs as <code>({@link List}, String<)*</code>.
     * @return One message per service, may be null
     */
    public final static DataMessage[] Evaluate(Object... argv){

        return ListService.Instance.evaluate(argv);
    }

    /**
     * The operating context of any service is intended as other services.
     */
    public interface Service
        extends AbstractService.Service
    {
        /**
         * @param argv Sequence of name value pairs, for name a member of {@link List}
         * 
         * @return Response lines (may be null)
         */
        public DataMessage[] evaluate(Object... argv);
    }


    /**
     * Instance constructor
     */
    private ListService(){
        super(List.class);

        for (Class clas: this){
            try {
                ListService.Service service = (ListService.Service)clas.newInstance();

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
        List operator = null, prev = null;

        for (int count = argv.length, cc = 0; cc < count; cc++){

            Object arg = argv[cc];

            if (null == arg)
                throw new IllegalArgumentException("Null argument");

            else if (null == operator){

                Class argclas = arg.getClass();

                if (List.class.isAssignableFrom(argclas)){

                    List next = (List)arg;
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
                    throw new IllegalArgumentException(String.format("Operator (%s) not in class List",argclas.getName()));
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
