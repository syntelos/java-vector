/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2013, John Pritchard, Syntelos
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
package geda;

/**
 * 
 */
public class Frame
    extends platform.Frame
{

    public Frame(){
        this(null);
    }
    public Frame(String title){
        super((null != title)?(title):("Vector GEDA"));
    }


    @Override
    public Display createDisplay(){

        return new Display();
    }


    public static void main(String[] argv){

        final Frame frame = new Frame();

        frame.init();

        if (frame.eval(argv))
            return;
        else
            System.exit(1);
    }

}
