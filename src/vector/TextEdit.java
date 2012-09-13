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
    protected boolean mouseIn;


    public TextEdit(){
        super();
    }


    @Override
    public void init(){
        super.init();

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

        this.modified();
        return this;
    }
    @Override
    public boolean isMouseIn(){
        return this.mouseIn;
    }
    public boolean input(Event e){
        switch(e.getType()){
        case MouseEntered:
            this.mouseIn = true;
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

                            this.modified();
                            this.outputScene();
                        }
                        break;
                    case END:
                        if (((Editor)this.string).end()){

                            this.outputOverlay();
                        }
                        break;
                    case HOME:
                        if (((Editor)this.string).home()){

                            this.outputOverlay();
                        }
                        break;
                    case LEFT:
                        if (((Editor)this.string).left()){

                            this.outputOverlay();
                        }
                        break;
                    case RIGHT:
                        if (((Editor)this.string).right()){

                            this.outputOverlay();
                        }
                        break;
                    case DELETE:
                        if (((Editor)this.string).delete()){

                            this.modified();
                            this.outputScene();
                        }
                        break;
                    default:
                        break;
                    }
                }
                else {
                    if (((Editor)this.string).add(k.getKeyChar(),this.cols())){

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

        Shape cursor = ((Editor)this.string).cursor(this);

        g.transform(this.getTransformParent());
        g.setColor(this.getColor());
        g.draw(cursor);

        return this;
    }
}
