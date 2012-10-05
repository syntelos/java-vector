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


public class Box
    extends vector.Container
{
    protected int x, y, width, height, color, linewidth, capstyle, dashstyle, dashlength, dashspace, filltype, fillwidth, angle1, pitch1, angle2, pitch2;


    public Box(){
        super();
    }
    public Box(LineNumberReader in) throws IOException {
        super();
        this.readGeda(in);
    }


    public Box readGeda(LineNumberReader in) throws IOException {
        final int lno = in.getLineNumber();
        final String line = in.readLine();
        StringTokenizer strtok = new StringTokenizer(line," ");
        if (16 == strtok.countTokens()){
            this.x = Integer.parseInt(strtok.nextToken());
            this.y = Integer.parseInt(strtok.nextToken());
            this.width = Integer.parseInt(strtok.nextToken());
            this.height = Integer.parseInt(strtok.nextToken());
            this.color = Integer.parseInt(strtok.nextToken());
            this.linewidth = Integer.parseInt(strtok.nextToken());
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
        }
        else
            throw new IllegalStateException(String.format("Format error: %d: %s",lno,line));

        return this;
    }
    public Box writeGeda(OutputStream out) throws IOException {

        return this;
    }
}
