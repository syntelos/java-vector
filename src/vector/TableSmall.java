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

        final Table.Layout layout = new Table.Layout(this,this.listContent(Component.class),this.cols,this.cellSpacing);

        final Bounds bounds = layout.content();

        this.setBoundsVector(bounds);

        /*
         * Layout (order parent)
         */
        final Border border = this.getBorder();
        if (null != border){

            border.resized();
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

    public String propertyNameOfValue(Class vac){
        if (null == vac)
            return null;
        else {
            String name = super.propertyNameOfValue(vac);
            if (null != name)
                return name;
            else {

                if (Float.class.isAssignableFrom(vac))
                    return "cellspacing";
                else if (Integer.class.isAssignableFrom(vac))
                    return "cols";
                else
                    return null;
            }
        }
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
