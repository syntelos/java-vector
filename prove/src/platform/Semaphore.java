package platform;

import java.util.concurrent.TimeUnit;

/**
 * Extension point for subclasses.
 */
public class Semaphore
    extends Object
{

    public Semaphore(int permits) {
        super();
    }
    public Semaphore(int permits, boolean fair) {
        super();
    }

    public void acquire() throws InterruptedException {
    }
    public boolean tryAcquire(){
        return true;
    }
    public boolean tryAcquire(long timeout)
        throws InterruptedException
    {
        return true;
    }
    public void release(){
    }

}
