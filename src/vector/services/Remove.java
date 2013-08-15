package vector.services;

/**
 * Built-in display service function for {@link vector.Remove Remove}.
 * 
 * @see vector.RemoveService
 */
public class Remove
    extends Object
    implements vector.RemoveService.Service
{

    public Remove(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.Remove.REMOVE == argv[0]){

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
