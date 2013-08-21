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
package platform;

import vector.Bounds;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Frame
    extends android.app.Activity
{


    protected volatile static DisplayMetrics screen;

    protected volatile static int width = 0;
    protected volatile static int height = 0;

    protected volatile static int x = 0;
    protected volatile static int y = 0;



    protected static void Init(DisplayMetrics screen){

        if (null != screen){

            Frame.screen = screen;

            final int w0 = screen.widthPixels;
            final int h0 = screen.heightPixels;

            final int sw, sh;
            if (w0 > h0){
                sw = w0;
                sh = h0;
            }
            else {
                sw = h0;
                sh = w0;
            }

            Frame.width = sw;
            Frame.height = sh;

            Frame.x = 0;
            Frame.y = 0;
        }
    }
    protected static float Points2Pixels(float points){
        final DisplayMetrics screen = Frame.screen;
        if (null != screen){

            final float dpi = (screen.scaledDensity*DisplayMetrics.DENSITY_MEDIUM);

            return (points/72f)*dpi;
        }
        else {

            final float dpi = (1.0f*DisplayMetrics.DENSITY_MEDIUM);

            return (points/72f)*dpi;
        }
    }
    protected static int Pixels2Points(float pixels){
        final DisplayMetrics screen = Frame.screen;
        if (null != screen){

            final float dpi = (screen.scaledDensity*DisplayMetrics.DENSITY_MEDIUM);

            return (int)((pixels/dpi)*72f);
        }
        else {

            final float dpi = (1.0f*DisplayMetrics.DENSITY_MEDIUM);

            return (int)((pixels/dpi)*72f);
        }
    }
    public final static Bounds Center(Bounds b){

        final float fw = Frame.width;

        if (null != b && 0.0f != fw){

            final float fh = Frame.height;

            final float sx = (fw/2);
            final float sy = (fh/2);

            final float bx = (b.width/2);
            final float by = (b.height/2);

            b.x = (sx-bx);
            b.y = (sy-by);
        }
        return b;
    }

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    protected static Frame Instance;


    protected Display display;


    public Frame(){
        super();

        Frame.Instance = this;

        this.reinit(this.createDisplay());
    }


    public Display getDisplay(){

        return this.display;
    }
    public Display createDisplay(){

        return new Display();
    }
    public void reinit(Display display){

        if (display != this.display){

            this.destroy(this.display);

            this.display = display;
            if (null != display){

                this.setContentView(display);

                display.init();
            }
        }
        else
            display.init();
    }
    public void init(){

        Resources resources = this.getResources();

        if (null != resources){

            Frame.Init(resources.getDisplayMetrics());

            this.reinit(this.display);
        }
    }
    public void destroy(Display display){

        if (null != display && display == this.display){
            this.display = null;

            display.destroy();
        }
    }
    public void destroy(){

        this.destroy(this.display);
    }
    /**
     * For subclasses not using Frame eval or Display open
     */
    public void modified(){

        Display display = this.display;
        if (null != display){

            display.modified();
        }
    }
    /**
     * For subclasses not using Frame eval or Display open
     */
    public void outputScene(){

        Display display = this.display;
        if (null != display){

            display.outputScene();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.init();
    }
    @Override
    protected void onStart() {
        super.onStart();

        this.modified();

        this.outputScene();
    }
    @Override
    protected void onResume() {
        super.onResume();

        final Display display = this.display;
        if (null != display){
            display.onResume();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        final Display display = this.display;
        if (null != display){
            display.onPause();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.destroy();
    }

    public final Frame warn(Throwable t, String fmt, Object... args){

        this.log.log(Level.WARNING,String.format(fmt,args),t);

        return this;
    }
    public final Frame warn(String fmt, Object... args){

        this.log.log(Level.WARNING,String.format(fmt,args));

        return this;
    }
    public final Frame error(Throwable t, String fmt, Object... args){

        this.log.log(Level.SEVERE,String.format(fmt,args),t);

        return this;
    }
    public final Frame error(String fmt, Object... args){

        this.log.log(Level.SEVERE,String.format(fmt,args));

        return this;
    }
    public final boolean open(File file){

        return this.display.open(file);
    }
    public final boolean open(URL url){

        return this.display.open(url);
    }
    public final boolean eval(String[] cli){
        if (0 < cli.length){

            String string = cli[0];
            try {
                try {
                    URL url = new URL(string);

                    if (!this.open(url)){

                        this.error("Reading failed '%s'",url);

                        return false;
                    }
                    else
                        return true;
                }
                catch (MalformedURLException exc){

                    File file = new File(string);
                    if (file.isFile()){

                        if (!this.open(file)){

                            this.error("Reading failed '%s'",file);

                            return false;
                        }
                        else
                            return true;
                    }
                    else {

                        this.error("File not found '%s'",file);

                        return false;
                    }
                }
            }
            catch (Throwable t){

                this.error(t,"Error reading '%s'",string);

                return false;
            }
        }
        else {

            this.error("Require file or url");

            return false;
        }
    }

}
