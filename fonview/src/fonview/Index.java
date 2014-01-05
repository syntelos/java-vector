/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2012, John Pritchard, Syntelos
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package fonview;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Index of font files used by {@link Cursor}
 */
public abstract class Index<C>
    extends lxl.ArrayList<C>
{
    /**
     * 
     */
    public final static class IF
        extends Index<File>
        implements java.io.FileFilter
    {

        public IF(File file){
            super();
            final File dir = file.getParentFile();
            for (File fi: dir.listFiles(this)){
                this.add(fi);
            }
        }


        public boolean accept(File file){
            if (file.isFile()){

                final String name = file.getName();
                final String[] parts = name.split(".");
                final String fext = parts[parts.length-1].toLowerCase();

                return ("ttf".equals(fext) || "jhf".equals(fext));
            }
            return false;
        }
    }
    /**
     * 
     */
    public final static class IU
        extends Index<URL>
    {

        public IU(URL url)
            throws IOException
        {
            super();
            final String base = Basename(url);
            final URL index = new URL(base+"/index.txt");
            final DataInputStream in = new DataInputStream(index.openStream());
            try {
                String line;
                while (true){
                    line = in.readLine();
                    if (null != line){
                        if (0 < line.length() && '#' != line.charAt(0)){
                            try {
                                URL u = new URL(line);
                                this.add(u);
                            }
                            catch(java.net.MalformedURLException m1){
                                try {
                                    URL u = new URL(base+'/'+line);
                                    this.add(u);
                                }
                                catch(java.net.MalformedURLException m2){
                                }
                            }
                        }
                    }
                    else
                        break;
                }
            }
            finally {
                in.close();
            }
        }


        public final static String Basename(URL u){
            String src = u.toExternalForm();
            int sx = src.lastIndexOf("/");
            if (0 < sx)
                return src.substring(0,sx);
            else
                throw new IllegalArgumentException(src);
        }
    }



    public Index(){
        super();
    }
}
