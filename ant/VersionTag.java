/*
 * VersionTag
 * Copyright (C) 2012 John Pritchard
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author jdp
 */
public class VersionTag 
    extends Object
{
    public final static String OPEN = "open";
    public final static String CLOSE = "close";


    public static void usage(){
        System.err.println("Usage");
        System.err.println();
        System.err.println("  VersionTag (open|close) [build.version]");
        System.err.println();
        System.err.println("Description");
        System.err.println();
        System.err.println("  Isolates build scripts from version control");
        System.err.println();
    }
    public static void main(String[] argv){
        if (0 < argv.length){

            final String prefix = argv[0];

            final String properties;

            if (1 < argv.length){
                properties = argv[1];
            }
            else {
                properties = "${user.dir}/build.version";
            }

            PropertySource(properties);

            final String version_major = Properties.getProperty("version.major");
            final String version_minor = Properties.getProperty("version.minor");
            final String version_build = Properties.getProperty("version.build");

            if (null != version_major &&
                null != version_minor &&
                null != version_build)
            {
                final String suffix = (version_major +'.'+ version_minor +'.'+ version_build);

                final String tag = (prefix +'-'+ suffix);

                if (OPEN.equals(prefix) || CLOSE.equals(prefix)){
                    try {
                        Tag(tag);

                        System.exit(0);
                    }
                    catch (Exception exc){
                        exc.printStackTrace();
                        System.exit(1);
                    }
                }
                else {
                    usage();
                    System.exit(1);
                }
            }
            else {
                System.err.println("Missing build properties values");
                System.exit(1);
            }
        }
        else {
            usage();
            System.exit(1);
        }
    }

    private final static java.util.Properties Properties = new java.util.Properties(java.lang.System.getProperties());

    /**
     * Initialize properties file
     */
    private final static void PropertySource(String value){
        File file = PropertyFile(value);
        if (file.isFile()){
            try {
                InputStream fin = new FileInputStream(file);
                try {
                    Properties.load(fin);
                }
                finally {
                    fin.close();
                }
            }
            catch (IOException exc){
                exc.printStackTrace();
                System.exit(1);
            }
        }
        else {
            System.err.printf("Properties file not found '%s' in value '%s'",file.getAbsolutePath(),value);
        }
    }
    /**
     * Parse and evaluate target value string
     */
    private final static String PropertyEval(String request){
        int start = request.indexOf('$');
        if (-1 < start){
            String q = request;
            StringBuilder string = new StringBuilder();
            while (true){

                if ('{' == q.charAt(start+1)){

                    if (0 < start){
                        string.append(q.substring(0,start));
                    }

                    int end = q.indexOf('}',start);
                    if (start < end){
                        String name = q.substring((start+2),end);
                        String value = Properties.getProperty(name);
                        if (null != value)
                            string.append(value);
                        else
                            throw new IllegalArgumentException(String.format("Property not found '%s' in '%s'",name,request));
                    }
                    else
                        throw new IllegalArgumentException(String.format("Syntax error found at '%s' in '%s'",q.substring(start),request));

                    q = q.substring(end+1);
                    start = q.indexOf('$');
                    if (0 > start){
                        string.append(q);
                        break;
                    }
                }
                else
                    throw new IllegalArgumentException(String.format("Syntax error found at '%s' in '%s'",q.substring(start),request));
            }
            return PropertyEval(string.toString());
        }
        else
            return request;
    }
    private final static File PropertyFile(String request){
        String value = PropertyEval(request);
        if (null != value && 0 > value.indexOf(':'))
            return new File(value);
        else
            throw new IllegalArgumentException(String.format("File not found in '%s' from '%s'",value,request));
    }
    private final static File[] PropertyFiles(String request){
        String value = PropertyEval(request);
        if (null != value){
            String[] list = value.split(":");
            int count = list.length;
            File[] re = new File[count];
            for (int cc = 0; cc < count; cc++){
                re[cc] = new File(list[cc]);
            }
            return re;
        }
        else
            throw new IllegalArgumentException(String.format("File not found in '%s' from '%s'",value,request));
    }

    private final static boolean Debug = (null != System.getProperty("Debug"));


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

    private final static boolean Tag(String tag)
        throws java.io.IOException,
               java.lang.InterruptedException
    {

        if (IsSvnRepo())
            return SvnTag(tag);

        else if (IsGitRepo())
            return GitTag(tag);
        else
            return false;
    }
    private final static boolean SvnTag(String tag)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (VersionTag.HaveSvn){
            final String[] cmd = new String[]{
                VersionTag.Svn, "tag", tag
            };
            final String cmdstring = Join(cmd);

            System.out.println(cmdstring);

            Process p = RT.exec(cmd,ENV,UserDir);
            if (0 == p.waitFor()){

                return true;
            }
            else {
                Copy(p.getInputStream(),System.out);
                Copy(p.getErrorStream(),System.err);
                return false;
            }
        }
        else
            return false;
    }
    private final static boolean GitTag(String tag)
        throws java.io.IOException,
               java.lang.InterruptedException
    {
        if (VersionTag.HaveGit){
            String[] cmd = new String[]{
                VersionTag.Git, "tag", tag
            };
            final String cmdstring = Join(cmd);

            System.out.println(cmdstring);

            Process p = RT.exec(cmd,ENV,UserDir);
            if (0 == p.waitFor()){

                return true;
            }
            else {
                Copy(p.getInputStream(),System.out);
                Copy(p.getErrorStream(),System.err);
                return false;
            }
        }
        else
            return false;
    }

    private final static boolean IsSvnRepo(){

        File testDir = new File(UserDir,".svn");

        return (testDir.isDirectory());
    }
    private final static boolean IsGitRepo(){

        File parent = UserDir;

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
    public final static String Join(String[] sary){
        final StringBuilder string = new StringBuilder();

        boolean once = true;
        for (String s : sary){
            if (once)
                once = false;
            else
                string.append(' ');

            string.append(s);
        }
        return string.toString();
    }
    private final static String[] ENV = new String[0];
    private final static File UserDir = new File(System.getProperty("user.dir"));
}
