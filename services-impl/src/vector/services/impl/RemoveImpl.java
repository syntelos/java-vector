package vector.services.impl;

/**
 * Built-in display service function for {@link vector.Remove Remove}.
 * 
 * @see vector.RemoveService
 */
public class RemoveImpl
    extends Object
    implements vector.services.RemoveService.Service
{

    public RemoveImpl(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.services.Remove.REMOVE == argv[0]){

                ////////////////////////////////////
                ////////////////////////////////////
                ////////////////////////////////////

                return null;
            }
            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not REMOVE",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
