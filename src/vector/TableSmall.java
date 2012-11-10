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

import json.Json;
import json.ObjectJson;

import java.util.Arrays;

/**
 * {@link TableSmall} orders staticly dimensioned children to fit a
 * defined number columns (default two), changing only their
 * locations and then calling {@link Component#relocated()}.
 */
public class TableSmall
    extends Container
    implements Component.Layout
{


    protected float cellSpacing;

    protected int cols;


    public TableSmall(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.cellSpacing = 0f;
        this.cols = 2;
    }
    @Override
    public void modified(){

        this.content = false;
        this.parent = false;
        this.scale = false;

        super.modified();

        this.layout();
    }
    @Override
    public void resized(){

        this.content = false;
        this.parent = false;
        this.scale = false;

        super.resized();

        this.layout();
    }
    public Component.Layout.Order queryLayout(){

        return Component.Layout.Order.Content;
    }
    public void layout(Component.Layout.Order order){

        if (Component.Layout.Order.Content != order)
            throw new UnsupportedOperationException();
        else
            this.modified();
    }
    public void layout(){

        final Component.Iterator cit = this.listContent(Component.class);

        final int count = cit.count();

        if (0 < count){
            final int cols = this.cols;
            if (0 < cols){
                final int rows = (int)Math.ceil((float)count/(float)cols);

                final int[] index = new int[rows*cols];

                final double cs = this.cellSpacing;

                final double[] colwidths = new double[cols];
                final double[] rowheights = new double[rows];

                Arrays.fill(colwidths,0.0);
                Arrays.fill(rowheights,0.0);

                int  rr, cc, cx = 0, cx1;

                /*
                 * spans are applied in sequence order to iterate over
                 * a sparse index set
                 */
                int span = 0;

                final String instance;
                if (this.isDebug())
                    instance = Integer.toHexString(System.identityHashCode(this)).toUpperCase();
                else
                    instance = null;


                measurement:
                for (rr = 0; rr < rows; rr++){

                    for (cc = 0; cc < cols; cc++){

                        Component c;

                        if (0 == span){
                            cx = (((rr)*cols)+(cc));

                            index[cx] = cx; //

                            if (cit.has(cx)){

                                if (this.isDebug())
                                    DebugTrace.out.printf("[%8s] + rr: %3d, cc: %3d, cx: %3d%n",instance,rr,cc,cx);

                                c = cit.get(cx);
                            }
                            else {
                                if (this.isDebug())
                                    DebugTrace.out.printf("[%8s] - rr: %3d, cc: %3d, cx: %3d%n",instance,rr,cc,cx);

                                break measurement;
                            }
                        }
                        else {
                            cx1 = (((rr)*cols)+(cc-span));

                            if (cx1 > cx && this.has(cx1)){

                                if (this.isDebug())
                                    DebugTrace.out.printf("[%8s] + rr: %3d, cc: %3d, cx: %3d, cx1: %3d, span: %3d%n",instance,rr,cc,cx,cx1,span);

                                index[cx1] = cx1; //

                                c = cit.get(cx1);
                                cx = cx1;
                            }
                            else {
                                if (this.isDebug())
                                    DebugTrace.out.printf("[%8s] - rr: %3d, cc: %3d, cx: %3d, cx1: %3d, span: %3d%n",instance,rr,cc,cx,cx1,span);

                                continue measurement;
                            }
                        }

                        Bounds cb = c.getBoundsVector();

                        colwidths[cc] = Math.max(colwidths[cc],(cs+cb.width));
                        rowheights[rr] = Math.max(rowheights[rr],(cs+cb.height));

                        if (c instanceof Table.Col.Span){

                            int s = ((Table.Col.Span)c).getTableColSpan();
                            if (1 < s){
                                span += (s-1);
                            }
                        }
                    }
                }


                double dx, dy, xx = cs, yy = cs;

                double tableWidth = (2.0*cs), tableHeight = tableWidth;

                definition:
                for (rr = 0; rr < rows; rr++){

                    dy = rowheights[rr];
                    tableHeight += dy;

                    for (cc = 0; cc < cols; cc++){

                        dx = colwidths[cc];

                        if (0 == rr){

                            tableWidth += dx;
                        }

                        Component c;
                        cx = (((rr)*cols)+(cc));
                        cx1 = index[cx];

                        if (cx1 >= cx && cit.has(cx1))
                            c = cit.get(cx1);
                        else
                            break definition;


                        final Bounds cb = c.getBoundsVector();
                        final Table.Cell cell = new Table.Cell(rr,cc,xx,yy,cb.width,cb.height);
                        c.setBoundsVector(cell);
                        c.relocated();

                        if (this.isDebug())
                            DebugTrace.out.printf("[%8s] > rr: %3d, cc: %3d, cx: %3d, cx1: %3d, cell: %s%n",instance,rr,cc,cx,cx1,cell);

                        xx += dx;
                    }
                    xx = cs;
                    yy += dy;
                }

                final Bounds bounds = this.getBoundsVector();
                bounds.width = (float)tableWidth;
                bounds.height = (float)tableHeight;

                this.setBoundsVector(bounds);
            }
        }
    }
    public final int getCols(){
        return this.cols;
    }
    public final TableSmall setCols(int cols){
        if (0 < cols){
            this.cols = cols;
        }
        return this;
    }
    public final TableSmall setCols(Integer cols){
        if (null != cols)
            return this.setCols(cols.intValue());
        else
            return this;
    }
    public float getCellSpacing(){
        return this.cellSpacing;
    }
    public TableSmall setCellSpacing(float cellSpacing){
        if (0.0 <= cellSpacing){
            this.cellSpacing = cellSpacing;
        }
        return this;
    }
    public TableSmall setCellSpacing(Float cellSpacing){
        if (null != cellSpacing)
            return this.setCellSpacing(cellSpacing.floatValue());
        else
            return this;
    }

    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("cols",this.getCols());

        thisModel.setValue("cellspacing",this.getCellSpacing());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setCols( (Integer)thisModel.getValue("cols",Integer.class));

        this.setCellSpacing( thisModel.getValue("cellspacing",Float.class));

        return true;
    }
}
