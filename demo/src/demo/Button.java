package demo;

import vector.Bounds;
import vector.Event;

import java.awt.geom.Point2D;
import java.util.Random;

public class Button
    extends vector.Button
{
    private final static Random PRNG = new Random();


    public Button(){
        super();
    }


    @Override
    public boolean input(Event e){
        if (super.input(e))
            return true;
        else {
            switch (e.getType()){
            case Action:
                this.setLocationVector(this.randomLocation());
                this.relocated();
                this.outputScene();
                return true;
            default:
                return false;
            }
        }
    }
    protected Point2D randomLocation(){
        final Bounds root = this.getRootContainer().getBoundsVector();
        final Bounds bounds = this.getBoundsVector();
        final float w = (root.width-bounds.width);
        final float h = (root.height-bounds.height);
        final float x = (PRNG.nextFloat()*w);
        final float y = (PRNG.nextFloat()*h);

        return new Point2D.Float(x,y);
    }
}
