package vector;

import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Text cursor logic
 */
public final class Cursor {

    /**
     * Text line left hand side is "zero", or a prompt.
     */
    public final static class Home 
        extends Object
    {
        public final static Home Nil = new Home();


        public final int length, lengthM1, lengthP1;

        public final CharSequence string;

        private final char[] cary;


        public Home(){
            this(0,null);
        }
        public Home(int length){
            this(length,null);
        }
        public Home(CharSequence string){
            this(string.length(),string);
        }
        public Home(int length, CharSequence string){
            super();

            if (null != string){
                if (length == string.length()){
                    this.length = length;
                    this.string = string;
                    this.cary = Text.ToCharArray(string);
                }
                else {
                    throw new IllegalArgumentException(String.valueOf(length));
                }
            }
            else if (-1 < length){
                this.length = length;
                this.string = Home.Fill(length);
                this.cary = Text.ToCharArray(string);
            }
            else {
                throw new IllegalArgumentException(String.valueOf(length));
            }

            this.lengthM1 = this.length-1;
            this.lengthP1 = this.length+1;
        }


        public char[] cat(){
            if (null != this.cary)
                return this.cary.clone();
            else
                return null;
        }
        public char[] cat(CharSequence text){

            return this.cat(Text.ToCharArray(text));
        }
        public char[] cat(char[] text){
            if (0 < this.length){
                if (null != text){
                    char[] cary = new char[this.length+text.length];
                    System.arraycopy(this.cary,0,cary,0,this.length);
                    System.arraycopy(text,0,cary,this.length,text.length);
                    return cary;
                }
                else
                    return this.cary.clone();
            }
            else if (null == text)
                return null;
            else
                return text;
        }
        public String trim(String text){
            if (this.length < text.length())
                return text.substring(this.length).trim();
            else
                return "";
        }
        public String trim(char[] text){
            if (null != text && this.length < text.length){

                final int count = (text.length-this.length);
                if (0 < count)
                    return new String(text,this.length,count).trim();
            }
            return "";
        }
        public int hashCode(){
            return this.string.hashCode();
        }
        public boolean equals(Object that){
            if (this == that)
                return true;
            else if (that instanceof Home)
                return this.equals( (Home)that);
            else
                return false;
        }
        public boolean equals(Home that){
            if (null == that)
                return false;
            else
                return this.string.equals(that.string);
        }
        public String toString(){
            return String.format("[%d]%s",length,string);
        }


        public final static String Fill(int len){
            StringBuilder string = new StringBuilder();
            for (int cc = 0; cc < len; cc++){
                string.append(' ');
            }
            return string.toString();
        }
    }


    public final Home home;

    private volatile boolean left = true;

    private volatile int index;


    public Cursor(Home home){
        super();
        if (null != home)
            this.home = home;
        else
            this.home = Home.Nil;
    }


    public boolean isLeft(){
        return this.left;
    }
    public boolean isRight(){
        return (!this.left);
    }
    public int getIndex(){
        return this.index;
    }
    public boolean home(){

        if (this.home.length != this.index || true != this.left){
            this.index = this.home.length;
            this.left = true;
            return true;
        }
        else
            return false;
    }
    public boolean left(int index){

        if (index != this.index || true != this.left){
            this.index = index;
            this.left = true;
            return true;
        }
        else
            return false;
    }
    public boolean end(int count){

        int index = (count-1);
        if (index != this.index || false != this.left){
            this.index = index;
            this.left = false;
            return true;
        }
        else
            return false;
    }
    public boolean move(int dx, int count){
        if (this.left){
            int index = this.index + dx;
            if (this.home.lengthM1 < index){

                if (index < count)
                    return this.left(index);
                else 
                    return this.end(count);
            }
            else 
                return this.home();
        }
        else {
            if (this.home.length > dx){
                if (true != this.left){
                    this.left = true;
                    return true;
                }
            }
            return false;
        }
    }
    public char[] backspace(char[] string){
        if (null == string)
            return string;
        else {
            int strlen = string.length;

            if (this.left){
                if (this.home.length < this.index){

                    char[] copier = new char[strlen-1];

                    if (1 < this.index)
                        System.arraycopy(string,0,copier,0,(this.index-1));

                    System.arraycopy(string,this.index,copier,(this.index-1),(strlen-this.index));

                    this.index--;

                    return copier;
                }
                else
                    return string;
            }
            else if (this.home.lengthP1 == strlen){
                this.index = this.home.length;
                this.left = true;
                return this.home.cat();
            }
            else if (this.home.length < this.index){

                char[] copier = new char[strlen-1];

                System.arraycopy(string,0,copier,0,(strlen-1));

                this.index--;

                return copier;
            }
            else 
                throw new IllegalStateException();
        }
    }
    public char[] delete(char[] string){
        if (null == string)
            return this.home.cat();
        else {
            int strlen = string.length;

            if (this.left){

                char[] copier = new char[strlen-1];

                if (0 < this.index)
                    System.arraycopy(string,0,copier,0,this.index);

                System.arraycopy(string,(this.index+1),copier,this.index,(strlen-this.index-1));

                return copier;
            }
            else
                return string;
        }
    }
    public char[] add(char[] string, char ch, int bound){
        if (0 < bound){
            if (null == string){
                this.left = false;
                this.index = this.home.length;
                return new char[]{ch};
            }
            else {
                int strlen = string.length;
                int newlen = (strlen+1);
                if (newlen <= bound){

                    char[] copier = new char[newlen];

                    if (this.left){

                        if (0 == this.index)
                            System.arraycopy(string,0,copier,1,strlen);
                        else {
                            System.arraycopy(string,0,copier,0,this.index);
                            System.arraycopy(string,this.index,copier,(this.index+1),(strlen-this.index));
                        }
                        copier[this.index] = ch;

                        this.index++;
                    }
                    else {

                        System.arraycopy(string,0,copier,0,strlen);

                        copier[strlen] = ch;

                        this.index = strlen;
                    }
                    return copier;
                }
            }
        }
        return string;
    }

}
