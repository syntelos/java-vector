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

import platform.Color;

import java.io.File;

import java.net.URL;

/**
 * Class typically returned by {@link Component#getRootContainer
 * Component getRootContainer}.
 * 
 * @see platform.Display
 */
public interface Display
    extends Component.Container
{

    public boolean hasBackground();

    public Color getBackgroundVector();

    public Display setBackground(Color background);

    public Display setBackground(String code);


    public boolean hasDocumentVector();

    public Document getDocumentVector();

    /**
     * @return Proportional window sizes
     */
    public Viewport getViewport();
    /**
     * Resize window
     */
    public Display setViewport(Viewport.Size size);

    /**
     * If add unique by class, then modify, center, and output.
     * Intended for dialog windows.
     * 
     * @param component Instance of component to add, or class of
     * component to remove when found
     * 
     * @return Shown (true) or removed (false).  Failure to show is
     * also false.
     */
    public boolean show(Component component);
    /**
     * If add unique by class, then modify, center, and output.
     * Intended for dialog windows.
     * 
     * @param component Class of component to add when not found, or
     * remove when found
     * 
     * @return Shown (true) or removed (false).  Failure to show is
     * also false.
     */
    public boolean show(Class<? extends Component> component);
    /**
     * Set location to center dimensions
     */
    public Display center(Component d);

    public boolean open(File json);

    public boolean open(URL json);

    public boolean copy(File json);

    public boolean copy(URL json);

    public boolean save();

}
