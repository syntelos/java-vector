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
 * Any number of pieces of {@link Text} will be relocated for one of
 * two layout strategies: wrapped or preformatted (not wrapped).
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
 * <h3>Layout</h3>
 * 
 * This class requires that {@link Text} objects be grouped for style,
 * directionality, and white - space changes.
 * 
 * White - space changes between vertical and horizontal kinds of
 * white - space are necessarily split into unique {@link Text}
 * objects.  The default (first char) {@link vector.text.Visual$Type
 * Visual Type} of a {@link Text} object must present horizontal white
 * - space with wrapping, and vertical white - space without wrapping.
 */
public class TextLayout
    extends Container<Component.Layout.Text>
{

    protected Font font;

    protected final Padding padding = new Padding();

    protected boolean wrap;


    public TextLayout(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.font = Font.Default;
        this.padding.set(this.font);
        this.wrap = false;
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

    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("font", this.getFont());
        thisModel.setValue("padding", this.getPadding());
        thisModel.setValue("wrap",this.getWrap());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setFont( (Font)thisModel.getValue("font",Font.class));
        this.setPadding( (Padding)thisModel.getValue("padding",Padding.class));
        this.setWrap( (Boolean)thisModel.getValue("wrap"));

        this.parse( (String)thisModel.getValue("text"));

        return true;
    }

    /**
     * Trivial text parser permits this class to accept a property
     * named "text", which is most useful for testing.
     */
    protected void parse(String text){
        final StringTokenizer strtok = new StringTokenizer(text," \t\n",true);

        while (strtok.hasMoreTokens()){
            String tok = strtok.nextToken();

            Text child = new Text();
            this.add(child);
            child.setFont(this.font);
            child.clearPadding();
            child.setText(tok);
            child.setFixed(true);
            child.modified();
        }
    }
    protected void layout(){

        Bounds shape;
        float x = this.padding.left, y = this.padding.top, lineH = 0, x1, y1;

        if (this.wrap){
            final Bounds bounds = this.getBoundsVector();

            for (Component.Layout.Text text: this){

                text.layout(Component.Layout.Order.Content);

                shape = text.getBoundsVector();

                x1 = (shape.x+shape.width);
                y1 = (shape.y+shape.height);

                if (0 == lineH){
                    lineH = y1;
                    shape.x = x;
                    shape.y = y;
                    text.setBoundsVector(shape);
                    text.relocated();

                    x += x1;
                }
                else {
                    lineH = Math.max(lineH,y1);

                    if ((x+shape.width) <= bounds.width){
                        shape.x = x;
                        shape.y = y;
                        text.setBoundsVector(shape);
                        text.relocated();

                        x += x1;
                    }
                    else {
                        x = this.padding.left;
                        y += lineH;

                        shape.x = x;
                        shape.y = y;
                        text.setBoundsVector(shape);
                        text.relocated();
                    }
                }
            }
        }
        else {
            x1 = 0; y1 = 0;

            for (Component.Layout.Text text: this){

                text.layout(Component.Layout.Order.Content);

                shape = text.getBoundsVector();

                Component.Layout.Text.Whitespace type = text.queryLayoutText();

                switch(type){
                case Vertical:

                    x = this.padding.left;
                    y += lineH;

                    x1 = (shape.x+shape.width);
                    y1 = (shape.y+shape.height);

                    lineH = y1;
                    break;
                default:

                    x += x1;

                    x1 = (shape.x+shape.width);
                    y1 = (shape.y+shape.height);

                    lineH = Math.max(lineH,y1);
                    break;
                }

                shape.x = x;
                shape.y = y;
                text.setBoundsVector(shape);
                text.relocated();
            }
        }
    }
}
