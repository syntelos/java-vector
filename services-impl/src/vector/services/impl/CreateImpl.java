package vector.services.impl;

import vector.data.DataMessage;

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


    public DataMessage[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.services.Create.CREATE == argv[0])

                return vector.services.DisplayService.Create(argv);

            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not CREATE",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
