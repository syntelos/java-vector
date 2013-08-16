package vector.services.impl;

/**
 * Built-in display service function for {@link vector.Create Create}.
 * 
 * @see vector.CreateService
 */
public class CreateImpl
    extends Object
    implements vector.services.CreateService.Service
{

    public CreateImpl(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.services.Create.CREATE == argv[0]){

                ////////////////////////////////////
                ////////////////////////////////////
                ////////////////////////////////////

                return null;
            }
            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not CREATE",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
