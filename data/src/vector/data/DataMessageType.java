/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2013, The DigiVac Company
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

import java.lang.reflect.Constructor;

/**
 * {@link DataMessage} types preserve internal knowledge about a
 * message source and syntax.
 */
public enum DataMessageType
    implements DataField<DataMessageType>
{
    /**
     * Syntactic enum, object array enum <code>(name, value)+</code>
     * pairs
     */
    TMTC,
    /**
     * Plain text, preformatted, string array lines
     */
    TEXT,
    /**
     * Program code text, preformatted, string array lines, tabs
     * expand to four space indentation
     */
    CODE,
    /**
     * Error message text
     */
    ERROR,
    /**
     * Info message text
     */
    INFO,
    /**
     * LAN message text: the only thing known about the message is the
     * source class
     */
    LAN,
    /**
     * WAN message text: the only thing known about the message is the
     * source class
     */
    WAN;

    public boolean isOperator(){
        return false;
    }
    /**
     * Messages employ TMTC syntax for a data language
     * @see #TMTC
     * @see #LAN
     * @see #WAN
     */
    public boolean isTMTC(){
        switch(this){
        case TMTC:
        case LAN:
        case WAN:
            return true;
        default:
            return false;
        }
    }

    public boolean isTEXT(){ return DataMessageType.TEXT == this;}
    public boolean isCODE(){ return DataMessageType.CODE == this;}
    public boolean isERROR(){ return DataMessageType.ERROR == this;}
    public boolean isINFO(){ return DataMessageType.INFO == this;}
    public boolean isLAN(){ return DataMessageType.LAN == this;}
    public boolean isWAN(){ return DataMessageType.WAN == this;}

    public boolean isNotTMTC(){
        switch(this){
        case TMTC:
        case LAN:
        case WAN:
            return false;
        default:
            return true;
        }
    }

    public boolean isNotTEXT(){ return DataMessageType.TEXT != this;}
    public boolean isNotCODE(){ return DataMessageType.CODE != this;}
    public boolean isNotERROR(){ return DataMessageType.ERROR != this;}
    public boolean isNotINFO(){ return DataMessageType.INFO != this;}
    public boolean isNotLAN(){ return DataMessageType.LAN != this;}
    public boolean isNotWAN(){ return DataMessageType.WAN != this;}

    public Object toObject(String uin){

        try {
            return Class.forName(uin);
        }
        catch (Exception next){
        }
        return uin;
    }
    public Object toObject(Object uin){
        if (uin instanceof Class)
            return (Class)uin;
        else if (uin instanceof String)
            return this.toObject( (String)uin);
        else
            return uin;
    }
    public String toString(Object data){

        return data.toString();
    }
    public boolean hasSubfieldClass(){
        return false;
    }
    public Class<DataSubfield> getSubfieldClass()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
    public boolean hasSubfieldDefault(){
        return false;
    }
    public DataSubfield getSubfieldDefault(){
        throw new UnsupportedOperationException();
    }
    public boolean hasMapping(){
        return false;
    }
    public Iterable<DataField<DataMessageType>> getMapping(){
        throw new UnsupportedOperationException();
    }
    public boolean hasAlternative(){
        return false;
    }
    public <DO extends vector.data.DataOperator> DO getAlternative()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
    public <DO extends vector.data.DataOperator> Class<DO> getAlternativeClass()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
    public boolean isPossibleString(){
        return true;
    }
    public boolean isPossibleClass(){
        return true;
    }
    public Class[] getPossibleValues(){
        return null;
    }
    public Constructor[] getPossibleCtors(){
        return null;
    }

    /**
     * Kind {@link DataMessageType} applies to {@link DataMessage} for
     * non TMTC messages.
     */
    public static class Kind
        extends DataKind<DataMessageType,DataSubfield>
    {
        public final static Kind Instance = new Kind();

        public Kind(){
            super(DataMessageType.class);
        }
    }


    /**
     * Interaction API accepts messages in data term format with text
     * elements, converting data enum to syntactic enum sequence.
     * 
     * The data term format is produced by top level message
     * processing for telemetry data.  An operator (alternative) enum
     * is expanded from the input data using the {@link DataOperator}
     * interface.  This interface is implemented by both the data and
     * operator syntax (data alternative) enums.
     * 
     * Convert data binding to syntactic binding while enumerating
     * operator evaluation interface values.
     */
    public final static Object[] ExpressionDataToSyntax(DataMessageTerm term, DataMessage message){
        if (null != term && null != message){
            final int termx = message.indexOf(term);
            if (-1 < termx){

                DataOperator fop = (DataOperator)term.name.field;

                if (fop.isSyntactic() || (!fop.hasAlternative())){
                    /*
                     * Simple extraction
                     */
                    final DataMessageTerm.Iterator expr_data = message.select(termx,message.size());

                    Object[] expr_syntactic = null;

                    DataField prev = null;

                    for (DataMessageTerm p: expr_data){
                        /*
                         * Use type TEXT as transparent
                         * (pass through) identifier
                         */
                        if (!p.name.isField(DataMessageType.TEXT)){
                            prev = p.name.field;

                            expr_syntactic = ObjectAdd(expr_syntactic,p.name);

                            expr_syntactic = ObjectAdd(expr_syntactic,prev.toObject(p.value));
                        }
                        else if (null == prev)
                            expr_syntactic = ObjectAdd(expr_syntactic,fop.toObject(p.value));
                        else
                            expr_syntactic = ObjectAdd(expr_syntactic,prev.toObject(p.value));

                    }
                    return expr_syntactic;
                }
                else {
                    /*
                     * Enum rebinding of names and values
                     */
                    DataOperator sop = fop.getAlternative();

                    if (sop.isSyntactic()){

                        final DataKind opkind = new DataKind(sop.getClass());
                        /*
                         * Convert input to operator syntax
                         */
                        final DataMessageTerm.Iterator expr_data = message.select(termx,message.size());

                        Object[] expr_syntactic = null;
                        /*
                         * Perform preprocessing normalization check for syntactic order
                         */
                        DataField prev = null;

                        for (DataMessageTerm p: expr_data){
                            /*
                             * Syntax order
                             */
                            if (DataMessageType.TEXT == p.name.field){

                                try {
                                    final DataIdentifier check = opkind.identifier(p.value);

                                    final DataField checkField = check.field; 

                                    if (null == prev || checkField.ordinal() > prev.ordinal()){

                                        expr_syntactic = ObjectAdd(expr_syntactic,checkField);

                                        prev = checkField;
                                    }
                                    else {
                                        throw new IllegalArgumentException(String.format("Operator (%s:%d) is out of order, following (%s:%d)",checkField.name(),checkField.ordinal(),prev.name(),prev.ordinal()));
                                    }
                                }
                                catch (RuntimeException exc){

                                    if (null == prev)
                                        expr_syntactic = ObjectAdd(expr_syntactic,sop.toObject(p.value));
                                    else
                                        expr_syntactic = ObjectAdd(expr_syntactic,prev.toObject(p.value));
                                }
                            }
                            else {
                                final DataIdentifier check = opkind.identifier(p.name);

                                final DataField checkField = check.field; 

                                if (null == prev || checkField.ordinal() > prev.ordinal()){

                                    expr_syntactic = ObjectAdd(expr_syntactic,checkField);
                                    expr_syntactic = ObjectAdd(expr_syntactic,checkField.toObject(p.value));

                                    prev = checkField;
                                }
                                else {
                                    throw new IllegalArgumentException(String.format("Operator (%s:%d) is out of order, following (%s:%d)",checkField.name(),checkField.ordinal(),prev.name(),prev.ordinal()));
                                }
                            }
                        }
                        return expr_syntactic;
                    }
                    else {
                        throw new IllegalArgumentException("Operator alternative is not syntactic");
                    }
                }
            }
            else {
                throw new IllegalArgumentException("Argument term not found in argument message");
            }
        }
        else {
            throw new IllegalArgumentException("Missing argument");
        }
    }
    /**
     * Terse object list concatenator excludes null values
     */
    public final static Object[] ObjectAdd(Object[] list, Object item){
        if (null == item)
            return list;
        else if (null == list)
            return new Object[]{item};
        else {
            final int len = list.length;
            Object[] copier = new Object[len+1];
            System.arraycopy(list,0,copier,0,len);
            copier[len] = item;
            return copier;
        }
    }
}
