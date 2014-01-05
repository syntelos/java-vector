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
package geda;

import java.io.LineNumberReader;
import java.io.IOException;

/**
 * Recreated via {@link Display} initialization to read a gEDA sch/sym file.
 */
public class Geda
    extends vector.Container
{


    public Geda(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.parent = true;
        this.scale = true;
        this.setMargin(new vector.Padding(100,100));
    }
    public boolean read(LineNumberReader in)
        throws IOException
    {
        this.init();

        int offset = 0;

        int type = in.read();

        vector.Container last = null, embed = null, attributed = null;
        

        while (-1 < type){
            switch(type){
            case 'v':
                if (0 == offset){
                    geda.Version version = new geda.Version(in);

                    this.add(version);

                    last = version;
                    break;
                }
                else
                    throw new IllegalArgumentException(String.format("Unrecognized type 'v' at line '%d'",in.getLineNumber()));
            case 'L':
                if (0 < offset){
                    geda.Line line = new geda.Line(in);

                    if (null != embed)
                        embed.add(line);
                    else 
                        this.add(line);

                    last = line;
                    break;
                }
                else
                    throw new IllegalArgumentException("Unrecognized file format");
            case 'G':
                if (0 < offset){
                    geda.Picture picture = new geda.Picture(in);

                    if (null != embed)
                        embed.add(picture);
                    else 
                        this.add(picture);

                    last = picture;
                    break;
                }
                else
                    throw new IllegalArgumentException("Unrecognized file format");
            case 'B':
                if (0 < offset){
                    geda.Box box = new geda.Box(in);

                    if (null != embed)
                        embed.add(box);
                    else 
                        this.add(box);

                    last = box;
                    break;
                }
                else
                    throw new IllegalArgumentException("Unrecognized file format");
            case 'V':
                if (0 < offset){
                    geda.Circle circle = new geda.Circle(in);

                    if (null != embed)
                        embed.add(circle);
                    else 
                        this.add(circle);

                    last = circle;
                    break;
                }
                else
                    throw new IllegalArgumentException("Unrecognized file format");
            case 'A':
                if (0 < offset){
                    geda.Arc arc = new geda.Arc(in);

                    if (null != embed)
                        embed.add(arc);
                    else 
                        this.add(arc);

                    last = arc;
                    break;
                }
                else
                    throw new IllegalArgumentException("Unrecognized file format");
            case 'T':
                if (0 < offset){
                    geda.Text text = new geda.Text(in);

                    if (text.isAttribute() && null != last)

                        last.add(text);

                    else if (null != attributed)
                        attributed.add(text);
                    else if (null != embed)
                        embed.add(text);
                    else 
                        this.add(text);

                    break;
                }
                else
                    throw new IllegalArgumentException("Unrecognized file format");
            case 'N':
                if (0 < offset){
                    geda.Net net = new geda.Net(in);

                    if (null != embed)
                        embed.add(net);
                    else 
                        this.add(net);

                    last = net;
                    break;
                }
                else
                    throw new IllegalArgumentException("Unrecognized file format");
            case 'U':
                if (0 < offset){
                    geda.Bus bus = new geda.Bus(in);

                    if (null != embed)
                        embed.add(bus);
                    else 
                        this.add(bus);

                    last = bus;
                    break;
                }
                else
                    throw new IllegalArgumentException("Unrecognized file format");
            case 'P':
                if (0 < offset){
                    geda.Pin pin = new geda.Pin(in);

                    if (null != embed)
                        embed.add(pin);
                    else 
                        this.add(pin);

                    last = pin;
                    break;
                }
                else
                    throw new IllegalArgumentException("Unrecognized file format");
            case 'C':
                if (0 < offset){
                    geda.Component component = new geda.Component(in);

                    if (null != embed)
                        embed.add(component);
                    else 
                        this.add(component);

                    last = component;
                    break;
                }
                else
                    throw new IllegalArgumentException("Unrecognized file format");
            case 'H':
                if (0 < offset){
                    geda.Path path = new geda.Path(in);

                    if (null != embed)
                        embed.add(path);
                    else 
                        this.add(path);

                    last = path;
                    break;
                }
                else
                    throw new IllegalArgumentException("Unrecognized file format");
            case 'F':
                if (0 < offset){
                    geda.Font font = new geda.Font(in);

                    if (null != embed)
                        embed.add(font);
                    else 
                        this.add(font);

                    break;
                }
                else
                    throw new IllegalArgumentException("Unrecognized file format");

            case '{':
                in.readLine();
                attributed = last;
                break;
            case '}':
                in.readLine();
                attributed = null;
                break;
            case '[':
                in.readLine();
                if (null == embed){
                    embed = last;
                    break;
                }
                else
                    throw new IllegalStateException("Nested embedding");

            case ']':
                in.readLine();
                embed = null;
                break;
            default:
                if (0 == offset)
                    throw new IllegalArgumentException("Unrecognized file format");
                else
                    throw new IllegalArgumentException(String.format("Unrecognized type '%c' at line '%d'",(new Character((char)type)),in.getLineNumber()));
            }
            type = in.read();
            offset += 1;
        }

        return (0 < this.count());
    }
}
