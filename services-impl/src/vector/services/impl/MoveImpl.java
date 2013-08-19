package vector.services.impl;

/**
 * Built-in display service function for {@link vector.Move Move}.
 * 
 * @see vector.MoveService
 */
public class MoveImpl
    extends Object
    implements vector.services.MoveService.Service
{

    public MoveImpl(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.services.Move.MOVE == argv[0])

                return vector.services.DisplayService.Move(argv);

            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not MOVE",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
