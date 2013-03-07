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

import platform.Color;
import platform.Shape;
import platform.Stroke;
import platform.Transform;
import platform.geom.Point;
import platform.geom.Rectangle;

import path.Winding;

import json.Json;
import json.ObjectJson;

/**
 * Component consumes and produces W3C SVG Path attribute "d" data.
 */
public class Path
    extends BorderComponent
    implements Component.Align2D
{

    protected Color color, colorOver;

    protected Stroke stroke, strokeOver;

    protected platform.Path path;

    protected Winding winding;

    protected boolean closed, content, fill;

    protected Align align;


    /**
     * Set winding non zero
     */
    public Path(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.closed = false;

        this.setWindingNonZero();

        this.color = Color.black;
        this.content = false;
        this.fill = false;
    }
    @Override
    public void destroy(){
        super.destroy();

        this.path = null;
        this.winding = null;
        this.colorOver = null;
        this.stroke = null;
        this.strokeOver = null;
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
    public Component.Layout.Order queryLayout(){

        if (this.content)
            return Component.Layout.Order.Content;
        else
            return Component.Layout.Order.Parent;
    }
    public Bounds queryBoundsContent(){

        if (null != this.path)
            return ((Shape)this.path).getBoundsVector();
        else
            return new Bounds();
    }
    public void layout(Component.Layout.Order order){
        switch(order){
        case Content:
            this.content = true;
            break;
        case Parent:
            this.content = false;
            break;
        default:
            throw new IllegalStateException(order.name());
        }
        this.modified();
    }
    public final boolean isContent(){

        return this.content;
    }
    public final Boolean getContent(){

        if (this.content)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    public final Path setContent(boolean content){

        this.content = content;
        return this;
    }
    public final Path setContent(Boolean content){

        if (null != content)
            return this.setContent(content.booleanValue());
        else
            return this;
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
    public final Path setFill(boolean fill){

        this.fill = fill;
        return this;
    }
    public final Path setFill(Boolean fill){

        if (null != fill)
            return this.setFill(fill.booleanValue());
        else
            return this;
    }
    public final Color getColor(){

        return this.color;
    }
    public final Path setColor(Color color){
        if (null != color){
            this.color = color;
        }
        return this;
    }
    public final Path setColor(String code){
        if (null != code)
            return this.setColor(new Color(code));
        else
            return this;
    }
    public final Color getColorOver(){

        return this.colorOver;
    }
    public final Path setColorOver(Color colorOver){
        if (null != colorOver){
            this.colorOver = colorOver;
        }
        return this;
    }
    public final Path setColorOver(String code){
        if (null != code)
            return this.setColorOver(new Color(code));
        else
            return this;
    }
    /**
     * @return Null or SVG Path attribute 'd' expression.
     */
    public String getD(){

        platform.Path p = this.path;
        if (null != p)
            return p.toString();
        else
            return null;
    }
    /**
     * Parse path data
     *
     * @param d SVG Path parameter "d" value data
     */
    public Path setD(String d){

        this.path = new path.Parser(d).apply(new platform.Path());

        return this;
    }
    public platform.Path getPath(){

        return this.path;
    }
    public platform.Path getCreatePath(Winding winding){

        if (null == this.path)
            this.setPath(new platform.Path(winding));

        return this.path;
    }
    public Path setPath(platform.Path path){

        this.path = path;

        return this;
    }
    public Winding getWinding(){
        return this.winding;
    }
    public boolean isWindingNonZero(){
        return (Winding.NonZero == this.winding);
    }
    public boolean isWindingEvenOdd(){
        return (Winding.EvenOdd == this.winding);
    }
    public Path setWindingNonZero(){
        return this.setWinding(Winding.NonZero);
    }
    public Path setWindingEvenOdd(){
        return this.setWinding(Winding.EvenOdd);
    }
    /**
     * Preparation to define new data: clear path and define winding.
     */
    public Path setWinding(Winding winding){
        if (null != winding){
            switch(winding){
            case EvenOdd:
                this.setPath( new platform.Path(Winding.EvenOdd));

                break;
            case NonZero:
                this.setPath( new platform.Path(Winding.NonZero));

                break;
            default:
                throw new IllegalArgumentException(winding.name());
            }
            this.winding = winding;

            this.closed = false;

            return this;
        }
        else
            throw new IllegalArgumentException();
    }
    public Align getAlign(){
        return this.align;
    }
    public String getAlignString(){
        Align align = this.align;
        if (null != align)
            return align.toString();
        else
            return null;
    }
    public Path setAlign(Align align){
        this.align = align;
        return this;
    }
    public Path setAlign(String align){
        return this.setAlign(Align.For(align));
    }
    public final Stroke getStroke(){

        return this.stroke;
    }
    public final Path setStroke(Stroke stroke){

        this.stroke = stroke;

        return this;
    }
    public final Stroke getStrokeOver(){

        return this.strokeOver;
    }
    public final Path setStrokeOver(Stroke strokeOver){

        this.strokeOver = strokeOver;

        return this;
    }

    public Path moveTo(Point p)
        throws Winding.Missing
    {
        return this.moveTo(p.x,p.y);
    }
    public Path moveTo(float x, float y)
        throws Winding.Missing
    {
        if (null == this.path)
            throw new Winding.Missing();
        else {

            this.path.moveTo(x,y);

            return this;
        }
    }
    public Path lineTo(Point p)
        throws Winding.Missing
    {
        return this.lineTo(p.x,p.y);
    }
    public Path lineTo(float x, float y)
        throws Winding.Missing
    {
        if (null == this.path)
            throw new Winding.Missing();
        else {

            this.path.lineTo(x,y);
            return this;
        }
    }
    public Path quadTo(Point a, Point b)
        throws Winding.Missing
    {
        return this.quadTo(a.x,a.y,b.x,b.y);
    }
    public Path quadTo(float ax, float ay, float bx, float by)
        throws Winding.Missing
    {
        if (null == this.path)
            throw new Winding.Missing();
        else {

            this.path.quadTo(ax,ay,bx,by);

            return this;
        }
    }
    public Path cubicTo(Point a, Point b, Point c)
        throws Winding.Missing
    {
        return this.cubicTo(a.x,a.y,b.x,b.y,c.x,c.y);
    }
    public Path cubicTo(float ax, float ay, float bx, float by, float cx, float cy)
        throws Winding.Missing
    {
        if (null == this.path)
            throw new Winding.Missing();
        else {

            this.path.cubicTo(ax,ay,bx,by,cx,cy);

            return this;
        }
    }
    /**
     * Clockwise outline box.
     */
    public Path outlineBoxCW(Rectangle box)
        throws Winding.Missing
    {
        return this.outlineBoxCW(box.x,box.y,box.width,box.height);
    }
    /**
     * Clockwise outline box.
     */
    public Path outlineBoxCW(float width, float height)
        throws Winding.Missing
    {
        return this.outlineBoxCW(0f,0f,width,height);
    }
    /**
     * Clockwise outline box.
     */
    public Path outlineBoxCW(float x, float y, float width, float height)
        throws Winding.Missing
    {
        if (null == this.path)
            throw new Winding.Missing();
        else {
            final float x1 = x;
            final float x2 = (x1+width);
            final float y1 = y;
            final float y2 = (y1+height);

            this.moveTo(x1,y1);
            this.lineTo(x2,y1);
            this.lineTo(x2,y2);
            this.lineTo(x1,y2);
            return this.closePath();
        }
    }
    /**
     * CW outline box with CCW inset box.
     */
    public Path outlineBoxCW(Rectangle box, float in)
        throws Winding.Missing
    {
        return this.outlineBoxCW(box.x,box.y,box.width,box.height,in);
    }
    /**
     * CW outline box with CCW inset box.
     */
    public Path outlineBoxCW(float width, float height, float in)
        throws Winding.Missing
    {
        return this.outlineBoxCW(0f,0f,width,height,in);
    }
    /**
     * CW outline box with CCW inset box.
     */
    public Path outlineBoxCW(float x, float y, float width, float height, float in)
        throws Winding.Missing
    {
        if (null == this.path)
            throw new Winding.Missing();
        else {
            float x1 = x;
            float x2 = (x1+width);
            float y1 = y;
            float y2 = (y1+height);

            this.moveTo(x1,y1);
            this.lineTo(x2,y1);
            this.lineTo(x2,y2);
            this.lineTo(x1,y2);
            this.closePath();

            x1 += in;
            x2 -= in;
            y1 += in;
            y2 -= in;

            this.moveTo(x1,y1);
            this.lineTo(x1,y2);
            this.lineTo(x2,y2);
            this.lineTo(x2,y1);

            return this.closePath();
        }
    }
    public Path closePath()
        throws Winding.Missing
    {
        if (null == this.path)
            throw new Winding.Missing();
        else {
            this.path.close();
            this.closed = true;
            return this;
        }
    }
    public Path clearPath(){

        return this.setWinding(this.winding);
    }
    public Path clearPath(Winding w){

        return this.setWinding(w);
    }

    public Path outputScene(Context g){

        super.outputScene(g);

        Shape shape = (Shape)this.path;
        if (null != shape){
            this.getTransformParent().transformFrom(g);

            if (this.mouseIn && null != this.colorOver)
                g.setColor(this.colorOver);
            else
                g.setColor(this.color);

            if (this.mouseIn && null != this.strokeOver)
                g.setStroke(this.strokeOver);
            else if (null != this.stroke)
                g.setStroke(this.stroke);


            if (this.fill)
                g.fill(shape);
            else
                g.draw(shape);
        }
        return this;
    }


    public ObjectJson toJson(){
        ObjectJson thisModel = (ObjectJson)super.toJson();

        thisModel.setValue("color", this.getColor());
        thisModel.setValue("color-over", this.getColorOver());
        thisModel.setValue("stroke",this.getStroke());
        thisModel.setValue("stroke-over",this.getStrokeOver());
        thisModel.setValue("content",this.getContent());
        thisModel.setValue("fill",this.getFill());
        thisModel.setValue("align",this.getAlignString());
        thisModel.setValue("d", this.getD());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setColor( (Color)thisModel.getValue("color",Color.class));
        this.setColorOver( (Color)thisModel.getValue("color-over",Color.class));
        this.setStroke( (Stroke)thisModel.getValue("stroke",Stroke.class));
        this.setStrokeOver( (Stroke)thisModel.getValue("stroke-over",Stroke.class));
        this.setContent( (Boolean)thisModel.getValue("content"));
        this.setFill( (Boolean)thisModel.getValue("fill"));
        this.setAlign( (String)thisModel.getValue("align"));
        this.setD( (String)thisModel.getValue("d"));

        return true;
    }
    public void layout(){

        if (null != this.path){

            if (this.content){

                this.setBoundsVectorInit(((Shape)this.path).getBoundsVector());
            }

            if (null != this.align){

                this.setBoundsVector(this.getBoundsVector().apply(this.align,this.getParentBounds()));
            }
        }
    }
}

