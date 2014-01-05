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

/**
 * General 2D alignment property value.
 * 
 * The class design of the user will define a layout strategy which
 * interprets the meaning of "bounding box" in the following
 * specification of the values of this class.  
 * 
 * The "bounding box" to align to should be the bounding box of the
 * immediate parent, which should but may not enclose (or contain) the
 * component that aligns itself to that box.  This is an indempotent
 * solution, while a component class may be designed for other
 * indempotent solutions.
 * 
 * This alignment should change the location of the component, while a
 * component class might possibly be designed for an alternative
 * interpretation such as scaling.
 */
public enum Align {
    /**
     * Align left edge of content to left edge of bounding box: <code>X = 0</code>
     */
    LEFT, 
    /**
     * Align right edge of content to right edge of bounding box: <code>X = (parent.width - this.width)</code>
     */
    RIGHT, 
    /**
     * Align center of content to center of bounding box: <code>X = (parent.width - this.width)/2.0; Y = (parent.height - this.height)/2.0</code>
     */
    CENTER, 
    /**
     * Align top edge of content to top edge of bounding box: <code>Y = 0</code>
     */
    TOP, 
    /**
     * Align bottom edge of content to bottom edge of bounding box: <code>Y = (parent.height - this.height)</code>
     */
    BOTTOM;


    /**
     * Apply alignment to component bounds and return
     * 
     * @param component Component bounds having this alignment
     * @param parent Parent bounding box circumscribing alignment
     * 
     * @return Component bounds
     */
    public Bounds apply(Bounds component, Bounds parent){
        switch(this){
        case LEFT:

            component.x = 0.0f;

            return component;

        case RIGHT:

            component.x = (parent.width - component.width);

            return component;

        case CENTER:

            component.x = (parent.width - component.width)/2.0f;
            component.y = (parent.height - component.height)/2.0f;

            return component;

        case TOP:

            component.y = 0.0f;

            return component;

        case BOTTOM:

            component.y = (parent.height - component.height);

            return component;

        default:
            throw new IllegalStateException(this.name());
        }
    }
    /**
     * Apply alignment to component bounds and return
     * 
     * @param component Component bounds having this alignment
     * @param margin Component bounds exterior margin
     * @param parent Parent bounding box circumscribing alignment
     * 
     * @return Component bounds
     */
    public Bounds apply(Bounds component, Padding margin, Bounds parent){
        switch(this){
        case LEFT:

            component.x = margin.left;

            return component;

        case RIGHT:

            component.x = (parent.width - component.width - margin.getWidth());

            return component;

        case CENTER:

            component.x = (parent.width - component.width - margin.getWidth())/2.0f;
            component.y = (parent.height - component.height - margin.getHeight())/2.0f;

            return component;

        case TOP:

            component.y = margin.top;

            return component;

        case BOTTOM:

            component.y = (parent.height - component.height - margin.bottom);

            return component;

        default:
            throw new IllegalStateException(this.name());
        }
    }
    public String toString(){
        return this.name().toLowerCase();
    }

    public final static Align For(String string){
        if (null == string)
            return null;
        else {
            try {
                return Align.valueOf(string.toUpperCase());
            }
            catch (RuntimeException exc){
                return null;
            }
        }
    }

    /**
     * Horizontal 1D alignment property value
     */
    public enum Horizontal {
        LEFT, CENTER, RIGHT;


        /**
         * Apply alignment to component bounds and return
         * 
         * @param component Component bounds having this alignment
         * @param parent Parent bounding box circumscribing alignment
         * 
         * @return Component bounds
         */
        public Bounds apply(Bounds component, Bounds parent){
            switch(this){
            case LEFT:

                component.x = 0.0f;

                return component;

            case CENTER:

                component.x = (parent.width - component.width)/2.0f;

                return component;

            case RIGHT:

                component.x = (parent.width - component.width);

                return component;

            default:
                throw new IllegalStateException(this.name());
            }
        }
        /**
         * Apply alignment to component bounds and return
         * 
         * @param component Component bounds having this alignment
         * @param margin Component bounds exterior margin
         * @param parent Parent bounding box circumscribing alignment
         * 
         * @return Component bounds
         */
        public Bounds apply(Bounds component, Padding margin, Bounds parent){
            switch(this){
            case LEFT:

                component.x = margin.left;

                return component;

            case CENTER:

                component.x = (parent.width - component.width - margin.getWidth())/2.0f;

                return component;

            case RIGHT:

                component.x = (parent.width - component.width - margin.getWidth());

                return component;

            default:
                throw new IllegalStateException(this.name());
            }
        }
        public String toString(){
            return this.name().toLowerCase();
        }

        public final static Horizontal For(String string){
            if (null == string)
                return null;
            else {
                try {
                    return Horizontal.valueOf(string.toUpperCase());
                }
                catch (RuntimeException exc){
                    return null;
                }
            }
        }
    }
    /**
     * Vertical 1D alignment property value
     */
    public enum Vertical {
        TOP, CENTER, BOTTOM;


        /**
         * Apply alignment to component bounds and return
         * 
         * @param component Component bounds having this alignment
         * @param parent Parent bounding box circumscribing alignment
         * 
         * @return Component bounds
         */
        public Bounds apply(Bounds component, Bounds parent){
            switch(this){

            case TOP:

                component.y = 0.0f;

                return component;

            case CENTER:

                component.y = (parent.height - component.height)/2.0f;

                return component;

            case BOTTOM:

                component.y = (parent.height - component.height);

                return component;

            default:
                throw new IllegalStateException(this.name());
            }
        }
        /**
         * Apply alignment to component bounds and return
         * 
         * @param component Component bounds having this alignment
         * @param margin Component bounds exterior margin
         * @param parent Parent bounding box circumscribing alignment
         * 
         * @return Component bounds
         */
        public Bounds apply(Bounds component, Padding margin, Bounds parent){
            switch(this){

            case TOP:

                component.y = margin.top;

                return component;

            case CENTER:

                component.y = (parent.height - component.height - margin.getHeight())/2.0f;

                return component;

            case BOTTOM:

                component.y = (parent.height - component.height - margin.bottom);

                return component;

            default:
                throw new IllegalStateException(this.name());
            }
        }
        public String toString(){
            return this.name().toLowerCase();
        }

        public final static Vertical For(String string){
            if (null == string)
                return null;
            else {
                try {
                    return Vertical.valueOf(string.toUpperCase());
                }
                catch (RuntimeException exc){
                    return null;
                }
            }
        }
    }

}
