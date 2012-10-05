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
package geda;

import java.io.LineNumberReader;
import java.io.IOException;
import java.io.OutputStream;

import java.util.StringTokenizer;


public class Component
    extends vector.Container
{
    protected int x, y, angle;
    protected boolean selectable, mirror;
    protected String basename;

    private boolean mark;


    public Component(){
        super();
    }
    public Component(LineNumberReader in) throws IOException {
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
    public Component readGeda(LineNumberReader in) throws IOException {
        this.mark = true;

        final int lno = in.getLineNumber();
        final String line = in.readLine();
        StringTokenizer strtok = new StringTokenizer(line," ");
        if (6 <= strtok.countTokens()){
            this.x = Integer.parseInt(strtok.nextToken());
            this.y = Integer.parseInt(strtok.nextToken());
            this.selectable = ("1".equals(strtok.nextToken()));
            this.angle = Integer.parseInt(strtok.nextToken());
            this.mirror = ("1".equals(strtok.nextToken()));
            this.basename = null;
            {
                StringBuilder basename = new StringBuilder();
                boolean once = true;
                while (strtok.hasMoreTokens()){
                    if (once)
                        once = false;
                    else
                        basename.append(' ');

                    basename.append(strtok.nextToken());
                }

                if (0 < basename.length())
                    this.basename = basename.toString();
                else
                    throw new IllegalStateException(String.format("Format error: %d: %s",lno,line));
            }
        }
        else
            throw new IllegalStateException(String.format("Format error: %d: %s",lno,line));

        return this;
    }
    public Component writeGeda(OutputStream out) throws IOException {

        return this;
    }
}
