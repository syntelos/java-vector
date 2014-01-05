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
package vector;

import platform.Color;
/**
 * 
 */
public enum NamedColors {
    white(Color.white),
    WHITE(Color.WHITE),
    lightGray(Color.lightGray),
    LIGHT_GRAY(Color.LIGHT_GRAY),
    gray(Color.gray),
    GRAY(Color.GRAY),
    darkGray(Color.darkGray),
    DARK_GRAY(Color.DARK_GRAY),
    black(Color.black),
    BLACK(Color.BLACK),
    red(Color.red),
    RED(Color.RED),
    pink(Color.pink),
    PINK(Color.PINK),
    orange(Color.orange),
    ORANGE(Color.ORANGE),
    yellow(Color.yellow),
    YELLOW(Color.YELLOW),
    green(Color.green),
    GREEN(Color.GREEN),
    magenta(Color.magenta),
    MAGENTA(Color.MAGENTA),
    cyan(Color.cyan),
    CYAN(Color.CYAN),
    blue(Color.blue),
    BLUE(Color.BLUE);

    public final Color value;

    private NamedColors(Color value){
        this.value = value;
    }


    public final static NamedColors For(String name){
        try {
            return NamedColors.valueOf(name);
        }
        catch (RuntimeException exc){

            return null;
        }
    }
}
