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

import java.lang.reflect.Constructor;

/**
 * This interface is implemented by data and syntactic enums that have
 * operational members.
 * 
 * Alternative representations of an enum member value may exist in
 * data and syntax enum classes.
 * 
 * A data enum (member) value that is an operator may have a syntactic
 * enum class that defines the evaluation invocation format.
 * 
 * Both the syntactic and data enums implement this interface, and
 * present each other as alternative representations.
 */
public interface DataOperator<D extends Enum<D>>
    extends DataField<D>
{
    /**
     * @param Optional identifier subfield may be null
     * @return Whether this enum value pair is an operator
     * @see DataField#isOperator()
     */
    public boolean isOperator(DataSubfield s);
    /**
     * @return Is an enum class representing a command and its
     * arguments
     */
    public boolean isSyntactic();
    /**
     * @return True for an enum that has data and syntactic
     * representations
     */
    public boolean hasAlternative();
    /**
     * @return Enum value for alternative representation of this
     * operator: data or syntactic
     * @exception java.lang.UnsupportedOperationException
     */
    public <DO extends DataOperator> DO getAlternative()
        throws java.lang.UnsupportedOperationException;
    /**
     * @exception java.lang.UnsupportedOperationException
     */
    public <DO extends DataOperator> Class<DO> getAlternativeClass()
        throws java.lang.UnsupportedOperationException;

    /**
     * @return Expression lists require this term
     */
    public boolean isRequired();
    /**
     * @return Expression lists haven an argument for this term
     */
    public boolean hasArgument();
    /**
     * @return Argument may be a string
     */
    public boolean isPossibleString();
    /**
     * @return Argument may be a Class
     */
    public boolean isPossibleClass();
    /**
     * Normalize representation.
     * 
     * @param value String or expected argument value type
     * 
     * @return Expected argument value type
     */
    public Object toObject(Object value);
    /**
     * Call services
     * 
     * @param argv List of (name-value pair)*
     * 
     * @return One message per evaluator
     */
    public DataMessage[] evaluate(Object... argv);

    public Class[] getPossibleValues();

    public Constructor[] getPossibleCtors();

}
