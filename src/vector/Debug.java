package vector;

import java.io.PrintStream;
import java.util.StringTokenizer;

/**
 * Call stack trace tool
 * 
 * <h3>Usage</h3>
 * <pre>
 * (new Debug("string").print());
 * (new Debug(o.toString()).print());
 * (new Debug("param: %d",param).print());
 * </pre>
 */
public class Debug 
    extends Throwable
{
    public final static PrintStream trace = System.err;


    /**
     * Call stack buffer
     */
    public final static class Buffer
        extends java.io.ByteArrayOutputStream
    {
        /**
         * Call stack parser
         */
        public final static class Stack
            extends Object
            implements Comparable<Stack>
        {

            public final String call;

            public final String source;

            public final int lno;

            public final boolean external;


            public Stack(String line){
                super();
                final StringTokenizer strtok = new StringTokenizer(line,"\t (:)");
                String at = strtok.nextToken();
                this.call = strtok.nextToken();
                this.source = strtok.nextToken();
                this.lno = Integer.parseInt(strtok.nextToken());
                this.external = (call.startsWith("java"));
            }


            public int compareTo(Stack that){
                if (this.source.equals(that.source)){

                    if (this.lno == that.lno)
                        return 0;
                    else if (this.lno > that.lno)
                        return +1;
                    else
                        return -1;
                }
                else
                    return Integer.MIN_VALUE;
            }
            public String toString(int indent){
                StringBuilder string = new StringBuilder();

                string.append(Buffer.Indent(indent));
                string.append(this.source);
                string.append(':');
                string.append(this.lno);
                string.append(':');
                string.append(this.call);

                return string.toString();
            }


            public final static Stack[] Add(Stack[] list, String line){
                if (null == line)
                    return list;
                else {
                    final Stack item = new Stack(line);

                    if (null == list)
                        return new Stack[]{item};
                    else {
                        int len = list.length;
                        Stack[] copier = new Stack[len+1];
                        System.arraycopy(list,0,copier,0,len);
                        copier[len] = item;
                        return copier;
                    }
                }
            }
        }

        private static String[] INDENTS ;

        public final static String Indent(int indent){
            if (null != INDENTS && indent < INDENTS.length && null != INDENTS[indent])
                return INDENTS[indent];
            else {
                String string;
                {
                    StringBuilder strbuf = new StringBuilder();

                    for (int cc = -1; cc < indent; cc++){
                        strbuf.append(' ');
                    }
                    string = strbuf.toString();
                }
                int len = (indent+1);
                if (null == INDENTS || len > INDENTS.length){
                    String[] copier = new String[len];
                    if (null != INDENTS){
                        System.arraycopy(INDENTS,0,copier,0,INDENTS.length);
                    }
                    copier[indent] = string;
                    INDENTS = copier;
                }
                else {
                    INDENTS[indent] = string;
                }
                return string;
            }
        }


        private static Buffer Previous;



        public final int pn, px;

        private Stack[] lines;

        private boolean external;

        private int indent = -1;


        public Buffer(){
            this(2);
        }
        public Buffer(int pop){
            super();
            if (-1 < pop){
                this.pn = pop;
                this.px = (pop-1);
            }
            else
                throw new IllegalArgumentException(String.valueOf(pop));
        }


        public int indent(){
            if (-1 == this.indent){

                if (null == Previous)

                    this.indent = 0;
                else 
                    this.indent = this.commonTo(Previous);

                Previous = this;
            }
            return this.indent;
        }
        public Stack[] compile(){
            Stack[] lines = this.lines;
            if (null == lines){
                boolean external = false;

                final StringTokenizer strtok = new StringTokenizer(super.toString(),"\r\n");
                final int count = strtok.countTokens();

                for (int cc = 0; cc < count; cc++){
                    String stack = strtok.nextToken();
                    /*
                     * pop (pn) lines from head stack trace
                     */
                    if (this.px < cc){
                        /*
                         * limit tail end of stack trace to first external
                         */
                        if (-1 < stack.indexOf("at java")){
                            lines = Stack.Add(lines, stack);
                            external = true;
                            break;
                        }
                        else
                            lines = Stack.Add(lines, stack);
                    }
                }
                this.lines = lines;
                this.external = external;
            }
            return lines;
        }
        public Stack last(){
            if (null == this.lines)
                this.compile();

            if (this.external)
                return this.lines[this.lines.length-2];
            else
                return this.lines[this.lines.length-1];
        }
        public int commonTo(Buffer that){
            if (null == that)
                return Integer.MIN_VALUE;
            else if (this == that)
                throw new IllegalStateException("Unintended");
            else {
                Stack[] thisStack = this.compile();
                Stack[] thatStack = that.compile();
                int thisTail = (thisStack.length-1);
                int thatTail = (thatStack.length-1);
                int ascent = 0;
                while (-1 < thisTail && -1 < thatTail){

                    if (0 == thisStack[thisTail].compareTo(thatStack[thatTail])){
                        ascent += 1;
                        thisTail -= 1;
                        thatTail -= 1;
                    }
                    else
                        break;
                }
                /*
                 * Two identical traces are necessarily disjoint:
                 * temporal coincindence
                 */
                if (thisTail == thatTail && ascent == thisStack.length)
                    return 0;
                else
                    return ascent;
            }
        }
        public String toString(){

            Stack[] list = this.compile();

            StringBuilder strbuf = new StringBuilder();

            final int indent = this.indent();

            boolean once = true;

            for (Stack stack: list){
                if (once)
                    once = false;
                else 
                    strbuf.append('\n');

                strbuf.append(stack.toString(indent));
            }
            return strbuf.toString();
        }
    }


    public final String message;

    private Buffer buffer;


    public Debug(String fmt, Object... args){
        this(String.format(fmt,args));
    }
    public Debug(String m){
        super(m);
        this.message = m;
    }


    public Buffer caller(){
        Buffer buffer = this.buffer;
        if (null == buffer){

            buffer = new Buffer(1);

            this.printStackTrace(new PrintStream(buffer));

            this.buffer = buffer;

            buffer.compile();
        }
        return buffer;
    }
    public void print(){
        Debug.trace.println(this.message);
        Debug.trace.println(this.caller());
    }
    public void printStackTrace(){
        Debug.trace.println(this.message);
        Debug.trace.println(this.caller());
    }
}
