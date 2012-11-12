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

/**
 * {@link TableBig} defines set of equally sized children, changing
 * both size and location and then calling {@link
 * Component#resized()}.
 */
public class TableBig
    extends Container
    implements Component.Layout
{

    protected float cellSpacing;

    protected double cellWidth, cellHeight;

    protected int rows, cols;


    public TableBig(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.cellSpacing = 0f;
        this.cellWidth = 0f;
        this.cellHeight = 0f;
        this.rows = 0;
        this.cols = 0;
    }
    @Override
    public void modified(){
        super.modified();

        this.layout();
    }
    @Override
    public void resized(){
        super.resized();

        this.layout();
    }
    public void layout(){

        final Component.Iterator cit = this.listContent(Component.class);

        final int count = cit.count();

        if (0 < count){
            final Bounds bounds = this.getBoundsVector();
            if (!bounds.isEmpty()){
                if (2 > count){
                    this.cols = 1;
                    this.rows = 1;
                }
                else {
                    this.cols = (int)Math.ceil(Math.sqrt(count));
                    this.rows = (count/this.cols);
                }
                final double ww = ((double)bounds.width-((double)this.cellSpacing*(double)(this.cols+1)));
                final double hh = ((double)bounds.height-((double)this.cellSpacing*(double)(this.rows+1)));

                this.cellWidth = (ww / (double)this.cols);
                this.cellHeight = (hh / (double)this.rows);


                final double x0 = this.cellSpacing;
                final double dx = (x0+this.cellWidth);
                final double dy = (x0+this.cellHeight);

                double xx = x0, yy = x0;

                int  rr, cc = 0, cx, cx1;

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

                layout:
                for (rr = 0; rr < this.rows; rr++){

                    for (cc = 0; cc < this.cols; cc++){

                        Component c;

                        cx = ((rr*this.cols)+cc);
                        if (0 == span){
                            cx1 = cx;

                            if (cit.has(cx)){

                                if (this.isDebug())
                                    DebugTrace.out.printf("[%8s] + rr: %3d, cc: %3d, cx: %3d%n",instance,rr,cc,cx);

                                c = cit.get(cx);
                            }
                            else {
                                if (this.isDebug())
                                    DebugTrace.out.printf("[%8s] - rr: %3d, cc: %3d, cx: %3d%n",instance,rr,cc,cx);

                                break layout;
                            }
                        }
                        else {
                            cx1 = (((rr)*cols)+(cc-span));

                            if (cx1 > cx && cit.has(cx1)){

                                if (this.isDebug())
                                    DebugTrace.out.printf("[%8s] + rr: %3d, cc: %3d, cx: %3d, cx1: %3d, span: %3d%n",instance,rr,cc,cx,cx1,span);

                                c = cit.get(cx);
                            }
                            else {
                                if (this.isDebug())
                                    DebugTrace.out.printf("[%8s] - rr: %3d, cc: %3d, cx: %3d, cx1: %3d, span: %3d%n",instance,rr,cc,cx,cx1,span);

                                continue layout;
                            }
                        }


                        final Table.Cell cell = new Table.Cell(rr,cc,1,cx1,xx,yy,this.cellWidth,this.cellHeight);

                        c.setBoundsVector(cell);
                        c.resized();


                        if (this.isDebug())
                            DebugTrace.out.printf("[%8s] > rr: %3d, cc: %3d, cx: %3d, cx1: %3d, cell: %s%n",instance,rr,cc,cx,cx1,cell);


                        xx += dx;

                        if (c instanceof Table.Col.Span){

                            int s = ((Table.Col.Span)c).getTableColSpan();
                            if (1 < s){
                                span += (s-1);
                            }
                        }
                        cx = cx1;
                    }

                    xx = x0;
                    yy += dy;
                }
            }
        }
    }
    public float getCellSpacing(){
        return this.cellSpacing;
    }
    public TableBig setCellSpacing(float cellSpacing){
        if (0.0 <= cellSpacing){
            this.cellSpacing = cellSpacing;
        }
        return this;
    }
    public TableBig setCellSpacing(Float cellSpacing){
        if (null != cellSpacing)
            return this.setCellSpacing(cellSpacing.floatValue());
        else
            return this;
    }

    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("cellspacing",this.getCellSpacing());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setCellSpacing( thisModel.getValue("cellspacing",Float.class));

        return true;
    }
}
