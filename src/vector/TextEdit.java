package vector;

import vector.text.Editor;

public class TextEdit
    extends Text
{
    private boolean mouseIn;


    public TextEdit(){
        super();
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

                return true;
            }
            else
                return false;
        default:
            return false;
        }
    }
}
