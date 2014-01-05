/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2012, John Pritchard, Syntelos
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
package platform.event;

import vector.Event;

import android.view.KeyEvent;

/**
 * 
 */
public class AbstractKey
    extends AbstractEvent
    implements Event.Key
{

    public final KeyCode code;

    public final char keyChar;

    public final int modifiers;


    public AbstractKey(Type type, KeyEvent evt){
        super(type);
        if (null != evt){

            this.code = KeyCode.For(evt.getKeyCode());
            this.keyChar = (char)evt.getUnicodeChar(evt.getMetaState());
            this.modifiers = 0;
        }
        else
            throw new IllegalArgumentException();
    }


    public final boolean isUp(){
        return (Event.Type.KeyUp == this.type);
    }
    public final boolean isDown(){
        return (Event.Type.KeyDown == this.type);
    }
    public KeyCode getCode(){
        return this.code;
    }
    public char getKeyChar(){
        return this.keyChar;
    }
    public boolean isControl(){
        return false;
    }
    public boolean isAlt(){
        return false;
    }
    public boolean isCode(){
        return this.code.isCode();
    }
    public boolean isChar(){
        return (!this.code.isCode());
    }
    protected StringBuilder toStringBuilder(){
        StringBuilder string = super.toStringBuilder();

        string.append(", code: ");
        string.append(this.code.name());

        if (this.isControl())
            string.append(", ctl");

        if (this.isAlt())
            string.append(", alt");

        if (this.isChar()){
            string.append(", char: '");
            string.append(this.keyChar);
            string.append('\'');
        }
        return string;
    }
}
