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
package fonview;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Cursor over index of font files used by {@link Display}
 */
public abstract class Cursor<C>
    extends Object
{
    /**
     * 
     */
    public final static class CF
        extends Cursor<File>
    {

        public CF(File f){
            super(new Index.IF(f),0);
        }


        public boolean next(Display display){
            this.location += 1;
            if (this.location >= this.index.size())
                this.location = 0;
            display.open(this.index.get(this.location));
            return true;
        }
        public boolean previous(Display display){
            this.location -= 1;
            if (0 > this.location)
                this.location = (this.index.size()+this.location);
            display.open(this.index.get(this.location));
            return true;
        }
    }
    /**
     * 
     */
    public final static class CU
        extends Cursor<URL>
    {

        public CU(URL u)
            throws IOException
        {
            super(new Index.IU(u),0);
        }


        public boolean next(Display display){
            this.location += 1;
            if (this.location >= this.index.size())
                this.location = 0;
            display.open(this.index.get(this.location));
            return true;
        }
        public boolean previous(Display display){
            this.location -= 1;
            if (0 > this.location)
                this.location = (this.index.size()+this.location);
            display.open(this.index.get(this.location));
            return true;
        }
    }


    protected final Index<C> index;

    protected int location;


    public Cursor(Index<C> index, int location){
        super();
        if (null != index){
            this.index = index;
            if (-1 < location){
                if (location < index.size())
                    this.location = location;
                else
                    this.location = 0;
            }
            else 
                this.location = (index.size()+location);
        }
        else
            throw new IllegalArgumentException();
    }


    public abstract boolean next(Display display);

    public abstract boolean previous(Display display);

}
