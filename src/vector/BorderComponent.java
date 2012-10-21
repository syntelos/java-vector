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

import json.Json;
import json.ObjectJson;

/**
 * A component with a border and a background can work independently
 * (see {@link #fixed}), or for its parent (container).  Therefore any
 * {@link Container} is bordered by adding Border, while some
 * components can subclass {@link BorderComponent} or replicate the
 * micro-container idea.
 */
public class BorderComponent
    extends AbstractComponent
{

    protected Border border;

    protected Color background, backgroundOver;


    public BorderComponent(){
        super();
    }


    @Override
    public void destroy(){
        super.destroy();

        Border border = this.border;
        if (null != border){
            this.border = null;
            border.destroy();
        }
        this.background = null;
        this.backgroundOver = null;
    }
    @Override
    public void resized(){
        super.resized();

        Border border = this.border;
        if (null != border){
            border.resized();
        }
    }
    @Override
    public void modified(){
        super.modified();

        Border border = this.border;
        if (null != border){
            border.modified();
        }
    }
    public final Color getBackground(){

        return this.background;
    }
    public final BorderComponent setBackground(Color background){

        this.background = background;

        return this;
    }
    public final BorderComponent setBackground(String code){
        if (null != code)
            return this.setBackground(new Color(code));
        else
            return this;
    }
    public final Color getBackgroundOver(){

        return this.backgroundOver;
    }
    public final BorderComponent setBackgroundOver(Color backgroundOver){

        this.backgroundOver = backgroundOver;

        return this;
    }
    public final BorderComponent setBackgroundOver(String code){
        if (null != code)
            return this.setBackgroundOver(new Color(code));
        else
            return this;
    }
    public final <T extends Border> T getBorder(){
        return (T)this.border;
    }
    public final BorderComponent setBorder(Border border){
        if (null != this.border){
            Border b = this.border;
            if (null != b && b != border){
                this.border = null;
                b.destroy();
            }
        }
        this.border = border;

        if (null != border){
            border.setParentVector(this);
            border.init();
        }

        return this;
    }
    public final BorderComponent setBorder(Border border, Json model){

        this.setBorder(border);

        if (null != border){

            border.fromJson(model);
        }
        return this;
    }
    public boolean input(Event e){

        switch(e.getType()){

        case MouseEntered:{
            this.mouseIn = true;

            Border border = this.border;
            if (null != border){

                border.input(e);
            }
            return true;
        }
        case MouseExited:{
            this.mouseIn = false;

            Border border = this.border;
            if (null != border){

                border.input(e);
            }
            return true;
        }
        case MouseMoved:
            if (this.mouseIn){

                Border border = this.border;
                if (null != border){

                    final Point point = this.transformFromParent(((Event.Mouse.Motion)e).getPoint());

                    border.input(new platform.event.MouseMoved(point));
                }
            }
            return false;

        default:
            return false;
        }
    }
    public BorderComponent outputScene(Context g){
        if (null != this.border || null != this.background){
            /*
             * Compatible with subclass override
             */
            final Transform restore = g.getTransform();
            try {
                if (null != this.border){

                    this.transformFrom(g);

                    if (this.mouseIn && null != this.backgroundOver){
                        g.setColor(this.backgroundOver);

                        Shape borderShape = this.border.getShape();
                        if (null != borderShape){
                            g.fill(borderShape);
                        }
                        else
                            g.fill(this.border.getBoundsVector());
                    }
                    else if (null != this.background){
                        g.setColor(this.background);

                        Shape borderShape = this.border.getShape();
                        if (null != borderShape){
                            g.fill(borderShape);
                        }
                        else
                            g.fill(this.border.getBoundsVector());
                    }

                    Context cg = g.create();
                    try {
                        border.outputScene(cg);
                    }
                    finally {
                        cg.dispose();
                    }
                }
                else if (null != this.background){
                    g.setColor(this.background);
                    g.fill(this.getBoundsVector());
                }
            }
            finally {
                g.setTransform(restore);
            }
        }
        return this;
    }
    public BorderComponent outputOverlay(Context g){
        if (null != this.border){
            /*
             * Compatible with subclass override
             */
            final Transform restore = g.getTransform();
            try {
                this.transformFrom(g);

                Context cg = g.create();
                try {
                    border.outputOverlay(cg);
                }
                finally {
                    cg.dispose();
                }
            }
            finally {
                g.setTransform(restore);
            }
        }
        return this;
    }

    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("border",this.getBorder());
        thisModel.setValue("background",this.getBackground());
        thisModel.setValue("background-over",this.getBackgroundOver());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setBorder( thisModel.getValue("border",Border.class), thisModel.at("border"));
        this.setBackground( thisModel.getValue("background",Color.class));
        this.setBackgroundOver( thisModel.getValue("background-over",Color.class));

        return true;
    }
}
