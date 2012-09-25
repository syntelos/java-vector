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

/**
 * Table cell bounds and (row,col) position
 * 
 * @see TableBig
 * @see TableSmall
 */
public final class TableCell
    extends Bounds
{
    /**
     * Counting from zero, inclusive
     */
    public final int row, col;


    public TableCell(int row, int col, double x, double y, double w, double h){
        super(x,y,w,h);
        this.row = row;
        this.col = col;
    }
    public TableCell(int row, int col, double x, double y, float w, float h){
        super((float)x,(float)y,w,h);
        this.row = row;
        this.col = col;
    }
}
