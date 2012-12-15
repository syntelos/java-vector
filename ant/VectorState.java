

import java.io.File;
import java.io.IOException;

import java.util.StringTokenizer;

/**
 * 
 */
public class VectorState
    extends VersionPub
{

    public final static int CMD_SET = 1;
    public final static int CMD_GET = 2;


    public static int Cmd(String cmd){

        if (null != cmd && 0 < cmd.length()){

            cmd = cmd.toLowerCase();

            switch(cmd.charAt(0)){
            case 'g':
                if ("get".equals(cmd))
                    return CMD_GET;
                else
                    return CMD_NONE;
            case 's':
                if ("set".equals(cmd))
                    return CMD_SET;
                else
                    return CMD_NONE;
            }
        }
        return CMD_NONE;
    }

    protected final static File StateTxt = new File("state.txt");


    public static void main(String[] argv){
        if (null != argv && 1 <= argv.length){

            final int cmd = VectorState.Cmd(argv[0]);
            if (0 < cmd){
                int argx = 1, argc = argv.length;
                String arg = null;
                try {
                    if (argx < argc)
                        arg = argv[argx];

                    switch(cmd){
                    case CMD_GET:
                        arg = ReadFile(StateTxt);

                        break;
                    case CMD_SET:
                        if (null != arg){
                            arg = WriteFile(StateTxt,arg);

                            AddFile(StateTxt);
                        }
                        break;
                    }
                    if (null != arg){
                        System.out.println(arg);
                    }
                    System.exit(0);
                }
                catch (Exception exc){
                    System.err.println();
                    switch(cmd){
                    case CMD_GET:
                        System.err.printf("Failed to get '%s' ",arg);
                        break;
                    case CMD_SET:
                        System.err.printf("Failed to set '%s' ",arg);
                        break;

                    }
                    exc.printStackTrace(System.err);
                    System.exit(1);
                }
            }
        }
        System.err.println(Usage);
        System.exit(1);
    }

}
