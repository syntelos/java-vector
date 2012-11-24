/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2012, The DigiVac Company
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
package platform;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The Display {@link Document} is only defined when one of the {@link
 * Display} open methods are called.  Each time the open method is
 * called, the document instance is replaced.
 */
public class Document 
    extends Object
    implements vector.Document
{

    public final static Document For(Display display){
        if (null != display)
            return display.getDocumentVector();
        else
            return null;
    }


    private final URL root;

    public Document(URL root){
        super();

        final String file = root.getFile();

        if (null != file && -1 < file.indexOf('.')){

            String url = root.toExternalForm();

            final int index = (url.length()-file.length()-1);

            url = url.substring(0,index);
            try {
                root = new URL(url);
            }
            catch (MalformedURLException exc){
                System.err.printf("Error: %s%n",url);
                exc.printStackTrace();
            }
        }
        this.root = root;

        System.out.printf("Document: %s%n",this.root);
    }
    public Document(File file)
        throws IOException
    {
        super();

        URL root = file.toURL();

        final String name = file.getName();

        if (null != name && -1 < name.indexOf('.')){

            String url = "file://"+file.getAbsolutePath();

            final int index = (url.length()-name.length()-1);

            url = url.substring(0,index);
            try {
                root = new URL(url);
            }
            catch (MalformedURLException exc){
                System.err.printf("Error: %s%n",url);
                exc.printStackTrace();
            }
        }
        this.root = root;

        System.out.printf("Document: %s%n",this.root);
    }


    public boolean hasRoot(){

        return (null != this.root);
    }
    public URL getRoot(){

        return this.root;
    }
    public URL reference(String path)
        throws MalformedURLException
    {
        if (null != path && 0 < path.length()){

            if ('/' == path.charAt(0))
                return new URL(this.root.getProtocol(),this.root.getHost(),this.root.getPort(),path,null);
            else {
                path = this.root.getPath() + '/' + path;

                return new URL(this.root.getProtocol(),this.root.getHost(),this.root.getPort(),path,null);
            }
        }
        return null;
    }
    public InputStream open(String path)
        throws IOException
    {
        URL u = this.reference(path);
        if (null != u)
            return u.openStream();
        else
            return null;
    }


    public static void main(String[] argv){

        try {
            Document doc = new Document(new URL(argv[0]));
            for (int cc = 1; cc < argv.length; cc++){

                String p = argv[cc];

                URL u = doc.reference(p);

                System.out.printf("%s: %s%n",p,u);
            }
            System.exit(0);
        }
        catch (Exception exc){
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
