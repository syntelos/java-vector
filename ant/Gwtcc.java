/*
 * Gwtcc Runner Script
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

import com.google.gwt.dev.Compiler;

import java.io.File;

/**
 * Run with two arguments, source and target.  Assume that user dir
 * has a familiar project structure, including the 'src', 'lib', and
 * 'ant' directories.
 */
public class Gwtcc
    extends Object
    implements java.io.FileFilter
{

    public final File srcd, tgtd;

    private boolean files;


    public Gwtcc(String srcd, String tgtd){
        this( new File(srcd), new File(tgtd));
    }
    public Gwtcc(File srcd, File tgtd){
        super();
        if (null != srcd && null != tgtd && srcd.isDirectory() && tgtd.isDirectory()){
            this.srcd = srcd;
            this.tgtd = tgtd;
        }
        else
            throw new IllegalArgumentException();
    }


    public void run(){
        String[] modules = this.modules();

        if (null != modules){
            for (String mod : modules){

                System.out.println(mod);

            }

        }
    }
    public String[] modules(){

        final File[] moduleFiles = this.list(null,(new File[]{this.srcd}));
        if (null != moduleFiles){

            String[] modules = null;

            for (File mf : moduleFiles){
                String path = mf.getAbsolutePath();
                int idx = path.lastIndexOf("/src/");
                if (-1 < idx){
                    String module = path.substring(idx+5);
                    module = module.substring(0,(module.length()-8));
                    module = module.replace('/','.');
                    modules = Add(modules,module);
                }
            }
            return modules;
        }
        else
            return null;
    }
    public File[] list(File[] modules, File[] dirs){

        if (null != dirs){

            for (File dir : dirs){

                this.files = true;

                File[] gwt_xml = dir.listFiles(this);

                modules = Cat(modules,gwt_xml);
            }

            for (File dir : dirs){

                this.files = false;

                File[] sub_dirs = dir.listFiles(this);

                modules = this.list(modules,sub_dirs);
            }
        }
        return modules;
    }

    public boolean accept(File file){

        if (this.files){
            if (file.isFile()){
                final String name = file.getName();

                return (name.endsWith(".gwt.xml"));
            }
            else
                return false;
        }
        else 
            return file.isDirectory();
    }


    public final static String[] Add(String[] list, String item){
        if (null == item)
            return list;
        else if (null == list)
            return new String[]{item};
        else {
            int len = list.length;
            String[] copier = new String[len+1];
            System.arraycopy(list,0,copier,0,len);
            copier[len] = item;
            return copier;
        }
    }
    public final static File[] Cat(File[] a, File[] b){
        if (null == a)
            return b;
        else if (null == b)
            return a;
        else {
            int alen = a.length;
            int blen = b.length;
            File[] copier = new File[alen+blen];
            System.arraycopy(a,0,copier,0,alen);
            System.arraycopy(b,0,copier,alen,blen);
            return copier;
        }
    }



    public static void usage(){
        System.err.println();
        System.err.println("Usage");
        System.err.println();
        System.err.println("    Gwtcc <src_dir> <tgt_dir>");
        System.err.println();
        System.exit(1);
    }
    public static void main(String[] argv){
        if (1 < argv.length){
            try {
                final Gwtcc gwtcc = new Gwtcc(argv[0],argv[1]);
                try {
                    gwtcc.run();
                }
                catch (Exception exc){
                    exc.printStackTrace();
                    System.exit(1);
                }
            }
            catch (RuntimeException exc){
                usage();
            }
        }
        else
            usage();
    }
}
