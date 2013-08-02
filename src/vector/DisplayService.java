/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2013, The DigiVac Company
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

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 */
public final class DisplayService
    extends services.Classes
{
    public final static DisplayService Instance = new DisplayService();
    /**
     * Indempotent class initialization
     */
    public static void Init(){
    }
    public static void Register(Display display){

        Instance.register(display);
    }
    public static void Deregister(Display display){

        Instance.deregister(display);
    }
    public static int IndexOf(Display display){

        return Instance.activeIndexOf(display);
    }
    public static int IndexOf(Class<Display> clas){

        return Instance.activeIndexOf(clas);
    }
    public static java.lang.Iterable<Display> Active(){

        return Instance.active();
    }
    public static <D extends Display> D[] ToArray(Class<D> component){

        return Instance.activeToArray(component);
    }
    public static <D extends Display> D Active(int idx){

        return Instance.active(idx);
    }
    public static Document Document(int idx){

        return Instance.document(idx);
    }
    public static boolean Read(int idx, String reference){

        return Instance.read(idx,reference);
    }
    public static boolean Read(int idx, URL reference){

        return Instance.read(idx,reference);
    }
    public static boolean Read(int idx, File reference){

        return Instance.read(idx,reference);
    }
    public static boolean Copy(int idx, String reference){

        return Instance.copy(idx,reference);
    }
    public static boolean Copy(int idx, URL reference){

        return Instance.copy(idx,reference);
    }
    public static boolean Copy(int idx, File reference){

        return Instance.copy(idx,reference);
    }
    public static boolean Save(int idx){

        return Instance.save(idx);
    }


    private final lxl.List<Display> active = new lxl.ArrayList();


    private DisplayService(){
        super(Display.class);
    }


    public void register(Display display){

        this.active.add(display);
    }
    public void deregister(Display display){

        this.active.remove(display);
    }
    public int activeIndexOf(Display display){

        return this.active.indexOf(display);
    }
    public int activeIndexOf(Class<Display> clas){

        final Display[] list = this.active.toArray(Display.class);

        return DisplayService.IndexOf(list,clas);
    }
    public <D extends Display> D[] activeToArray(Class<D> component){

        return (D[])this.active.toArray(component);
    }
    public java.lang.Iterable<Display> active(){

        return this.active.values();
    }
    public <D extends Display> D active(int idx){

        return (D)this.active.get(idx);
    }
    public Document document(int idx){

        final Display display = this.active.get(idx);
        if (null != display)

            return display.getDocumentVector();
        else
            return null;
    }
    public boolean read(int idx, String reference){

        final Display display = this.active.get(idx);
        if (null != display){
            try {
                URL url = new URL(reference);

                return display.open(url);
            }
            catch (MalformedURLException nurl){

                File file = new File(reference);

                if (file.isFile())
                    return display.open(file);
                else
                    return false;
            }
        }
        else
            return false;
    }
    public boolean read(int idx, URL reference){

        final Display display = this.active.get(idx);
        if (null != display)

            return display.open(reference);
        else
            return false;
    }
    public boolean read(int idx, File reference){

        final Display display = this.active.get(idx);
        if (null != display)

            return display.open(reference);
        else
            return false;
    }
    public boolean copy(int idx, String reference){

        final Display display = this.active.get(idx);
        if (null != display){
            try {
                URL url = new URL(reference);

                return display.copy(url);
            }
            catch (MalformedURLException nurl){

                File file = new File(reference);

                if (file.isFile())
                    return display.copy(file);
                else
                    return false;
            }
        }
        else
            return false;
    }
    public boolean copy(int idx, URL reference){

        final Display display = this.active.get(idx);
        if (null != display)

            return display.copy(reference);
        else
            return false;
    }
    public boolean copy(int idx, File reference){

        final Display display = this.active.get(idx);
        if (null != display)

            return display.copy(reference);
        else
            return false;
    }
    public boolean save(int idx){

        final Display display = this.active.get(idx);
        if (null != display)

            return display.save();
        else
            return false;
    }


    public final static int IndexOf(Display[] list, Class<Display> clas){
        if (null == list || null == clas)
            return -1;
        else {
            final int len = list.length;

            for (int cc = 0; cc < len; cc++){

                if (clas.isAssignableFrom(list[cc].getClass())){
                    return cc;
                }
            }
            return -1;
        }
    }
}
