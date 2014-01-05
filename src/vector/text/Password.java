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
package vector.text;

public class Password
    extends Editor
{

    public Password(){
        super();
    }
    public Password(CharSequence string){
        super(string);
    }
    public Password(Home home){
        super(home);
    }
    public Password(Home home, CharSequence string){
        super(home,string);
    }


    protected boolean set(char[] string){
        if (null == string){
            this.logical = null;
            this.visual = null;
        }
        else {
            this.logical = string;
            final int len = string.length;
            {
                final char[] visual = new char[len];

                java.util.Arrays.fill(visual,'*');

                this.visual = visual;
            }
        }
        return true;
    }

}
