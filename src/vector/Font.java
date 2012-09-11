package vector;

import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

/**
 * A universal font primitive.
 */
public class Font
    extends java.awt.Font
{

    public final static Font decode(String string){

        return new Font(string);
    }

    /**
     * Default size and padding proportions
     */
    public final static float SZ = 11f;
    public final static float PW = 4f;
    public final static float PH = 1f;

    protected final static Toolkit TK = Toolkit.getDefaultToolkit();

    public final static Font Default = new Font(new java.awt.Font(Font.MONOSPACED,Font.PLAIN,(int)Font.SZ));


    protected final Padding padding;
    public final FontRenderContext frc;
    public final FontMetrics metrics;
    public final int ascent, descent, height, em;


    public Font(String code){
        this(java.awt.Font.decode(code));
    }
    public Font(java.awt.Font font){
        super(font);
        float prop = font.getSize2D()/SZ;
        this.padding = new Padding( prop*PW, prop*PH);
        this.metrics = TK.getFontMetrics(this);
        this.ascent = this.metrics.getAscent();
        this.descent = this.metrics.getDescent();
        this.height = (this.ascent + this.descent);
        this.em = this.metrics.charWidth(0x20);
        FontRenderContext frc = this.metrics.getFontRenderContext();
        if (frc.isAntiAliased())
            this.frc = frc;
        else
            this.frc = new FontRenderContext(frc.getTransform(),true,frc.usesFractionalMetrics());
    }


    public final Padding getPadding(){
        Padding padding = this.padding;
        if (null != padding)
            return padding.clone();
        else
            throw new IllegalStateException("Missing padding");
    }
    public final Font decrementSize(){
        return this.decrementSize(2);
    }
    public final Font incrementSize(){
        return this.incrementSize(2);
    }
    public final Font decrementSize(float dy){

        float size = this.getSize2D()-dy;

        if (6 <= size)
            return new Font(this.deriveFont(size));
        else
            return this;
    }
    public final Font incrementSize(float dy){

        float size = this.getSize2D()+dy;

        return new Font(this.deriveFont(size));
    }
    public final GlyphVector createGlyphVector(char[] cary){

        return this.createGlyphVector(this.frc,cary);
    }
    public final float em(float n){

        return (n*this.em);
    }
    public final String getStyleString(){

        if (this.isPlain())
            return "plain";

        else if (this.isBold()){

            if (this.isItalic())
                return "bolditalic";
            else
                return "bold";
        }
        else 
            return "italic";
    }
    public final String toString(){
        StringBuilder string = new StringBuilder();
        string.append(this.getName());
        string.append('-');
        string.append(this.getStyleString());
        string.append('-');
        string.append(this.getSize());
        return string.toString();
    }
}
