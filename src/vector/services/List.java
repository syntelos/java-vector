package vector.services;

/**
 * Built-in display service function for {@link vector.List List}.
 * 
 * @see vector.ListService
 */
public class List
    extends Object
    implements vector.ListService.Service
{

    public List(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.List.LIST == argv[0]){

                ////////////////////////////////////
                ////////////////////////////////////
                ////////////////////////////////////

                return null;
            }
            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not LIST",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
