package vector.services;

/**
 * Built-in display service function for {@link vector.Resize Resize}.
 * 
 * @see vector.ResizeService
 */
public class Resize
    extends Object
    implements vector.ResizeService.Service
{

    public Resize(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.Resize.RESIZE == argv[0]){

                ////////////////////////////////////
                ////////////////////////////////////
                ////////////////////////////////////

                return null;
            }
            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not RESIZE",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
