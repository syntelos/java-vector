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
            this.mouseIn = true;
            this.blink.set();
            return true;
        case MouseExited:
            this.mouseIn = false;
            return true;
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
            return false;
        }
    }
    public Text outputOverlay(Graphics2D g){

        if (this.mouseIn){

            g.setColor(this.getColor());

            Shape border = this.getBoundsVector();

            g.draw(border);


            if (this.blink.high()){

                Shape cursor = ((Editor)this.string).cursor(this);

                g.transform(this.getTransformParent());
                g.draw(cursor);
            }
        }
        return this;
    }
}
