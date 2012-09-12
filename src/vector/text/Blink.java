package vector.text;

public class Blink
    extends Object
{
    public final long cycle;

    private volatile long time;

    private volatile boolean state;


    public Blink(long cycle){
        super();
        if (0L < cycle)
            this.cycle = cycle;
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
}