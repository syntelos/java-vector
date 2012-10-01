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
package vector;

/**
 * General 2D alignment property value
 */
public enum Align {
    LEFT, RIGHT, CENTER, TOP, BOTTOM;


    public String toString(){
        return this.name().toLowerCase();
    }

    public final static Align For(String string){
        if (null == string)
            return null;
        else {
            try {
                return Align.valueOf(string.toUpperCase());
            }
            catch (RuntimeException exc){
                return null;
            }
        }
    }

    /**
     * Horizontal 1D alignment property value
     */
    public enum Horizontal {
        LEFT, CENTER, RIGHT;


        public String toString(){
            return this.name().toLowerCase();
        }

        public final static Horizontal For(String string){
            if (null == string)
                return null;
            else {
                try {
                    return Horizontal.valueOf(string.toUpperCase());
                }
                catch (RuntimeException exc){
                    return null;
                }
            }
        }
    }
    /**
     * Vertical 1D alignment property value
     */
    public enum Vertical {
        TOP, CENTER, BOTTOM;


        public String toString(){
            return this.name().toLowerCase();
        }

        public final static Vertical For(String string){
            if (null == string)
                return null;
            else {
                try {
                    return Vertical.valueOf(string.toUpperCase());
                }
                catch (RuntimeException exc){
                    return null;
                }
            }
        }
    }

}
