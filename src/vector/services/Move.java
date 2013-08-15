package vector.services;

/**
 * Built-in display service function for {@link vector.Move Move}.
 * 
 * @see vector.MoveService
 */
public class Move
    extends Object
    implements vector.MoveService.Service
{

    public Move(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.Move.MOVE == argv[0]){

                ////////////////////////////////////
                ////////////////////////////////////
                ////////////////////////////////////

                return null;
            }
            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not MOVE",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
