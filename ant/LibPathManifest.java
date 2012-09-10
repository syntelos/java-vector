

import java.util.StringTokenizer;

public class LibPathManifest {




    public static void main(String[] argv){
        boolean once = true;

        for (String arg: argv){
            StringTokenizer strtok = new StringTokenizer(arg,":;");
            final int count = strtok.countTokens();
            for (int cc = 0; cc < count; cc++){

                String pel = Rel(strtok.nextToken());

                if (once)
                    once = false;
                else
                    System.out.print(" ");

                System.out.print(pel);
            }
        }
        System.out.println();
    }

    public final static String Rel(String dirpath){
        int idx0 = dirpath.replace('\\','/').lastIndexOf("/lib/");
        return dirpath.substring(idx0+1);
    }
}
