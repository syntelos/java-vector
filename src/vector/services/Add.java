package vector.services;

/**
 * Built-in display service function for {@link vector.Add Add}.
 * 
 * @see vector.AddService
 */
public class Add
    extends Object
    implements vector.AddService.Service
{

    public Add(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.Add.ADD == argv[0]){

                ////////////////////////////////////
                ////////////////////////////////////
                ////////////////////////////////////

                return null;
            }
            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not ADD",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
