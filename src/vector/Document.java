/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2012, The DigiVac Company
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

import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class represents the container of a Display, with or without a
 * (non empty) source document.  Operations in the platform Frame may
 * be exposed here for platforms like Applet that have no window
 * manager component (Frame).
 * 
 * Members of this class should perform platform I/O for Frame and
 * Display.  This would permit more portable and shared (copied) code
 * between platforms.
 * 
 * @see platform.Document
 */
public interface Document
{
    /**
     * @return Active document
     */
    public boolean hasRoot();
    /**
     * @return Null or document root
     */
    public URL getRoot();
    /**
     * @param path Absolute or relative path 
     * 
     * @return Null or reference from document root
     */
    public URL reference(String path)
        throws MalformedURLException;
    /**
     * @return Null or stream
     */
    public InputStream open(String path)
        throws IOException;

}
