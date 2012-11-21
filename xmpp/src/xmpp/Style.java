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
package xmpp;

import platform.Font;
import platform.Color;
import platform.Image;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;


public class Style
    extends java.lang.Object
{

    protected static Style Instance;
    /**
     * Persistent channel
     */
    public static Style Instance(){
        if (null == Instance){
            Instance = new Style();
        }
        return Instance;
    }


    public final static Font FontSmall(){
        return Instance().getFontSmall();
    }
    public final static Font FontLarge(){
        return Instance().getFontLarge();
    }
    public final static Font FontTitle(){
        return Instance().getFontTitle();
    }
    public final static Color BG(){
        return Instance().getBG();
    }
    public final static Color BG(float opacity){
        return Instance().getBG(opacity);
    }
    public final static Color BGD(){
        return Instance().getBGD();
    }
    public final static Color BGD(float opacity){
        return Instance().getBGD(opacity);
    }
    public final static Color FG(){
        return Instance().getFG();
    }
    public final static Color FG(float opacity){
        return Instance().getFG(opacity);
    }
    public final static Color OK(){
        return Instance().getOK();
    }
    public final static Color OK(float opacity){
        return Instance().getOK(opacity);
    }
    public final static Color AY(){
        return Instance().getAY();
    }
    public final static Color AY(float opacity){
        return Instance().getAY(opacity);
    }
    public final static Color ST(){
        return Instance().getST();
    }
    public final static Color ST(float opacity){
        return Instance().getST(opacity);
    }
    public final static Color NG(){
        return Instance().getNG();
    }
    public final static Color NG(float opacity){
        return Instance().getNG(opacity);
    }

    /**
     * Properties based style 
     */
    public static class Properties
        extends java.util.Properties
    {
        public final static String DefaultClassPathLocation = "/ui.style.properties";

        /**
         * Default class path location
         */
        public Properties(){
            this(Properties.DefaultClassPathLocation);
        }
        /**
         * Class path binding
         */
        public Properties(String path){
            super();
            try {
                InputStream in = this.getClass().getResourceAsStream(path);
                if (null != in){
                    try {
                        this.load(in);
                    }
                    finally {
                        in.close();
                    }
                }
            }
            catch (IOException exc){
            }
        }
        /**
         * Network binding
         */
        public Properties(URL url){
            super();
            try {
                InputStream in = url.openStream();
                if (null != in){
                    try {
                        this.load(in);
                    }
                    finally {
                        in.close();
                    }
                }
            }
            catch (IOException exc){
            }
        }


        public Color getColor(String name){
            String value = this.getProperty(name);
            if (null != value){
                try {
                    return Color.decode(value);
                }
                catch (RuntimeException exc){
                }
            }
            return null;
        }
        public Font getFont(String name){
            String value = this.getProperty(name);
            if (null != value)
                return Font.decode(value);
            else
                return null;
        }
        public int getInt(String name, int def){
            String value = this.getProperty(name);
            if (null != value){
                try {
                    return Integer.parseInt(value);
                }
                catch (RuntimeException exc){
                }
            }
            return def;
        }
        public Image getImage(String name){

            return BindImage(this.getClass(),this.getProperty(name));
        }
        public Image getImage(Class cx, String name){

            return BindImage(cx,this.getProperty(name));
        }


        /**
         * 
         */
        public final static Image BindImage(Class cx, String resource){
            if (null != resource){
                try {
                    URL url = new URL(resource);
                    return new Image(url);
                }
                catch (java.net.MalformedURLException exc){

                    if (null != cx){

                        URL url = cx.getResource(resource);
                        if (null != url)
                            return new Image(url);
                    }
                }
            }
            return null;
        }
    }



    public final static String DefaultFontSmall = "monospaced-16", DefaultFontLarge = "monospaced-24", DefaultFontTitle = "monospaced-bold-36";

    public final static Color DefaultBg = new Color(0xEFEFEF);
    public final static Color DefaultBgd = new Color(0x808080);
    public final static Color DefaultFg = new Color(0x000000);
    public final static Color DefaultOk = new Color(0x00AF00);
    public final static Color DefaultAy = new Color(0xAFAF00);
    public final static Color DefaultSt = new Color(0x0000AF);
    public final static Color DefaultNg = new Color(0xAF0000);



    protected Font fontSmall, fontLarge, fontTitle;

    protected Color bg, bgd, fg, ok, ay, st, ng;


    protected Style(){
        this((new Properties()),DefaultFontSmall,DefaultFontLarge,DefaultFontTitle,DefaultBg,DefaultBgd,DefaultFg,DefaultOk,DefaultAy,DefaultSt,DefaultNg);
    }
    /**
     * Properties with default values
     */
    protected Style(Properties properties,
                    String fontSmall, String fontLarge, String fontTitle, 
                    Color bg, Color bgd, Color fg, Color ok, Color ay, Color st, Color ng)
    {
        super();
        Font testFont = properties.getFont("fnt.data");
        if (null != testFont)
            this.fontSmall = testFont;
        else {
            testFont = properties.getFont("font.small");
            if (null != testFont)
                this.fontSmall = testFont;
            else {
                testFont = platform.Font.decode(fontSmall);
                if (null != testFont)
                    this.fontSmall = testFont;
                else
                    throw new IllegalStateException("font.small");
            }
        }
        testFont = properties.getFont("fnt.fg");
        if (null != testFont)
            this.fontLarge = testFont;
        else {
            testFont = properties.getFont("font.large");
            if (null != testFont)
                this.fontLarge = testFont;
            else {
                testFont = platform.Font.decode(fontLarge);
                if (null != testFont)
                    this.fontLarge = testFont;
                else
                    throw new IllegalStateException("font.large");
            }
        }

        testFont = properties.getFont("fnt.title");
        if (null != testFont)
            this.fontTitle = testFont;
        else {
            testFont = properties.getFont("font.title");
            if (null != testFont)
                this.fontTitle = testFont;
            else {
                testFont = platform.Font.decode(fontTitle);
                if (null != testFont)
                    this.fontTitle = testFont;
                else
                    throw new IllegalStateException("font.title");
            }
        }

        Color testColor = properties.getColor("bg");
        if (null != testColor)
            this.bg = testColor;
        else if (null != bg)
            this.bg = bg;
        else
            throw new IllegalStateException("color.bg");


        testColor = properties.getColor("bgd");
        if (null != testColor)
            this.bgd = testColor;
        else {
            testColor = properties.getColor("color.bgd");
            if (null != testColor)
                this.bgd = testColor;
            else if (null != bgd)
                this.bgd = bgd;
            else
                throw new IllegalStateException("color.bgd");
        }

        testColor = properties.getColor("fg");
        if (null != testColor)
            this.fg = testColor;
        else {
            testColor = properties.getColor("color.fg");
            if (null != testColor)
                this.fg = fg;
            else if (null != fg)
                this.fg = fg;
            else
                throw new IllegalStateException("color.fg");
        }

        testColor = properties.getColor("ok");
        if (null != testColor)
            this.ok = testColor;
        else {
            testColor = properties.getColor("color.ok");
            if (null != testColor)
                this.ok = ok;
            else if (null != ok)
                this.ok = ok;
            else
                throw new IllegalStateException("color.ok");
        }

        testColor = properties.getColor("ay");
        if (null != testColor)
            this.ay = testColor;
        else {
            testColor = properties.getColor("color.ay");
            if (null != testColor)
                this.ay = ay;
            else if (null != ay)
                this.ay = ay;
            else
                throw new IllegalStateException("color.ay");
        }

        testColor = properties.getColor("st");
        if (null != testColor)
            this.st = testColor;
        else {
            testColor = properties.getColor("color.st");
            if (null != testColor)
                this.st = st;
            else if (null != st)
                this.st = st;
            else
                throw new IllegalStateException("color.st");
        }

        testColor = properties.getColor("ng");
        if (null != testColor)
            this.ng = testColor;
        else {
            testColor = properties.getColor("color.ng");
            if (null != testColor)
                this.ng = ng;
            else if (null != ng)
                this.ng = ng;
            else
                throw new IllegalStateException("color.ng");
        }
    }


    public Font getFontSmall(){
        return this.fontSmall;
    }
    public Font getFontLarge(){
        return this.fontLarge;
    }
    public Font getFontTitle(){
        return this.fontTitle;
    }
    public Color getBG(){
        return this.bg;
    }
    public Color getBG(float opacity){
        return this.bg.opacity(opacity);
    }
    public Color getBGD(){
        return this.bgd;
    }
    public Color getBGD(float opacity){
        return this.bgd.opacity(opacity);
    }
    public Color getFG(){
        return this.fg;
    }
    public Color getFG(float opacity){
        return this.fg.opacity(opacity);
    }
    public Color getOK(){
        return this.ok;
    }
    public Color getOK(float opacity){
        return this.ok.opacity(opacity);
    }
    public Color getAY(){
        return this.ay;
    }
    public Color getAY(float opacity){
        return this.ay.opacity(opacity);
    }
    public Color getST(){
        return this.st;
    }
    public Color getST(float opacity){
        return this.st.opacity(opacity);
    }
    public Color getNG(){
        return this.ng;
    }
    public Color getNG(float opacity){
        return this.ng.opacity(opacity);
    }

}
