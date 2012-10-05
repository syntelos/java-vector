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


public class Pin
    extends vector.Container
{
    protected int x1, y1, x2, y2, color, pintype, whichend;

    private boolean mark;


    public Pin(){
        super();
    }
    public Pin(LineNumberReader in) throws IOException {
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

            final vector.Path path = (vector.Path)this.addUnique(new vector.Path());
            path.init();
            path.setContent(true);
            path.setColor(ColorMap.Color(this.color));
            path.moveTo(this.x1,this.y1);
            path.lineTo(this.x2,this.y2);
        }
        super.modified();
    }
    public Pin readGeda(LineNumberReader in) throws IOException {
        this.mark = true;

        final int lno = in.getLineNumber();
        final String line = in.readLine();
        StringTokenizer strtok = new StringTokenizer(line," ");
        if (7 == strtok.countTokens()){
            this.x1 = Integer.parseInt(strtok.nextToken());
            this.y1 = Integer.parseInt(strtok.nextToken());
            this.x2 = Integer.parseInt(strtok.nextToken());
            this.y2 = Integer.parseInt(strtok.nextToken());
            this.color = Integer.parseInt(strtok.nextToken());
            this.pintype = Integer.parseInt(strtok.nextToken());
            this.whichend = Integer.parseInt(strtok.nextToken());
        }
        else
            throw new IllegalStateException(String.format("Format error: %d: %s",lno,line));

        return this;
    }
    public Pin writeGeda(OutputStream out) throws IOException {

        return this;
    }
}
