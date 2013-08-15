package vector.services;

/**
 * Built-in display service function for {@link vector.Cat Cat}.
 * 
 * @see vector.CatService
 */
public class Cat
    extends Object
    implements vector.CatService.Service
{

    public Cat(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.Cat.CAT == argv[0]){

                ////////////////////////////////////
                ////////////////////////////////////
                ////////////////////////////////////

                return null;
            }
            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not CAT",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
