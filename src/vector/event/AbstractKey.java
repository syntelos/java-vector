package vector.event;

import vector.Event;

import java.awt.event.InputEvent;
import static java.awt.event.InputEvent.*;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;

/**
 * 
 */
public class AbstractKey
    extends AbstractEvent
    implements Event.Key
{

    public final int keyCode;

    public final char keyChar;

    public final int modifiers;


    public AbstractKey(Type type, KeyEvent evt){
        super(type);
        if (null != evt){

            this.keyCode = evt.getKeyCode();
            this.keyChar = evt.getKeyChar();
            this.modifiers = evt.getModifiers();
        }
        else
            throw new IllegalArgumentException();
    }


    public final boolean isUp(){
        return (Event.Type.KeyUp == this.type);
    }
    public final boolean isDown(){
        return (Event.Type.KeyDown == this.type);
    }
    public int getKeyCode(){
        return this.keyCode;
    }
    public char getKeyChar(){
        return this.keyChar;
    }
    public boolean isControl(){
        return (0 != (this.modifiers & CTRL_MASK));
    }
    public boolean isAlt(){
        return (0 != (this.modifiers & ALT_MASK));
    }
    public boolean isCode(){
        return AbstractKey.IsCode(this.keyCode);
    }
    public boolean isChar(){
        return (!AbstractKey.IsCode(this.keyCode));
    }

    public final static boolean IsCode(int keyCode){
        switch (keyCode) {
        case VK_HOME:
        case VK_END:
        case VK_PAGE_UP:
        case VK_PAGE_DOWN:
        case VK_UP:
        case VK_DOWN:
        case VK_LEFT:
        case VK_RIGHT:
        case VK_BEGIN:
        case VK_KP_LEFT: 
        case VK_KP_UP: 
        case VK_KP_RIGHT: 
        case VK_KP_DOWN: 
        case VK_F1:
        case VK_F2:
        case VK_F3:
        case VK_F4:
        case VK_F5:
        case VK_F6:
        case VK_F7:
        case VK_F8:
        case VK_F9:
        case VK_F10:
        case VK_F11:
        case VK_F12:
        case VK_F13:
        case VK_F14:
        case VK_F15:
        case VK_F16:
        case VK_F17:
        case VK_F18:
        case VK_F19:
        case VK_F20:
        case VK_F21:
        case VK_F22:
        case VK_F23:
        case VK_F24:
        case VK_PRINTSCREEN:
        case VK_SCROLL_LOCK:
        case VK_CAPS_LOCK:
        case VK_NUM_LOCK:
        case VK_PAUSE:
        case VK_INSERT:
        case VK_FINAL:
        case VK_CONVERT:
        case VK_NONCONVERT:
        case VK_ACCEPT:
        case VK_MODECHANGE:
        case VK_KANA:
        case VK_KANJI:
        case VK_ALPHANUMERIC:
        case VK_KATAKANA:
        case VK_HIRAGANA:
        case VK_FULL_WIDTH:
        case VK_HALF_WIDTH:
        case VK_ROMAN_CHARACTERS:
        case VK_ALL_CANDIDATES:
        case VK_PREVIOUS_CANDIDATE:
        case VK_CODE_INPUT:
        case VK_JAPANESE_KATAKANA:
        case VK_JAPANESE_HIRAGANA:
        case VK_JAPANESE_ROMAN:
        case VK_KANA_LOCK:
        case VK_INPUT_METHOD_ON_OFF:
        case VK_AGAIN:
        case VK_UNDO:
        case VK_COPY:
        case VK_PASTE:
        case VK_CUT:
        case VK_FIND:
        case VK_PROPS:
        case VK_STOP:
        case VK_HELP:
        case VK_WINDOWS:
        case VK_CONTEXT_MENU:
            return true;
        default:
            return false;
        }
    }
}
