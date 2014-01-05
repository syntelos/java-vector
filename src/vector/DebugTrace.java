/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2012, John Pritchard, Syntelos
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package vector;

import java.io.PrintStream;
import java.util.StringTokenizer;

/**
 * Call stack trace print tool.  Print an information string and the
 * source stack trace (including the print).
 * 
 * <pre>
 * new DebugTrace(string).print();
 * new DebugTrace(object).print();
 * new DebugTrace("format: %d",int).print();
 * </pre>
 * 
 * <p> Consecutive trace prints are indented for the call stack
 * relation to the previous debug trace print, as an aid to the
 * intensive application of this tool. </p>
 * 
 * <h3>Output</h3>
 * 
 * <p> This class presents the standard error stream for debug message
 * output.  This may evolve to a system property for stream targets.
 * However, the utility of this facility is not as high as for
 * logging.  </p>
 * 
 * <p> Development-time debugging is distinct from logging which
 * applies to production and development run-times.  Debug message
 * code is temporary, it is not committed into version control, and
 * has far less utility from the tooling available to logging. </p>
 * 
 */
public class DebugTrace 
    extends Throwable
{
    /**
     * Output target for debug messages is the process error output
     * stream.
     */
    public final static PrintStream out = System.err;


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



        private Stack[] lines;

        private boolean external;

        private int indent = -1;



        public Buffer(int indent){
            super();

            if (-1 == indent){

                if (null == Previous)

                    this.indent = 0;
                else 
                    this.indent = this.commonTo(Previous);//[NPE:2]
            }
            else
                this.indent = indent;

            Previous = this;
        }


        public int indent(){

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
                     * pop (1) line from head stack trace
                     */
                    if (0 < cc){
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
                if (null == thisStack || null == thatStack)
                    return 0;
                else {
                    int thisTail = (thisStack.length-1);//[NPE:3]
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
        }
        public String toString(){

            Stack[] list = this.compile();

            StringBuilder strbuf = new StringBuilder();

            boolean once = true;

            for (Stack stack: list){
                if (once)
                    once = false;
                else 
                    strbuf.append('\n');

                strbuf.append(stack.toString(this.indent));
            }
            return strbuf.toString();
        }
    }


    public final String message;

    public final int indent;

    private Buffer buffer;


    public DebugTrace(String fmt, Object... args){
        this(String.format(fmt,args));
    }
    public DebugTrace(int indent, String fmt, Object... args){
        this(indent, String.format(fmt,args));
    }
    public DebugTrace(Object o){
        this(o.toString());
    }
    public DebugTrace(int indent, Object o){
        this(indent,o.toString());
    }
    public DebugTrace(String m){
        this(-1,m);
    }
    public DebugTrace(int indent, String m){
        super(m);
        this.message = m;
        this.indent = indent;
    }
    public DebugTrace(){
        super();
        this.message = "";
        this.indent = -1;
    }


    public Buffer caller(){
        Buffer buffer = this.buffer;
        if (null == buffer){

            buffer = new Buffer(this.indent);//[NPE:1]

            this.printStackTrace(new PrintStream(buffer));

            this.buffer = buffer;

            buffer.compile();
        }
        return buffer;
    }
    public void print(){
        DebugTrace.out.println(this.message);
        DebugTrace.out.println(this.caller());//[NPE:0]
    }
    public void print(boolean stack){
        DebugTrace.out.println(this.message);
        if (stack)
            DebugTrace.out.println(this.caller());
    }
    public void printStackTrace(){
        DebugTrace.out.println(this.message);
        DebugTrace.out.println(this.caller());
    }
}
