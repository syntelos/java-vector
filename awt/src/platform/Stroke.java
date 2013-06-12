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
package platform;

import json.Json;
import json.ObjectJson;

import lxl.List;

/**
 * 
 */
public class Stroke
    extends java.awt.BasicStroke
    implements vector.Stroke
{
    /**
     * 
     */
    public enum Join {
        MITER, ROUND, BEVEL;

        public String toString(){
            return this.name().toLowerCase();
        }

        public final static Join Default = MITER;

        public final static Join For(String string){
            if (null == string)
                return Join.Default;
            else {
                try {
                    return Join.valueOf(string.toUpperCase());
                }
                catch (RuntimeException exc){
                    return Join.Default;
                }
            }
        }
        private final static Join[] Values = Join.values();

        public final static Join For(int x){
            return Values[x];
        }
        public final static Join For(Object value){
            if (value instanceof Number)
                return Join.For( ((Number)value).intValue());
            else if (value instanceof String)
                return Join.For( (String)value);
            else
                return Join.Default;
        }
    }
    /**
     * 
     */
    public enum Cap {
        BUTT, ROUND, SQUARE;

        public String toString(){
            return this.name().toLowerCase();
        }

        public final static Cap Default = SQUARE;

        public final static Cap For(String string){
            if (null == string)
                return Cap.Default;
            else {
                try {
                    return Cap.valueOf(string.toUpperCase());
                }
                catch (RuntimeException exc){
                    return Cap.Default;
                }
            }
        }
        private final static Cap[] Values = Cap.values();

        public final static Cap For(int x){
            return Values[x];
        }
        public final static Cap For(Object value){
            if (value instanceof Number)
                return Cap.For( ((Number)value).intValue());
            else if (value instanceof String)
                return Cap.For( (String)value);
            else
                return Cap.Default;
        }
    }

    /**
     * If not null, override any other color definition for the
     * application of this stroke.
     */
    protected Color color;


    public Stroke(Json model){
        this(LineWidth(model),EndCap(model),LineJoin(model),MiterLimit(model),DashArray(model),DashPhase(model),Color(model));
    }
    public Stroke(float lineWidth, int endCap, int lineJoin, float miterLimit, float[] dashArray, float dashPhase){
        super(lineWidth,endCap,lineJoin,miterLimit,dashArray,dashPhase);
    }
    public Stroke(float lineWidth, int endCap, int lineJoin, float miterLimit, float[] dashArray, float dashPhase, Color color){
        super(lineWidth, endCap, lineJoin, miterLimit, dashArray, dashPhase);

        this.color = color;
    }
    public Stroke(float lineWidth){
        super(lineWidth,Cap.Default.ordinal(),Join.Default.ordinal(),MiterLimitDefault,DashArrayDefault,DashPhaseDefault);
    }
    public Stroke(float lineWidth, Color color){
        super(lineWidth,Cap.Default.ordinal(),Join.Default.ordinal(),MiterLimitDefault,DashArrayDefault,DashPhaseDefault);

        this.color = color;
    }
    public Stroke(Object na){
        this( (java.awt.BasicStroke)na);
    }
    public Stroke(java.awt.BasicStroke s){
        super(s.getLineWidth(),s.getEndCap(),s.getLineJoin(),s.getMiterLimit(),s.getDashArray(),s.getDashPhase());
    }


    public boolean hasColor(){
        return (null != this.color);
    }
    public Color getColorVector(){
        return this.color;
    }
    public final String getEndCapString(){

        return Cap.For(this.getEndCap()).toString();
    }
    public final String getLineJoinString(){

        return Join.For(this.getLineJoin()).toString();
    }
    public ObjectJson toJson(){

        ObjectJson thisModel =  new ObjectJson();

        thisModel.setValue("class",this.getClass().getName());
        thisModel.setValue("color",this.color);
        thisModel.setValue("line-width",this.getLineWidth());
        thisModel.setValue("end-cap",this.getEndCapString());
        thisModel.setValue("line-join",this.getLineJoinString());
        thisModel.setValue("miter-limit",this.getMiterLimit());
        thisModel.setValue("dash-array",this.getDashArray());
        thisModel.setValue("dash-phase",this.getDashPhase());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        throw new UnsupportedOperationException();
    }

    public final static Color ColorDefault = null;

    public final static Color Color(Json model){
        Object value = model.getValue("color");

        if (value instanceof Color)
            return (Color)value;
        else
            return ColorDefault;
    }
    public final static float LineWidthDefault = 1.0f;

    public final static float LineWidth(Json model){
        Object value = model.getValue("line-width");

        if (value instanceof Number)
            return ((Number)value).floatValue();
        else
            return LineWidthDefault;
    }
    public final static int EndCap(Json model){
        Object value = model.getValue("end-cap");

        return Cap.For(value).ordinal();
    }
    public final static int LineJoin(Json model){
        Object value = model.getValue("line-join");

        return Join.For(value).ordinal();
    }

    public final static float MiterLimitDefault = 10.0f;

    public final static float MiterLimit(Json model){
        Object value = model.getValue("miter-limit");

        if (value instanceof Number)
            return ((Number)value).floatValue();
        else
            return MiterLimitDefault;
    }

    public final static float[] DashArrayDefault = null;

    public final static float[] DashArray(Json model){
        Json array = model.at("dash-array");

        if (null != array){
            float[] list = array.getValue(float[].class);
            if (null == list || 1 > list.length)
                return DashArrayDefault;
            else 
                return list;
        }
        else
            return DashArrayDefault;
    }

    public final static float DashPhaseDefault = 0.0f;

    public final static float DashPhase(Json model){
        Object value = model.getValue("dash-phase");

        if (value instanceof Number)
            return ((Number)value).floatValue();
        else
            return DashPhaseDefault;
    }
}
