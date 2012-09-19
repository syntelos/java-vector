package vector;

import vector.text.Editor;

import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * Line editor
 */
public class TextEdit
    extends Text
{

    protected Blink blink;


    public TextEdit(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.blink = new Blink(this,800);

        if (this.string instanceof Editor)
            ((Editor)this.string).end();
        else
            this.string = new Editor();
    }
    @Override
    public Text setText(String text){

        if (null != text && 0 < text.length())

            this.string = new Editor(text);
        else
            this.string = new Editor();

        return this;
    }
    public boolean input(Event e){
        switch(e.getType()){
        case MouseEntered:

            if (null != this.colorOver || null != this.strokeOver){

                this.outputScene();
            }
            this.blink.set();

            return super.input(e);

        case MouseExited:

            this.blink.set();

            return super.input(e);

        case KeyUp:
            if (this.mouseIn){
                final Event.Key k = (Event.Key)e;
                if (k.isCode()){
                    switch(k.getCode()){
                    case BACK_SPACE:
                        if (((Editor)this.string).backspace()){
                            this.blink.set();
                            this.modified();
                            this.outputScene();
                        }
                        break;
                    case END:
                        if (((Editor)this.string).end()){
                            this.blink.set();
                            this.outputOverlay();
                        }
                        break;
                    case HOME:
                        if (((Editor)this.string).home()){
                            this.blink.set();
                            this.outputOverlay();
                        }
                        break;
                    case LEFT:
                        if (((Editor)this.string).left()){
                            this.blink.set();
                            this.outputOverlay();
                        }
                        break;
                    case RIGHT:
                        if (((Editor)this.string).right()){
                            this.blink.set();
                            this.outputOverlay();
                        }
                        break;
                    case DELETE:
                        if (((Editor)this.string).delete()){
                            this.blink.set();
                            this.modified();
                            this.outputScene();
                        }
                        break;
                    default:
                        break;
                    }
                }
                else {
                    if (((Editor)this.string).add(k.getKeyChar(),this.getCols())){
                        this.blink.set();
                        this.modified();
                        this.outputScene();
                    }
                }
                return true;
            }
            else
                return false;
        default:
            return super.input(e);
        }
    }
    public Text outputOverlay(Graphics2D g){

        super.outputOverlay(g);

        if (this.mouseIn && this.blink.high()){

            g.setColor(this.getColor());

            Shape cursor = ((Editor)this.string).cursor(this);

            this.getTransformParent().transformFrom(g);

            g.draw(cursor);
        }
        return this;
    }
}
