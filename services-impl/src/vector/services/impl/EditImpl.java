package vector.services.impl;

/**
 * Built-in display service function for {@link vector.Edit Edit}.
 * 
 * @see vector.EditService
 */
public class EditImpl
    extends Object
    implements vector.services.EditService.Service
{

    public EditImpl(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.services.Edit.EDIT == argv[0]){

                ////////////////////////////////////
                ////////////////////////////////////
                ////////////////////////////////////

                return null;
            }
            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not EDIT",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
