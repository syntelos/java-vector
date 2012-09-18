package vector;

import json.Json;
import json.ObjectJson;

import lxl.List;

/**
 * Stroke binding
 */
public class Stroke
    extends java.awt.BasicStroke
    implements json.Builder
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
    public final Color color;


    public Stroke(Json model){
        super(Stroke.LineWidth(model),Stroke.EndCap(model),Stroke.LineJoin(model),Stroke.Miterlimit(model),
              Stroke.DashArray(model),Stroke.DashPhase(model));

        this.color = model.getValue("color",Color.class);
    }
    public Stroke(float lineWidth, Cap endCap, Join lineJoin, float miterLimit, float[] dashArray, float dashPhase, Color color){
        super(lineWidth,endCap.ordinal(),lineJoin.ordinal(),miterLimit,dashArray,dashPhase);
        this.color = color;
    }
    public Stroke(float lineWidth, Color color){
        this(lineWidth,Cap.Default,Join.Default,10.0f,null,1.0f,color);
    }


    public final boolean hasColor(){
        return (null != this.color);
    }
    public final Color getColor(){
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


    public final static float LineWidth(Json model){
        Object value = model.getValue("line-width");

        if (value instanceof Number)
            return ((Number)value).floatValue();
        else
            return 1.0f;
    }
    public final static int EndCap(Json model){
        Object value = model.getValue("end-cap");

        return Cap.For(value).ordinal();
    }
    public final static int LineJoin(Json model){
        Object value = model.getValue("line-join");

        return Join.For(value).ordinal();
    }
    public final static float Miterlimit(Json model){
        Object value = model.getValue("miter-limit");

        if (value instanceof Number)
            return ((Number)value).floatValue();
        else
            return 10.0f;
    }
    public final static float[] DashArray(Json model){
        Object value = model.getValue("dash-array");

        if (value instanceof List){
            Number[] list = (Number[])((List)value).toArray(Number.class);
            if (null == list)
                return null;
            else {
                final int count = list.length;
                if (1 > count)
                    return null;
                else {

                    final float[] re = new float[count];

                    for (int cc = 0; cc < count; cc++){

                        Number n = list[cc];

                        if (null != n)
                            re[cc] = n.floatValue();
                        else
                            throw new IllegalArgumentException(value.toString());
                    }
                    return re;
                }
            }
        }
        else if (value instanceof float[]){

            return (float[])value;
        }
        else
            return null;
    }
    public final static float DashPhase(Json model){
        Object value = model.getValue("dash-phase");

        if (value instanceof Number)
            return ((Number)value).floatValue();
        else
            return 0.0f;
    }
}
