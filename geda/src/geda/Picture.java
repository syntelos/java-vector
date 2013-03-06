/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2013, John Pritchard, Syntelos
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
package geda;

import java.io.LineNumberReader;
import java.io.IOException;
import java.io.OutputStream;

import java.util.StringTokenizer;


public class Picture
    extends vector.Container
{
    protected int x, y, width, height, angle;

    protected boolean mirrored, embedded;

    protected String filename, encoded;

    private boolean mark;


    public Picture(){
        super();
    }
    public Picture(LineNumberReader in) throws IOException {
        super();
        this.readGeda(in);
    }


    @Override
    public void init(){
        super.init();

        this.content = true;
    }
    @Override
    public void modified(){
        if (this.mark){
            this.mark = false;


        }
        super.modified();
    }
    public Picture readGeda(LineNumberReader in) throws IOException {
        this.mark = true;

        final int lno1 = in.getLineNumber();
        final String line1 = in.readLine();
        StringTokenizer strtok = new StringTokenizer(line1," ");
        if (7 == strtok.countTokens()){
            this.x = Integer.parseInt(strtok.nextToken());
            this.y = Integer.parseInt(strtok.nextToken());
            this.width = Integer.parseInt(strtok.nextToken());
            this.height = Integer.parseInt(strtok.nextToken());
            this.angle = Integer.parseInt(strtok.nextToken());
            this.mirrored = ("1".equals(strtok.nextToken()));
            this.embedded = ("1".equals(strtok.nextToken()));

            final int lno2 = in.getLineNumber();
            final String line2 = in.readLine();
            strtok = new StringTokenizer(line1," ");
            if (1 == strtok.countTokens()){

                this.filename = strtok.nextToken();

                this.encoded = null;

                if (this.embedded){
                    StringBuilder encoded = new StringBuilder();

                    int lno = in.getLineNumber();
                    String line = in.readLine();

                    while (null != line){

                        if (".".equals(line)){
                            this.encoded = encoded.toString();
                            break;
                        }
                        else {
                            encoded.append(line);
                            lno = in.getLineNumber();
                            line = in.readLine();
                        }
                    }
                    if (null == this.encoded){
                        throw new IllegalStateException(String.format("Format error: %d: <missing picture code>",lno));
                    }

                }

            }
            else
                throw new IllegalStateException(String.format("Format error: %d: %s",lno2,line2));
        }
        else
            throw new IllegalStateException(String.format("Format error: %d: %s",lno1,line1));

        return this;
    }
    public Picture writeGeda(OutputStream out) throws IOException {

        return this;
    }
}
