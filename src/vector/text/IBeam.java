package vector.text;

import vector.Font;
import vector.Padding;
import vector.Text;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;


/**
 * Cursor glyph
 */
public class IBeam {

    private volatile int index;

    private volatile boolean left;


    public IBeam(){
        super();
    }


    public Shape getShape(int index, boolean left, Text text){

        this.index = index;
        this.left = left;

        final Font font = text.getFont();

        final Path2D.Float cursor = new Path2D.Float();

        final Point2D.Float top;
        if (left){

            top = text.getTopLeft(index);
        }
        else {

            top = text.getTopRight(index);
        }

        final float x = top.x, y0 = top.y, y1 = (y0+font.height);

        cursor.moveTo(x,y0);
        cursor.lineTo(x,y1);

        return cursor;
    }
    public Shape getShape(Text text){

        this.index = 0;
        this.left = true;

        final Font font = text.getFont();
        final Padding padding = font.getPadding();

        final Path2D.Float cursor = new Path2D.Float();

        final float x = padding.left, y0 = padding.top, y1 = (y0+font.height);

        cursor.moveTo(x,y0);
        cursor.lineTo(x,y1);

        return cursor;
    }
}
