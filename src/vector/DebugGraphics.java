package vector;


import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * Graphics output trace tool
 */
public class DebugGraphics
    extends java.awt.Graphics2D
{

    private final int depth;

    private java.awt.Graphics2D instance;


    public DebugGraphics(Graphics instance){
        super();
        if (null != instance){

            this.instance = (java.awt.Graphics2D)instance;
            if (instance instanceof DebugGraphics)
                this.depth = ((DebugGraphics)instance).depth+1;
            else
                this.depth = 1;
        }
        else
            throw new IllegalArgumentException();
    }
    public DebugGraphics(DebugGraphics debug, Graphics instance){
        super();
        if (null != debug && null != instance){

            this.instance = (java.awt.Graphics2D)instance;
            this.depth = (debug.depth+1);
        }
        else
            throw new IllegalArgumentException();
    }


    public void transform(AffineTransform affineTransform0)
    {
        DebugTrace.out.printf("[%d] transform(%s)%n", this.depth, affineTransform0);
        this.instance.transform(affineTransform0);
    }
    public void fill(Shape shape0)
    {
        DebugTrace.out.printf("[%d] fill(%s)%n", this.depth, shape0);
        this.instance.fill(shape0);
    }
    public void rotate(double dob0)
    {
        DebugTrace.out.printf("[%d] rotate(%g)%n", this.depth, dob0);
        this.instance.rotate(dob0);
    }
    public void rotate(double dob0, double dob1, double dob2)
    {
        DebugTrace.out.printf("[%d] rotate(%g, %g, %g)%n", this.depth, dob0, dob1, dob2);
        this.instance.rotate(dob0, dob1, dob2);
    }
    public void scale(double dob0, double dob1)
    {
        DebugTrace.out.printf("[%d] scale(%g, %g)%n", this.depth, dob0, dob1);
        this.instance.scale(dob0, dob1);
    }
    public void addRenderingHints(Map map0)
    {
        DebugTrace.out.printf("[%d] addRenderingHints(%s)%n", this.depth, map0);
        this.instance.addRenderingHints(map0);
    }
    public void clip(Shape shape0)
    {
        DebugTrace.out.printf("[%d] clip(%s)%n", this.depth, shape0);
        this.instance.clip(shape0);
    }
    public void draw(Shape shape0)
    {
        DebugTrace.out.printf("[%d] draw(%s)%n", this.depth, shape0);
        this.instance.draw(shape0);
    }
    public void draw3DRect(int a0, int a1, int a2, int a3, boolean bool4)
    {
        DebugTrace.out.printf("[%d] draw3DRect(%d, %d, %d, %d, %b)%n", this.depth, a0, a1, a2, a3, bool4);
        this.instance.draw3DRect(a0, a1, a2, a3, bool4);
    }
    public void drawGlyphVector(GlyphVector glyphVector0, float flo1, float flo2)
    {
        DebugTrace.out.printf("[%d] drawGlyphVector(%s, %f, %f)%n", this.depth, glyphVector0, flo1, flo2);
        this.instance.drawGlyphVector(glyphVector0, flo1, flo2);
    }
    public boolean drawImage(Image image0, AffineTransform affineTransform1, ImageObserver imageObserver2)
    {
        DebugTrace.out.printf("[%d] drawImage(%s, %s, %s)%n", this.depth, image0, affineTransform1, imageObserver2);
        return this.instance.drawImage(image0, affineTransform1, imageObserver2);
    }
    public void drawImage(BufferedImage bufferedImage0, BufferedImageOp bufferedImageOp1, int a2, int a3)
    {
        DebugTrace.out.printf("[%d] drawImage(%s, %s, %d, %d)%n", this.depth, bufferedImage0, bufferedImageOp1, a2, a3);
        this.instance.drawImage(bufferedImage0, bufferedImageOp1, a2, a3);
    }
    public void drawRenderableImage(RenderableImage renderableImage0, AffineTransform affineTransform1)
    {
        DebugTrace.out.printf("[%d] drawRenderableImage(%s, %s)%n", this.depth, renderableImage0, affineTransform1);
        this.instance.drawRenderableImage(renderableImage0, affineTransform1);
    }
    public void drawRenderedImage(RenderedImage renderedImage0, AffineTransform affineTransform1)
    {
        DebugTrace.out.printf("[%d] drawRenderedImage(%s, %s)%n", this.depth, renderedImage0, affineTransform1);
        this.instance.drawRenderedImage(renderedImage0, affineTransform1);
    }
    public void drawString(String string0, int a1, int a2)
    {
        DebugTrace.out.printf("[%d] drawString(%s, %d, %d)%n", this.depth, string0, a1, a2);
        this.instance.drawString(string0, a1, a2);
    }
    public void drawString(String string0, float flo1, float flo2)
    {
        DebugTrace.out.printf("[%d] drawString(%s, %f, %f)%n", this.depth, string0, flo1, flo2);
        this.instance.drawString(string0, flo1, flo2);
    }
    public void drawString(AttributedCharacterIterator attributedCharacterIterator0, int a1, int a2)
    {
        DebugTrace.out.printf("[%d] drawString(%s, %d, %d)%n", this.depth, attributedCharacterIterator0, a1, a2);
        this.instance.drawString(attributedCharacterIterator0, a1, a2);
    }
    public void drawString(AttributedCharacterIterator attributedCharacterIterator0, float flo1, float flo2)
    {
        DebugTrace.out.printf("[%d] drawString(%s, %f, %f)%n", this.depth, attributedCharacterIterator0, flo1, flo2);
        this.instance.drawString(attributedCharacterIterator0, flo1, flo2);
    }
    public void fill3DRect(int a0, int a1, int a2, int a3, boolean bool4)
    {
        DebugTrace.out.printf("[%d] fill3DRect(%d, %d, %d, %d, %b)%n", this.depth, a0, a1, a2, a3, bool4);
        this.instance.fill3DRect(a0, a1, a2, a3, bool4);
    }
    public Color getBackground()
    {
        DebugTrace.out.printf("[%d] getBackground()%n", this.depth);
        return this.instance.getBackground();
    }
    public Composite getComposite()
    {
        DebugTrace.out.printf("[%d] getComposite()%n", this.depth);
        return this.instance.getComposite();
    }
    public GraphicsConfiguration getDeviceConfiguration()
    {
        DebugTrace.out.printf("[%d] getDeviceConfiguration()%n", this.depth);
        return this.instance.getDeviceConfiguration();
    }
    public FontRenderContext getFontRenderContext()
    {
        DebugTrace.out.printf("[%d] getFontRenderContext()%n", this.depth);
        return this.instance.getFontRenderContext();
    }
    public Paint getPaint()
    {
        DebugTrace.out.printf("[%d] getPaint()%n", this.depth);
        return this.instance.getPaint();
    }
    public Object getRenderingHint(RenderingHints.Key renderingHints_Key0)
    {
        DebugTrace.out.printf("[%d] getRenderingHint(%s)%n", this.depth, renderingHints_Key0);
        return this.instance.getRenderingHint(renderingHints_Key0);
    }
    public RenderingHints getRenderingHints()
    {
        DebugTrace.out.printf("[%d] getRenderingHints()%n", this.depth);
        return this.instance.getRenderingHints();
    }
    public Stroke getStroke()
    {
        DebugTrace.out.printf("[%d] getStroke()%n", this.depth);
        return this.instance.getStroke();
    }
    public AffineTransform getTransform()
    {
        DebugTrace.out.printf("[%d] getTransform()%n", this.depth);
        return this.instance.getTransform();
    }
    public boolean hit(Rectangle rectangle0, Shape shape1, boolean bool2)
    {
        DebugTrace.out.printf("[%d] hit(%s, %s, %b)%n", this.depth, rectangle0, shape1, bool2);
        return this.instance.hit(rectangle0, shape1, bool2);
    }
    public void setBackground(Color color0)
    {
        DebugTrace.out.printf("[%d] setBackground(%s)%n", this.depth, color0);
        this.instance.setBackground(color0);
    }
    public void setComposite(Composite composite0)
    {
        DebugTrace.out.printf("[%d] setComposite(%s)%n", this.depth, composite0);
        this.instance.setComposite(composite0);
    }
    public void setPaint(Paint paint0)
    {
        DebugTrace.out.printf("[%d] setPaint(%s)%n", this.depth, paint0);
        this.instance.setPaint(paint0);
    }
    public void setRenderingHint(RenderingHints.Key renderingHints_Key0, Object object1)
    {
        DebugTrace.out.printf("[%d] setRenderingHint(%s, %s)%n", this.depth, renderingHints_Key0, object1);
        this.instance.setRenderingHint(renderingHints_Key0, object1);
    }
    public void setRenderingHints(Map map0)
    {
        DebugTrace.out.printf("[%d] setRenderingHints(%s)%n", this.depth, map0);
        this.instance.setRenderingHints(map0);
    }
    public void setStroke(Stroke stroke0)
    {
        DebugTrace.out.printf("[%d] setStroke(%s)%n", this.depth, stroke0);
        this.instance.setStroke(stroke0);
    }
    public void setTransform(AffineTransform affineTransform0)
    {
        DebugTrace.out.printf("[%d] setTransform(%s)%n", this.depth, affineTransform0);
        this.instance.setTransform(affineTransform0);
    }
    public void shear(double dob0, double dob1)
    {
        DebugTrace.out.printf("[%d] shear(%g, %g)%n", this.depth, dob0, dob1);
        this.instance.shear(dob0, dob1);
    }
    public void translate(int a0, int a1)
    {
        DebugTrace.out.printf("[%d] translate(%d, %d)%n", this.depth, a0, a1);
        this.instance.translate(a0, a1);
    }
    public void translate(double dob0, double dob1)
    {
        DebugTrace.out.printf("[%d] translate(%g, %g)%n", this.depth, dob0, dob1);
        this.instance.translate(dob0, dob1);
    }
    public void finalize()
    {
        DebugTrace.out.printf("[%d] finalize()%n", this.depth);
        this.instance.finalize();
    }
    public String toString()
    {
        DebugTrace.out.printf("[%d] toString()%n", this.depth);
        return this.instance.toString();
    }
    public Graphics create()
    {
        DebugTrace.out.printf("[%d] create()%n", this.depth);
        return new DebugGraphics(this,this.instance.create());
    }
    public Graphics create(int a0, int a1, int a2, int a3)
    {
        DebugTrace.out.printf("[%d] create(%d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3);
        return new DebugGraphics(this,this.instance.create(a0, a1, a2, a3));
    }
    public boolean drawImage(Image image0, int a1, int a2, ImageObserver imageObserver3)
    {
        DebugTrace.out.printf("[%d] drawImage(%s, %d, %d, %s)%n", this.depth, image0, a1, a2, imageObserver3);
        return this.instance.drawImage(image0, a1, a2, imageObserver3);
    }
    public boolean drawImage(Image image0, int a1, int a2, int a3, int a4, ImageObserver imageObserver5)
    {
        DebugTrace.out.printf("[%d] drawImage(%s, %d, %d, %d, %d, %s)%n", this.depth, image0, a1, a2, a3, a4, imageObserver5);
        return this.instance.drawImage(image0, a1, a2, a3, a4, imageObserver5);
    }
    public boolean drawImage(Image image0, int a1, int a2, Color color3, ImageObserver imageObserver4)
    {
        DebugTrace.out.printf("[%d] drawImage(%s, %d, %d, %s, %s)%n", this.depth, image0, a1, a2, color3, imageObserver4);
        return this.instance.drawImage(image0, a1, a2, color3, imageObserver4);
    }
    public boolean drawImage(Image image0, int a1, int a2, int a3, int a4, Color color5, ImageObserver imageObserver6)
    {
        DebugTrace.out.printf("[%d] drawImage(%s, %d, %d, %d, %d, %s, %s)%n", this.depth, image0, a1, a2, a3, a4, color5, imageObserver6);
        return this.instance.drawImage(image0, a1, a2, a3, a4, color5, imageObserver6);
    }
    public boolean drawImage(Image image0, int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, ImageObserver imageObserver9)
    {
        DebugTrace.out.printf("[%d] drawImage(%s, %d, %d, %d, %d, %d, %d, %d, %d, %s)%n", this.depth, image0, a1, a2, a3, a4, a5, a6, a7, a8, imageObserver9);
        return this.instance.drawImage(image0, a1, a2, a3, a4, a5, a6, a7, a8, imageObserver9);
    }
    public boolean drawImage(Image image0, int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, Color color9, ImageObserver imageObserver10)
    {
        DebugTrace.out.printf("[%d] drawImage(%s, %d, %d, %d, %d, %d, %d, %d, %d, %s, %s)%n", this.depth, image0, a1, a2, a3, a4, a5, a6, a7, a8, color9, imageObserver10);
        return this.instance.drawImage(image0, a1, a2, a3, a4, a5, a6, a7, a8, color9, imageObserver10);
    }
    public void fillRect(int a0, int a1, int a2, int a3)
    {
        DebugTrace.out.printf("[%d] fillRect(%d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3);
        this.instance.fillRect(a0, a1, a2, a3);
    }
    public Color getColor()
    {
        DebugTrace.out.printf("[%d] getColor()%n", this.depth);
        return this.instance.getColor();
    }
    public void setColor(Color color0)
    {
        DebugTrace.out.printf("[%d] setColor(%s)%n", this.depth, color0);
        this.instance.setColor(color0);
    }
    public void clearRect(int a0, int a1, int a2, int a3)
    {
        DebugTrace.out.printf("[%d] clearRect(%d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3);
        this.instance.clearRect(a0, a1, a2, a3);
    }
    public void clipRect(int a0, int a1, int a2, int a3)
    {
        DebugTrace.out.printf("[%d] clipRect(%d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3);
        this.instance.clipRect(a0, a1, a2, a3);
    }
    public void copyArea(int a0, int a1, int a2, int a3, int a4, int a5)
    {
        DebugTrace.out.printf("[%d] copyArea(%d, %d, %d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3, a4, a5);
        this.instance.copyArea(a0, a1, a2, a3, a4, a5);
    }
    public void dispose()
    {
        DebugTrace.out.printf("[%d] dispose()%n", this.depth);
        this.instance.dispose();
    }
    public void drawArc(int a0, int a1, int a2, int a3, int a4, int a5)
    {
        DebugTrace.out.printf("[%d] drawArc(%d, %d, %d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3, a4, a5);
        this.instance.drawArc(a0, a1, a2, a3, a4, a5);
    }
    public void drawBytes(byte[] bb0, int a1, int a2, int a3, int a4)
    {
        DebugTrace.out.printf("[%d] drawBytes(%d, %d, %d, %d, %d)%n", this.depth, bb0, a1, a2, a3, a4);
        this.instance.drawBytes(bb0, a1, a2, a3, a4);
    }
    public void drawChars(char[] ch0, int a1, int a2, int a3, int a4)
    {
        DebugTrace.out.printf("[%d] drawChars(%c, %d, %d, %d, %d)%n", this.depth, ch0, a1, a2, a3, a4);
        this.instance.drawChars(ch0, a1, a2, a3, a4);
    }
    public void drawLine(int a0, int a1, int a2, int a3)
    {
        DebugTrace.out.printf("[%d] drawLine(%d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3);
        this.instance.drawLine(a0, a1, a2, a3);
    }
    public void drawOval(int a0, int a1, int a2, int a3)
    {
        DebugTrace.out.printf("[%d] drawOval(%d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3);
        this.instance.drawOval(a0, a1, a2, a3);
    }
    public void drawPolygon(int[] a0, int[] a1, int a2)
    {
        DebugTrace.out.printf("[%d] drawPolygon(%d, %d, %d)%n", this.depth, a0, a1, a2);
        this.instance.drawPolygon(a0, a1, a2);
    }
    public void drawPolygon(Polygon polygon0)
    {
        DebugTrace.out.printf("[%d] drawPolygon(%s)%n", this.depth, polygon0);
        this.instance.drawPolygon(polygon0);
    }
    public void drawPolyline(int[] a0, int[] a1, int a2)
    {
        DebugTrace.out.printf("[%d] drawPolyline(%d, %d, %d)%n", this.depth, a0, a1, a2);
        this.instance.drawPolyline(a0, a1, a2);
    }
    public void drawRect(int a0, int a1, int a2, int a3)
    {
        DebugTrace.out.printf("[%d] drawRect(%d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3);
        this.instance.drawRect(a0, a1, a2, a3);
    }
    public void drawRoundRect(int a0, int a1, int a2, int a3, int a4, int a5)
    {
        DebugTrace.out.printf("[%d] drawRoundRect(%d, %d, %d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3, a4, a5);
        this.instance.drawRoundRect(a0, a1, a2, a3, a4, a5);
    }
    public void fillArc(int a0, int a1, int a2, int a3, int a4, int a5)
    {
        DebugTrace.out.printf("[%d] fillArc(%d, %d, %d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3, a4, a5);
        this.instance.fillArc(a0, a1, a2, a3, a4, a5);
    }
    public void fillOval(int a0, int a1, int a2, int a3)
    {
        DebugTrace.out.printf("[%d] fillOval(%d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3);
        this.instance.fillOval(a0, a1, a2, a3);
    }
    public void fillPolygon(int[] a0, int[] a1, int a2)
    {
        DebugTrace.out.printf("[%d] fillPolygon(%d, %d, %d)%n", this.depth, a0, a1, a2);
        this.instance.fillPolygon(a0, a1, a2);
    }
    public void fillPolygon(Polygon polygon0)
    {
        DebugTrace.out.printf("[%d] fillPolygon(%s)%n", this.depth, polygon0);
        this.instance.fillPolygon(polygon0);
    }
    public void fillRoundRect(int a0, int a1, int a2, int a3, int a4, int a5)
    {
        DebugTrace.out.printf("[%d] fillRoundRect(%d, %d, %d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3, a4, a5);
        this.instance.fillRoundRect(a0, a1, a2, a3, a4, a5);
    }
    public Shape getClip()
    {
        DebugTrace.out.printf("[%d] getClip()%n", this.depth);
        return this.instance.getClip();
    }
    public Rectangle getClipBounds()
    {
        DebugTrace.out.printf("[%d] getClipBounds()%n", this.depth);
        return this.instance.getClipBounds();
    }
    public Rectangle getClipBounds(Rectangle rectangle0)
    {
        DebugTrace.out.printf("[%d] getClipBounds(%s)%n", this.depth, rectangle0);
        return this.instance.getClipBounds(rectangle0);
    }
    public Rectangle getClipRect()
    {
        DebugTrace.out.printf("[%d] getClipRect()%n", this.depth);
        return this.instance.getClipRect();
    }
    public Font getFont()
    {
        DebugTrace.out.printf("[%d] getFont()%n", this.depth);
        return this.instance.getFont();
    }
    public FontMetrics getFontMetrics()
    {
        DebugTrace.out.printf("[%d] getFontMetrics()%n", this.depth);
        return this.instance.getFontMetrics();
    }
    public FontMetrics getFontMetrics(Font font0)
    {
        DebugTrace.out.printf("[%d] getFontMetrics(%s)%n", this.depth, font0);
        return this.instance.getFontMetrics(font0);
    }
    public boolean hitClip(int a0, int a1, int a2, int a3)
    {
        DebugTrace.out.printf("[%d] hitClip(%d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3);
        return this.instance.hitClip(a0, a1, a2, a3);
    }
    public void setClip(int a0, int a1, int a2, int a3)
    {
        DebugTrace.out.printf("[%d] setClip(%d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3);
        this.instance.setClip(a0, a1, a2, a3);
    }
    public void setClip(Shape shape0)
    {
        DebugTrace.out.printf("[%d] setClip(%s)%n", this.depth, shape0);
        this.instance.setClip(shape0);
    }
    public void setFont(Font font0)
    {
        DebugTrace.out.printf("[%d] setFont(%s)%n", this.depth, font0);
        this.instance.setFont(font0);
    }
    public void setPaintMode()
    {
        DebugTrace.out.printf("[%d] setPaintMode()%n", this.depth);
        this.instance.setPaintMode();
    }
    public void setXORMode(Color color0)
    {
        DebugTrace.out.printf("[%d] setXORMode(%s)%n", this.depth, color0);
        this.instance.setXORMode(color0);
    }
}
