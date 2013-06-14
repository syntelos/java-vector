package platform;

import java.util.concurrent.TimeUnit;

/**
 * Extension point for subclasses.
 */
public class Semaphore
    extends java.util.concurrent.Semaphore
{

    public Semaphore(int permits) {
        super(permits);
    }
    public Semaphore(int permits, boolean fair) {
        super(permits,fair);
    }

    @Override
    public void acquire() throws InterruptedException {
        super.acquire();
    }
    @Override
    public boolean tryAcquire(){
        return super.tryAcquire();
    }
    public boolean tryAcquire(long timeout)
        throws InterruptedException
    {
        return super.tryAcquire(timeout,TimeUnit.MICROSECONDS);
    }
    @Override
    public void release(){
        super.release();
    }

}
