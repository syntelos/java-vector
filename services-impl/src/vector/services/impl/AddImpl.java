package vector.services.impl;

import vector.data.DataMessage;

/**
 * Built-in display service function for {@link vector.Add Add}.
 * 
 * @see vector.AddService
 */
public class AddImpl
    extends Object
    implements vector.services.AddService.Service
{

    public AddImpl(){
        super();
    }


    public DataMessage[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.services.Add.ADD == argv[0])

                return vector.services.DisplayService.Add(argv);

            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not ADD",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
