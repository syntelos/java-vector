
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
public class VersionPub
    extends Object
    implements java.io.FileFilter
{
    public final static int CMD_NONE = 0;
    public final static int CMD_ADD = 1;
    public final static int CMD_DELETE = 2;
    public final static int CMD_PUB = 3;

    public static int Cmd(String cmd){

        if (null != cmd && 0 < cmd.length()){

            cmd = cmd.toLowerCase();

            switch(cmd.charAt(0)){
            case 'a':
                if ("add".equals(cmd))
                    return CMD_ADD;
                else
                    return CMD_NONE;
            case 'd':
                if ("delete".equals(cmd))
                    return CMD_DELETE;
                else
                    return CMD_NONE;
            case 'p':
                if ("pub".equals(cmd))
                    return CMD_PUB;
                else
                    return CMD_NONE;
            }
        }
        return CMD_NONE;
    }

    protected final static String[] ENV = new String[0];

    /**
     * Eight bit US-ASCII
     */
    protected final static String ReadFile(File file){
        try {
            InputStream in = new java.io.FileInputStream(file);
            try {
                ByteArrayOutputStream string = new ByteArrayOutputStream();
                byte[] iob = new byte[128];
                int read;
                while (0 < (read = in.read(iob,0,128)))
                    string.write(iob,0,read);

                if (0 < string.size()){

                    iob = string.toByteArray();

                    return new String(iob,0,0,iob.length).trim();
                }
            }
            finally {
                in.close();
            }
        }
        catch (IOException exc){
        }
        return null;
    }
    /**
     * Eight bit US-ASCII
     */
    protected final static String WriteFile(File file, String line){
        if (null != line){
            line = line.trim();
            final int linelen = line.length();
            if (0 < linelen){
                try {
                    OutputStream out = new java.io.FileOutputStream(file);
                    try {
                        ByteArrayOutputStream string = new ByteArrayOutputStream();

                        final char[] content = line.toCharArray();
                
                        final byte[] iob = new byte[linelen];
                        {
                            for (int cc = 0; cc < linelen; cc++){
                                iob[cc] = (byte)(content[cc] & 0xff);
                            }
                        }
                        out.write(iob,0,linelen);
                        out.write('\n');
                        out.flush();

                        return line;
                    }
                    finally {
                        out.close();
                    }
                }
                catch (IOException exc){
                }
            }
        }
        return null;
    }
    protected final static void Copy(InputStream in, OutputStream out){
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

    protected final static boolean DeleteFile(File file)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (file.isFile()){
            File dir = file.getParentFile();
            if (null == dir)
                dir = CWD;

            if (IsSvnRepo(file)){
                if (SvnContains(file))
                    return SvnDelete(file);
                else if (Debug){
                    System.out.printf("+ delete %s in %s%n",file.getName(),dir.getPath());
                    return true;
                }
                else
                    return file.delete();
            }
            else if (GitContains(file))
                return GitDelete(file);
            else if (Debug){
                System.out.printf("+ delete %s in %s%n",file.getName(),dir.getPath());
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
        if (VersionPub.HaveSvn){
            String[] cmd = new String[]{
                VersionPub.Svn, "delete", "--force", file.getName()
            };
            File dir = file.getParentFile();
            if (null == dir)
                dir = CWD;

            if (Debug){
                System.out.printf("+ svn delete --force %s in %s%n",file.getName(),dir.getPath());

                return true;
            }
            else {

                Process p = RT.exec(cmd,ENV,dir);
                if (0 == p.waitFor()){

                    return true;
                }
                else {
                    System.out.printf("| svn delete --force %s in %s%n",file.getName(),dir.getPath());
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
        if (VersionPub.HaveGit){
            String[] cmd = new String[]{
                VersionPub.Git, "rm", "-f", file.getName()
            };
            File dir = file.getParentFile();
            if (null == dir)
                dir = CWD;

            if (Debug){
                System.out.printf("+ git rm -f %s in %s%n",file.getName(),dir.getPath());

                return true;
            }
            else {
                Process p = RT.exec(cmd,ENV,file.getParentFile());
                if (0 == p.waitFor()){

                    return true;
                }
                else {
                    System.out.printf("| git rm -f %s in %s%n",file.getName(),dir.getPath());
                    Copy(p.getInputStream(),System.out);
                    Copy(p.getErrorStream(),System.err);
                    return false;
                }
            }
        }
        else
            return false;
    }
    protected final static boolean AddFile(File file)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (VersionPub.HaveSvn && IsSvnRepo(file)){
            if (SvnContains(file))
                return false;
            else
                return SvnAdd(file);
        }
        else if (VersionPub.HaveGit && IsGitRepo(file)){

            if (GitContains(file)){

                return false;
            }
            else {

                return GitAdd(file);
            }
        }
        else {

            return false;
        }
    }
    private final static boolean SvnAdd(File file)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (VersionPub.HaveSvn){
            String[] cmd = new String[]{
                VersionPub.Svn, "add", file.getName()
            };
            File dir = file.getParentFile();
            if (null == dir)
                dir = CWD;

            if (Debug){
                System.out.printf("+ svn add %s in %s%n",file.getName(),dir.getPath());

                return true;
            }
            else {
                Process p = RT.exec(cmd,ENV,dir);
                if (0 == p.waitFor()){

                    return true;
                }
                else {
                    System.out.printf("| svn add %s in %s%n",file.getName(),dir.getPath());
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
        if (VersionPub.HaveGit){

            String[] cmd = new String[]{
                VersionPub.Git, "add", file.getName()
            };
            File dir = file.getParentFile();
            if (null == dir)
                dir = CWD;

            if (Debug){
                System.out.printf("+ git add %s in %s%n",file.getName(),dir.getPath());

                return true;
            }
            else {
                Process p = RT.exec(cmd,ENV,dir);
                if (0 == p.waitFor()){

                    return true;
                }
                else {
                    System.out.printf("| git add %s in %s%n",file.getName(),dir.getPath());
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
        File dir = file.getParentFile();
        if (null == dir)
            dir = CWD;

        dir = new File(dir,".svn");
        return (dir.isDirectory());
    }
    private final static boolean IsGitRepo(File file){

        File parent = file.getAbsoluteFile();
        if (!file.isDirectory()){
            parent = file.getParentFile();

            if (null == parent)
                parent = CWD;
        }

        while (null != parent){
            File dir = new File(parent,".git");
            if (dir.isDirectory()){

                return true;
            }
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
        if (VersionPub.HaveSvn){
            String[] cmd = new String[]{
                VersionPub.Svn, "diff", file.getName()
            };
            File dir = file.getParentFile();
            if (null == dir)
                dir = CWD;

            Process p = RT.exec(cmd,ENV,dir);
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
        if (VersionPub.HaveGit){
            String[] cmd = new String[]{
                VersionPub.Git, "ls-files", "--others"
            };
            File dir = file.getParentFile();
            if (null == dir)
                dir = CWD;

            Process p = RT.exec(cmd,ENV,dir);
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

    public final static String Usage = "Usage\n\n"+
        "\tVersionPub [add|delete|pub] <name>\n\n"+
        "\t\tAdd or delete file, or publish name.\n\n"+
        "\tVersionPub <name>\n\n"+
        "\t\tPublish name, deleting old and adding new.\n"+
        "\t\tPrint existing version.\n";

    /**
     * @param name file name prefix
     * @return file name
     */
    public final static String Publish(String name)
        throws IOException, InterruptedException
    {
        final File[] listing = CWD.listFiles(new VersionPub(name));

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

            final int cmd = VersionPub.Cmd(argv[0]);
            if (0 < cmd){
                int argx = 1, argc = argv.length;
                String arg = null;
                try {
                    for (; argx < argc; argx++){

                        if (1 < argx){
                            System.out.write(' ');
                        }

                        arg = argv[argx];

                        switch(cmd){
                        case CMD_ADD:
                            if (AddFile(new File(arg)))
                                System.out.print(arg);
                            break;
                        case CMD_DELETE:
                            if (DeleteFile(new File(arg)))
                                System.out.print(arg);
                            break;
                        case CMD_PUB:
                            System.out.print(Publish(arg));
                            break;
                        }
                    }
                    System.out.println();
                }
                catch (Exception exc){
                    System.err.println();
                    switch(cmd){
                    case CMD_ADD:
                        System.err.printf("Failed to add '%s' ",arg);
                        break;
                    case CMD_DELETE:
                        System.err.printf("Failed to delete '%s' ",arg);
                        break;
                    case CMD_PUB:
                        System.err.printf("Failed to pub '%s' ",arg);
                        break;
                    }
                    exc.printStackTrace(System.err);
                    System.exit(1);
                }
            }
            else {

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
            }
            System.exit(0);
        }
        else {
            System.err.println(Usage);
            System.exit(1);
        }
    }


    public final String name;

    public final int index;


    public VersionPub(String name){
        super();
        this.name = name;
        this.index = name.length();
    }
    protected VersionPub(){
        super();
        throw new UnsupportedOperationException();
    }


    public boolean accept(File file){
        if (file.isFile()){
            final String name = file.getName();

            if (name.startsWith(this.name) && name.endsWith(".jar")){

                String version = name.substring(index);
                if (0 < version.length() && '-' == version.charAt(0)){

                    while (0 < version.length() && '-' == version.charAt(0))
                        version = version.substring(1);

                    if (0 < version.length()){
                        final char number = version.charAt(0);
                        return ('0' <= number && '9' >= number);
                    }
                }
            }
        }
        return false;
    }
}
