/*
 * Java Vector
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

public class Blink
    extends Object
{
    public final long cycle;

    private volatile long time;

    private volatile boolean state;


    /**
     * Basic time marker
     */
    public Blink(long cycle){
        super();
        if (0L < cycle){

            this.cycle = cycle;
        }
        else
            throw new IllegalArgumentException(String.valueOf(cycle));
    }
    /**
     * Time marker for output overlay animation
     */
    public Blink(Component component, long cycle){
        this(cycle);
        if (null != component){

            component.outputOverlayAnimate(cycle);
        }
        else
            throw new IllegalArgumentException(String.valueOf(cycle));
    }


    public boolean high(){
        final long time = System.currentTimeMillis();
        if (this.cycle < (time-this.time)){

            this.time = time;
            this.state = (!this.state);
        }
        return this.state;
    }
    public Blink set(){
        this.state = true;
        this.time = System.currentTimeMillis();
        return this;
    }
}
