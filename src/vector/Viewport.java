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
package vector;

import platform.geom.Rectangle;

/**
 * 
 */
public class Viewport
    extends lxl.ArrayList<Bounds>
{
    public enum Size
        implements ActionLabel
    {
        Eighth(0.125,"Eighth"), Quarter(0.25,"Quarter"), Half(0.5,"Half"), Unit(1.0,"Unit"), OnePlusQuarter(1.25,"One + Quarter"), OnePlusHalf(1.5,"One + Half"), Double(2.0,"Double");


        protected final static Size[] Sizes = Size.values();

        public final static Size[] Sizes(){

            return Sizes.clone();
        }

        public final static int Columns;
        static {
            int column = 0;
            for (Size size: Sizes){
                column = Math.max(column,size.label.length());
            }
            Columns = column;
        }

        /**
         * Accepts camel strings
         * <pre>
         * Eighth eighth
         * OnePlusQuarter onePlusQuarter  one-plus-quarter
         * <pre>
         * as well as factors
         * <pre>
         * 0.125
         * 1.25
         * <pre>
         */
        public final static Size For(String string){
            if (null != string && 0 < string.length()){
                try {
                    return Size.valueOf(Component.Tools.Camel(string));
                }
                catch (RuntimeException exc1){
                    try {
                        double number = java.lang.Double.parseDouble(string);

                        for (Size size: Size.Sizes){

                            if (size.factor == number){
                                return size;
                            }
                        }
                    }
                    catch (RuntimeException exc2){
                    }
                }
            }
            return null;
        }


        public final double factor;

        public final String label;


        private Size(double factor, String label){
            this.factor = factor;
            this.label = label;
        }


        public String getActionLabel(){
            return this.label;
        }
        public Bounds get(Viewport viewport){
            if (null != viewport)
                return viewport.get(this.ordinal());
            else
                return null;
        }
    }


    public Viewport(Rectangle prime){
        super();
        for (Size size: Size.Sizes){
            this.add(new Bounds(prime,size.factor));
        }
    }
    public Viewport(double w, double h){
        super();
        for (Size size: Size.Sizes){
            this.add(new Bounds(w,h,size.factor));
        }
    }


    public Bounds get(Size size){
        if (null != size)
            return this.get(size.ordinal());
        else
            return this.get(Size.Unit.ordinal());
    }
    public Viewport.Size search(Rectangle search){

        final int count = this.size();
        for (int cc = 0; cc < count; cc++){

            Bounds b = this.get(cc);
            if (b.contains(search))
                return Size.Sizes[cc];
        }
        return null;
    }
}
