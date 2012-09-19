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
package vector.event;

import vector.Component;

/**
 * Animate the overlay
 */
public abstract class Repainter
    extends java.lang.Thread
{
    public final static long DefaultCycle = 1000L;
    /**
     * Animate the Scene
     */
    public final static class Scene
        extends Repainter
    {

        public Scene(Component c){
            super(c);
        }

        public void run(){
            try {

                while (!this.cancelled){

                    this.component.outputScene();

                    synchronized(this.monitor){
                        this.monitor.wait(this.cycle);
                    }
                }
            }
            catch (InterruptedException exc){
                return;
            }
            finally {
                this.component = null;
            }
        }
    }
    /**
     * Animate the Overlay
     */
    public final static class Overlay
        extends Repainter
    {

        public Overlay(Component c){
            super(c);
        }

        public void run(){
            try {

                while (!this.cancelled){

                    this.component.outputOverlay();

                    synchronized(this.monitor){
                        this.monitor.wait(this.cycle);
                    }
                }
            }
            catch (InterruptedException exc){
                return;
            }
            finally {
                this.component = null;
            }
        }
    }


    protected final Object monitor = new Object();

    protected Component component;

    protected volatile long cycle = DefaultCycle;

    protected volatile boolean cancelled;


    public Repainter(Component component){
        super("Repainter");
        this.setDaemon(true);
        if (null != component)
            this.component = component;
        else
            throw new IllegalArgumentException();
    }


    public void period(long requested){

        requested /= 2;

        if (0 < requested){

            this.cycle = Math.min(this.cycle,requested);
        }
    }
    public void cancel(){
        this.cancelled = true;
        synchronized(this.monitor){
            this.monitor.notify();
        }
    }
    public abstract void run();
}
