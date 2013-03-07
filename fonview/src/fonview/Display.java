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

import fv3.font.HersheyFont;
import fv3.font.HersheyFontReader;
import fv3.font.TTFFont;
import fv3.font.TTFFontReader;

import platform.Color;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * One child {@link Fonview}
 */
public class Display
    extends platform.Display
{

    private Cursor cursor;


    public Display(){
        super();
    }


    @Override
    public void init(){

        this.setBackground(Color.white);

        super.init();
    }
    @Override
    public void modified(){

        super.modified();
    }
    public Fonview get(){
        if (this.has(0))
            return (Fonview)this.get(0);
        else
            return null;
    }
    public boolean next(){

        return this.cursor.next(this);
    }
    public boolean previous(){

        return this.cursor.previous(this);
    }
    @Override
    public boolean open(File file){
        try {
            this.cursor = new Cursor.CF(file);

            final String name, fext;
            {
                final String[] parts = file.getName().split(".");
                name = parts[0];
                fext = parts[parts.length-1].toLowerCase();
            }
            if ("ttf".equals(fext)){

                this.init();

                Fonview fonview = new Fonview();

                this.add(fonview);

                if (fonview.open(new TTFFont(name,new TTFFontReader(file)))){

                    this.modified();

                    this.outputScene();

                    return true;
                }
                else
                    return false;
            }
            else if ("jhf".equals(fext)){

                this.init();

                Fonview fonview = new Fonview();

                this.add(fonview);

                if (fonview.open(new HersheyFont(name,new HersheyFontReader(file)))){

                    this.modified();

                    this.outputScene();

                    return true;
                }
            }
            else
                this.error("Unrecognized file type '%s'",fext);
        }
        catch (IOException exc){

            this.error(exc,"Reading file '%s'",file);
        }
        return false;
    }
    @Override
    public boolean open(URL url){
        try {
            this.cursor = new Cursor.CU(url);

            final String[] path = url.getPath().split("/");
            final String file = path[path.length-1];
            final String name, fext;
            {
                final String[] parts = file.split(".");
                name = parts[0];
                fext = parts[parts.length-1].toLowerCase();
            }
            if ("ttf".equals(fext)){

                this.init();

                Fonview fonview = new Fonview();

                this.add(fonview);

                if (fonview.open(new TTFFont(name,new TTFFontReader(name,url)))){

                    this.modified();

                    this.outputScene();

                    return true;
                }
            }
            else if ("jhf".equals(fext)){

                this.init();

                Fonview fonview = new Fonview();

                this.add(fonview);

                if (fonview.open(new HersheyFont(name,new HersheyFontReader(name,url)))){

                    this.modified();

                    this.outputScene();

                    return true;
                }
            }
            else
                this.error("Unrecognized file type '%s'",fext);
        }
        catch (IOException exc){

            this.error(exc,"Reading URL '%s'",url);
        }
        return false;
    }
}
