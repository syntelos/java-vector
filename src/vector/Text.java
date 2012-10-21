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

import vector.font.GlyphVector;
import vector.text.Visual;

import platform.Color;
import platform.Font;
import platform.Path;
import platform.Shape;
import platform.Transform;
import platform.geom.Point;
import platform.geom.Rectangle;

import json.Json;
import json.ObjectJson;



/**
 * Attributed text includes unicode string, color and font.  
 * 
 * Note that {@link TextLayout} requires {@link Text} objects split
 * into instances containing contiguous groups of characters for
 * style, directionality, and white - space.
 */
public class Text
    extends BorderComponent
    implements CharSequence,
               Component.Margin,
               Component.Layout.Text
{

    protected Font font;

    protected final Padding padding = new Padding();

    protected final Padding margin = new Padding();

    protected Color color, colorOver;

    protected Stroke stroke, strokeOver;

    protected boolean fill, fixed;

    protected int cols;

    protected Visual string;

    protected GlyphVector vector;

    protected Shape shape;

    private float[] localPositions;


    public Text(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.font = Font.Default;
        this.color = Color.black;
        this.fill = true;
        this.fixed = false;
        this.cols = 25;
        this.padding.set(this.font);
        this.margin.init();
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
        this.colorOver = null;
        this.stroke = null;
        this.strokeOver = null;
    }
    /**
     * Calls {@link #layout()}
     */
    @Override
    public void resized(){

        this.layout();

        super.resized();
    }
    /**
     * Clears the visual state derived from properties, and calls
     * {@link #layout()}
     */
    @Override
    public void modified(){

        this.vector = null;
        this.shape = null;
        this.localPositions = null;

        this.layout();

        super.modified();
    }
    public Order queryLayout(){
        if (this.fixed)
            return Order.Content;
        else
            return Order.Parent;
    }
    public void layout(Order order){
        switch(order){
        case Parent:
            this.fixed = false;
            break;
        case Content:
            this.fixed = true;
            break;
        default:
            throw new IllegalStateException(order.name());
        }
        this.modified();
    }
    public Whitespace queryLayoutText(){
        switch(this.getType()){
        case LineSeparator:
        case ParagraphSeparator:
        case Control:
            return Whitespace.Vertical;
        case SpaceSeparator:
            return Whitespace.Horizontal;
        default:
            return Whitespace.Inline;
        }
    }
    public final Font getFont(){

        return this.font;
    }
    /**
     * Define font and padding 
     */
    public final vector.Text setFont(Font font){
        if (null != font){

            this.font = font;

            this.padding.set(font);
        }
        return this;
    }
    public final Padding getPadding(){

        return this.padding.clone();
    }
    public final vector.Text setPadding(Padding padding){

        if (null != padding){

            this.padding.set(padding);
        }
        return this;
    }
    /**
     * For {@link TextLayout} children
     */
    public final vector.Text clearPadding(){

        this.padding.init();

        return this;
    }
    public final Padding getMargin(){

        return this.margin.clone();
    }
    public final vector.Text setMargin(Padding margin){

        if (null != margin){

            this.margin.set(margin);
        }
        return this;
    }
    /**
     * For {@link TextLayout} children
     */
    public final vector.Text clearMargin(){

        this.margin.init();

        return this;
    }
    public final Color getColor(){

        return this.color;
    }
    public final vector.Text setColor(Color color){
        if (null != color){
            this.color = color;
        }
        return this;
    }
    public final Color getColorOver(){

        return this.colorOver;
    }
    public final vector.Text setColorOver(Color colorOver){
        if (null != colorOver){
            this.colorOver = colorOver;
        }
        return this;
    }
    public final boolean isFill(){
        return this.fill;
    }
    public final Boolean getFill(){
        return this.fill;
    }
    public final vector.Text setFill(boolean fill){
        this.fill = fill;
        return this;
    }
    public final vector.Text setFill(Boolean fill){
        if (null != fill)
            this.fill = fill;
        return this;
    }
    public final boolean isFixed(){
        return this.fixed;
    }
    public final Boolean getFixed(){
        return this.fixed;
    }
    public final vector.Text setFixed(boolean fixed){
        this.fixed = fixed;
        return this;
    }
    public final vector.Text setFixed(Boolean fixed){
        if (null != fixed){
            this.fixed = fixed;
        }
        return this;
    }
    public String getText(){

        return this.toString();
    }
    public vector.Text setText(String text){

        if (null != text && 0 < text.length()){

            this.string = new Visual(text);

            this.cols = text.length();

            this.shape = null;
        }
        else {
            this.string = null;

            this.shape = null;
        }
        return this;
    }
    public final int getCols(){
        return this.cols;
    }
    public final vector.Text setCols(int cols){
        if (0 < cols){
            this.cols = cols;
        }
        return this;
    }
    public final vector.Text setCols(Integer cols){
        if (null != cols)
            return this.setCols(cols.intValue());
        else
            return this;
    }
    public final Stroke getStroke(){

        return this.stroke;
    }
    public final vector.Text setStroke(Stroke stroke){

        this.stroke = stroke;

        return this;
    }
    public final Stroke getStrokeOver(){

        return this.strokeOver;
    }
    public final vector.Text setStrokeOver(Stroke strokeOver){

        this.strokeOver = strokeOver;

        return this;
    }
    public final boolean isEmpty(){
        return (null == this.string || this.string.isEmpty());
    }
    public final boolean isNotEmpty(){
        return (null != this.string && this.string.isNotEmpty());
    }
    public final Visual.Type getType(){
        if (null != this.string)
            return this.string.getType();
        else
            return Visual.Type.EmptyString;
    }
    public final Visual.Type getType(int idx){
        if (null != this.string)
            return this.string.getType(idx);
        else
            throw new ArrayIndexOutOfBoundsException(String.valueOf(idx));
    }
    public final Visual.Direction getDirection(){
        if (null != this.string)
            return this.string.getDirection();
        else
            return Visual.Direction.Neutral;
    }
    public final Visual.Direction getDirection(int idx){
        if (null != this.string)
            return this.string.getDirection(idx);
        else
            throw new ArrayIndexOutOfBoundsException(String.valueOf(idx));
    }
    public final int length(){
        if (null == this.string)
            return 0;
        else
            return this.string.logicalLength();
    }
    public final char charAt(int idx){
        Visual string = this.string;
        if (null != string)
            return string.logicalCharAt(idx);
        else
            throw new IndexOutOfBoundsException(String.format("[%d]",idx));
    }
    public final CharSequence subSequence(int start, int end){
        Visual string = this.string;
        if (null != string)
            return string.logicalSubSequence(start,end);
        else
            throw new IndexOutOfBoundsException(String.format("[%d,%d)",start,end));
    }
    /**
     * @return Logical string as in getText
     */
    public final String toString(){

        if (null != this.string)
            return this.string.logicalString();
        else
            return new String();
    }
    /**
     * @return Baseline origin in shape coordinate space 
     */
    public Point getShapeBaseline(){

        return this.font.queryBaseline(this.padding);
    }
    private float[] positions(){
        if (null == this.localPositions){

            this.localPositions = this.font.queryBaselinePositions(this.padding,this.vector);
        }
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
    public final Point getTopLeft(int idx){

        final int ix = (idx<<1);

        return new Point(this.positions()[ix],this.padding.top);
    }
    /**
     * Shape coordinate space
     */
    public final Point getTopRight(int idx){

        final int ix = (idx+1)<<1;

        return new Point(this.positions()[ix],this.padding.top);
    }
    /**
     * Shape coordinate space
     */
    public final Point getBaseline(int idx){

        final float[] positions = this.positions();

        final int ix = (idx<<1);
        final int iy = (ix+1);

        return new Point(positions[ix],positions[iy]);
    }
    /**
     * @param start Inclusive glyph index
     * @param end Exclusive glyph index
     * @return Font bounding box for indexed glyphs in shape
     * coordinate space
     */
    public final Bounds subArea(int start, int end){

        final float[] positions = this.positions();

        final float x1 = positions[start<<1];
        final float y1 = this.padding.top;
        final float x2 = positions[(end-1)<<1];
        final float y2 = (y1+this.font.height);

        return new Bounds(x1,y1,(x2-x1),(y2-y1));
    }
    /**
     * @return Glyphs bounding box in shape coordinate space
     */
    public final Bounds queryBoundsContent(){

        return this.font.boundingBox(this.getPadding(),1,this.shapeAreaWidth());
    }
    public float shapeAreaWidth(){
        if (this.fixed)
            return (this.font.em*this.cols);
        else {
            Shape shape = this.shape;
            if (null != shape){
                Bounds bounds = shape.getBoundsVector();

                return (float)(bounds.getX()+bounds.getWidth());
            }
            else {
                return (this.font.em*this.cols);
            }
        }
    }
    public vector.Text outputScene(Context g){

        super.outputScene(g);

        final Shape shape = this.shape;
        if (null != shape){
            final boolean mouseIn = this.mouseIn;

            final Transform restore = g.getTransform();
            try {

                this.getTransformParent().transformFrom(g);

                if (mouseIn && null != this.colorOver)
                    g.setColor(this.colorOver);
                else
                    g.setColor(this.color);

                if (this.fill){
                    g.fill(shape);
                }

                if (mouseIn && null != this.strokeOver){

                    g.setStroke(this.strokeOver);

                    g.draw(shape);
                }
                else if (null != this.stroke){

                    g.setStroke(this.stroke);

                    g.draw(shape);
                }
            }
            finally {
                g.setTransform(restore);
            }
        }
        return this;
    }
    public ObjectJson toJson(){

        ObjectJson thisModel = super.toJson();

        thisModel.setValue("text", this.toString());
        thisModel.setValue("font", this.getFont());
        thisModel.setValue("padding", this.getPadding());
        thisModel.setValue("margin", this.getMargin());
        thisModel.setValue("color", this.getColor());
        thisModel.setValue("color-over", this.getColorOver());
        thisModel.setValue("fill",this.getFill());
        thisModel.setValue("fixed",this.getFixed());
        thisModel.setValue("cols",this.getCols());
        thisModel.setValue("stroke",this.getStroke());
        thisModel.setValue("stroke-over",this.getStrokeOver());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setText( (String)thisModel.getValue("text"));
        this.setFont( (Font)thisModel.getValue("font",Font.class));
        this.setPadding( (Padding)thisModel.getValue("padding",Padding.class));
        this.setMargin( (Padding)thisModel.getValue("margin",Padding.class));
        this.setColor( (Color)thisModel.getValue("color",Color.class));
        this.setColorOver( (Color)thisModel.getValue("color-over",Color.class));
        this.setFill( (Boolean)thisModel.getValue("fill"));
        this.setFixed( (Boolean)thisModel.getValue("fixed"));
        this.setCols( (Integer)thisModel.getValue("cols",Integer.class));
        this.setStroke( (Stroke)thisModel.getValue("stroke",Stroke.class));
        this.setStrokeOver( (Stroke)thisModel.getValue("stroke-over",Stroke.class));

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
    public void layout(){

        if (null == this.shape && 0 < this.length()){

            final Point baseline = this.getShapeBaseline();

            this.vector = this.font.createGlyphVector(this.string.toString());

            this.shape = this.vector.createOutline(baseline);

            this.localPositions = null;
        }

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
    public void layoutScaleToDimensions(){
        Bounds shape = this.queryBoundsContent();
        if (null != shape)
            this.transform.scaleFromAbsolute(this.getBoundsVector(),shape);
        else
            this.setTransformLocal(1f,1f);
    }
    /**
     * May be called from {@link #layout()} to set dimensions from
     * parent.
     */
    public void resizeToParent(){
        this.setBoundsVectorInit(this.getParentVector(),this.getMargin());
    }
    /**
     * May be called from {@link #layout()} to set dimensions from
     * {@link #queryBoundsContent()}.
     */
    public void resizeToShapeArea(){
        this.setBoundsVectorInit(this.queryBoundsContent());
    }
    /**
     * Define transform local as 1:1 scale.
     */
    public void layoutScaleToShapeArea(){
        this.setTransformLocal(1f,1f);
    }
}
