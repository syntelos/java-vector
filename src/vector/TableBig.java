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

    protected int rows, cols;


    public TableBig(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.cellSpacing = 0f;
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

        final Table.Layout layout = new Table.Layout(this,this.listContent(Component.class),this.cellSpacing);

        this.rows = layout.rows;
        this.cols = layout.cols;

        layout.resized();
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
