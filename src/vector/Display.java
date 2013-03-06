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
package vector;

import platform.Color;

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
     */
    public Display show(Component component);

    public Display show(Class<? extends Component> component);
    /**
     * Set location to center dimensions
     */
    public Display center(Component d);

}
