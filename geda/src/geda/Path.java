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

import java.awt.Graphics2D;

import java.io.LineNumberReader;
import java.io.IOException;
import java.io.OutputStream;

import java.util.StringTokenizer;


public class Path
    extends vector.Container
{
    protected int color, width, capstyle, dashstyle, dashlength, dashspace, filltype, fillwidth, angle1, pitch1, angle2, pitch2, numlines;
    protected String[] lines;
    protected String lines_buffer;

    protected boolean mark;


    public Path(){
        super();
    }
    public Path(LineNumberReader in) throws IOException {
        super();
        this.readGeda(in);
    }


    @Override
    public void init(){
        super.init();

        this.mark = false;
        this.content = true;
    }
    @Override
    public void modified(){
        if (this.mark){
            this.mark = false;
            this.init();

        }
        super.modified();
    }
    public Path readGeda(LineNumberReader in) throws IOException {
        this.mark = true;

        final int lno = in.getLineNumber();
        final String line = in.readLine();
        StringTokenizer strtok = new StringTokenizer(line," ");
        if (13 == strtok.countTokens()){
            this.color = Integer.parseInt(strtok.nextToken());
            this.width = Integer.parseInt(strtok.nextToken());
            this.capstyle = Integer.parseInt(strtok.nextToken());
            this.dashstyle = Integer.parseInt(strtok.nextToken());
            this.dashlength = Integer.parseInt(strtok.nextToken());
            this.dashspace = Integer.parseInt(strtok.nextToken());
            this.filltype = Integer.parseInt(strtok.nextToken());
            this.fillwidth = Integer.parseInt(strtok.nextToken());
            this.angle1 = Integer.parseInt(strtok.nextToken());
            this.pitch1 = Integer.parseInt(strtok.nextToken());
            this.angle2 = Integer.parseInt(strtok.nextToken());
            this.pitch2 = Integer.parseInt(strtok.nextToken());
            this.numlines = Integer.parseInt(strtok.nextToken());
            this.lines = null;
            this.lines_buffer = null;
            if (0 < this.numlines){
                StringBuilder lines_buffer = new StringBuilder();
                String[] lines = new String[this.numlines];
                for (int cc = 0; cc < this.numlines; cc++){
                    int ln = in.getLineNumber();
                    String li = in.readLine();
                    lines[cc] = li;
                    if (0 < cc){
                        lines_buffer.append(' ');
                    }
                    lines_buffer.append(li);
                }
                this.lines = lines;
                this.lines_buffer = lines_buffer.toString();
            }
        }
        else
            throw new IllegalStateException(String.format("Format error: %d: %s",lno,line));

        return this;
    }
    public Path writeGeda(OutputStream out) throws IOException {

        return this;
    }
}
