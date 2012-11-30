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
import platform.event.NamedAction;
import platform.geom.Point;
import platform.geom.Rectangle;

import java.lang.reflect.Method;

/**
 * Fixed swatch panel functions as dialog and button.
 */
public class ColorPalette<E extends Enum<E>>
    extends BorderComponent
    implements Event.NamedAction.Producer<E>,
               Event.NamedAction.Consumer<E>
{

    private final static Stroke StrokeOver = new Stroke(2.0f,Color.gray.opacity(0.5f));


    protected Class<Enum<E>> enumClass;

    protected Enum<E> enumValue;

    private Method enumValueOf;

    private float binX, binY, mbinX, mbinY;

    private Rectangle bin, binOver;

    private Color selected;

    private int overRow, overCol;



    public ColorPalette(){
        super();
    }



    @Override
    public void init(){
        super.init();

        this.binX = 0f;
        this.binY = 0f;
        this.mbinX = 0f;
        this.mbinY = 0f;
        this.overRow = -1;
        this.overCol = -1;
    }
    @Override
    public void destroy(){
        super.destroy();

        this.enumClass = null;
        this.enumValueOf = null;
        this.enumValue = null;

        this.bin = null;
        this.binOver = null;
        this.selected = null;
    }
    @Override
    public void modified(){
        super.modified();

        final Bounds b = this.getBoundsVector();

        this.binX = (b.width/S);
        this.binY = (b.height/S);
        this.mbinX = (this.binX/2f);
        this.mbinY = (this.binY/2f);
        this.bin = new Rectangle(0,0,this.binX,this.binY);
        this.binOver = new Rectangle(0,0,(this.binX+2),(this.binY+2));
    }
    public Color getSelected(){
        return this.selected;
    }
    public final Class<Enum<E>> getEnumClass(){
        return this.enumClass;
    }
    public final ColorPalette setEnumClass(Class<Enum<E>> clas){
        if (null != clas)
            this.enumClass = clas;
        return this;
    }
    public final String getEnumClassName(){
        if (null != this.enumClass)
            return this.enumClass.getName();
        else
            return null;
    }
    public final ColorPalette setEnumClassName(String name){
        if (null != name){
            try {
                Class clas = Class.forName(name);
                if (clas.isEnum()){

                    this.enumClass = (Class<Enum<E>>)clas;
                }
                else
                    throw new IllegalArgumentException(name);
            }
            catch (ClassNotFoundException exc){
                throw new IllegalArgumentException(name,exc);
            }
        }
        return this;
    }
    public final Enum<E> getEnumValue(){
        return this.enumValue;
    }
    public final ColorPalette setEnumValue(Enum<E> value){

        if (null != value)
            this.enumValue = value;

        return this;
    }
    public final String getEnumValueName(){
        if (null != this.enumValue)
            return this.enumValue.name();
        else
            return null;
    }
    public final ColorPalette setEnumValueName(String name){
        final Enum<E> value = Component.Tools.EnumValueOf(this.enumValueOf,name);
        return this.setEnumValue(value);
    }
    @Override
    public ColorPalette outputScene(Context g){

        Color reg = g.getColor();

        Bounds b = this.getBoundsVector();

        for (float x = 0, y; x < b.width; x += this.binX){

            this.bin.x = x;

            for (y = 0; y < b.height; y += this.binY){

                this.bin.y = y;

                int col = (int)((x+this.mbinX)/this.binX);
                int row = (int)((y+this.mbinY)/this.binY);


                Color color = ColorSwatches[row][col];

                g.setColor(color);

                g.fill(this.bin);

            }
        }

        g.setColor(reg);

        return this;
    }
    @Override
    public ColorPalette outputOverlay(Context g){

        Color reg = g.getColor();

        Bounds b = this.getBoundsVector();

        for (float x = 0, y; x < b.width; x += this.binX){

            this.bin.x = x;

            for (y = 0; y < b.height; y += this.binY){

                this.bin.y = y;

                int col = (int)((x+this.mbinX)/this.binX);
                int row = (int)((y+this.mbinY)/this.binY);


                Color color = ColorSwatches[row][col];


                if (col == this.overCol && row == this.overRow){
                    this.binOver.x = (x-1);
                    this.binOver.y = (y-1);
                    g.setStroke(StrokeOver);
                    g.draw(this.binOver);
                }

                g.setColor(color);

                g.fill(this.bin);
            }
        }

        g.setColor(reg);

        return this;
    }
    @Override
    public boolean input(Event e){
        if (super.input(e))
            return true;
        else {
            switch(e.getType()){
            case MouseMoved:{
                Point p = ((Event.Mouse.Point)e).getPoint();

                this.overCol = (int)(p.x/this.binX);
                this.overRow = (int)(p.y/this.binY);

                this.outputOverlay();

                return true;
            }
            case MouseUp:

                if (this.mouseIn){

                    if (null != this.enumValue){

                        final Component root = this.getRootContainer();
                        if (null != root){
                            /*
                             */
                            final Point local = ((Event.Mouse.Point)e).getPoint();

                            final int col = (int)(local.x/this.binX);
                            final int row = (int)(local.y/this.binY);

                            this.selected = ColorSwatches[row][col];
                            /*
                             */
                            final NamedAction action = new NamedAction(this.enumValue);

                            root.input(action);
                        }
                        else
                            throw new IllegalStateException();
                    }
                    return true;
                }
                else
                    return false;

            case Action:{
                Event.NamedAction action = (Event.NamedAction)e;

                if (this.mouseIn && action.isValueClass(this.enumClass)){

                    this.drop(this);

                    return true;
                }
                else
                    return false;
            }
            default:
                return false;
            }
        }
    }

    private final static Color[][] ColorSwatches = {
        {
            new Color(   0,   0,   0),
            new Color(   0,   0,  51),
            new Color(   0,   0, 102),
            new Color(   0,   0, 153),
            new Color(   0,   0, 204),
            new Color(   0,   0, 255),
            new Color(   0,   0, 255),
            new Color(   0,  51,   0),
            new Color(   0,  51,  51),
            new Color(   0,  51, 102),
            new Color(   0,  51, 153),
            new Color(   0,  51, 204),
            new Color(   0,  51, 255),
            new Color(   0,  51, 255),
            new Color(   0, 102,   0),
        },
        {
            new Color(   0, 102,  51),
            new Color(   0, 102, 102),
            new Color(   0, 102, 153),
            new Color(   0, 102, 204),
            new Color(   0, 102, 255),
            new Color(   0, 102, 255),
            new Color(   0, 153,   0),
            new Color(   0, 153,  51),
            new Color(   0, 153, 102),
            new Color(   0, 153, 153),
            new Color(   0, 153, 204),
            new Color(   0, 153, 255),
            new Color(   0, 153, 255),
            new Color(   0, 204,   0),
            new Color(   0, 204,  51),
        },
        {
            new Color(   0, 204, 102),
            new Color(   0, 204, 153),
            new Color(   0, 204, 204),
            new Color(   0, 204, 255),
            new Color(   0, 204, 255),
            new Color(   0, 255,   0),
            new Color(   0, 255,  51),
            new Color(   0, 255, 102),
            new Color(   0, 255, 153),
            new Color(   0, 255, 204),
            new Color(   0, 255, 255),
            new Color(   0, 255, 255),
            new Color(   0, 255, 255),
            new Color(  51,   0,   0),
            new Color(  51,   0,  51),
        },
        {
            new Color(  51,   0, 102),
            new Color(  51,   0, 153),
            new Color(  51,   0, 204),
            new Color(  51,   0, 255),
            new Color(  51,   0, 255),
            new Color(  51,  51,   0),
            new Color(  51,  51,  51),
            new Color(  51,  51, 102),
            new Color(  51,  51, 153),
            new Color(  51,  51, 204),
            new Color(  51,  51, 255),
            new Color(  51,  51, 255),
            new Color(  51, 102,   0),
            new Color(  51, 102,  51),
            new Color(  51, 102, 102),
        },
        {
            new Color(  51, 102, 153),
            new Color(  51, 102, 204),
            new Color(  51, 102, 255),
            new Color(  51, 153,   0),
            new Color(  51, 153,  51),
            new Color(  51, 153, 102),
            new Color(  51, 153, 153),
            new Color(  51, 153, 204),
            new Color(  51, 153, 255),
            new Color(  51, 204,   0),
            new Color(  51, 204,  51),
            new Color(  51, 204, 102),
            new Color(  51, 204, 153),
            new Color(  51, 204, 204),
            new Color(  51, 204, 255),
        },
        {
            new Color(  51, 255,   0),
            new Color(  51, 255,  51),
            new Color(  51, 255, 102),
            new Color(  51, 255, 153),
            new Color(  51, 255, 204),
            new Color(  51, 255, 255),
            new Color( 102,   0,   0),
            new Color( 102,   0,  51),
            new Color( 102,   0, 102),
            new Color( 102,   0, 153),
            new Color( 102,   0, 204),
            new Color( 102,   0, 255),
            new Color( 102,  51,   0),
            new Color( 102,  51,  51),
            new Color( 102,  51, 102),
        },
        {
            new Color( 102,  51, 153),
            new Color( 102,  51, 204),
            new Color( 102,  51, 255),
            new Color( 102, 102,   0),
            new Color( 102, 102,  51),
            new Color( 102, 102, 102),
            new Color( 102, 102, 153),
            new Color( 102, 102, 204),
            new Color( 102, 102, 255),
            new Color( 102, 153,   0),
            new Color( 102, 153,  51),
            new Color( 102, 153, 102),
            new Color( 102, 153, 153),
            new Color( 102, 153, 204),
            new Color( 102, 153, 255),
        },
        {
            new Color( 102, 204,   0),
            new Color( 102, 204,  51),
            new Color( 102, 204, 102),
            new Color( 102, 204, 153),
            new Color( 102, 204, 204),
            new Color( 102, 204, 255),
            new Color( 102, 255,   0),
            new Color( 102, 255,  51),
            new Color( 102, 255, 102),
            new Color( 102, 255, 153),
            new Color( 102, 255, 204),
            new Color( 102, 255, 255),
            new Color( 153,   0,   0),
            new Color( 153,   0,  51),
            new Color( 153,   0, 102),
        },
        {
            new Color( 153,   0, 153),
            new Color( 153,   0, 204),
            new Color( 153,   0, 255),
            new Color( 153,  51,   0),
            new Color( 153,  51,  51),
            new Color( 153,  51, 102),
            new Color( 153,  51, 153),
            new Color( 153,  51, 204),
            new Color( 153,  51, 255),
            new Color( 153, 102,   0),
            new Color( 153, 102,  51),
            new Color( 153, 102, 102),
            new Color( 153, 102, 153),
            new Color( 153, 102, 204),
            new Color( 153, 102, 255),
        },
        {
            new Color( 153, 153,   0),
            new Color( 153, 153,  51),
            new Color( 153, 153, 102),
            new Color( 153, 153, 153),
            new Color( 153, 153, 204),
            new Color( 153, 153, 255),
            new Color( 153, 204,   0),
            new Color( 153, 204,  51),
            new Color( 153, 204, 102),
            new Color( 153, 204, 153),
            new Color( 153, 204, 204),
            new Color( 153, 204, 255),
            new Color( 153, 255,   0),
            new Color( 153, 255,  51),
            new Color( 153, 255, 102),
        },
        {
            new Color( 153, 255, 153),
            new Color( 153, 255, 204),
            new Color( 153, 255, 255),
            new Color( 204,   0,   0),
            new Color( 204,   0,  51),
            new Color( 204,   0, 102),
            new Color( 204,   0, 153),
            new Color( 204,   0, 204),
            new Color( 204,   0, 255),
            new Color( 204,  51,   0),
            new Color( 204,  51,  51),
            new Color( 204,  51, 102),
            new Color( 204,  51, 153),
            new Color( 204,  51, 204),
            new Color( 204,  51, 255),
        },
        {
            new Color( 204, 102,   0),
            new Color( 204, 102,  51),
            new Color( 204, 102, 102),
            new Color( 204, 102, 153),
            new Color( 204, 102, 204),
            new Color( 204, 102, 255),
            new Color( 204, 153,   0),
            new Color( 204, 153,  51),
            new Color( 204, 153, 102),
            new Color( 204, 153, 153),
            new Color( 204, 153, 204),
            new Color( 204, 153, 255),
            new Color( 204, 204,   0),
            new Color( 204, 204,  51),
            new Color( 204, 204, 102),
        },
        {
            new Color( 204, 204, 153),
            new Color( 204, 204, 204),
            new Color( 204, 204, 255),
            new Color( 204, 255,   0),
            new Color( 204, 255,  51),
            new Color( 204, 255, 102),
            new Color( 204, 255, 153),
            new Color( 204, 255, 204),
            new Color( 204, 255, 255),
            new Color( 255,   0,   0),
            new Color( 255,   0,  51),
            new Color( 255,   0, 102),
            new Color( 255,   0, 153),
            new Color( 255,   0, 204),
            new Color( 255,   0, 255),
        },
        {
            new Color( 255,  51,   0),
            new Color( 255,  51,  51),
            new Color( 255,  51, 102),
            new Color( 255,  51, 153),
            new Color( 255,  51, 204),
            new Color( 255,  51, 255),
            new Color( 255, 102,   0),
            new Color( 255, 102,  51),
            new Color( 255, 102, 102),
            new Color( 255, 102, 153),
            new Color( 255, 102, 204),
            new Color( 255, 102, 255),
            new Color( 255, 153,   0),
            new Color( 255, 153,  51),
            new Color( 255, 153, 102),
        },
        {
            new Color( 255, 153, 153),
            new Color( 255, 153, 204),
            new Color( 255, 153, 255),
            new Color( 255, 204,   0),
            new Color( 255, 204,  51),
            new Color( 255, 204, 102),
            new Color( 255, 204, 153),
            new Color( 255, 204, 204),
            new Color( 255, 204, 255),
            new Color( 255, 255,   0),
            new Color( 255, 255,  51),
            new Color( 255, 255, 102),
            new Color( 255, 255, 153),
            new Color( 255, 255, 204),
            new Color( 255, 255, 255),
        }

    };

    public final static int N = 225;
    public final static float S = 15;
}
