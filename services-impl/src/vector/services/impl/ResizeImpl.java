package vector.services.impl;

/**
 * Built-in display service function for {@link vector.Resize Resize}.
 * 
 * @see vector.ResizeService
 */
public class ResizeImpl
    extends Object
    implements vector.services.ResizeService.Service
{

    public ResizeImpl(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.services.Resize.RESIZE == argv[0])

                return vector.services.DisplayService.Resize(argv);

            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not RESIZE",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
