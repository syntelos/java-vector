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
package platform.event;

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
        public final static String PropertyName = "platform.event.Repainter.Scene.Debug";
        public final static boolean Debug;
        static {
            String config = System.getProperty(PropertyName);
            boolean debug = (null != config && "true".equalsIgnoreCase(config));
            Debug = debug;
            if (Debug){
                vector.DebugTrace.out.println(PropertyName);
            }
        }

        /**
         * Permit one scene repainter to be active
         */
        public final static class Swarm
            extends platform.Semaphore
        {
            public final static Swarm Instance = new Swarm();

            private Swarm(){
                super(1);
            }
        }


        public Scene(Component c){
            super(c);
        }

        public void run(){
            try {

                while (!this.cancelled){

                    if (Scene.Swarm.Instance.tryAcquire()){
                        /*
                         * Take control
                         */
                        try {
                            final Component component = this.component;

                            while (!this.cancelled){

                                if (Debug){
                                    vector.DebugTrace.out.printf("%s (%s) outputScene%n",PropertyName,this.getName());
                                }

                                component.outputScene();

                                synchronized(this.monitor){
                                    this.monitor.wait(this.cycle);
                                }
                            }
                        }
                        finally {
                            Scene.Swarm.Instance.release();
                        }
                    }
                    else {
                        synchronized(this.monitor){
                            this.monitor.wait(this.cycle*10);
                        }
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
        public final static String PropertyName = "platform.event.Repainter.Overlay.Debug";
        public final static boolean Debug;
        static {
            String config = System.getProperty(PropertyName);
            boolean debug = (null != config && "true".equalsIgnoreCase(config));
            Debug = debug;
            if (Debug){
                vector.DebugTrace.out.println(PropertyName);
            }
        }

        /**
         * Permit one overlay repainter to be active
         */
        public final static class Swarm
            extends platform.Semaphore
        {
            public final static Swarm Instance = new Swarm();

            private Swarm(){
                super(1);
            }
        }


        public Overlay(Component c){
            super(c);
        }

        public void run(){
            try {

                while (!this.cancelled){

                    if (Overlay.Swarm.Instance.tryAcquire()){
                        /*
                         * Take control
                         */
                        try {
                            final Component component = this.component;

                            while (!this.cancelled){

                                if (Debug){
                                    vector.DebugTrace.out.printf("%s (%s) outputOverlay%n",PropertyName,this.getName());
                                }

                                component.outputOverlay();

                                synchronized(this.monitor){
                                    this.monitor.wait(this.cycle);
                                }
                            }
                        }
                        finally {
                            Overlay.Swarm.Instance.release();
                        }
                    }
                    else {
                        synchronized(this.monitor){
                            this.monitor.wait(this.cycle*10);
                        }
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
        super("Repainter/"+component.getClass().getName());
        this.setDaemon(true);
        this.setPriority(6);
        this.component = component;
    }


    public void period(long requested){

        if (0 < requested){

            this.cycle = requested;
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
