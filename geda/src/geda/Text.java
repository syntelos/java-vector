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


public class Text
    extends vector.Container
{
    public static class NameValue
        extends Object
    {

        public String name, value;


        public NameValue(String string){
            super();
            if (null != string && 0 < string.length()){
                int idx = string.indexOf('=');
                if (-1 < idx){
                    this.name = string.substring(0,idx);
                    this.value = string.substring(idx+1);
                }
                else
                    this.value = string;
            }
        }


        public boolean isAttribute(){
            return (null != this.name);
        }
    }

    protected int x, y, color, size, visibility, show_name_value, angle, alignment, num_lines;

    protected NameValue[] lines;

    private boolean mark;


    public Text(){
        super();
    }
    public Text(LineNumberReader in) throws IOException {
        super();
        this.readGeda(in);
    }


    public boolean isAttribute(){
        return (null != this.lines && this.lines[0].isAttribute());
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
    public geda.Text readGeda(LineNumberReader in) throws IOException {
        this.mark = true;

        final int lno = in.getLineNumber();
        final String line = in.readLine();
        StringTokenizer strtok = new StringTokenizer(line," ");
        if (9 == strtok.countTokens()){
            this.x = Integer.parseInt(strtok.nextToken());
            this.y = Integer.parseInt(strtok.nextToken());
            this.color = Integer.parseInt(strtok.nextToken());
            this.size = Integer.parseInt(strtok.nextToken());
            this.visibility = Integer.parseInt(strtok.nextToken());
            this.show_name_value = Integer.parseInt(strtok.nextToken());
            this.angle = Integer.parseInt(strtok.nextToken());
            this.alignment = Integer.parseInt(strtok.nextToken());
            this.num_lines = Integer.parseInt(strtok.nextToken());
            if (0 < this.num_lines){
                NameValue[] lines = new NameValue[this.num_lines];

                for (int cc = 0; cc < this.num_lines; cc++){
                    int ln = in.getLineNumber();
                    lines[cc] = new NameValue(in.readLine());
                }
                this.lines = lines;
            }
            else
                this.lines = null;
        }
        else
            throw new IllegalStateException(String.format("Format error: %d: %s",lno,line));

        return this;
    }
    public geda.Text writeGeda(OutputStream out) throws IOException {

        return this;
    }
}
