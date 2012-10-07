
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.StringTokenizer;

/**
 * Pick the most recent version of a named jar file target.  Finding
 * more than one version, drop the older and add the newer into
 * version control.
 * 
 * This process permits the build script to handle cyclic dependencies
 * among packages by not requiring independence or bootstraping.
 */
public class JarPublisher
    extends Object
    implements java.io.FileFilter
{

    private final static String[] ENV = new String[0];

    private final static void Copy(InputStream in, OutputStream out){
        try {
            try {
                byte[] iob = new byte[128];
                int read;
                while (0 < (read = in.read(iob,0,128)))
                    out.write(iob,0,read);
            }
            finally {
                in.close();
            }
        }
        catch (IOException exc){
        }
    }
    public final static File[] Add(File[] list, File item){
        if (null == item)
            return list;
        else if (null == list)
            return new File[]{item};
        else {
            int len = list.length;
            File[] copier = new File[len+1];
            System.arraycopy(list,0,copier,0,len);
            copier[len] = item;
            return copier;
        }
    }
    private final static boolean Contains(ByteArrayOutputStream stdout, String name){
        return Contains(stdout.toByteArray(),name);
    }
    private final static boolean Contains(byte[] stdout, String name){
        if (null != stdout && 0 < stdout.length)
            return Contains(new String(stdout,0,0,stdout.length),name);
        else
            return false;
    }
    private final static boolean Contains(String stdout, String name){
        StringTokenizer strtok = new StringTokenizer(stdout,"\r\n");
        while (strtok.hasMoreTokens()){
            if (name.equals(strtok.nextToken()))
                return true;
        }
        return false;
    }

    private final static boolean Debug = false;

    private final static String Svn, Git;
    static {
        String svn = null, git = null, rm = null;
        try {
            StringTokenizer strtok = new StringTokenizer(System.getenv("PATH"),File.pathSeparator);
            File chk;
            while (strtok.hasMoreTokens()){
                String pel = strtok.nextToken();
                chk = new File(pel,"svn");
                if (chk.isFile() && chk.canExecute()){
                    svn = chk.getPath();
                    if (null != git && null != rm)
                        break;
                }
                else {
                    chk = new File(pel,"svn.exe");
                    if (chk.isFile() && chk.canExecute()){
                        svn = chk.getPath();
                        if (null != git && null != rm)
                            break;
                    }
                }
                chk = new File(pel,"git");
                if (chk.isFile() && chk.canExecute()){
                    git = chk.getPath();
                    if (null != svn && null != rm)
                        break;
                }
                else {
                    chk = new File(pel,"git.exe");
                    if (chk.isFile() && chk.canExecute()){
                        git = chk.getPath();
                        if (null != svn && null != rm)
                            break;
                    }
                }
            }
        }
        catch (Exception exc){
            throw new InternalError();
        }
        Svn = svn;
        Git = git;
    }

    public final static boolean HaveSvn = (null != Svn);
    public final static boolean HaveGit = (null != Git);

    static {
        if (HaveSvn){
            if (HaveGit){
                if (Debug)
                    System.err.println("Using git and subversion.  Using Debug (Dry Run).");
            }
            else {
                if (Debug)
                    System.err.println("Using subversion but not git.  Using Debug (Dry Run).");
            }
        }
        else if (HaveGit){
            if (Debug)
                System.err.println("Using git but not subversion.  Using Debug (Dry Run).");
        }
        else {
            if (Debug)
                System.err.println("Not using either of subversion or git.  Using Debug (Dry Run).");
        }
    }

    private final static Runtime RT = Runtime.getRuntime();

    private final static boolean DeleteFile(File file)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (file.isFile()){
            if (IsSvnRepo(file)){
                if (SvnContains(file))
                    return SvnDelete(file);
                else if (Debug){
                    System.out.printf("+ delete %s in %s%n",file.getName(),file.getParentFile().getPath());
                    return true;
                }
                else
                    return file.delete();
            }
            else if (GitContains(file))
                return GitDelete(file);
            else if (Debug){
                System.out.printf("+ delete %s in %s%n",file.getName(),file.getParentFile().getPath());
                return true;
            }
            else
                return file.delete();
        }
        else
            return false;
    }
    private final static boolean SvnDelete(File file)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (JarPublisher.HaveSvn){
            String[] cmd = new String[]{
                JarPublisher.Svn, "delete", "--force", file.getName()
            };

            if (Debug){
                System.out.printf("+ svn delete --force %s in %s%n",file.getName(),file.getParentFile().getPath());

                return true;
            }
            else {
                Process p = RT.exec(cmd,ENV,file.getParentFile());
                if (0 == p.waitFor()){

                    return true;
                }
                else {
                    System.out.printf("| svn delete --force %s in %s%n",file.getName(),file.getParentFile().getPath());
                    Copy(p.getInputStream(),System.out);
                    Copy(p.getErrorStream(),System.err);
                    return false;
                }
            }
        }
        else
            return false;
    }
    private final static boolean GitDelete(File file)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (JarPublisher.HaveGit){
            String[] cmd = new String[]{
                JarPublisher.Git, "rm", "-f", file.getName()
            };

            if (Debug){
                System.out.printf("+ git rm -f %s in %s%n",file.getName(),file.getParentFile().getPath());

                return true;
            }
            else {
                Process p = RT.exec(cmd,ENV,file.getParentFile());
                if (0 == p.waitFor()){

                    return true;
                }
                else {
                    System.out.printf("| git rm -f %s in %s%n",file.getName(),file.getParentFile().getPath());
                    Copy(p.getInputStream(),System.out);
                    Copy(p.getErrorStream(),System.err);
                    return false;
                }
            }
        }
        else
            return false;
    }
    private final static boolean AddFile(File file)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (JarPublisher.HaveSvn && IsSvnRepo(file)){
            if (SvnContains(file))
                return false;
            else
                return SvnAdd(file);
        }
        else if (JarPublisher.HaveGit && IsGitRepo(file)){
            if (GitContains(file))
                return false;
            else
                return GitAdd(file);
        }
        else
            return false;
    }
    private final static boolean SvnAdd(File file)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (JarPublisher.HaveSvn){
            String[] cmd = new String[]{
                JarPublisher.Svn, "add", file.getName()
            };

            if (Debug){
                System.out.printf("+ svn add %s in %s%n",file.getName(),file.getParentFile().getPath());

                return true;
            }
            else {
                Process p = RT.exec(cmd,ENV,file.getParentFile());
                if (0 == p.waitFor()){

                    return true;
                }
                else {
                    System.out.printf("| svn add %s in %s%n",file.getName(),file.getParentFile().getPath());
                    Copy(p.getInputStream(),System.out);
                    Copy(p.getErrorStream(),System.err);
                    return false;
                }
            }
        }
        else
            return false;
    }
    private final static boolean GitAdd(File file)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (JarPublisher.HaveGit){
            String[] cmd = new String[]{
                JarPublisher.Git, "add", file.getName()
            };

            if (Debug){
                System.out.printf("+ git add %s in %s%n",file.getName(),file.getParentFile().getPath());

                return true;
            }
            else {
                Process p = RT.exec(cmd,ENV,file.getParentFile());
                if (0 == p.waitFor()){

                    return true;
                }
                else {
                    System.out.printf("| git add %s in %s%n",file.getName(),file.getParentFile().getPath());
                    Copy(p.getInputStream(),System.out);
                    Copy(p.getErrorStream(),System.err);
                    return false;
                }
            }
        }
        else
            return false;
    }
    private final static boolean IsSvnRepo(File file){
        File dir = new File(file.getParentFile(),".svn");
        return (dir.isDirectory());
    }
    private final static boolean IsGitRepo(File file){

        File parent = file;
        if (!file.isDirectory())
            parent = file.getParentFile();

        while (null != parent){
            File dir = new File(parent,".git");
            if (dir.isDirectory())
                return true;
            else {
                parent = parent.getParentFile();
            }
        }
        return false;
    }
    private final static boolean SvnContains(File file)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (JarPublisher.HaveSvn){
            String[] cmd = new String[]{
                JarPublisher.Svn, "diff", file.getName()
            };

            Process p = RT.exec(cmd,ENV,file.getParentFile());
            if (0 == p.waitFor()){

                return true;
            }
            else {
                return false;
            }
        }
        else
            return false;
    }
    private final static boolean GitContains(File file)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (JarPublisher.HaveGit){
            String[] cmd = new String[]{
                JarPublisher.Git, "ls-files", "--others"
            };

            Process p = RT.exec(cmd,ENV,file.getParentFile());
            if (0 == p.waitFor()){
                /*
                 * Is a git repo
                 */
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                Copy(p.getInputStream(),buf);
                if (Contains(buf,file.getName()))
                    return false;
                else
                    return true;
            }
            else {
                /*
                 * Not a git repo
                 */
                return false;
            }
        }
        else
            return false;
    }

    public final static File CWD = new File(System.getProperty("user.dir"));

    /**
     * @param name file name prefix
     * @return file name
     */
    public final static String Publish(String name)
        throws IOException, InterruptedException
    {
        final File[] listing = CWD.listFiles(new JarPublisher(name));

        if (null == listing)
            throw new InternalError();
        else {
            final int count = listing.length;
            switch(count){
            case 0:
                return "";
            case 1:
                return listing[0].getName();
            default:
                java.util.Arrays.sort(listing);
                final int term = (count-1);
                for (int cc = 0 ; cc < count; cc++){
                    final File file = listing[cc];

                    if (cc < term)
                        DeleteFile(file);
                    else {
                        AddFile(file);
                        return file.getName();
                    }
                }
                throw new IllegalStateException();
            }
        }
    }

    public static void main(String[] argv){
        if (null != argv && 1 <= argv.length){

            boolean once = true;

            for (String name: argv){
                if (once)
                    once = false;
                else
                    System.out.write(' ');

                try {
                    System.out.print(Publish(name));
                }
                catch (Exception exc){
                    exc.printStackTrace();
                    System.exit(1);
                }
            }
            System.out.println();
            System.exit(0);
        }
        else {
            System.err.println("Usage: JarPublisher <name>");
            System.exit(1);
        }
    }


    public final String name;

    public final int index;


    public JarPublisher(String name){
        super();
        this.name = name;
        this.index = name.length();
    }

    public boolean accept(File file){
        if (file.isFile()){
            final String name = file.getName();

            if (name.startsWith(this.name) && name.endsWith(".jar")){

                String version = name.substring(index);
                while ('-' == version.charAt(0))
                    version = version.substring(1);

                final char number = version.charAt(0);
                return ('0' <= number && '9' >= number);
            }
        }
        return false;
    }
}
