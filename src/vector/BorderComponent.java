package vector;

import json.Json;
import json.ObjectJson;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * A component with a border and a background can work independently
 * (see {@link #fixed}), or for its parent (container).  Therefore any
 * {@link Container} is bordered by adding Border, while some
 * components can subclass {@link BorderComponent} or replicate the
 * micro-container idea.
 */
public class BorderComponent
    extends AbstractComponent
{

    protected Border border;

    protected Color background, backgroundOver;


    public BorderComponent(){
        super();
    }


    @Override
    public void destroy(){
        super.destroy();

        Border border = this.border;
        if (null != border){
            this.border = null;
            border.destroy();
        }
        this.background = null;
    }
    @Override
    public void resized(){
        super.resized();

        Border border = this.border;
        if (null != border){
            border.resized();
        }
    }
    @Override
    public void modified(){
        super.modified();

        Border border = this.border;
        if (null != border){
            border.modified();
        }
    }
    public final Color getBackground(){

        return this.background;
    }
    public final BorderComponent setBackground(Color background){

        this.background = background;

        return this;
    }
    public final BorderComponent setBackground(String code){
        if (null != code)
            return this.setBackground(new Color(code));
        else
            return this;
    }
    public final Color getBackgroundOver(){

        return this.backgroundOver;
    }
    public final BorderComponent setBackgroundOver(Color backgroundOver){

        this.backgroundOver = backgroundOver;

        return this;
    }
    public final BorderComponent setBackgroundOver(String code){
        if (null != code)
            return this.setBackgroundOver(new Color(code));
        else
            return this;
    }
    public final <T extends Border> T getBorder(){
        return (T)this.border;
    }
    public final BorderComponent setBorder(Border border){
        if (null != this.border){
            Border b = this.border;
            if (null != b && b != border){
                this.border = null;
                b.destroy();
            }
        }
        this.border = border;

        if (null != border){
            border.setParentVector(this);
            border.init();
        }

        return this;
    }
    public final BorderComponent setBorder(Border border, Json model){

        this.setBorder(border);

        if (null != border){

            border.fromJson(model);
        }
        return this;
    }
    public boolean input(Event e){

        switch(e.getType()){

        case MouseEntered:{
            this.mouseIn = true;

            Border border = this.border;
            if (null != border){

                border.input(e);
            }
            return true;
        }
        case MouseExited:{
            this.mouseIn = false;

            Border border = this.border;
            if (null != border){

                border.input(e);
            }
            return true;
        }
        case MouseMoved:
            if (this.mouseIn){

                Border border = this.border;
                if (null != border){

                    final Point2D.Float point = this.transformFromParent(((Event.Mouse.Motion)e).getPoint());

                    border.input(new vector.event.MouseMoved(point));
                }
            }
            return false;

        default:
            return false;
        }
    }
    public BorderComponent outputScene(Graphics2D g){
        /*
         * Compatible with subclass override
         */
        final AffineTransform restore = g.getTransform();
        try {
            if (null != this.border){

                this.getTransformParent().transformFrom(g);

                if (this.mouseIn && null != this.backgroundOver){
                    g.setColor(this.backgroundOver);

                    Shape borderShape = this.border.getShape();
                    if (null != borderShape){
                        g.fill(borderShape);
                    }
                    else
                        g.fill(this.border.getBoundsVector());
                }
                else if (null != this.background){
                    g.setColor(this.background);

                    Shape borderShape = this.border.getShape();
                    if (null != borderShape){
                        g.fill(borderShape);
                    }
                    else
                        g.fill(this.border.getBoundsVector());
                }

                Graphics2D cg = (Graphics2D)g.create();
                try {
                    border.outputScene(cg);
                }
                finally {
                    cg.dispose();
                }
            }
            else if (null != this.background){
                g.setColor(this.background);
                g.fill(this.getBoundsVector());
            }
        }
        finally {
            g.setTransform(restore);
        }
        return this;
    }
    public BorderComponent outputOverlay(Graphics2D g){
        /*
         * Compatible with subclass override
         */
        final AffineTransform restore = g.getTransform();
        try {
            if (null != this.border){

                this.getTransformParent().transformFrom(g);

                Graphics2D cg = (Graphics2D)g.create();
                try {
                    border.outputOverlay(cg);
                }
                finally {
                    cg.dispose();
                }
            }
        }
        finally {
            g.setTransform(restore);
        }
        return this;
    }

    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("border",this.getBorder());
        thisModel.setValue("background",this.getBackground());
        thisModel.setValue("background-over",this.getBackgroundOver());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setBorder( thisModel.getValue("border",Border.class), thisModel.at("border"));
        this.setBackground( thisModel.getValue("background",Color.class));
        this.setBackgroundOver( thisModel.getValue("background-over",Color.class));

        return true;
    }
}
