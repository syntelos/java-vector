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
import platform.Transform;
import platform.geom.Point;
import platform.geom.Rectangle;

import json.Json;
import json.ObjectJson;

/**
 * Component consumes and produces W3C SVG Path attribute "d" data.
 */
public class Path
    extends AbstractComponent
    implements Component.Align2D
{

    /** 
     * Winding algorithms make more sense for a ray from centroid
     * point to subject point.
     * 
     * <h3>Non zero winding</h3>
     * 
     * <p> From P, a ray R intersects the outline having clock-wise
     * (CW) or counter-clock-wise (CCW) direction.  </p>
     * 
     * <p> An accumulator A is initialized to zero, and incremented
     * for a CCW intersection, and decremented for a CW
     * intersection.</p>
     * 
     * <p> For A equal to zero P is "outside" the outline, otherwise P
     * is "inside" the outline. </p>
     * 
     * <h3>Even odd winding</h3>
     * 
     * <p> From P, a ray R intersects the outline an even or odd
     * number of times.  If even, P is "outside" the outline.
     * Otherwise when P is odd, P is "inside" the outline.  </p>
     * 
     * <h3>Future</h3>
     * 
     * <p> The Winding enum constant {@link Path$Winding#Future
     * Future} represents and unknown, wait and see status. </p>
     */
    public static enum Winding {
        EvenOdd, NonZero, Future;


        public final static Winding For(int rule){
            switch(rule){
            case 0:
                return EvenOdd;
            case 1:
                return NonZero;
            default:
                return null;
            }
        }

        /**
         * Missing call to set winding before emitting path data.
         */
        public static class Missing
            extends IllegalStateException
        {

            public Missing(){
                super("Require winding");
            }
        }
    }


    protected Color color, colorOver;

    protected Stroke stroke, strokeOver;

    protected platform.Path path;

    protected Path.Winding winding;

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
            return new Bounds(this.path);
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

        Shape path = this.path;
        if (null != path)
            return platform.Path.ToString(path);
        else
            return null;
    }
    /**
     * Parse path data
     *
     * @param d SVG Path parameter "d" value data
     */
    public Path setD(String d){

        this.path = Path.Parser.Apply(new platform.Path(),d);

        return this;
    }
    public platform.Path getPath(){

        return this.path;
    }
    public platform.Path getCreatePath(Path.Winding winding){

        if (null == this.path)
            this.setPath(new platform.Path(winding));

        return this.path;
    }
    public Path setPath(platform.Path path){

        this.path = path;

        return this;
    }
    public Path.Winding getWinding(){
        return this.winding;
    }
    public boolean isWindingNonZero(){
        return (Path.Winding.NonZero == this.winding);
    }
    public boolean isWindingEvenOdd(){
        return (Path.Winding.EvenOdd == this.winding);
    }
    public Path setWindingNonZero(){
        return this.setWinding(Path.Winding.NonZero);
    }
    public Path setWindingEvenOdd(){
        return this.setWinding(Path.Winding.EvenOdd);
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
        throws vector.Path.Winding.Missing
    {
        return this.moveTo(p.x,p.y);
    }
    public Path moveTo(float x, float y)
        throws vector.Path.Winding.Missing
    {
        if (null == this.path)
            throw new Path.Winding.Missing();
        else {

            this.path.moveTo(x,y);

            return this;
        }
    }
    public Path lineTo(Point p)
        throws vector.Path.Winding.Missing
    {
        return this.lineTo(p.x,p.y);
    }
    public Path lineTo(float x, float y)
        throws vector.Path.Winding.Missing
    {
        if (null == this.path)
            throw new Path.Winding.Missing();
        else {

            this.path.lineTo(x,y);
            return this;
        }
    }
    public Path quadTo(Point a, Point b)
        throws vector.Path.Winding.Missing
    {
        return this.quadTo(a.x,a.y,b.x,b.y);
    }
    public Path quadTo(float ax, float ay, float bx, float by)
        throws vector.Path.Winding.Missing
    {
        if (null == this.path)
            throw new Path.Winding.Missing();
        else {

            this.path.quadTo(ax,ay,bx,by);

            return this;
        }
    }
    public Path curveTo(Point a, Point b, Point c)
        throws vector.Path.Winding.Missing
    {
        return this.curveTo(a.x,a.y,b.x,b.y,c.x,c.y);
    }
    public Path curveTo(float ax, float ay, float bx, float by, float cx, float cy)
        throws vector.Path.Winding.Missing
    {
        if (null == this.path)
            throw new Path.Winding.Missing();
        else {

            this.path.curveTo(ax,ay,bx,by,cx,cy);

            return this;
        }
    }
    /**
     * Clockwise outline box.
     */
    public Path outlineBoxCW(Rectangle box)
        throws vector.Path.Winding.Missing
    {
        return this.outlineBoxCW(box.x,box.y,box.width,box.height);
    }
    /**
     * Clockwise outline box.
     */
    public Path outlineBoxCW(float width, float height)
        throws vector.Path.Winding.Missing
    {
        return this.outlineBoxCW(0f,0f,width,height);
    }
    /**
     * Clockwise outline box.
     */
    public Path outlineBoxCW(float x, float y, float width, float height)
        throws vector.Path.Winding.Missing
    {
        if (null == this.path)
            throw new Path.Winding.Missing();
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
        throws vector.Path.Winding.Missing
    {
        return this.outlineBoxCW(box.x,box.y,box.width,box.height,in);
    }
    /**
     * CW outline box with CCW inset box.
     */
    public Path outlineBoxCW(float width, float height, float in)
        throws vector.Path.Winding.Missing
    {
        return this.outlineBoxCW(0f,0f,width,height,in);
    }
    /**
     * CW outline box with CCW inset box.
     */
    public Path outlineBoxCW(float x, float y, float width, float height, float in)
        throws vector.Path.Winding.Missing
    {
        if (null == this.path)
            throw new Path.Winding.Missing();
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
        throws vector.Path.Winding.Missing
    {
        if (null == this.path)
            throw new Path.Winding.Missing();
        else {
            this.path.closePath();
            this.closed = true;
            return this;
        }
    }
    public Path clearPath(){

        return this.setWinding(this.winding);
    }
    public Path clearPath(Path.Winding w){

        return this.setWinding(w);
    }

    public Path outputScene(Context g){
        Shape shape = this.path;
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
    public Path outputOverlay(Context g){

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

                this.setBoundsVector(this.path.getBoundsVector());
            }

            if (null != this.align){

                this.setBoundsVector(this.getBoundsVector().apply(this.align,this.getParentBounds()));
            }
        }
    }


    /**
     * Parse SVG Path "d" attribute value expression.
     */
    public final static class Parser
        extends Object
        implements Iterable<Parser.Token>,
                   java.util.Iterator<Parser.Token>
    {
        public enum Token {
            Unknown, Coordinate, M, m, Z, z, L, l, H, h, V, v, C, c, S, s, Q, q, T, t, A, a;
        }


        private final char[] string;
        private int index;
        private java.lang.Double coordinate;


        public Parser(String string){
            super();
            if (null != string){
                this.string = string.trim().toCharArray();
                if (0 == this.string.length)
                    throw new IllegalArgumentException();
            }
            else
                throw new IllegalArgumentException();
        }


        public java.lang.Double getCoordinate(){
            java.lang.Double c = this.coordinate;
            if (null != this.coordinate){
                this.coordinate = null;
                return c;
            }
            else if (this.hasNext() && Token.Coordinate == this.next()){
                c = this.coordinate;
                this.coordinate = null;
                return c;
            }
            else
                throw new java.util.NoSuchElementException();
        }
        public boolean hasNext(){
            return (this.index < this.string.length);
        }
        public Parser.Token next(){
            this.coordinate = null;
            if (this.index < this.string.length){
                Parser.Token token = null;
                int start = this.index;
                int end = start;
                boolean decpt = false;
                scan:
                while (this.index < this.string.length){

                    switch(this.string[this.index]){
                    case ' ':
                    case ',':
                        if (this.index != start){
                            end = (this.index-1);
                            this.index++;
                            break scan;
                        }
                        break;
                    case '.':
                        if (null != token){
                            if (decpt || Parser.Token.Coordinate != token){
                                end = (this.index-1);
                                break scan;
                            }
                        }
                        else
                            token = Parser.Token.Coordinate;

                        decpt = true;
                        break;
                    case '-':
                        if (null != token){
                            end = (this.index-1);
                            break scan;
                        }
                        else
                            token = Parser.Token.Coordinate;
                        break;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        if (null == token)
                            token = Parser.Token.Coordinate;
                        else if (token != Parser.Token.Coordinate){
                            end = (this.index-1);
                            break scan;
                        }
                        break;
                    default:
                        if (null == token)
                            return Parser.Token.valueOf(String.valueOf(this.string[this.index++]));
                        else {
                            end = (this.index-1);
                            break scan;
                        }
                    }
                    this.index++;
                }

                if (Parser.Token.Coordinate == token){

                    if (start == end && this.index == this.string.length)
                        end = (this.index-1);

                    this.coordinate = java.lang.Double.parseDouble(new String(this.string,start,(end-start+1)));
                    return token;
                }
                else
                    return Parser.Token.Unknown;
            }
            else
                throw new java.util.NoSuchElementException();
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
        public java.util.Iterator<Parser.Token> iterator(){
            return this;
        }

        public final static platform.Path Apply(platform.Path shape, String pexpr){
            return (platform.Path)Apply(shape,new vector.Path.Parser(pexpr));
        }
        public final static vector.geom.Path Apply(vector.geom.Path shape, vector.Path.Parser p){
            vector.Path.Parser.Token last = null;

            double mx = 0, my = 0, sx = 0, sy = 0;

            for (vector.Path.Parser.Token tok : p){
                switch(tok){
                case Coordinate:
                case M:
                    shape.moveTo((mx = p.getCoordinate()),(my = p.getCoordinate()));
                    sx = mx;
                    sy = my;
                    break;
                case m:
                    shape.moveTo((mx += p.getCoordinate()),(my += p.getCoordinate()));
                    sx = mx;
                    sy = my;
                    break;
                case Z:
                case z:
                    shape.close();
                    break;
                case L:
                    shape.lineTo((sx = p.getCoordinate()),(sy = p.getCoordinate()));
                    break;
                case l:
                    shape.lineTo((sx += p.getCoordinate()),(sy += p.getCoordinate()));
                    break;
                case H:
                    sx = p.getCoordinate();
                    shape.lineTo(sx,sy);
                    break;
                case h:
                    sx += p.getCoordinate();
                    shape.lineTo(sx,sy);
                    break;
                case V:
                    sy = p.getCoordinate();
                    shape.lineTo(sx,sy);
                    break;
                case v:
                    sy += p.getCoordinate();
                    shape.lineTo(sx,sy);
                    break;
                case C:
                    shape.curveTo(p.getCoordinate(),p.getCoordinate(),
                                  p.getCoordinate(),p.getCoordinate(),
                                  (sx = p.getCoordinate()),(sy = p.getCoordinate()));
                    break;
                case c:
                    shape.curveTo((sx + p.getCoordinate()),(sy + p.getCoordinate()),
                                  (sx + p.getCoordinate()),(sy + p.getCoordinate()),
                                  (sx += p.getCoordinate()),(sy += p.getCoordinate()));
                    break;
                case S:
                case s:
                    throw new UnsupportedOperationException(tok.name());
                case Q:
                    shape.quadTo(p.getCoordinate(),p.getCoordinate(),
                                 (sx = p.getCoordinate()),(sy = p.getCoordinate()));
                    break;
                case q:
                    shape.quadTo((sx + p.getCoordinate()),(sy + p.getCoordinate()),
                                 (sx += p.getCoordinate()),(sy += p.getCoordinate()));
                    break;
                case T:
                case t:
                    throw new UnsupportedOperationException(tok.name());
                case A:
                case a:
                    throw new UnsupportedOperationException(tok.name());
                default:
                    throw new IllegalArgumentException(tok.name());
                }
                last = tok;
            }
            return shape;
        }
    }
}

