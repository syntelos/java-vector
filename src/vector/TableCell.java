package vector;

/**
 * Table cell bounds and (row,col) position
 * 
 * @see TableBig
 * @see TableSmall
 */
public final class TableCell
    extends java.awt.geom.Rectangle2D.Float
{
    /**
     * Counting from zero, inclusive
     */
    public final int row, col;


    public TableCell(int row, int col, double x, double y, double w, double h){
        super((float)x,(float)y,(float)w,(float)h);
        this.row = row;
        this.col = col;
    }
    public TableCell(int row, int col, double x, double y, float w, float h){
        super((float)x,(float)y,w,h);
        this.row = row;
        this.col = col;
    }
}
