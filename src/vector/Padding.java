package vector;

import java.util.StringTokenizer;

/**
 * Internal padding (as opposed to external margin) is contained
 * within the boundaries of a {@link Component}.
 * 
 * @see Component
 * @see Text
 */
public class Padding
    extends Object
    implements Cloneable
{
    public final static Padding Default(){
        return new Padding(4,4);
    }
    public final static Padding Nil(){
        return new Padding(0,0);
    }


    public float left, right, top, bottom;


    public Padding(){
        super();
    }
    public Padding(float p){
        this(p,p);
    }
    public Padding(float w, float h){
        super();
        this.set(w,h);
    }
    public Padding(float left, float right, float top, float bottom){
        super();
        this.set(left, right, top, bottom);
    }
    public Padding(double left, double right, double top, double bottom){
        super();
        this.set(left, right, top, bottom);
    }
    public Padding(String code){
        super();
        StringTokenizer strtok = new StringTokenizer(code,")(,");
        switch(strtok.countTokens()){
        case 2:{
            final float w = Float.parseFloat(strtok.nextToken());
            final float h = Float.parseFloat(strtok.nextToken());
            this.set(w,h);
            break;
        }
        case 4:{
            final float l = Float.parseFloat(strtok.nextToken());
            final float r = Float.parseFloat(strtok.nextToken());
            final float t = Float.parseFloat(strtok.nextToken());
            final float b = Float.parseFloat(strtok.nextToken());
            this.set(l,r,t,b);
            break;
        }
        default:
            throw new IllegalArgumentException(code);
        }
    }


    public float getWidth(){
        return (this.left + this.right);
    }
    public float getHeight(){
        return (this.top + this.bottom);
    }
    public Padding set(java.awt.geom.RectangularShape rect){

        return this.set(rect.getX(), 0.0, rect.getY(), 0.0);
    }
    public Padding set(float left, float right, float top, float bottom){
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        return this;
    }
    public Padding set(double left, double right, double top, double bottom){
        this.left = (float)left;
        this.right = (float)right;
        this.top = (float)top;
        this.bottom = (float)bottom;
        return this;
    }
    public Padding set(float w, float h){
        float w2 = (w/2.0f);
        float h2 = (h/2.0f);
        this.left = w2;
        this.right = w2;
        this.top = h2;
        this.bottom = h2;
        return this;
    }
    public Padding clone(){
        try {
            return (Padding)super.clone();
        }
        catch (CloneNotSupportedException exc){

            throw new InternalError();
        }
    }
    protected StringBuilder toStringBuilder(){
        StringBuilder string = new StringBuilder();
        string.append('(');
        string.append(this.left);
        string.append(',');
        string.append(this.right);
        string.append(',');
        string.append(this.top);
        string.append(',');
        string.append(this.bottom);
        string.append(')');
        return string;
    }
    public final String toString(){
        return this.toStringBuilder().toString();
    }
}
