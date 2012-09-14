package vector;

import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Immutable universal event for a single input method, intended for
 * ease of application development.
 */
public interface Event {
    /**
     * Mouse drag is a point action
     * 
     * Mouse moved includes enter and exit
     */
    public enum Type {
        MouseEntered, MouseExited, MouseMoved, MouseDown, MouseUp, MouseDrag, MouseWheel, KeyDown, KeyUp, Action;

        public boolean isMouse(){
            switch(this){
            case MouseEntered:
            case MouseExited:
            case MouseMoved:
            case MouseDown:
            case MouseUp:
            case MouseDrag:
            case MouseWheel:
                return true;
            default:
                return false;
            }
        }
        public boolean isKey(){
            switch(this){
            case KeyDown:
            case KeyUp:
                return true;
            default:
                return false;
            }
        }
        public boolean isAction(){
            switch(this){
            case Action:
                return true;
            default:
                return false;
            }
        }

    }
    /**
     * Point action indicates button use
     */
    public interface Mouse
        extends Event
    {
        public enum Action {
            Entered, Exited, Moved, Point1, Point2, Point3, Wheel;


            public boolean isMotion(){
                switch(this){
                case Entered:
                case Exited:
                case Moved:
                    return true;
                default:
                    return false;
                }
            }
            public boolean isPoint(){
                switch(this){
                case Point1:
                case Point2:
                case Point3:
                    return true;
                default:
                    return false;
                }
            }
            public boolean isWheel(){
                switch(this){
                case Wheel:
                    return true;
                default:
                    return false;
                }
            }

            public final static Action PointButton(MouseEvent evt){
                switch(evt.getButton()){
                case BUTTON1:
                    return Event.Mouse.Action.Point1;

                case BUTTON2:
                    return Event.Mouse.Action.Point2;

                case BUTTON3:
                    return Event.Mouse.Action.Point3;

                default:
                    return null;
                }
            }
        }

        public Action getAction();

        public boolean isMotion();

        public boolean isPoint();

        public boolean isWheel();

        public Event apply(AffineTransform parent);

        public interface Motion
            extends Mouse
        {
            public Point2D getPoint();
        }
        public interface Point
            extends Mouse
        {
            public Point2D getPoint();
        }
        public interface Wheel
            extends Mouse
        {
            public int getCount();
        }
    }
    /**
     * Keyboard input employs {@link java.awt.event.KeyEvent AWT
     * KeyEvent} values, however, only the essential reliable
     * properties have been propagated.
     */
    public interface Key
        extends Event
    {
        public enum Code {
            ENTER('\n'),
            BACK_SPACE('\b'),
            TAB('\t'),
            CANCEL(0x03),
            CLEAR(0x0C),
            SHIFT(0x10),
            CONTROL(0x11),
            ALT(0x12),
            PAUSE(0x13),
            CAPS_LOCK(0x14),
            ESCAPE(0x1B),
            SPACE(0x20),
            PAGE_UP(0x21),
            PAGE_DOWN(0x22),
            END(0x23),
            HOME(0x24),
            LEFT(0x25),
            UP(0x26),
            RIGHT(0x27),
            DOWN(0x28),
            COMMA(0x2C),
            MINUS(0x2D),
            PERIOD(0x2E),
            SLASH(0x2F),
            _0(0x30),
            _1(0x31),
            _2(0x32),
            _3(0x33),
            _4(0x34),
            _5(0x35),
            _6(0x36),
            _7(0x37),
            _8(0x38),
            _9(0x39),
            SEMICOLON(0x3B),
            EQUALS(0x3D),
            A(0x41),
            B(0x42),
            C(0x43),
            D(0x44),
            E(0x45),
            F(0x46),
            G(0x47),
            H(0x48),
            I(0x49),
            J(0x4A),
            K(0x4B),
            L(0x4C),
            M(0x4D),
            N(0x4E),
            O(0x4F),
            P(0x50),
            Q(0x51),
            R(0x52),
            S(0x53),
            T(0x54),
            U(0x55),
            V(0x56),
            W(0x57),
            X(0x58),
            Y(0x59),
            Z(0x5A),
            OPEN_BRACKET(0x5B),
            BACK_SLASH(0x5C),
            CLOSE_BRACKET(0x5D),
            NUMPAD0(0x60),
            NUMPAD1(0x61),
            NUMPAD2(0x62),
            NUMPAD3(0x63),
            NUMPAD4(0x64),
            NUMPAD5(0x65),
            NUMPAD6(0x66),
            NUMPAD7(0x67),
            NUMPAD8(0x68),
            NUMPAD9(0x69),
            MULTIPLY(0x6A),
            ADD(0x6B),
            SEPARATOR(0x6C),
            SUBTRACT(0x6D),
            DECIMAL(0x6E),
            DIVIDE(0x6F),
            DELETE(0x7F),
            NUM_LOCK(0x90),
            SCROLL_LOCK(0x91),
            F1(0x70),
            F2(0x71),
            F3(0x72),
            F4(0x73),
            F5(0x74),
            F6(0x75),
            F7(0x76),
            F8(0x77),
            F9(0x78),
            F10(0x79),
            F11(0x7A),
            F12(0x7B),
            F13(0xF000),
            F14(0xF001),
            F15(0xF002),
            F16(0xF003),
            F17(0xF004),
            F18(0xF005),
            F19(0xF006),
            F20(0xF007),
            F21(0xF008),
            F22(0xF009),
            F23(0xF00A),
            F24(0xF00B),
            PRINTSCREEN(0x9A),
            INSERT(0x9B),
            HELP(0x9C),
            META(0x9D),
            BACK_QUOTE(0xC0),
            QUOTE(0xDE),
            KP_UP(0xE0),
            KP_DOWN(0xE1),
            KP_LEFT(0xE2),
            KP_RIGHT(0xE3),
            DEAD_GRAVE(0x80),
            DEAD_ACUTE(0x81),
            DEAD_CIRCUMFLEX(0x82),
            DEAD_TILDE(0x83),
            DEAD_MACRON(0x84),
            DEAD_BREVE(0x85),
            DEAD_ABOVEDOT(0x86),
            DEAD_DIAERESIS(0x87),
            DEAD_ABOVERING(0x88),
            DEAD_DOUBLEACUTE(0x89),
            DEAD_CARON(0x8a),
            DEAD_CEDILLA(0x8b),
            DEAD_OGONEK(0x8c),
            DEAD_IOTA(0x8d),
            DEAD_VOICED_SOUND(0x8e),
            DEAD_SEMIVOICED_SOUND(0x8f),
            AMPERSAND(0x96),
            ASTERISK(0x97),
            QUOTEDBL(0x98),
            LESS(0x99),
            GREATER(0xa0),
            BRACELEFT(0xa1),
            BRACERIGHT(0xa2),
            AT(0x0200),
            COLON(0x0201),
            CIRCUMFLEX(0x0202),
            DOLLAR(0x0203),
            EURO_SIGN(0x0204),
            EXCLAMATION_MARK(0x0205),
            INVERTED_EXCLAMATION_MARK(0x0206),
            LEFT_PARENTHESIS(0x0207),
            NUMBER_SIGN(0x0208),
            PLUS(0x0209),
            RIGHT_PARENTHESIS(0x020A),
            UNDERSCORE(0x020B),
            WINDOWS(0x020C),
            CONTEXT_MENU(0x020D),
            FINAL(0x0018),
            CONVERT(0x001C),
            NONCONVERT(0x001D),
            ACCEPT(0x001E),
            MODECHANGE(0x001F),
            KANA(0x0015),
            KANJI(0x0019),
            ALPHANUMERIC(0x00F0),
            KATAKANA(0x00F1),
            HIRAGANA(0x00F2),
            FULL_WIDTH(0x00F3),
            HALF_WIDTH(0x00F4),
            ROMAN_CHARACTERS(0x00F5),
            ALL_CANDIDATES(0x0100),
            PREVIOUS_CANDIDATE(0x0101),
            CODE_INPUT(0x0102),
            JAPANESE_KATAKANA(0x0103),
            JAPANESE_HIRAGANA(0x0104),
            JAPANESE_ROMAN(0x0105),
            KANA_LOCK(0x0106),
            INPUT_METHOD_ON_OFF(0x0107),
            CUT(0xFFD1),
            COPY(0xFFCD),
            PASTE(0xFFCF),
            UNDO(0xFFCB),
            AGAIN(0xFFC9),
            FIND(0xFFD0),
            PROPS(0xFFCA),
            STOP(0xFFC8),
            COMPOSE(0xFF20),
            ALT_GRAPH(0xFF7E),
            BEGIN(0xFF58),
            UNDEFINED(0x0);


            public final int code;


            private Code(int code){
                this.code = code;
            }


            /**
             * Non-printing action keys
             */
            public boolean isCode(){
                switch (this) {
                case ENTER:
                case BACK_SPACE:
                case CANCEL:
                case CLEAR:
                case SHIFT:
                case CONTROL:
                case ALT:
                case PAUSE:
                case CAPS_LOCK:
                case ESCAPE:
                case PAGE_UP:
                case PAGE_DOWN:
                case END:
                case HOME:
                case LEFT:
                case UP:
                case RIGHT:
                case DOWN:
                case BEGIN:
                case DELETE:
                case NUM_LOCK:
                case SCROLL_LOCK:
                case F1:
                case F2:
                case F3:
                case F4:
                case F5:
                case F6:
                case F7:
                case F8:
                case F9:
                case F10:
                case F11:
                case F12:
                case F13:
                case F14:
                case F15:
                case F16:
                case F17:
                case F18:
                case F19:
                case F20:
                case F21:
                case F22:
                case F23:
                case F24:
                case PRINTSCREEN:
                case INSERT:
                case HELP:
                case META:
                case KP_UP: 
                case KP_DOWN: 
                case KP_LEFT: 
                case KP_RIGHT: 

                case FINAL:
                case CONVERT:
                case NONCONVERT:
                case ACCEPT:
                case MODECHANGE:
                case KANA:
                case KANJI:
                case ALPHANUMERIC:
                case KATAKANA:
                case HIRAGANA:
                case FULL_WIDTH:
                case HALF_WIDTH:
                case ROMAN_CHARACTERS:
                case ALL_CANDIDATES:
                case PREVIOUS_CANDIDATE:
                case CODE_INPUT:
                case JAPANESE_KATAKANA:
                case JAPANESE_HIRAGANA:
                case JAPANESE_ROMAN:
                case KANA_LOCK:
                case INPUT_METHOD_ON_OFF:
                case AGAIN:
                case UNDO:
                case COPY:
                case PASTE:
                case CUT:
                case FIND:
                case PROPS:
                case STOP:
                case WINDOWS:
                case CONTEXT_MENU:
                    return true;
                default:
                    return false;
                }
            }

            public final static Code For(int code){
                switch(code){
                case '\n': return ENTER;
                case '\b': return BACK_SPACE;
                case '\t': return TAB;
                case 0x03: return CANCEL;
                case 0x0C: return CLEAR;
                case 0x10: return SHIFT;
                case 0x11: return CONTROL;
                case 0x12: return ALT;
                case 0x13: return PAUSE;
                case 0x14: return CAPS_LOCK;
                case 0x1B: return ESCAPE;
                case 0x20: return SPACE;
                case 0x21: return PAGE_UP;
                case 0x22: return PAGE_DOWN;
                case 0x23: return END;
                case 0x24: return HOME;
                case 0x25: return LEFT;
                case 0x26: return UP;
                case 0x27: return RIGHT;
                case 0x28: return DOWN;
                case 0x2C: return COMMA;
                case 0x2D: return MINUS;
                case 0x2E: return PERIOD;
                case 0x2F: return SLASH;
                case 0x30: return _0;
                case 0x31: return _1;
                case 0x32: return _2;
                case 0x33: return _3;
                case 0x34: return _4;
                case 0x35: return _5;
                case 0x36: return _6;
                case 0x37: return _7;
                case 0x38: return _8;
                case 0x39: return _9;
                case 0x3B: return SEMICOLON;
                case 0x3D: return EQUALS;
                case 0x41: return A;
                case 0x42: return B;
                case 0x43: return C;
                case 0x44: return D;
                case 0x45: return E;
                case 0x46: return F;
                case 0x47: return G;
                case 0x48: return H;
                case 0x49: return I;
                case 0x4A: return J;
                case 0x4B: return K;
                case 0x4C: return L;
                case 0x4D: return M;
                case 0x4E: return N;
                case 0x4F: return O;
                case 0x50: return P;
                case 0x51: return Q;
                case 0x52: return R;
                case 0x53: return S;
                case 0x54: return T;
                case 0x55: return U;
                case 0x56: return V;
                case 0x57: return W;
                case 0x58: return X;
                case 0x59: return Y;
                case 0x5A: return Z;
                case 0x5B: return OPEN_BRACKET;
                case 0x5C: return BACK_SLASH;
                case 0x5D: return CLOSE_BRACKET;
                case 0x60: return NUMPAD0;
                case 0x61: return NUMPAD1;
                case 0x62: return NUMPAD2;
                case 0x63: return NUMPAD3;
                case 0x64: return NUMPAD4;
                case 0x65: return NUMPAD5;
                case 0x66: return NUMPAD6;
                case 0x67: return NUMPAD7;
                case 0x68: return NUMPAD8;
                case 0x69: return NUMPAD9;
                case 0x6A: return MULTIPLY;
                case 0x6B: return ADD;
                case 0x6C: return SEPARATOR;
                case 0x6D: return SUBTRACT;
                case 0x6E: return DECIMAL;
                case 0x6F: return DIVIDE;
                case 0x7F: return DELETE;
                case 0x90: return NUM_LOCK;
                case 0x91: return SCROLL_LOCK;
                case 0x70: return F1;
                case 0x71: return F2;
                case 0x72: return F3;
                case 0x73: return F4;
                case 0x74: return F5;
                case 0x75: return F6;
                case 0x76: return F7;
                case 0x77: return F8;
                case 0x78: return F9;
                case 0x79: return F10;
                case 0x7A: return F11;
                case 0x7B: return F12;
                case 0xF000: return F13;
                case 0xF001: return F14;
                case 0xF002: return F15;
                case 0xF003: return F16;
                case 0xF004: return F17;
                case 0xF005: return F18;
                case 0xF006: return F19;
                case 0xF007: return F20;
                case 0xF008: return F21;
                case 0xF009: return F22;
                case 0xF00A: return F23;
                case 0xF00B: return F24;
                case 0x9A: return PRINTSCREEN;
                case 0x9B: return INSERT;
                case 0x9C: return HELP;
                case 0x9D: return META;
                case 0xC0: return BACK_QUOTE;
                case 0xDE: return QUOTE;
                case 0xE0: return KP_UP;
                case 0xE1: return KP_DOWN;
                case 0xE2: return KP_LEFT;
                case 0xE3: return KP_RIGHT;
                case 0x80: return DEAD_GRAVE;
                case 0x81: return DEAD_ACUTE;
                case 0x82: return DEAD_CIRCUMFLEX;
                case 0x83: return DEAD_TILDE;
                case 0x84: return DEAD_MACRON;
                case 0x85: return DEAD_BREVE;
                case 0x86: return DEAD_ABOVEDOT;
                case 0x87: return DEAD_DIAERESIS;
                case 0x88: return DEAD_ABOVERING;
                case 0x89: return DEAD_DOUBLEACUTE;
                case 0x8a: return DEAD_CARON;
                case 0x8b: return DEAD_CEDILLA;
                case 0x8c: return DEAD_OGONEK;
                case 0x8d: return DEAD_IOTA;
                case 0x8e: return DEAD_VOICED_SOUND;
                case 0x8f: return DEAD_SEMIVOICED_SOUND;
                case 0x96: return AMPERSAND;
                case 0x97: return ASTERISK;
                case 0x98: return QUOTEDBL;
                case 0x99: return LESS;
                case 0xa0: return GREATER;
                case 0xa1: return BRACELEFT;
                case 0xa2: return BRACERIGHT;
                case 0x0200: return AT;
                case 0x0201: return COLON;
                case 0x0202: return CIRCUMFLEX;
                case 0x0203: return DOLLAR;
                case 0x0204: return EURO_SIGN;
                case 0x0205: return EXCLAMATION_MARK;
                case 0x0206: return INVERTED_EXCLAMATION_MARK;
                case 0x0207: return LEFT_PARENTHESIS;
                case 0x0208: return NUMBER_SIGN;
                case 0x0209: return PLUS;
                case 0x020A: return RIGHT_PARENTHESIS;
                case 0x020B: return UNDERSCORE;
                case 0x020C: return WINDOWS;
                case 0x020D: return CONTEXT_MENU;
                case 0x0018: return FINAL;
                case 0x001C: return CONVERT;
                case 0x001D: return NONCONVERT;
                case 0x001E: return ACCEPT;
                case 0x001F: return MODECHANGE;
                case 0x0015: return KANA;
                case 0x0019: return KANJI;
                case 0x00F0: return ALPHANUMERIC;
                case 0x00F1: return KATAKANA;
                case 0x00F2: return HIRAGANA;
                case 0x00F3: return FULL_WIDTH;
                case 0x00F4: return HALF_WIDTH;
                case 0x00F5: return ROMAN_CHARACTERS;
                case 0x0100: return ALL_CANDIDATES;
                case 0x0101: return PREVIOUS_CANDIDATE;
                case 0x0102: return CODE_INPUT;
                case 0x0103: return JAPANESE_KATAKANA;
                case 0x0104: return JAPANESE_HIRAGANA;
                case 0x0105: return JAPANESE_ROMAN;
                case 0x0106: return KANA_LOCK;
                case 0x0107: return INPUT_METHOD_ON_OFF;
                case 0xFFD1: return CUT;
                case 0xFFCD: return COPY;
                case 0xFFCF: return PASTE;
                case 0xFFCB: return UNDO;
                case 0xFFC9: return AGAIN;
                case 0xFFD0: return FIND;
                case 0xFFCA: return PROPS;
                case 0xFFC8: return STOP;
                case 0xFF20: return COMPOSE;
                case 0xFF7E: return ALT_GRAPH;
                case 0xFF58: return BEGIN;
                default: return UNDEFINED;
                }
            }
        }

        public boolean isUp();

        public boolean isDown();
        /**
         * @return Code input
         * @see #isCode()
         */
        public Code getCode();
        /**
         * @return Character input
         * @see #isChar()
         */
        public char getKeyChar();
        /**
         * @return With CONTROL modifier
         */
        public boolean isControl();
        /**
         * @return With ALT modifier
         */
        public boolean isAlt();
        /**
         * Use key code
         */
        public boolean isCode();
        /**
         * Use key char
         */
        public boolean isChar();
    }
    /**
     * Propagation of button and menu events.  Dispatch global
     * application actions via root container input.
     */
    public interface NamedAction
        extends Event
    {

        public String getName();
    }

    public Type getType();

    public boolean isMouse();

    public boolean isKey();

    public boolean isAction();

}
