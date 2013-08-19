package vector.services.impl;

/**
 * Built-in display service function for {@link vector.Cat Cat}.
 * 
 * @see vector.CatService
 */
public class CatImpl
    extends Object
    implements vector.services.CatService.Service
{

    public CatImpl(){
        super();
    }


    public String[] evaluate(Object... argv){

        if (null != argv && 0 < argv.length){

            if (vector.services.Cat.CAT == argv[0])

                return vector.services.DisplayService.Cat(argv);

            else
                throw new IllegalArgumentException(String.format("First argument (%s) is not CAT",argv[0]));
        }
        else
            throw new IllegalArgumentException("Missing arguments");
    }
}
