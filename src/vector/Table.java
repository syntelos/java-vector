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
package vector;

/**
 * Interfaces for children of {@link TableSmall} and {@link TableSquare}.
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
     * @see TableSquare
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
         * Value of <code>"col-span"</code>, default one
         */
        public final int spanCol;
        /**
         * Component list index
         */
        public final int start;

        public final Component component;


        /**
         * @param row Row index from zero
         * @param col Column index from zero
         * @param sc Value of property <code>"col-span"</code> will
         * have one subtracted
         */
        public Cell(int row, int col, int sc, int ix, Component c){
            super();
            this.row = row;
            this.col = col;

            if (1 < sc)
                this.spanCol = sc;
            else
                this.spanCol = 1;

            this.start = ix;
            this.component = c;
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

    /**
     * Layout function for columns-fixed or square tables.
     */
    public static class Layout
        extends Bounds
    {
        public enum Type {
            /**
             * A fixed number of columns are wrapped over content
             */
            Cols, 
            /**
             * A parent sized square table is applied to content
             */
            Square;
        }


        public final Component parent;
        public final Component.Iterator content;
        public final Type type;
        public final int count, cols, rows;
        public final float cs;
        public final Table.Cell[] index;
        public final float[] colwidths;
        public final float[] rowheights;


        public Layout(Component parent, Component.Iterator content, float cs){
            this(parent, content, Type.Square, 0, cs);
        }
        public Layout(Component parent, Component.Iterator content, int cols, float cs){
            this(parent, content, Type.Cols, cols, cs);
        }
        public Layout(Component parent, Component.Iterator content, Type type, int cols, float cs){
            super(parent.getBoundsVector());
            this.parent = parent;
            this.content = content;
            this.type = type;
            this.count = content.count();

            switch(type){
            case Cols:
                this.cols = cols;
                break;
            case Square:
                this.cols = (int)Math.ceil(Math.sqrt(this.count));
                break;
            default:
                throw new IllegalStateException(type.name());
            }
            this.cs = cs;

            final int cellspace = (int)Math.ceil(count*1.5f);

            this.index = new Table.Cell[cellspace];

            this.colwidths = new float[cellspace];
            this.rowheights = new float[cellspace];

            java.util.Arrays.fill(this.colwidths,0f);
            java.util.Arrays.fill(this.rowheights,0f);

            int  rr, cc, cx = 0, cx1;

            /*
             * spans are applied in sequence order to iterate over
             * a sparse index set
             */
            int spanCols = 0;

            Component c = null;

            measurement:
            for (rr = 0; 0 < this.cols; rr++){

                for (cc = 0; cc < this.cols; cc++){

                    if (0 == spanCols){
                        cx = (((rr)*this.cols)+(cc));

                        cx1 = cx;

                        if (content.has(cx)){

                            c = content.get(cx);
                        }
                        else {
                            break measurement;
                        }
                    }
                    else {
                        cx1 = (((rr)*this.cols)+(cc-spanCols));

                        if (content.has(cx1)){

                            if (cx1 > cx){

                                c = content.get(cx1);

                                cx = cx1;
                            }
                            else {

                                continue measurement;
                            }
                        }
                        else {

                            break measurement;
                        }
                    }

                    final Bounds cb = c.getBoundsVector();

                    if (c instanceof Table.Col.Span){

                        final int s = ((Table.Col.Span)c).getTableColSpan();
                        if (1 < s){

                            spanCols += (s-1);

                            this.index[cx1] = new Table.Cell(rr,cc,s,cx1,c);


                            this.colwidths[cc] = ColW(this.colwidths,cc,cb.width,s,this.cs);
                            this.rowheights[rr] = RowH(this.rowheights[rr],cb.height);
                        }
                        else {

                            this.index[cx1] = new Table.Cell(rr,cc,1,cx1,c);

                            this.colwidths[cc] = ColW(this.colwidths[cc],cb.width);
                            this.rowheights[rr] = RowH(this.rowheights[rr],cb.height);
                        }
                    }
                    else {
                        this.index[cx1] = new Table.Cell(rr,cc,1,cx1,c);

                        this.colwidths[cc] = ColW(this.colwidths[cc],cb.width);
                        this.rowheights[rr] = RowH(this.rowheights[rr],cb.height);
                    }

                    this.index[cx1].setFrameVector(cb);
                }
            }
            this.rows = rr;
        }


        /**
         * Fill a number of columns
         * @return New bounds for caller
         * @see TableSmall
         */
        public final Bounds content(){

            int rr, cc, cx = 0, cx1;
            /*
             * spans are applied in sequence order to iterate over
             * a sparse index set
             */
            int spanCols = 0;

            Table.Cell cell = null;

            float dx, dy, dw, dh, xx = this.cs, yy = this.cs;

            this.width = (2f*this.cs);
            this.height = this.width;


            for (rr = 0; rr < this.rows; rr++){

                dy = rowheights[rr];
                dh = (dy+this.cs);
                this.height += dh;

                for (cc = 0; cc < this.cols; cc++){

                    dx = this.colwidths[cc];
                    dw = (dx+this.cs);

                    if (0 == rr){

                        this.width += dw;
                    }

                    if (0 == spanCols){
                        cx = (((rr)*this.cols)+(cc));

                        cx1 = cx;

                        cell = this.index[cx1];

                        if (null != cell){

                            cell.setFrame(xx,yy,dx,dy);

                            cell.component.setBoundsVector(cell);
                            cell.component.relocated();

                            spanCols += (cell.spanCol-1);
                        }
                        else
                            return this;
                    }
                    else {

                        cx1 = (((rr)*this.cols)+(cc-spanCols));

                        if (cx1 > cx){

                            cx = cx1;

                            cell = this.index[cx1];

                            if (null != cell){
                                cell.setFrame(xx,yy,dx,dy);

                                cell.component.setBoundsVector(cell);
                                cell.component.relocated();

                                spanCols += (cell.spanCol-1);
                            }
                            else
                                return this;
                        }
                        else {

                            cell.width += dx;

                            cell.component.setBoundsVector(cell);
                            cell.component.relocated();
                        }
                    }

                    xx += dw;
                }
                xx = cs;
                yy += dh;
            }
            return this;
        }
        /**
         * Fill a square table
         * @see TableSquare
         */
        public void square(){

            final Bounds bounds = this.parent.getBoundsVector();

            final double ww = ((double)bounds.width-((double)this.cs*(double)(this.cols+1)));
            final double hh = ((double)bounds.height-((double)this.cs*(double)(this.rows+1)));

            final float cellWidth = (float)(ww / (double)this.cols);
            final float cellHeight = (float)(hh / (double)this.rows);

            final float x0 = this.cs;
            final float dx = (x0+cellWidth);
            final float dy = (x0+cellHeight);

            float xx = x0, yy = x0;

            int  rr = 0, cc = 0, cx = 0, cx1;

            final int count = this.index.length;


            for (int idx = 0; idx < count; idx++){

                Table.Cell cell = this.index[idx];

                if (null != cell){

                    if (rr != cell.row){
                        yy += dy;
                        rr = cell.row;
                        xx = x0;
                        cc = cell.col;
                    }
                    else if (cc != cell.col){
                        if (0 == cell.col)
                            xx = x0;
                        else
                            xx += (dx*cell.spanCol);

                        cc = cell.col;
                    }

                    cell.setFrame(xx,yy,((cellWidth*cell.spanCol)+(this.cs*(cell.spanCol-1))),cellHeight);

                    cell.component.setBoundsVector(cell);

                    cell.component.resized();
                }
                else
                    return;
            }
        }

        public final static float ColW(float a, float b){
            return Math.max(a,b);
        }
        public final static float ColW(float[] list, int lix, float cw, int span, float cs){

            final int check = lix+span;

            float sum = 0;
            for (int li = lix; li < check; li++){
                sum += list[li];
            }

            sum += (cs * (span-1));

            if (sum < cw){

                float add = (cw-sum)/span;

                for (int li = lix; li < check; li++){
                    list[li] += add;
                }
            }
            return list[lix];
        }
        public final static float RowH(float a, float b){
            return Math.max(a,b);
        }
    }

}
