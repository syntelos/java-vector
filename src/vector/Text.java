package vector;

import json.Json;
import json.ObjectJson;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


/**
 * Text line
 */
public class Text
    extends AbstractComponent
    implements CharSequence
{

    protected Font font = Font.Default;

    protected Color color = Color.black;

    protected boolean outline = false, fixed = false;

    protected int cols = 25;

    protected CharSequence string;

    protected GlyphVector vector;

    protected Shape shape;

    private float[] localPositions;


    public Text(){
        super();
    }


    /**
     * This method will not remove the text string, itself, in order
     * that a subclass may be defined for a static text string.
     * Subclasses, however, are free to set the string to null in an
     * overriding method.
     */
    @Override
    public void destroy(){
        super.destroy();

        this.vector = null;
        this.shape = null;
        this.localPositions = null;
    }
    /**
     * Calls {@link #layout()}
     */
    @Override
    public void resized(){
        super.resized();

        this.layout();
    }
    /**
     * Clears the visual state derived from properties, and calls
     * {@link #layout()}
     */
    @Override
    public void modified(){
        super.modified();

        this.vector = null;
        this.shape = null;
        this.localPositions = null;

        this.layout();
    }
    public final Font getFont(){

        return this.font;
    }
    public final Text setFont(Font font){
        if (null != font){
            this.font = font;
            this.modified();
            return this;
        }
        else
            throw new IllegalArgumentException();
    }
    public final Text setFont(java.awt.Font font){
        if (font instanceof Font)
            return this.setFont((Font)font);
        else
            return this.setFont(new Font(font));
    }
    public final Text setFont(String code){
        if (null != code)
            return this.setFont(Font.decode(code));
        else
            return this;
    }
    public final Color getColor(){

        return this.color;
    }
    public final Text setColor(Color color){
        if (null != color){
            this.color = color;
        }
        return this;
    }
    public final Text setColor(String code){
        if (null != code)
            return this.setColor(new Color(code));
        else
            return this;
    }
    public final boolean isOutline(){
        return this.outline;
    }
    public final Boolean getOutline(){
        return this.outline;
    }
    public final Text setOutline(boolean outline){
        this.outline = outline;
        return this;
    }
    public final Text setOutline(Boolean outline){
        if (null != outline)
            this.outline = outline;
        return this;
    }
    public final boolean isFixed(){
        return this.fixed;
    }
    public final Boolean getFixed(){
        return this.fixed;
    }
    public final Text setFixed(boolean fixed){
        this.fixed = fixed;
        return this;
    }
    public final Text setFixed(Boolean fixed){
        if (null != fixed){
            this.fixed = fixed;

            this.modified();
        }
        return this;
    }
    public final String getText(){
        return this.toString();
    }
    public Text setText(String text){

        if (null != text && 0 < text.length())

            this.string = text;
        else
            this.string = null;

        this.modified();
        return this;
    }
    public final int getCols(){
        return this.cols;
    }
    public final Text setCols(int cols){
        if (0 < cols){
            this.cols = cols;

            this.modified();
        }
        return this;
    }
    public final Text setCols(Integer cols){
        if (null != cols)
            return this.setCols(cols.intValue());
        else
            return this;
    }
    public final boolean isEmpty(){
        return (null == this.string || 0 == this.string.length());
    }
    public final int length(){
        if (null == this.string)
            return 0;
        else
            return this.string.length();
    }
    public final char charAt(int idx){
        CharSequence string = this.string;
        if (null != string)
            return string.charAt(idx);
        else
            throw new IndexOutOfBoundsException(String.format("[%d]",idx));
    }
    public final CharSequence subSequence(int start, int end){
        CharSequence string = this.string;
        if (null != string)
            return string.subSequence(start,end);
        else
            throw new IndexOutOfBoundsException(String.format("[%d,%d)",start,end));
    }
    public final String toString(){

        if (null != this.string)
            return this.string.toString();
        else
            return new String();
    }
    /**
     * @return Baseline origin in shape coordinate space 
     */
    public Point2D.Float getShapeBaseline(){

        final float x = this.font.padding.left;
        final float y = (this.font.ascent + this.font.padding.top);

        return new Point2D.Float(x,y);
    }
    protected Shape shape(){
        if (null == this.shape && 0 < this.length()){

            final Point2D.Float baseline = this.getShapeBaseline();

            this.vector = font.createGlyphVector(this.string);

            this.shape = this.vector.getOutline(baseline.x,baseline.y);

            this.localPositions = null;
        }
        return this.shape;
    }
    protected GlyphVector vector(){
        if (null == this.vector){
            if (null != this.shape())
                return this.vector;
            else
                return null;
        }
        else
            return this.vector;
    }
    private float[] positions(){
        if (null == this.localPositions){

            final int c = this.length();

            if (0 < c){
                final Point2D.Float baseline = this.getShapeBaseline();

                final int count = (c+1);

                final GlyphVector vector = this.vector();

                if (null != vector){
                    this.localPositions = vector.getGlyphPositions(0,count,new float[count<<1]);

                    for (int cc = 0; cc < count; cc++){
                        int ix = (cc<<1);
                        int iy = (ix+1);
                        this.localPositions[ix] += baseline.x;
                        this.localPositions[iy] += baseline.y;
                    }
                    return this.localPositions;
                }
                else
                    return new float[0];
            }
            else
                return new float[0];
        }
        else
            return this.localPositions;
    }
    /**
     * Glyph position coordinates in shape coordinate space.
     * 
     * <pre>
     * ix = (idx*2);
     * iy = (ix+1);
     * </pre>
     * @return An array of coordinates in order
     * <i>{Xo,Yo,...,Xn,Yn}</i>, or an array of length zero
     */
    public final float[] getPositionsLocal(){

        return this.positions().clone();
    }
    /**
     * Shape coordinate space
     */
    public final Point2D.Float getTopLeft(int idx){

        final int ix = (idx<<1);

        return new Point2D.Float(this.positions()[ix],this.font.padding.top);
    }
    /**
     * Shape coordinate space
     */
    public final Point2D.Float getTopRight(int idx){

        final int ix = (idx+1)<<1;

        return new Point2D.Float(this.positions()[ix],this.font.padding.top);
    }
    /**
     * Shape coordinate space
     */
    public final Point2D.Float getBaseline(int idx){

        final float[] positions = this.positions();

        final int ix = (idx<<1);
        final int iy = (ix+1);

        return new Point2D.Float(positions[ix],positions[iy]);
    }
    /**
     * @param start Inclusive glyph index
     * @param end Exclusive glyph index
     * @return Font bounding box for indexed glyphs in shape
     * coordinate space
     */
    public final Rectangle2D.Float subArea(int start, int end){

        final float[] positions = this.positions();

        final float x1 = positions[start<<1];
        final float y1 = this.font.padding.top;
        final float x2 = positions[(end-1)<<1];
        final float y2 = (y1+this.font.height);

        return new Rectangle2D.Float(x1,y1,(x2-x1),(y2-y1));
    }
    /**
     * @return Font bounding box for all glyphs in shape coordinate
     * space
     */
    public final Rectangle2D.Float shapeArea(){

        return this.font.boundingBox(1,this.shapeAreaWidth());
    }
    protected float shapeAreaWidth(){
        if (this.fixed)
            return this.font.em*this.getCols();
        else {
            Shape shape = this.shape();
            if (null != shape){
                Rectangle2D bounds = shape.getBounds2D();
                return (float)(bounds.getX()+bounds.getWidth());
            }
            else {
                return this.font.em*this.getCols();
            }
        }
    }
    public Text outputScene(Graphics2D g){
        Shape shape = this.shape();
        if (null != shape){
            g.transform(this.getTransformParent());
            g.setColor(this.getColor());
            if (this.outline)
                g.draw(shape);
            else
                g.fill(shape);
        }
        return this;
    }
    public Text outputOverlay(Graphics2D g){

        return this;
    }
    public ObjectJson toJson(){
        ObjectJson thisModel = (ObjectJson)super.toJson();

        thisModel.setValue("font", this.getFont().toString());

        thisModel.setValue("color", this.getColor());

        thisModel.setValue("outline",this.getOutline());

        thisModel.setValue("fixed",this.getFixed());

        thisModel.setValue("cols",this.getCols());

        thisModel.setValue("text", this.toString());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){
        super.fromJson(thisModel);

        this.setFont( (String)thisModel.getValue("font"));

        this.setText( (String)thisModel.getValue("text"));

        this.setColor( (Color)thisModel.getValue("color",Color.class));

        this.setOutline( (Boolean)thisModel.getValue("outline"));

        this.setFixed( (Boolean)thisModel.getValue("fixed"));

        this.setCols( (Integer)thisModel.getValue("cols",Integer.class));

        return true;
    }
    /**
     * Called from {@link #modified()} and {@link #resized()}.  
     * 
     * When not fixed this method, defined here, calls {@link
     * #resizeToParent()} and {@link #layoutScaleToDimensions()}.
     * 
     * When fixed this method calls {@link #resizeToShapeArea()} and
     * {@link #layoutScaleToShapeArea()}.
     */
    protected void layout(){
        if (this.fixed){
            this.resizeToShapeArea();
            this.layoutScaleToShapeArea();
        }
        else {
            this.resizeToParent();
            this.layoutScaleToDimensions();
        }
    }
    /**
     * May be called from {@link #layout()} to scale the local
     * transform to fit the padded font-text to the dimensions.
     */
    protected void layoutScaleToDimensions(){
        Rectangle2D.Float shape = this.shapeArea();
        if (null != shape)
            this.scaleTransformLocalAbsolute(shape);
        else
            this.setTransformLocal(1f,1f);
    }
    /**
     * May be called from {@link #layout()} to set dimensions from
     * parent.
     */
    protected void resizeToParent(){
        this.setBoundsVectorInit(this.getParentVector());
    }
    /**
     * May be called from {@link #layout()} to set dimensions from
     * {@link #shapeArea()}.
     */
    protected void resizeToShapeArea(){
        this.setBoundsVectorInit(this.shapeArea());
    }
    /**
     * Define transform local as 1:1 scale.
     */
    protected void layoutScaleToShapeArea(){
        this.setTransformLocal(1f,1f);
    }

    /**
     * @return Null or non empty 
     */
    public final static char[] ToCharArray(char[] cary){
        if (null == cary || 1 > cary.length)
            return null;
        else
            return cary;
    }
    /**
     * @return Null or non empty 
     */
    public final static char[] ToCharArray(CharSequence string){

        if (null == string || 1 > string.length())
            return null;
        else if (string instanceof String)
            return ((String)string).toCharArray();
        else {
            final int count = string.length();
            if (0 < count){
                char[] cary = new char[count];
                for (int cc = 0; cc < count; cc++){

                    cary[cc] = string.charAt(cc);
                }
                return cary;
            }
            else
                return null;
        }
    }
}
