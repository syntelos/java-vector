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

import vector.text.Editor;
import vector.text.Home;

import java.awt.Shape;

/**
 * Line editor
 */
public class TextEdit
    extends Text
{
    protected Home home;

    protected Blink blink;


    public TextEdit(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.home = Home.Nil;

        this.blink = new Blink(this,800);

        if (this.string instanceof Editor)
            ((Editor)this.string).end();
        else
            this.string = new Editor(this.home);
    }
    @Override
    public void destroy(){
        super.destroy();

        this.home = null;
        this.blink = null;
    }
    @Override
    public TextEdit setText(String text){

        if (null != text && 0 < text.length())

            this.string = new Editor(this.home,text);
        else
            this.string = new Editor(this.home);

        return this;
    }
    public boolean input(Event e){
        switch(e.getType()){
        case MouseEntered:

            if (null != this.colorOver || null != this.strokeOver){

                this.outputScene();
            }
            this.blink.set();

            return super.input(e);

        case MouseExited:

            this.blink.set();

            return super.input(e);

        case KeyUp:
            if (this.mouseIn){
                final Event.Key k = (Event.Key)e;
                if (k.isCode()){
                    switch(k.getCode()){
                    case BACK_SPACE:
                        if (((Editor)this.string).backspace()){
                            this.blink.set();
                            this.modified();
                            this.outputScene();
                        }
                        break;
                    case END:
                        if (((Editor)this.string).end()){
                            this.blink.set();
                            this.outputOverlay();
                        }
                        break;
                    case HOME:
                        if (((Editor)this.string).home()){
                            this.blink.set();
                            this.outputOverlay();
                        }
                        break;
                    case LEFT:
                        if (((Editor)this.string).left()){
                            this.blink.set();
                            this.outputOverlay();
                        }
                        break;
                    case RIGHT:
                        if (((Editor)this.string).right()){
                            this.blink.set();
                            this.outputOverlay();
                        }
                        break;
                    case DELETE:
                        if (((Editor)this.string).delete()){
                            this.blink.set();
                            this.modified();
                            this.outputScene();
                        }
                        break;
                    default:
                        break;
                    }
                }
                else {
                    if (((Editor)this.string).add(k.getKeyChar(),this.getCols())){
                        this.blink.set();
                        this.modified();
                        this.outputScene();
                    }
                }
                return true;
            }
            else
                return false;
        default:
            return super.input(e);
        }
    }
    public TextEdit outputOverlay(Context g){

        super.outputOverlay(g);

        if (this.mouseIn && this.blink.high()){

            g.setColor(this.getColor());

            Shape cursor = ((Editor)this.string).cursor(this);

            this.getTransformParent().transformFrom(g);

            g.draw(cursor);
        }
        return this;
    }
}
