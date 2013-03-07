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
package fonview;

import fv3.font.Font;
import fv3.font.Glyph;

import vector.Event;

/**
 * Recreated via {@link Display} initialization to display fv3 font
 * glyphs from a TTF or Hershey font file.
 */
public class Fonview
    extends vector.Container
{

    protected Font font;

    protected Glyph glyph;

    protected int index;


    public Fonview(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.index = 0;
        this.parent = true;
        this.scale = true;
        this.setMargin(new vector.Padding(100,100));
    }
    @Override
    public void destroy(){
        super.destroy();

        this.font = null;
        this.glyph = null;
    }
    public boolean open(Font font){
        this.init();

        this.font = font;

        return (null != (this.glyph = this.font.get(this.index)));
    }
    public boolean input(Event e){
        if (super.input(e))
            return true;
        else {
            switch(e.getType()){
            case KeyUp:
                if (this.mouseIn){
                    Event.Key ek = (Event.Key)e;
                    if (ek.isCode()){
                        switch(ek.getCode()){

                        case KP_UP:
                            return this.keyUp();

                        case KP_DOWN:
                            return this.keyDown();

                        case KP_LEFT:
                            return this.keyLeft();

                        case KP_RIGHT:
                            return this.keyRight();

                        default:
                            break;
                        }
                    }
                }
                return false;

            default:
                return false;
            }
        }
    }
    private boolean keyUp(){
        Display display = this.getRootContainer();
        return display.previous();
    }
    private boolean keyDown(){
        Display display = this.getRootContainer();
        return display.next();
    }
    private boolean keyRight(){
        this.index += 1;
        Glyph glyph = this.font.get(this.index);
        if (null == glyph){
            this.index = 0;
            glyph = this.font.get(this.index);
        }

        this.outputScene();

        return (null != (this.glyph = glyph));
    }
    private boolean keyLeft(){
        this.index -= 1;
        Glyph glyph = this.font.get(this.index);
        if (null == glyph){
            this.index = (this.font.getLength()-1);
            glyph = this.font.get(this.index);
        }

        this.outputScene();

        return (null != (this.glyph = glyph));
    }

}
