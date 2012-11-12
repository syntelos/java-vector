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
 * Interfaces for children of {@link TableSmall} and {@link TableBig}.
 */
public interface Table {
    /**
     * 
     */
    public interface Col
        extends Table
    {
        /**
         * 
         */
        public interface Span
            extends Col
        {
            /**
             * @return Less than one for no effect.  One for single
             * cell, or more
             */
            public int getTableColSpan();
        }
    }

    /**
     * Table cell bounds and (row,col) position
     * 
     * @see TableBig
     * @see TableSmall
     */
    public final class Cell
        extends Bounds
    {
        /**
         * Counting from zero, inclusive
         */
        public final int row, col;
        /**
         * Span minus one: zero for single column; one for
         * <code>"col-span": 2</code>.
         */
        public final int spanCol;
        /**
         * Component list index
         */
        public final int start;


        public Cell(int row, int col, int sc, int ix, double x, double y, double w, double h){
            super(x,y,w,h);
            this.row = row;
            this.col = col;
            this.spanCol = (sc-1);
            this.start = ix;
        }
        public Cell(int row, int col, int sc, int ix, double x, double y, float w, float h){
            super((float)x,(float)y,w,h);
            this.row = row;
            this.col = col;
            this.spanCol = (sc-1);
            this.start = ix;
        }
        /**
         * @param row Row index from zero
         * @param col Column index from zero
         * @param sc Value of property <code>"col-span"</code> will
         * have one subtracted
         */
        public Cell(int row, int col, int sc, int ix){
            super();
            this.row = row;
            this.col = col;
            this.spanCol = (sc-1);
            this.start = ix;
        }
        public Cell(int row, int col, int ix){
            this(row,col,1,ix);
        }


        public void setFrame(double x, double y, double dx, double dy){

            this.x = (float)x;
            this.y = (float)y;
            this.width = (float)dx;
            this.height = (float)dy;
        }

        public String toString(){
            StringBuilder string = new StringBuilder();
            string.append("cell: ");
            string.append(this.start);
            string.append(", col-span: ");
            string.append(this.spanCol);
            string.append(", x: ");
            string.append(this.x);
            string.append(", y: ");
            string.append(this.y);
            string.append(", w: ");
            string.append(this.width);
            string.append(", h: ");
            string.append(this.height);

            return string.toString();
        }
    }

}
