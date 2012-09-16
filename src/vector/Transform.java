package vector;

import java.util.StringTokenizer;

/**
 * 
 */
public class Transform
    extends java.awt.geom.AffineTransform
{
    public static Transform getTranslateInstance(double tx, double ty){
        Transform t = new Transform();
        t.setToTranslation(tx,ty);
        return t;
    }
    public static Transform getRotateInstance(double theta){
        Transform t = new Transform();
        t.setToRotation(theta);
        return t;
    }
    public static Transform getRotateInstance(double theta, double cx, double cy){
        Transform t = new Transform();
        t.setToRotation(theta,cx,cy);
        return t;
    }
    public static Transform getRotateInstance(double rx, double ry){
        Transform t = new Transform();
        t.setToRotation(rx,ry);
        return t;
    }
    public static Transform getRotateInstance(double rx, double ry, double cx, double cy){
        Transform t = new Transform();
        t.setToRotation(rx,ry,cx,cy);
        return t;
    }
    public static Transform getScaleInstance(double sx, double sy){
        Transform t = new Transform();
        t.setToScale(sx,sy);
        return t;
    }
    public static Transform getShearInstance(double shx, double shy){
        Transform t = new Transform();
        t.setToShear(shx,shy);
        return t;
    }


    public Transform(){
        super();
    }
    public Transform(String string){
        super();
        if (null != string){
            StringTokenizer strtok = new StringTokenizer(string,"][)(,");
            switch(strtok.countTokens()){
            case 0:
                break;
            case 6:
                try {
                    final double m00 = Double.parseDouble(strtok.nextToken());
                    final double m01 = Double.parseDouble(strtok.nextToken());
                    final double m02 = Double.parseDouble(strtok.nextToken());
                    final double m10 = Double.parseDouble(strtok.nextToken());
                    final double m11 = Double.parseDouble(strtok.nextToken());
                    final double m12 = Double.parseDouble(strtok.nextToken());

                    this.setTransform(m00,m10,m01,m11,m02,m12);
                }
                catch (RuntimeException exc){
                    throw new IllegalArgumentException(string,exc);
                }
                break;
            default:
                throw new IllegalArgumentException(string);
            }
        }
    }
    public Transform(java.awt.geom.AffineTransform t){
        super(t);
    }


    public Bounds transform(Bounds in){
        double[] src = new double[]{
            in.x, in.y,
            (in.x+in.width), (in.y+in.height)
        };
        double[] tgt = new double[4];

        super.transform(src,0,tgt,0,2);

        in.x = (float)tgt[0];
        in.y = (float)tgt[1];
        in.width = (float)(tgt[2]-tgt[0]);
        in.height = (float)(tgt[3]-tgt[1]);

        return in;
    }
    public Transform clone(){
        return (Transform)super.clone();
    }
    public String toString(){
        final double[] matrix = new double[6];
        this.getMatrix(matrix);
        
        StringBuilder string = new StringBuilder();
        string.append("((");
        string.append(matrix[0]);
        string.append(',');
        string.append(matrix[2]);
        string.append(',');
        string.append(matrix[4]);
        string.append(")(");
        string.append(matrix[1]);
        string.append(',');
        string.append(matrix[3]);
        string.append(',');
        string.append(matrix[5]);
        string.append("))");
        return string.toString();
    }
}
