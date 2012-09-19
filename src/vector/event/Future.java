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

/**
 * An inevitable future event that is postponed by interaction events.
 */
public final class Future
    extends java.lang.Thread
{

    public interface Expiration {

        public void interactionFutureCancelled(Object cancel);

        public void interactionFutureExpired();
    }

    public final static long DefaultTimeout = 1000L;

    private volatile static long TN = 0;


    private final Object monitor = new Object();

    private Future.Expiration app;
    private final Object cancel;
    private final long timeout, cycle;
    private volatile long expiration;
    private volatile boolean cancelled;


    public Future(Future.Expiration app){
        this(app,DefaultTimeout);
    }
    public Future(Future.Expiration app, Object cancel){
        this(app,cancel,DefaultTimeout);
    }
    public Future(Future.Expiration app, Object cancel, long timeout){
        super("Future/"+(++TN));
        if (null != app){
            this.app = app;
            this.cancel = cancel;

            if (0L < timeout){
                this.timeout = timeout;
                this.cycle = (long)(timeout * 1.2);
            }
            else
                throw new IllegalArgumentException();
        }
        else
            throw new IllegalArgumentException();
    }


    public void interaction(){
        this.expiration = (System.currentTimeMillis()+this.timeout);
    }
    public void cancel(){
        this.cancelled = true;
        this.expiration = 0L;
        synchronized(this.monitor){
            this.monitor.notify();
        }
    }
    public void execute(){
        this.expiration = 0L;
        synchronized(this.monitor){
            this.monitor.notify();
        }
    }
    private final boolean expired(){
        return (0L < (System.currentTimeMillis()-this.expiration));
    }
    public void run(){
        try {
            /*
             * (init)
             */
            this.interaction();
            /*
             * (run)
             */
            while (!this.expired()){
                synchronized(this.monitor){
                    this.monitor.wait(this.cycle);
                }
            }
        }
        catch (InterruptedException exc){
            return;
        }
        finally {
            if (this.cancelled)
                this.app.interactionFutureCancelled(this.cancel);
            else
                this.app.interactionFutureExpired();
            this.app = null;
        }
    }
}
