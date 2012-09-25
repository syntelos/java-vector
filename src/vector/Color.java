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

import json.Hex;

/**
 * 32 bit alpha-color I/O in <code>'#AARRGGBB'</code> string format.
 * Also accepts <code>'#RRGGBB'</code> string format.  Presently not
 * accepting <code>'#RGB'</code> format.  
 * 
 * Unrecognized format produces black color.
 */
public class Color
    extends java.awt.Color
{
    /*
     * Subclass overrides
     */
    public final static Color white      = new Color(255, 255, 255);
    public final static Color WHITE      = white;
    public final static Color lightGray  = new Color(192, 192, 192);
    public final static Color LIGHT_GRAY = lightGray;
    public final static Color gray       = new Color(128, 128, 128);
    public final static Color GRAY       = gray;
    public final static Color darkGray   = new Color(64, 64, 64);
    public final static Color DARK_GRAY  = darkGray;
    public final static Color black 	 = new Color(0, 0, 0);
    public final static Color BLACK      = black;
    public final static Color red        = new Color(255, 0, 0);
    public final static Color RED        = red;
    public final static Color pink       = new Color(255, 175, 175);
    public final static Color PINK       = pink;
    public final static Color orange 	 = new Color(255, 200, 0);
    public final static Color ORANGE     = orange;
    public final static Color yellow 	 = new Color(255, 255, 0);
    public final static Color YELLOW     = yellow;
    public final static Color green 	 = new Color(0, 255, 0);
    public final static Color GREEN      = green;
    public final static Color magenta	 = new Color(255, 0, 255);
    public final static Color MAGENTA    = magenta;
    public final static Color cyan 	     = new Color(0, 255, 255);
    public final static Color CYAN       = cyan;
    public final static Color blue 	     = new Color(0, 0, 255);
    public final static Color BLUE       = blue;


    public Color(java.awt.Color color){
        super(color.getRGB(),true);
    }
    public Color(String code){
        this(Hex.decode(Parse(code)));
    }
    private Color(byte[] code){
        super(R(code),G(code),B(code),A(code));
    }
    public Color(int r, int g, int b){
        super(r,g,b);
    }
    public Color(int r, int g, int b, int a){
        super(r,g,b,a);
    }
    public Color(int argb){
        super(argb,(0 != ((argb>>24)&0xFF)));
    }


    public String toString(){
        StringBuilder string = new StringBuilder();
        string.append('#');

        string.append(Hex.encode(this.getRGB()));

        return string.toString();
    }


    public final static int R(byte[] code){
        if (null == code)
            return 0;
        else {
            switch (code.length){
            case 3:
                return (code[0] & 0xFF);
            case 4:
                return (code[1] & 0xFF);
            default:
                return 0;
            }
        }
    }
    public final static int G(byte[] code){
        if (null == code)
            return 0;
        else {
            switch (code.length){
            case 3:
                return (code[1] & 0xFF);
            case 4:
                return (code[2] & 0xFF);
            default:
                return 0;
            }
        }
    }
    public final static int B(byte[] code){
        if (null == code)
            return 0;
        else {
            switch (code.length){
            case 3:
                return (code[2] & 0xFF);
            case 4:
                return (code[3] & 0xFF);
            default:
                return 0;
            }
        }
    }
    public final static int A(byte[] code){
        if (null == code)
            return 0;
        else {
            switch (code.length){
            case 3:
                return 0xFF;
            case 4:{
                final int value = (code[0] & 0xFF);
                if (0 == value)
                    return 0xFF;
                else
                    return value;
            }
            default:
                return 0;
            }
        }
    }
    public final static String Parse(String s){
        if (null == s || 1 > s.length())
            return null;
        else if ('#' == s.charAt(0))
            return s.substring(1);
        else
            return null;
    }
}
