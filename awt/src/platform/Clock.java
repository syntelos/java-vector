
package platform;

/**
 * A strongly monotonic series from the system clock.
 */
public abstract class Clock
    extends Object
{
    private static volatile long ClockTime = 0L;


    /**
     * @return System time
     */
    public final static long currentTimeMillis(){

        final long clockTime = ClockTime;

        final long irregularTime = java.lang.System.currentTimeMillis();

        if (irregularTime > clockTime || 0L == clockTime){

            return (ClockTime = irregularTime);
        }
        else 
            return clockTime;
    }
}
