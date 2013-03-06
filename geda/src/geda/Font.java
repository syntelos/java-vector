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


public class Font
    extends vector.Container
{
    protected char character;
    protected int width;
    protected boolean flag;

    private boolean mark;


    public Font(){
        super();
    }
    public Font(LineNumberReader in) throws IOException {
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
    public Font readGeda(LineNumberReader in) throws IOException {
        this.mark = true;

        final int lno = in.getLineNumber();
        final String line = in.readLine();
        StringTokenizer strtok = new StringTokenizer(line," ");
        switch (strtok.countTokens()){
        case 2:
            this.character = ' ';
            this.width = Integer.parseInt(strtok.nextToken());
            this.flag = ("1".equals(strtok.nextToken()));
            break;
        case 3:
            this.character = strtok.nextToken().charAt(0);
            this.width = Integer.parseInt(strtok.nextToken());
            this.flag = ("1".equals(strtok.nextToken()));
            break;
        default:
            throw new IllegalStateException(String.format("Format error: %d: %s",lno,line));
        }
        return this;
    }
    public Font writeGeda(OutputStream out) throws IOException {

        return this;
    }
}
