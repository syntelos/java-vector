/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2013, John Pritchard, Syntelos
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
package vector.data;

import java.util.StringTokenizer;

/**
 * The {@link DataMessageType} defines the use of {@link DataKind} and
 * the parsing of appended strings.  The TMTC message types (see
 * {@link DataMessageType#isTMTC()}) invoke {@link DataKind} / {@link
 * DataField} message processing, otherwise no message string
 * processing occurs and no {@link DataMessageTerm terms} will be
 * present.
 * 
 * <h3>TMTC Syntax</h3>
 * 
 * <pre>
 * tmtc-message = (tmtc-message-line)*
 * tmtc-message-line = (tmtc-message-expression-set | tmtc-message-expression-info) (LF)
 * tmtc-message-expression-info = (#) (~LF)
 * tmtc-message-expression-set = ((tmtc-message-expression) (( ) (tmtc-message-expression))*)*
 * tmtc-message-expression = (data-field-name)
 * tmtc-message-expression = (data-field-name) (?)
 * tmtc-message-expression = (data-field-name) (=) (data-value-string)
 * tmtc-message-expression = (data-field-name) ( ) (data-value-string)
 * </pre>
 * 
 * 
 * @since Java/1.5
 */
public class DataMessage
    extends java.lang.Object
    implements java.lang.Appendable,
               java.lang.CharSequence,
               java.lang.Iterable<DataMessageTerm>
{
    /**
     * Convenience for non TMTC message type ERROR
     */
    public static class Error
        extends DataMessage
    {
        public Error(String string){
            super(DataMessageType.ERROR,string);
        }
    }
    /**
     * Convenience for non TMTC message type TEXT
     */
    public static class Text
        extends DataMessage
    {
        public Text(String string){
            super(DataMessageType.TEXT,string);
        }
    }
    /**
     * Convenience for non TMTC message type CODE
     */
    public static class Code
        extends DataMessage
    {
        public Code(String string){
            super(DataMessageType.CODE,string);
        }
    }
    /**
     * Convenience for non TMTC message type INFO
     */
    public static class Info
        extends DataMessage
    {
        public Info(String string){
            super(DataMessageType.INFO,string);
        }
    }


    public final long time;

    public final DataKind kind;

    public final DataMessageType type;

    protected DataMessageTerm[] content;

    protected StringBuilder string;


    /**
     * Any one kind
     */
    public DataMessage(DataKind kind, DataMessageType type){
        this(-1L,kind,type);
    }
    /**
     * Any one kind
     */
    public DataMessage(long time, DataKind kind, DataMessageType type){
        super();

        if (-1L < time)
            this.time = time;
        else
            this.time = DataClock.currentTimeMillis();

        if (null != kind && null != type){
            this.kind = kind;
            this.type = type;
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    /**
     * Kind {@link DataMessageType}
     */
    public DataMessage(DataMessageType type, String string){
        this(-1L,type,string);
    }
    /**
     * Kind {@link DataMessageType}
     */
    public DataMessage(long time, DataMessageType type, String string){
        super();

        if (-1L < time)
            this.time = time;
        else
            this.time = DataClock.currentTimeMillis();

        if (null != type){
            this.kind = DataMessageType.Kind.Instance;
            this.type = type;
            this.append(string);
        }
        else {
            throw new IllegalArgumentException();
        }
    }



    public boolean isTMTC(){ return this.type.isTMTC();}
    public boolean isTEXT(){ return this.type.isTEXT();}
    public boolean isCODE(){ return this.type.isCODE();}
    public boolean isERROR(){ return this.type.isERROR();}
    public boolean isINFO(){ return this.type.isINFO();}
    public boolean isLAN(){ return this.type.isLAN();}
    public boolean isWAN(){ return this.type.isWAN();}

    public boolean isNotTMTC(){ return this.type.isNotTMTC();}
    public boolean isNotTEXT(){ return this.type.isNotTEXT();}
    public boolean isNotCODE(){ return this.type.isNotCODE();}
    public boolean isNotERROR(){ return this.type.isNotERROR();}
    public boolean isNotINFO(){ return this.type.isNotINFO();}
    public boolean isNotLAN(){ return this.type.isNotLAN();}
    public boolean isNotWAN(){ return this.type.isNotWAN();}

    /**
     * Append character to string for non TMTC messages.
     * 
     * @exception java.lang.UnsupportedOperationException Using this
     * method for a TMTC message type
     */
    @Override
    public DataMessage append(char ch)
        throws java.lang.UnsupportedOperationException
    {
        if (this.isTMTC())
            throw new UnsupportedOperationException();
        else {
            if (null == this.string){
                this.string = new StringBuilder();
            }

            this.string.append(ch);

            return this;
        }
    }
    /**
     * Expression parser requires complete input expressions
     */
    @Override
    public DataMessage append(CharSequence string, int start, int end){
        if (null == string)
            return this;
        else 
            return this.append(string.subSequence(start,end));
    }
    /**
     * In the TMTC state, the TMTC expression parser will place
     * unrecognized TMTC expression terms into {@link
     * DataMessageTerm$Text TEXT terms}.
     * 
     * Otherwise, each call (string argument) is placed into a
     * DataMessageTerm$Text TEXT term}.
     * 
     * Whitespace, empty and null arguments have no effect (on the
     * state of this message).
     */
    @Override
    public DataMessage append(CharSequence string){

        if (null == string || 1 > string.length())

            return this;

        else if (this.isNotTMTC()){

            if (string instanceof String){
                string = ((String)string).trim();
            }

            if (0 < string.length()){

                this.add(new DataMessageTerm.Text(string));
            }
            return this;
        }
        else {

            final StringTokenizer strtok = new StringTokenizer(string.toString(),"# =?\t\r\n",true);

            char sep = 0;
            Object name = null, value = null;
            boolean info = false;

            parse:
            while (strtok.hasMoreTokens()){
                String tok = strtok.nextToken();

                switch(tok.charAt(0)){
                case ' ':
                    sep = ' ';

                    if (info){
                        this.string.append(sep);
                    }
                    break;
                case '#':
                    info = (0 == sep || '\r' == sep || '\n' == sep);
                    sep = '#';

                    if (info){
                        if (null == this.string){
                            this.string = new StringBuilder();
                        }
                        else if (0 != this.string.length()){
                            this.string.append('\n');
                        }
                        this.string.append(sep);
                    }
                    else
                        throw new IllegalArgumentException(String.format("Error misplaced '#' in (%s)",string));
                    break;
                case '=':
                    sep = '=';

                    if (info){
                        this.string.append(sep);
                    }
                    break;
                case '?':
                    sep = '?';

                    if (info){
                        this.string.append(sep);
                    }
                    break;
                case '\t':
                    sep = '\t';

                    if (info){
                        this.string.append(sep);
                    }
                    break;
                case '\r':
                    info = false;
                    sep = '\n';
                    break;
                case '\n':
                    info = false;
                    sep = '\n';
                    break;
                default:
                    if (info){

                        this.string.append(tok);
                    }
                    else if (null == name){
                        try {
                            name = kind.identifier(tok);
                        }
                        catch (RuntimeException exc){
                            /*
                             * accept operator syntax
                             */
                            this.add(new DataMessageTerm.Text(tok));
                            name = null;
                            value = null;
                        }
                    }
                    else if (null == value && (' ' == sep || '=' == sep)){
                        /*
                         * lossless state management
                         */
                        value = tok;
                        this.add(new DataMessageTerm(this.kind,name,value));
                        name = null;
                        value = null;
                    }
                    else {
                        this.add(new DataMessageTerm(this.kind,name,null));
                        /*
                         * lossless state management
                         */
                        name = tok;
                        value = null;
                        try {
                            name = kind.identifier(tok);
                        }
                        catch (RuntimeException exc){
                            /*
                             * accept operator syntax
                             */
                            this.add(new DataMessageTerm.Text(tok));
                            name = null;
                            value = null;
                        }
                    }
                    break;
                }
            }
            return this;
        }
    }
    public int size(){
        final DataMessageTerm[] content = this.content;
        if (null == content)
            return 0;
        else
            return content.length;
    }
    public DataMessageTerm add(DataMessageTerm term){
        if (null == term)
            return null;
        else {
            this.content = DataMessageTerm.Add(this.content,term);

            if (null == this.string){
                this.string = new StringBuilder();
            }
            else if (0 != this.string.length()){
                this.string.append('\n');
            }

            this.string.append(term.string);

            return term;
        }
    }
    public DataMessageTerm add(DataIdentifier id, Object value){

        return this.add(new DataMessageTerm(this.time,this.kind,id,value));
    }
    public DataMessageTerm add(DataIdentifier id){

        return this.add(new DataMessageTerm(this.time,this.kind,id));
    }
    public DataMessageTerm add(DataField field, DataSubfield subfield, Object value){

        return this.add(new DataMessageTerm(this.time,this.kind,new DataIdentifier(field,subfield),value));
    }
    public DataMessageTerm add(DataField field, Object value){

        return this.add(new DataMessageTerm(this.time,this.kind,new DataIdentifier(field),value));
    }
    public DataMessageTerm add(DataField field){

        return this.add(new DataMessageTerm(this.time,this.kind,new DataIdentifier(field),null));
    }
    /**
     * Employs identifier matching so that mapped named are captured.
     * 
     * @param name Data identifier for matching to term names in
     * <code>argument.matches(term.name)</code>
     * 
     * @return Negative one for not found, otherwise index of term
     * with name matching argument name
     */
    public final int indexOf(DataIdentifier name){

        return DataMessageTerm.IndexOf(this.content,name);
    }
    /**
     * Object identity by reference
     */
    public final int indexOf(DataMessageTerm term){

        return DataMessageTerm.IndexOf(this.content,term);
    }
    /**
     * Employs identifier matching so that mapped named are captured.
     * 
     * @param name Data identifier for matching to term names in
     * <code>argument.matches(term.name)</code>
     * 
     * @return Null for not found, otherwise term with name matching
     * argument name
     */
    public final DataMessageTerm get(int idx){

        final DataMessageTerm[] content = this.content;

        if (-1 < idx && null != content && idx < content.length)
            return content[idx];
        else
            return null;
    }
    public final DataMessageTerm get(DataIdentifier name){

        final DataMessageTerm[] content = this.content;

        final int idx = DataMessageTerm.IndexOf(content,name);

        if (-1 < idx && null != content && idx < content.length)
            return content[idx];
        else
            return null;
    }
    /**
     * Employs identifier matching so that mapped named are captured.
     * 
     * @param name Data identifier for matching to term names in
     * <code>argument.matches(term.name)</code>
     * 
     * @return Iterator may be empty but never null
     */
    public final DataMessageTerm.Iterator list(DataIdentifier name){

        DataMessageTerm[] list = null;

        for (DataMessageTerm term: this){

            if (name.matches(term.name)){

                list = DataMessageTerm.Add(list,term);
            }
        }
        return new DataMessageTerm.Iterator(list);
    }
    public final boolean contains(DataIdentifier name){
        return (-1 != this.indexOf(name));
    }
    public final boolean containsNot(DataIdentifier name){
        return (-1 == this.indexOf(name));
    }
    public final DataMessageTerm.Iterator select(int start, int end){
        if (-1 < start && 0 < (end-start)){
            final DataMessageTerm[] content = this.content;
            if (null != content){
                return new DataMessageTerm.Iterator(content,start,end);
            }
        }
        return new DataMessageTerm.Iterator();
    }
    public final DataMessageTerm.Iterator head(int count){
        if (0 < count){
            final DataMessageTerm[] content = this.content;
            if (null != content){
                return new DataMessageTerm.Iterator(content,0,count);
            }
        }
        return new DataMessageTerm.Iterator();
    }
    public final DataMessageTerm.Iterator tail(int count){
        if (0 < count){
            final DataMessageTerm[] content = this.content;
            if (null != content){
                final int len = content.length;
                if (count >= len)
                    return new DataMessageTerm.Iterator(content,0,count);
                else {
                    return new DataMessageTerm.Iterator(content,(len-count),count);
                }
            }
        }
        return new DataMessageTerm.Iterator();
    }
    public final DataMessageTerm.Iterator iterator(){

        return new DataMessageTerm.Iterator(this.content);
    }
    public final int length(){
        StringBuilder string = this.string;
        if (null == string)
            return 0;
        else
            return string.length();
    }
    public final char charAt(int idx){
        StringBuilder string = this.string;
        if (null == string)
            throw new IndexOutOfBoundsException(String.valueOf(idx));
        else
            return string.charAt(idx);
    }
    public final CharSequence subSequence(int start, int end){
        StringBuilder string = this.string;
        if (null == string)
            throw new IndexOutOfBoundsException(String.valueOf(start));
        else
            return string.subSequence(start,end);
    }
    public final String toString(){
        StringBuilder string = this.string;
        if (null == string)
            return "";
        else
            return string.toString();
    }


    /**
     * 
     */
    public static class Iterator
        extends Object
        implements java.lang.Iterable<DataMessage>,
                   java.util.Iterator<DataMessage>
    {
        private final int length;
        private final DataMessage[] list;
        private int index;

        public Iterator(DataMessage[] list){
            super();
            if (null == list){
                this.list = null;
                this.length = 0;
            }
            else {
                this.list = list;
                this.length = list.length;
            }
        }


        public Iterator reset(){
            this.index = 0;
            return this;
        }
        public boolean hasNext(){
            return (this.index < this.length);
        }
        public DataMessage next(){
            if (this.index < this.length)
                return this.list[this.index++];
            else
                throw new java.util.NoSuchElementException(String.valueOf(this.index));
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
        public java.util.Iterator<DataMessage> iterator(){
            return this.reset();
        }
    }


    public final static int IndexOf(DataMessage[] list, DataMessage field){
        if (null == field || null == list)
            return -1;
        else {
            final int count = list.length;
            for (int cc = 0; cc < count; cc++){
                if (field == list[cc])
                    return cc;
            }
            return -1;
        }
    }
    public final static DataMessage[] Copy(DataMessage[] list){
        if (null == list)
            return null;
        else {
            final int len = list.length;
            DataMessage[] copier = new DataMessage[len];
            System.arraycopy(list,0,copier,0,len);
            return copier;
        }
    }
    public final static DataMessage[] Add(DataMessage[] list, DataMessage item){
        if (null == item)
            return list;
        else if (null == list)
            return new DataMessage[]{item};
        else {
            final int len = list.length;
            DataMessage[] copier = new DataMessage[len+1];
            System.arraycopy(list,0,copier,0,len);
            copier[len] = item;
            return copier;
        }
    }
    public final static DataMessage[] Add(DataMessage[] list, DataMessage[] sublist){
        if (null == sublist)
            return list;
        else if (null == list)
            return sublist;
        else {
            final int listl = list.length;
            final int sublistl = sublist.length;
            DataMessage[] copier = new DataMessage[listl+sublistl];
            System.arraycopy(list,0,copier,0,listl);
            System.arraycopy(sublist,0,copier,listl,sublistl);
            return copier;
        }
    }
    public final static DataMessage[] List(java.lang.Iterable<DataMessage> it){
        return List(it.iterator());
    }
    public final static DataMessage[] List(java.util.Iterator<DataMessage> it){
        DataMessage[] list = null;
        while (it.hasNext()){
            list = Add(list,it.next());
        }
        return list;
    }
}
