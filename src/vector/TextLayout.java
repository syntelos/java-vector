/*
 * Java Vector
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

import vector.text.Visual;

import json.Json;
import json.ObjectJson;

import java.awt.Graphics2D;
import java.util.StringTokenizer;

/**
 * Any number of {@link Component$Layout$Text} components will be
 * relocated for one of two layout styles: wrapped or preformatted
 * (not wrapped).  In the wrapped case, an instance of this class is a
 * paragraph of text.
 * 
 * Both of these layout styles are bottom-up strategies.  The bounding
 * box of instances of this class will be defined by explicit state or
 * derived from the content (children) of this class.
 * 
 * <h3>Font &amp; Padding</h3>
 * 
 * This class employs "font" and "padding" for overall layout
 * purposes, exclusively.  The font is employed for its default
 * padding.  The default padding may be modified by the "padding"
 * property.  The default font is defined in the {@link Font} class.
 * 
 * In the layout process, the individual text padding is cleared -- as
 * necessarily true in the application of this class.
 * 
 * <h3>Attributed Text</h3>
 * 
 * Group {@link Component$Layout$Text} objects for style,
 * directionality, and character content.  {@link
 * Component$Layout$Text} objects should be composed so that each
 * object contains only one {@link Component$Layout$Text$Whitespace
 * kind of character content}.
 */
public class TextLayout
    extends Container<Component.Layout.Text>
{

    protected Font font;

    protected final Padding padding = new Padding();

    protected boolean wrap, debug;


    public TextLayout(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.font = Font.Default;
        this.padding.set(this.font);
        this.wrap = false;
        this.debug = false;
    }
    @Override
    public void resized(){

        this.layout();

        super.resized();
    }
    @Override
    public void modified(){

        this.layout();

        super.modified();
    }
    @Override
    public void relocated(){

        this.layout();

        super.relocated();
    }

    public final Font getFont(){

        return this.font;
    }
    /**
     * Define font and padding 
     */
    public final TextLayout setFont(Font font){
        if (null != font){

            this.font = font;

            this.padding.set(font);
        }
        return this;
    }
    public final TextLayout setFont(java.awt.Font font){
        if (font instanceof Font)
            return this.setFont((Font)font);
        else
            return this.setFont(new Font(font));
    }
    public final Padding getPadding(){

        return this.padding.clone();
    }
    public final TextLayout setPadding(Padding padding){

        if (null != padding){

            this.padding.set(padding);
        }
        return this;
    }
    public final TextLayout clearPadding(){

        this.padding.init();

        return this;
    }
    /**
     * Resize to size of children
     */
    public final boolean isWrap(){
        return this.wrap;
    }
    public final Boolean getWrap(){
        if (this.wrap)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    public final Container setWrap(boolean wrap){

        this.wrap = wrap;

        return this;
    }
    public final Container setWrap(Boolean wrap){
        if (null != wrap)
            return this.setWrap(wrap.booleanValue());
        else
            return this;
    }
    /**
     * Resize to size of children
     */
    public final boolean isDebug(){
        return this.debug;
    }
    public final Boolean getDebug(){
        if (this.debug)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    public final Container setDebug(boolean debug){

        this.debug = debug;

        return this;
    }
    public final Container setDebug(Boolean debug){
        if (null != debug)
            return this.setDebug(debug.booleanValue());
        else
            return this;
    }

    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("font", this.getFont());
        thisModel.setValue("padding", this.getPadding());
        thisModel.setValue("wrap",this.getWrap());
        thisModel.setValue("debug",this.getDebug());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setFont( (Font)thisModel.getValue("font",Font.class));
        this.setPadding( (Padding)thisModel.getValue("padding",Padding.class));
        this.setWrap( (Boolean)thisModel.getValue("wrap"));
        this.setDebug( (Boolean)thisModel.getValue("debug"));

        this.parse( (String)thisModel.getValue("text"));

        return true;
    }

    /**
     * Trivial text parser permits this class to accept a property
     * named "text", which is most useful for testing.
     */
    protected void parse(String text){
        final StringTokenizer strtok = new StringTokenizer(text," \t\n",true);

        final StringBuilder sp = new StringBuilder();

        while (strtok.hasMoreTokens()){
            String tok = strtok.nextToken();

            if (" ".equals(tok)){

                sp.append(' ');

                continue;
            }
            else if (0 < sp.length()){

                Text child = new Text();
                this.add(child);

                child.setFont(this.font);
                child.clearPadding();
                child.setText(sp.toString());
                child.setFixed(true);

                if (this.debug){
                    Border debug = new Border();

                    child.setBorder(debug);

                    debug.setColor(Color.red);
                    debug.setColorOver(Color.green);
                }

                child.modified();

                sp.setLength(0);
            }

            Text child = new Text();
            this.add(child);

            child.setFont(this.font);
            child.clearPadding();
            child.setText(tok);
            child.setFixed(true);

            if (this.debug){
                Border debug = new Border();

                child.setBorder(debug);

                debug.setColor(Color.red);
                debug.setColorOver(Color.green);
            }

            child.modified();
        }
    }
    protected void layout(){
        Bounds shape;
        final float cr = this.padding.left;
        float lf = 0;
        float x = cr, y = this.padding.top, w, h;


        if (this.wrap){
            final Bounds bounds = this.getBoundsVector();

            for (Component.Layout.Text text: this.list(Component.Layout.Text.class)){

                text.layout(Component.Layout.Order.Content);

                shape = text.queryBoundsContent();

                w = (shape.x+shape.width);
                h = (shape.y+shape.height);
                
                shape.width = w;
                shape.height = h;

                if (0 == lf){
                    lf = h;
                    shape.x = x;
                    shape.y = y;
                    text.setBoundsVector(shape);
                    text.relocated();

                    x += w;
                }
                else {
                    lf = Math.max(lf,h);

                    if ((x+shape.width) <= bounds.width){
                        shape.x = x;
                        shape.y = y;
                        text.setBoundsVector(shape);
                        text.relocated();

                        x += w;
                    }
                    else {
                        x = cr;
                        y += lf;

                        shape.x = x;
                        shape.y = y;
                        text.setBoundsVector(shape);
                        text.relocated();
                    }
                }
            }
        }
        else {
            w = 0; h = 0;

            for (Component.Layout.Text text: this.list(Component.Layout.Text.class)){

                text.layout(Component.Layout.Order.Content);

                shape = text.queryBoundsContent();

                w = (shape.x+shape.width);
                h = (shape.y+shape.height);

                shape.x = x;
                shape.y = y;

                shape.width = w;
                shape.height = h;

                text.setBoundsVector(shape);
                text.relocated();

                final Component.Layout.Text.Whitespace type = text.queryLayoutText();

                switch(type){
                case Vertical:

                    x = cr;
                    y += lf;

                    lf = h;
                    break;
                default:

                    x += w;

                    lf = Math.max(lf,h);

                    break;
                }
            }
        }
    }
}
