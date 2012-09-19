/*
 * Java Vector
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

import java.awt.geom.Rectangle2D;
import java.util.Arrays;

/**
 * {@link TableSmall} orders staticly dimensioned children to fit a
 * defined number columns (default two), changing only their
 * locations and then calling {@link Component#relocated()}.
 */
public class TableSmall
    extends Container
{


    protected float cellSpacing;

    protected int cols;


    public TableSmall(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.cols = 2;
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
    protected void layout(){

        final int count = this.count();
        if (0 < count){
            final int cols = this.cols;
            if (0 < cols){
                final int rows = (int)Math.ceil((float)count/(float)cols);

                final double cs = this.cellSpacing;

                final double[] colwidths = new double[cols];
                final double[] rowheights = new double[rows];

                Arrays.fill(colwidths,0.0);
                Arrays.fill(rowheights,0.0);

                int  rr, cc, cx;

                measurement:
                for (rr = 0; rr < rows; rr++){

                    for (cc = 0; cc < cols; cc++){

                        cx = ((rr*cols)+cc);

                        if (this.has(cx)){

                            Component c = this.get(cx);
                            Bounds cb = c.getBoundsVector();

                            colwidths[cc] = Math.max(colwidths[cc],(cs+cb.width));
                            rowheights[rr] = Math.max(rowheights[rr],(cs+cb.height));
                        }
                        else
                            break measurement;
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
                        cx = ((rr*cols)+cc);

                        if (this.has(cx)){

                            Component c = this.get(cx);
                            Bounds cb = c.getBoundsVector();
                            c.setBoundsVector(new TableCell(rr,cc,xx,yy,cb.width,cb.height));
                            c.relocated();

                            xx += dx;
                        }
                        else
                            break definition;
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

        Component.Tools.DecodeComponents(this,thisModel);

        this.modified();

        return true;
    }
}
