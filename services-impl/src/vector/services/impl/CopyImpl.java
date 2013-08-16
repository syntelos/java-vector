package vector.services.impl;

/**
 * Built-in display service function for {@link vector.Copy Copy}.
 * 
 * @see vector.CopyService
 */
public class CopyImpl
    extends Object
    implements vector.services.CopyService.Service
{

    public CopyImpl(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.services.Copy.COPY == argv[0]){

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
