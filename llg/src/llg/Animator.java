/*
 * Copyright (c) 2009 John Pritchard, all rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package llg;

import java.awt.Component;
import java.awt.Rectangle;

/**
 * Repainter to coalesce events.
 * 
 * @see Awtk
 * @author jdp
 */
public final class Animator
    extends java.lang.Thread
{
    /**
     * Promotes uniform game time across different computers.
     */
    public static class Sync {

        private final static long DT = 40L;

        private volatile long last;

        public Sync(){
            super();
        }

        public synchronized void wake(){
            this.notify();
        }
        public synchronized void waitfor(){
            long last = System.currentTimeMillis();
            try {
                long dt = (DT - (last - this.last));
                this.last = last;
                if (0 < dt){
                    dt = Math.min(DT,dt);
                    this.wait(dt);
                }
            }
            catch (InterruptedException exc){
                throw new ThreadDeath();
            }
        }
    }


    private final BackingStore graphics;

    private final Sync sync = new Sync();

    private final Aut.Animation aut;

    private volatile boolean running = true;


    Animator(BackingStore graphics){
        super("LLG Animator");
        this.setDaemon(true);
        this.setPriority(MIN_PRIORITY);
        if (null != graphics){
            this.graphics = graphics;
            this.aut = new Aut.Animation(this);
            this.aut.start();
        }
        else
            throw new IllegalArgumentException("Missing graphics");
    }


    public void halt(){
        this.running = false;
        try {
            this.interrupt();

            Thread.sleep(Sync.DT+10L);

            this.stop();
        }
        catch (Exception exc){
            return;
        }
    }
    public void repaint(){
        this.sync.wake();
    }

    public void run(){

        BackingStore graphics = this.graphics;

        Sync sync = this.sync;
        Aut.Animation aut = this.aut;
        try {
            while (this.running){
                try {
                    sync.waitfor();

                    graphics.reinit();

                    aut.enter();

                    graphics.paint();
                }
                catch (InterruptedException aux){

                    interrupted();//(clear interrupt)
                }
                catch (Exception exc){
                    exc.printStackTrace();
                }
                finally {
                    aut.exit();
                }
            }
        }
        finally {
            aut.halt();
        }
    }
}
