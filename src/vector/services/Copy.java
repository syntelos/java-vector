package vector.services;

/**
 * Built-in display service function for {@link vector.Copy Copy}.
 * 
 * @see vector.CopyService
 */
public class Copy
    extends Object
    implements vector.CopyService.Service
{

    public Copy(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.Copy.COPY == argv[0]){

                ////////////////////////////////////
                ////////////////////////////////////
                ////////////////////////////////////

                return null;
            }
            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not COPY",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
