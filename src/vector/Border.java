package vector;

import json.Json;
import json.ObjectJson;

import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * Border for parent.
 */
public class Border
    extends AbstractComponent
{
    public enum Style {
        SQUARE, ROUND;


        public String toString(){
            return this.name().toLowerCase();
        }

        public final static Style For(String string){
            if (null == string)
                return null;
            else {
                try {
                    return Style.valueOf(string.toUpperCase());
                }
                catch (RuntimeException exc){
                    return null;
                }
            }
        }
    }


    protected Color color = Color.black, colorOver;
    protected Style style = Style.SQUARE;
    protected boolean fill = false, fixed = false;
    protected float arc;
    protected Stroke stroke, strokeOver;
    protected Shape shape;


    public Border(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.color = Color.black;
        this.colorOver = null;
        this.style = Style.SQUARE;
        this.fill = false;
        this.fixed = false;
        this.stroke = null;
        this.strokeOver = null;
        this.arc = 0f;
        this.shape = null;
    }
    @Override
    public void destroy(){
        super.destroy();

        this.shape = null;
    }
    @Override
    public void resized(){
        super.resized();

        this.layout();
    }
    @Override
    public void modified(){
        super.modified();

        this.layout();
    }
    public final boolean isFill(){

        return this.fill;
    }
    public final Boolean getFill(){

        if (this.fill)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    public final Border setFill(boolean fill){

        this.fill = fill;
        return this;
    }
    public final Border setFill(Boolean fill){

        if (null != fill)
            return this.setFill(fill.booleanValue());
        else
            return this;
    }
    public final boolean isFixed(){

        return this.fixed;
    }
    public final Boolean getFixed(){

        if (this.fixed)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    public final Border setFixed(boolean fixed){

        this.fixed = fixed;
        return this;
    }
    public final Border setFixed(Boolean fixed){

        if (null != fixed)
            return this.setFixed(fixed.booleanValue());
        else
            return this;
    }
    public final float getArc(){

        return this.arc;
    }
    public final Border setArc(float arc){
        if (0.0f <= arc){
            this.arc = arc;
        }
        return this;
    }
    public final Border setArc(Number arc){

        if (null != arc)
            return this.setArc(arc.floatValue());
        else
            return this;
    }
    public final Color getColor(){

        return this.color;
    }
    public final Border setColor(Color color){
        if (null != color){
            this.color = color;
        }
        return this;
    }
    public final Border setColor(String code){
        if (null != code)
            return this.setColor(new Color(code));
        else
            return this;
    }
    public final Color getColorOver(){

        return this.colorOver;
    }
    public final Border setColorOver(Color colorOver){
        if (null != colorOver){
            this.colorOver = colorOver;
        }
        return this;
    }
    public final Border setColorOver(String code){
        if (null != code)
            return this.setColorOver(new Color(code));
        else
            return this;
    }
    public final Style getStyle(){

        return this.style;
    }
    public final Border setStyle(Style style){
        if (null != style){
            this.style = style;
        }
        return this;
    }
    public final Border setStyle(String code){
        if (null != code)
            return this.setStyle(Style.For(code));
        else
            return this;
    }
    public final Stroke getStroke(){

        return this.stroke;
    }
    public final Border setStroke(Stroke stroke){

        this.stroke = stroke;

        return this;
    }
    public final Stroke getStrokeOver(){

        return this.strokeOver;
    }
    public final Border setStrokeOver(Stroke strokeOver){

        this.strokeOver = strokeOver;

        return this;
    }
    public final Shape getShape(){

        return this.shape;
    }
    public final Border outputScene(Graphics2D g){

        final Shape shape = this.shape;
        if (null != shape){
            final boolean mouseIn = this.mouseIn;

            final Color colorOver = this.colorOver;
            if (null != colorOver && mouseIn) 
                g.setColor(colorOver);
            else
                g.setColor(this.color);

            if (this.fill){

                g.fill(shape);
            }

            final Stroke strokeOver = this.strokeOver;
            if (null != strokeOver && mouseIn){

                if (null != strokeOver.color)
                    g.setColor(strokeOver.color);

                g.setStroke(strokeOver);

                g.draw(shape);
            }
            else if (null != this.stroke){

                if (null != stroke.color)
                    g.setColor(stroke.color);

                g.setStroke(this.stroke);

                g.draw(shape);
            }

        }
        return this;
    }
    public Border outputOverlay(Graphics2D g){

        return this;
    }
    protected void layout(){

        if (!this.fixed){

            this.setBoundsVectorInit(this.getParentVector());
        }

        Bounds bounds = this.getBoundsVector();
        switch(this.style){
        case SQUARE:
            this.shape = bounds;
            break;
        case ROUND:
            this.shape = bounds.round(this.arc);
            break;
        default:
            throw new IllegalStateException(this.style.name());
        }
    }
    public boolean input(Event e){
        if (super.input(e)){

            this.outputScene();
            return true;
        }
        else
            return false;
    }

    public ObjectJson toJson(){

        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("color",this.getColor());
        thisModel.setValue("color-over",this.getColorOver());
        thisModel.setValue("style",this.getStyle());
        thisModel.setValue("fill",this.getFill());
        thisModel.setValue("fixed",this.getFixed());
        thisModel.setValue("arc",this.getArc());
        thisModel.setValue("stroke",this.getStroke());
        thisModel.setValue("stroke-over",this.getStrokeOver());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setColor( thisModel.getValue("color",Color.class));
        this.setColorOver( thisModel.getValue("color-over",Color.class));
        this.setStyle( (String)thisModel.getValue("style"));
        this.setFill( (Boolean)thisModel.getValue("fill"));
        this.setFixed( (Boolean)thisModel.getValue("fixed"));
        this.setArc( (Number)thisModel.getValue("arc"));
        this.setStroke( (Stroke)thisModel.getValue("stroke",Stroke.class));
        this.setStrokeOver( (Stroke)thisModel.getValue("stroke-over",Stroke.class));

        this.modified();

        return true;
    }
}
