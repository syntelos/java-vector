package vector.text;

import vector.Text;
import vector.Font;

import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Line editor
 */
public class Editor
    extends Object
    implements CharSequence
{

    protected char[] string;

    public final Home home;

    public final Cursor cursor;


    public Editor(){
        this(Home.Nil);
    }
    public Editor(char[] text){
        this(Home.Nil,text);
    }
    public Editor(CharSequence text){
        this(Home.Nil,text);
    }
    public Editor(Home home){
        super();
        this.cursor = new Cursor(home);
        this.home = this.cursor.home;
    }
    public Editor(Home home, char[] text){
        super();
        this.cursor = new Cursor(home);
        this.home = this.cursor.home;
        this.set(text);
    }
    public Editor(Home home, CharSequence text){
        super();
        this.cursor = new Cursor(home);
        this.home = this.cursor.home;
        this.set(text);
    }


    public String trim(){
        return this.home.trim(this.string);
    }
    public Shape cursorDrawable(Text text){

        return this.cursor.getIBeam(text);
    }
    public boolean cursorHome(){

        return this.cursor.home();
    }
    public boolean cursorEnd(){

        return this.cursor.end(this.length());
    }
    public boolean cursor(int dx){

        return this.cursor.move(dx,this.length());
    }
    public boolean backspace(){

        char[] update = this.cursor.backspace(this.string);
        if (update != this.string){

            this.string = update;
            return true;
        }
        else
            return false;
    }
    public boolean delete(){

        char[] update = this.cursor.delete(this.string);
        if (update != this.string){

            this.string = update;
            return true;
        }
        else
            return false;
    }
    public boolean add(char key, int bound){

        char[] update = this.cursor.add(this.string, key, bound);
        if (update != this.string){

            this.string = update;
            return true;
        }
        else
            return false;
    }
    /**
     * @return Modified
     */
    public boolean set(CharSequence string){
        if (this.equals(string))
            return false;
        else {
            this.string = this.home.cat(string);
            this.cursor.home();

            return true;
        }
    }
    /**
     * @return Modified
     */
    public boolean set(char[] cary){
        if (this.equals(cary))
            return false;
        else {
            this.string = ToCharArray(cary);
            this.cursor.end(this.length());

            return true;
        }
    }
    public Editor add(CharSequence string){
        if (null == this.string){
            this.set(string);
            return this;
        }
        else {
            char[] source = ToCharArray(string);
            if (null != source){
                int thisLength = this.string.length;
                int sourceLength = source.length;
                char[] copier = new char[thisLength+sourceLength];
                System.arraycopy(this.string,0,copier,0,thisLength);
                System.arraycopy(source,0,copier,thisLength,sourceLength);
                this.string = copier;

                this.cursor.end(this.length());
            }
            return this;
        }
    }
    public Editor add(int index, CharSequence string){
        if (null == this.string){
            if (0 == index){
                this.set(string);
                return this;
            }
            else
                throw new IllegalArgumentException(String.valueOf(index));
        }
        else {
            char[] source = ToCharArray(string);
            if (null != source){
                int thisLength = this.string.length;
                int sourceLength = source.length;
                if (0 == index){
                    char[] copier = new char[thisLength+sourceLength];
                    System.arraycopy(source,0,copier,0,sourceLength);
                    System.arraycopy(this.string,0,copier,sourceLength,thisLength);
                    this.string = copier;

                    this.cursor.left(index);
                }
                else if (index == (thisLength-1)){
                    char[] copier = new char[thisLength+sourceLength];
                    System.arraycopy(this.string,0,copier,0,thisLength);
                    System.arraycopy(source,0,copier,thisLength,sourceLength);
                    this.string = copier;

                    this.cursor.left(index);
                }
                else if (index < thisLength){
                    char[] copier = new char[thisLength+sourceLength];
                    System.arraycopy(this.string,0,copier,0,index);
                    System.arraycopy(source,0,copier,index,sourceLength);
                    System.arraycopy(this.string,index,copier,(index+sourceLength),(thisLength-index-1));
                    this.string = copier;

                    this.cursor.left(index);
                }
                else 
                    throw new IllegalArgumentException(String.valueOf(index));
            }
        }
        return this;
    }
    /*
     * String buffer / List of chars 
     */
    public boolean isEmpty(){
        return (null == this.string);
    }
    public boolean isNotEmpty(){
        return (null != this.string);
    }
    public Editor clear(){
        this.string = null;
        this.cursor.home();

        return this;
    }
    /*
     * CharSequence 
     */
    public int length(){
        if (null == this.string)
            return 0;
        else
            return this.string.length;
    }
    public char charAt(int idx){
        if (-1 < idx && idx < this.length())
            return this.string[idx];
        else
            throw new ArrayIndexOutOfBoundsException(String.valueOf(idx));
    }
    public String subSequence(int start, int end){
        if (-1 < start && start <= end && end < this.length()){
            int count = (end-start);
            if (0 < count){
                char[] string = new char[count];
                System.arraycopy(this.string,start,string,0,count);
                return new String(string);
            }
            else
                return "";
        }
        else
            throw new ArrayIndexOutOfBoundsException(String.format("(%d,%d)",start,end));
    }
    /*
     * CharSequence
     */
    public String toString(){
        if (null == this.string)
            return "";
        else
            return new String(this.string);
    }
    /*
     * Object
     */
    public boolean equals(Object that){
        if (that instanceof CharSequence)
            return this.equals( (CharSequence)that);
        else if (that instanceof char[])
            return this.equals( (char[])that);
        else
            return false;
    }
    public boolean equals(CharSequence that){
        /*
         * Null is not equal to anything so that set(o) always
         * initializes
         */
        if (null == that || null == this.string)
            return false;
        else {
            final int count = this.length();
            if (count == that.length()){
                for (int cc = 0; cc < count; cc++){
                    if (this.charAt(cc) != that.charAt(cc))
                        return false;
                }
                return true;
            }
            else
                return false;
        }
    }
    public boolean equals(char[] that){
        /*
         * Null is not equal to anything so that set(o) always
         * initializes
         */
        if (null == that || null == this.string)
            return false;
        else {
            final int count = this.length();
            if (count == that.length){
                for (int cc = 0; cc < count; cc++){
                    if (this.charAt(cc) != that[cc])
                        return false;
                }
                return true;
            }
            else
                return false;
        }
    }


    public final static char[] ToCharArray(char[] cary){
        if (null == cary || 1 > cary.length)
            return null;
        else
            return cary;
    }
    public final static char[] ToCharArray(CharSequence string){

        if (null == string || 1 > string.length())
            return null;
        else if (string instanceof String)
            return ((String)string).toCharArray();
        else {
            final int count = string.length();
            if (0 < count){
                char[] cary = new char[count];
                for (int cc = 0; cc < count; cc++){

                    cary[cc] = string.charAt(cc);
                }
                return cary;
            }
            else
                return null;
        }
    }
    public final static char Hash(char[] cary){
        if (null == cary)
            return (char)0;
        else {
            final int count = cary.length;
            int hash = 0;
            for (int cc = 0; cc < count; cc++){
                hash ^= cary[cc];
            }
            return (char)hash;
        }
    }
    public final static Editor[] Add(Editor[] list, Alignment alignment, Editor item){
        if (null == item)
            return list;
        else if (null == list)
            return new Editor[]{item};
        else {
            final int last = list.length;
            Editor[] copier = new Editor[last+1];

            switch(alignment){
            case Bottom:
                System.arraycopy(list,0,copier,0,last);
                copier[last] = item;
                return copier;

            case Top:
                System.arraycopy(list,0,copier,1,last);
                copier[0] = item;
                return copier;

            default:
                throw new IllegalArgumentException(alignment.name());
            }
        }
    }
    public final static Editor[] Current(Editor[] list, Alignment alignment, Editor item){
        if (null == item || null == list)
            throw new IllegalArgumentException();
        else {
            final int last = (list.length-1);

            switch(alignment){
            case Bottom:
                System.arraycopy(list,1,list,0,last);
                list[last] = item;
                return list;

            case Top:
                System.arraycopy(list,0,list,1,last);
                list[0] = item;
                return list;

            default:
                throw new IllegalArgumentException(alignment.name());
            }
        }
    }
    public final static Editor[] Previous(Editor[] list, Alignment alignment, int rows, Editor item){
        if (null == item || null == list)
            throw new IllegalArgumentException();
        else if (list.length < rows){
            final int len = list.length;

            Editor[] copier = new Editor[len+1];
            switch(alignment){
            case Bottom:{
                final int prev = (len-1);
                Editor bot = list[prev];

                System.arraycopy(list,0,copier,0,len);
                copier[prev] = item;
                copier[len] = bot;
                return copier;
            }
            case Top:{
                Editor top = list[0];
                System.arraycopy(list,0,copier,1,len);
                copier[0] = top;
                copier[1] = item;
                return copier;
            }
            default:
                throw new IllegalArgumentException(alignment.name());
            }
        }
        else {
            final int len = list.length;

            switch(alignment){
            case Bottom:{
                final int bot = (len-1);

                System.arraycopy(list,1,list,0,bot);
                list[bot-1] = item;
                return list;
            }
            case Top:{
                int top = (len-1);

                System.arraycopy(list,0,list,1,top);
                list[1] = item;
                return list;
            }
            default:
                throw new IllegalArgumentException(alignment.name());
            }
        }
    }
}
