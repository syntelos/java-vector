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
package vector.text;

import static java.lang.Character.*;

/**
 * Visually ordered character sequence.
 */
public class Visual
    extends Object
{
    /**
     * @see Visual$Directionality
     */
    public enum Direction {
        Neutral, Left, Right;

        public final int span(String string){
            if (null == string)
                return 0;
            else {
                return this.span(string.toCharArray(),0);
            }
        }
        /**
         * @param string Array of characters
         * @return Number of characters in span from offset zero
         */
        public final int span(char[] string){
            return this.span(string,0);
        }
        /**
         * @param string Array of characters
         * @param ofs Offset in string
         * @return Number of characters in span from offset
         */
        public final int span(char[] string, int ofs){
            if (null == string)
                return 0;
            else {
                final int count = string.length;
                if (Neutral == this)
                    return (count-ofs);
                else if (0 < count){
                    Direction current;

                    for (int cc = ofs; cc < count; cc++){

                        current = Direction.For(string[cc]);
                        if (current != this && Neutral != current){
                            return (cc-ofs);
                        }
                    }
                    return (count-ofs);
                }
                else
                    return 0;
            }
        }


        public final static Direction For(char ch){

            return Directionality.For(ch).direction;
        }
        public final static Direction For(char[] string, int ofs){
            if (null == string)
                return null;
            else {
                final int count = string.length;

                for (int cc = ofs; cc < count; cc++){
                    Direction dir = Direction.For(string[ofs]);
                    if (Direction.Neutral != dir)
                        return dir;
                }
                return Direction.Neutral;
            }
        }
    }
    /**
     * @see java.lang.Character#getDirectionality(char)
     */
    public enum Directionality {
        Undefined(Direction.Neutral),
        LeftToRight(Direction.Left),
        RightToLeft(Direction.Right),
        RightToLeftArabic(Direction.Right),
        EuropeanNumber(Direction.Left),
        EuropeanNumberSeparator(Direction.Left),
        EuropeanNumberTerminator(Direction.Left),
        ArabicNumber(Direction.Left),
        CommonNumberSeparator(Direction.Neutral), // (comma)
        NonspacingMark(Direction.Neutral),
        BoundaryNeutral(Direction.Neutral),
        ParagraphSeparator(Direction.Neutral),
        SegmentSeparator(Direction.Neutral),
        Whitespace(Direction.Neutral),
        OtherNeutrals(Direction.Neutral),
        LeftToRightEmbedding(Direction.Left),
        LeftToRightOverride(Direction.Left),
        RightToLeftEmbedding(Direction.Right),
        RightToLeftOverride(Direction.Right),
        PopDirectionalFormat(Direction.Neutral);


        public final Direction direction;


        private Directionality(Direction direction){
            this.direction = direction;
        }


        public final static Directionality For(char ch){
            switch(Character.getDirectionality(ch)){
            case DIRECTIONALITY_UNDEFINED:
                return Directionality.Undefined;
            case DIRECTIONALITY_LEFT_TO_RIGHT:
                return Directionality.LeftToRight;
            case DIRECTIONALITY_RIGHT_TO_LEFT:
                return Directionality.RightToLeft;
            case DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC:
                return Directionality.RightToLeftArabic;
            case DIRECTIONALITY_EUROPEAN_NUMBER:
                return Directionality.EuropeanNumber;
            case DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR:
                return Directionality.EuropeanNumberSeparator;
            case DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR:
                return Directionality.EuropeanNumberTerminator;
            case DIRECTIONALITY_ARABIC_NUMBER:
                return Directionality.ArabicNumber;
            case DIRECTIONALITY_COMMON_NUMBER_SEPARATOR:
                return Directionality.CommonNumberSeparator;
            case DIRECTIONALITY_NONSPACING_MARK:
                return Directionality.NonspacingMark;
            case DIRECTIONALITY_BOUNDARY_NEUTRAL:
                return Directionality.BoundaryNeutral;
            case DIRECTIONALITY_PARAGRAPH_SEPARATOR:
                return Directionality.ParagraphSeparator;
            case DIRECTIONALITY_SEGMENT_SEPARATOR:
                return Directionality.SegmentSeparator;
            case DIRECTIONALITY_WHITESPACE:
                return Directionality.Whitespace;
            case DIRECTIONALITY_OTHER_NEUTRALS:
                return Directionality.OtherNeutrals;
            case DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING:
                return Directionality.LeftToRightEmbedding;
            case DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE:
                return Directionality.LeftToRightOverride;
            case DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING:
                return Directionality.RightToLeftEmbedding;
            case DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE:
                return Directionality.RightToLeftOverride;
            case DIRECTIONALITY_POP_DIRECTIONAL_FORMAT:
                return Directionality.PopDirectionalFormat;
            default:
                throw new IllegalStateException("0x"+Integer.toHexString(ch));
            }
        }
    }
    /**
     * @see java.lang.Character#getType(char)
     */
    public enum Type {
        EmptyString,
        CombiningSpacingMark,
        ConnectorPunctuation,
        Control,
        CurrencySymbol,
        DashPunctuation,
        DecimalDigitNumber,
        EnclosingMark,
        EndPunctuation,
        FinalQuotePunctuation,
        Format,
        InitialQuotePunctuation,
        LetterNumber,
        LineSeparator,
        LowercaseLetter,
        MathSymbol,
        ModifierLetter,
        ModifierSymbol,
        NonSpacingMark,
        OtherLetter,
        OtherNumber,
        OtherPunctuation,
        OtherSymbol,
        ParagraphSeparator,
        PrivateUse,
        SpaceSeparator,
        StartPunctuation,
        Surrogate,
        TitlecaseLetter,
        Unassigned,
        UppercaseLetter;


        public final int span(String string){
            if (null == string)
                return 0;
            else {
                return this.span(string.toCharArray());
            }
        }
        public final int span(char[] string){
            if (null == string)
                return 0;
            else {
                final int count = string.length;
                if (0 < count){
                    Type current;

                    for (int cc = 0; cc < count; cc++){

                        current = Type.For(string[cc]);
                        if (current != this){
                            return cc;
                        }
                    }
                    return count;
                }
                else
                    return 0;
            }
        }


        public final static Type For(char ch){
            switch(Character.getType(ch)){
            case COMBINING_SPACING_MARK:
                return Type.CombiningSpacingMark;
            case CONNECTOR_PUNCTUATION:
                return Type.ConnectorPunctuation;
            case CONTROL:
                return Type.Control;
            case CURRENCY_SYMBOL:
                return Type.CurrencySymbol;
            case DASH_PUNCTUATION:
                return Type.DashPunctuation;
            case DECIMAL_DIGIT_NUMBER:
                return Type.DecimalDigitNumber;
            case ENCLOSING_MARK:
                return Type.EnclosingMark;
            case END_PUNCTUATION:
                return Type.EndPunctuation;
            case FINAL_QUOTE_PUNCTUATION:
                return Type.FinalQuotePunctuation;
            case FORMAT:
                return Type.Format;
            case INITIAL_QUOTE_PUNCTUATION:
                return Type.InitialQuotePunctuation;
            case LETTER_NUMBER:
                return Type.LetterNumber;
            case LINE_SEPARATOR:
                return Type.LineSeparator;
            case LOWERCASE_LETTER:
                return Type.LowercaseLetter;
            case MATH_SYMBOL:
                return Type.MathSymbol;
            case MODIFIER_LETTER:
                return Type.ModifierLetter;
            case MODIFIER_SYMBOL:
                return Type.ModifierSymbol;
            case NON_SPACING_MARK:
                return Type.NonSpacingMark;
            case OTHER_LETTER:
                return Type.OtherLetter;
            case OTHER_NUMBER:
                return Type.OtherNumber;
            case OTHER_PUNCTUATION:
                return Type.OtherPunctuation;
            case OTHER_SYMBOL:
                return Type.OtherSymbol;
            case PARAGRAPH_SEPARATOR:
                return Type.ParagraphSeparator;
            case PRIVATE_USE:
                return Type.PrivateUse;
            case SPACE_SEPARATOR:
                return Type.SpaceSeparator;
            case START_PUNCTUATION:
                return Type.StartPunctuation;
            case SURROGATE:
                return Type.Surrogate;
            case TITLECASE_LETTER:
                return Type.TitlecaseLetter;
            case UNASSIGNED:
                return Type.Unassigned;
            case UPPERCASE_LETTER:
                return Type.UppercaseLetter;
            default:
                throw new IllegalStateException("0x"+Integer.toHexString(ch));
            }
        }
    }

    protected char[] logical, visual;


    public Visual(){
        super();
    }
    public Visual(CharSequence text){
        super();
        this.set(text);
    }


    /**
     * @return Text modified
     */
    public boolean set(CharSequence string){
        if (this.logicalEquals(string))
            return false;
        else if (null == string || 1 > string.length()){
            boolean re = (null == this.logical);
            this.logical = null;
            this.visual = null;
            return re;
        }
        else {
            return this.set(ToCharArray(string));
        }
    }
    /**
     * @param string New string, not empty
     * @return True
     */
    protected boolean set(char[] string){
        if (null == string){
            this.logical = null;
            this.visual = null;
            return true;
        }
        else {
            this.logical = string;

            Direction direction = Direction.For(this.logical,0);

            if (null != direction){

                int span = direction.span(this.logical);

                if (span == this.logical.length){

                    switch(direction){
                    case Neutral:
                    case Left:
                        this.visual = this.logical;
                        break;
                    case Right:
                        this.visual = Reverse(this.logical);
                        break;
                    default:
                        throw new IllegalStateException(direction.name());
                    }
                }
                else {
                    final int count = this.logical.length;

                    StringBuilder visual = new StringBuilder();

                    switch(direction){
                    case Neutral:
                    case Left:

                        for (int cc = 0; cc < count; ){

                            switch(direction){
                            case Neutral:
                            case Left:
                                visual.append(this.logical,cc,span);
                                break;
                            case Right:
                                visual.append(Reverse(this.logical,cc,span));
                                break;
                            default:
                                throw new IllegalStateException(direction.name());
                            }
                            cc += span;
                            direction = Direction.For(this.logical,cc);
                            span = direction.span(this.logical,cc);
                        }
                        this.visual = ToCharArray(visual.toString());
                        break;

                    case Right:

                        for (int cc = 0; cc < count; ){

                            switch(direction){
                            case Left:
                                visual.insert(0,this.logical,cc,span);
                                break;
                            case Neutral:
                            case Right:
                                visual.insert(0,Reverse(this.logical,cc,span));
                                break;
                            default:
                                throw new IllegalStateException(direction.name());
                            }
                            cc += span;
                            direction = Direction.For(this.logical,cc);
                            span = direction.span(this.logical,cc);
                        }
                        this.visual = ToCharArray(visual.toString());
                        break;

                    default:
                        throw new IllegalStateException(direction.name());
                    }
                }
            }
            else {
                this.visual = null;
            }
            return true;
        }
    }
    public final boolean isEmpty(){
        return (null == this.logical);
    }
    public final boolean isNotEmpty(){
        return (null != this.logical);
    }
    public Visual clear(){
        this.logical = null;
        this.visual = null;

        return this;
    }
    public final int logicalLength(){
        if (null == this.logical)
            return 0;
        else
            return this.logical.length;
    }
    public final int visualLength(){
        if (null == this.visual)
            return 0;
        else
            return this.visual.length;
    }
    public final Type getType(){
        if (0 < this.visualLength())
            return Type.For(this.visual[0]);
        else
            return Type.EmptyString;
    }
    public final Type getType(int idx){
        if (-1 < idx && idx < this.visualLength())
            return Type.For(this.visual[idx]);
        else
            throw new ArrayIndexOutOfBoundsException(String.valueOf(idx));
    }
    public final Direction getDirection(){
        if (0 < this.visualLength())
            return Direction.For(this.visual[0]);
        else
            return Direction.Neutral;
    }
    public final Direction getDirection(int idx){
        if (-1 < idx && idx < this.visualLength())
            return Direction.For(this.visual[idx]);
        else
            throw new ArrayIndexOutOfBoundsException(String.valueOf(idx));
    }
    public final char logicalCharAt(int idx){
        if (-1 < idx && idx < this.logicalLength())
            return this.logical[idx];
        else
            throw new ArrayIndexOutOfBoundsException(String.valueOf(idx));
    }
    public final String logicalSubSequence(int start, int end){
        if (-1 < start && start <= end && end < this.logicalLength()){
            int count = (end-start);
            if (0 < count){
                char[] string = new char[count];
                System.arraycopy(this.logical,start,string,0,count);
                return new String(string);
            }
            else
                return "";
        }
        else
            throw new ArrayIndexOutOfBoundsException(String.format("(%d,%d)",start,end));
    }
    public final char visualCharAt(int idx){
        if (-1 < idx && idx < this.visualLength())
            return this.visual[idx];
        else
            throw new ArrayIndexOutOfBoundsException(String.valueOf(idx));
    }
    public final String visualSubSequence(int start, int end){
        if (-1 < start && start <= end && end < this.visualLength()){
            int count = (end-start);
            if (0 < count){
                char[] string = new char[count];
                System.arraycopy(this.visual,start,string,0,count);
                return new String(string);
            }
            else
                return "";
        }
        else
            throw new ArrayIndexOutOfBoundsException(String.format("(%d,%d)",start,end));
    }
    public final String visualString(){
        if (null == this.visual)
            return "";
        else
            return new String(this.visual);
    }
    public final String logicalString(){
        if (null == this.logical)
            return "";
        else
            return new String(this.logical);
    }
    /**
     * @return Visually ordered string (for Font create glyph vector)
     */
    public final String toString(){

        return this.visualString();
    }
    public final char[] logicalCharArray(){
        if (null == this.logical)
            return new char[0];
        else
            return this.logical.clone();
    }
    public final char[] visualCharArray(){
        if (null == this.visual)
            return new char[0];
        else
            return this.visual.clone();
    }
    public final boolean logicalEquals(Object that){
        if (that instanceof CharSequence)
            return this.logicalEquals( (CharSequence)that);
        else
            return false;
    }
    public final boolean logicalEquals(CharSequence that){
        if (null == that)
            return false;
        else {
            final int count = this.logicalLength();
            if (count == that.length()){
                for (int cc = 0; cc < count; cc++){
                    if (this.logicalCharAt(cc) != that.charAt(cc))
                        return false;
                }
                return true;
            }
            else
                return false;
        }
    }
    public final boolean visualEquals(Object that){
        if (that instanceof CharSequence)
            return this.visualEquals( (CharSequence)that);
        else
            return false;
    }
    public final boolean visualEquals(CharSequence that){
        if (null == that)
            return false;
        else {
            final int count = this.visualLength();
            if (count == that.length()){
                for (int cc = 0; cc < count; cc++){
                    if (this.visualCharAt(cc) != that.charAt(cc))
                        return false;
                }
                return true;
            }
            else
                return false;
        }
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
    public final static char[] Reverse(char[] string){
        char[] re = string.clone();
        final int count = string.length;
        final int term = (count-1);
        final int half = (count>>1);
        for (int a = 0, b; a < half; a++){
            b = (term-a);

            re[a] = string[b];
            re[b] = string[a];
        }
        return re;
    }
    public final static char[] Reverse(char[] string, int ofs, int count){
        char[] re = new char[count];
        final int term = (count-1);
        final int half = (count>>1)+1;
        for (int a = 0, b; a < half; a++){
            b = (term-a);
            if (b == a)
                re[a] = string[a+ofs];
            else if (b < count){
                re[a] = string[b+ofs];
                re[b] = string[a+ofs];
            }
        }
        return re;
    }
}
