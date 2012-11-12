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

                final Table.Cell[] index = new Table.Cell[count];

                final double cs = this.cellSpacing;

                final double[] colwidths = new double[count];
                final double[] rowheights = new double[count];

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

                char debug = ' ';

                Component c = null;

                measurement:
                for (rr = 0; ; rr++){

                    for (cc = 0; cc < cols; cc++){

                        if (0 == span){
                            cx = (((rr)*cols)+(cc));

                            cx1 = cx;

                            if (cit.has(cx)){

                                debug = '+';

                                c = cit.get(cx);
                            }
                            else {
                                debug = '-';

                                break measurement;
                            }
                        }
                        else {
                            cx1 = (((rr)*cols)+(cc-span));

                            if (cit.has(cx1)){

                                if (cx1 > cx){

                                    debug = '*';

                                    c = cit.get(cx1);
                                }
                                else {
                                    debug = '/';

                                    continue measurement;
                                }
                            }
                            else {
                                debug = '|';

                                break measurement;
                            }
                        }

                        Bounds cb = c.getBoundsVector();

                        colwidths[cc] = Math.max(colwidths[cc],(cs+cb.width));
                        rowheights[rr] = Math.max(rowheights[rr],(cs+cb.height));

                        if (c instanceof Table.Col.Span){

                            int s = ((Table.Col.Span)c).getTableColSpan();
                            if (1 < s){

                                span += (s-1);

                                index[cx1] = new Table.Cell(rr,cc,s,cx1);
                            }
                            else
                                index[cx1] = new Table.Cell(rr,cc,cx1);
                        }
                        else
                            index[cx1] = new Table.Cell(rr,cc,cx1);

                        index[cx1].setFrame(cb);


                        if ('*' == debug)
                            cx = cx1;
                    }
                }

                final int rows = rr;


                span = 0;
                c = null;

                Table.Cell cell = null;

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

                        if (0 == span){
                            cx = (((rr)*cols)+(cc));

                            cx1 = cx;

                            if (cit.has(cx)){

                                c = cit.get(cx);

                                cell = index[cx1];

                                cell.setFrame(xx,yy,dx,dy);

                                c.setBoundsVector(cell);
                                c.relocated();

                                span += cell.spanCol;
                            }
                            else
                                break definition;
                        }
                        else {

                            cx1 = (((rr)*cols)+(cc-span));

                            if (cx1 > cx && cit.has(cx1)){

                                c = cit.get(cx1);

                                cx = cx1;

                                cell = index[cx1];

                                cell.setFrame(xx,yy,dx,dy);

                                c.setBoundsVector(cell);
                                c.relocated();

                                span += cell.spanCol;
                            }
                            else {

                                cell.width += dx;

                                c.setBoundsVector(cell);
                                c.relocated();
                            }
                        }

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
