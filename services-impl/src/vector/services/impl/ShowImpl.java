package vector.services.impl;

/**
 * Built-in display service function for {@link vector.Show Show}.
 * 
 * @see vector.ShowService
 */
public class ShowImpl
    extends Object
    implements vector.services.ShowService.Service
{

    public ShowImpl(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.services.Show.SHOW == argv[0]){

                ////////////////////////////////////
                ////////////////////////////////////
                ////////////////////////////////////

                return null;
            }
            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not SHOW",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
