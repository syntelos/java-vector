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
     * LAN message text
     */
    LAN,
    /**
     * WAN message text
     */
    WAN;

    public boolean isOperator(){
        return false;
    }

    public boolean isTMTC(){
        return (DataMessageType.TMTC == this || DataMessageType.LAN == this);
    }

    public boolean isTEXT(){ return DataMessageType.TEXT == this;}
    public boolean isCODE(){ return DataMessageType.CODE == this;}
    public boolean isERROR(){ return DataMessageType.ERROR == this;}
    public boolean isINFO(){ return DataMessageType.INFO == this;}
    public boolean isLAN(){ return DataMessageType.LAN == this;}
    public boolean isWAN(){ return DataMessageType.WAN == this;}

    public boolean isNotTMTC(){
        return (DataMessageType.TMTC != this && DataMessageType.LAN != this);
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
}
