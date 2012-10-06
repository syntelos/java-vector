
import java.io.File;

/**
 * Given an ordered list of android platform version numbers on the
 * command line (e.g. 15 14 13 12 11 10), return the first
 * <code>android.jar</code> found.  Requires environment variable
 * <code>"ANDROID_HOME"</code>.
 */
public class AndroidLibPath {

    final static String Root = System.getenv("ANDROID_HOME");


    public static void main(String[] argv){

        if (null != Root){
            final File root = new File(Root);
            if (root.isDirectory()){
                final File platforms = new File(root,"platforms");
                if (platforms.isDirectory()){

                    if (null != argv && 0 < argv.length){

                        for (String version: argv){
                            File dir;

                            if (version.startsWith("android-"))
                                dir = new File(platforms,version);
                            else
                                dir = new File(platforms,"android-"+version);

                            if (dir.isDirectory()){
                                File jar = new File(dir,"android.jar");
                                if (jar.isFile()){
                                    System.out.println(jar.getAbsolutePath());
                                    System.exit(0);
                                    return;
                                }
                            }
                        }
                    }

                    System.err.println("Requested directory in ANDROID_HOME not found");
                    System.exit(1);
                }
                else {
                    System.err.printf("Directory '%s' in ANDROID_HOME not found%n",platforms.getAbsolutePath());
                    System.exit(1);
                }
            }
            else {
                System.err.printf("Directory '%s' from environment variable 'ANDROID_HOME=%s' not found%n",root.getAbsolutePath(),Root);
                System.exit(1);
            }
        }
        else {
            System.err.println("Environment variable 'ANDROID_HOME' not found");
            System.exit(1);
        }
    }

}
