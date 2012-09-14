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
