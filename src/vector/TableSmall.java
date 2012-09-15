package vector;

import json.Json;
import json.ObjectJson;

import java.awt.geom.Rectangle2D;

/**
 * {@link TableSmall} orders staticly dimensioned children to fit a
 * defined number columns.
 */
public class TableSmall
    extends Container
{


    protected float cellSpacing;

    protected int cols;


    public TableSmall(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.cols = 2;
    }
    @Override
    public void modified(){
        super.modified();

        this.layout();
    }
    @Override
    public void resized(){
        super.resized();

        this.layout();
    }
    protected void layout(){

        final int count = this.count();
        if (0 < count){
            final int cols = this.cols;
            if (0 < cols){
                final int rows = (count/cols);

                final double cs = this.cellSpacing;

                final double[] colwidths = new double[cols];
                final double[] rowheights = new double[rows];

                int  rr, cc, cx;

                measurement:
                for (rr = 0; rr < rows; rr++){

                    for (cc = 0; cc < cols; cc++){

                        cx = ((rr*cols)+cc);

                        if (this.has(cx)){

                            Component c = this.get(cx);
                            Rectangle2D.Float cb = c.getBoundsVector();

                            colwidths[cc] = Math.max(colwidths[cc],(cs+cb.width));
                            rowheights[rr] = Math.max(rowheights[rr],(cs+cb.height));
                        }
                        else
                            break measurement;
                    }
                }


                double dx, dy, xx = cs, yy = cs;

                double tableWidth = (2.0*cs), tableHeight = tableWidth;

                definition:
                for (rr = 0; ; rr++){

                    dy = rowheights[rr];
                    tableHeight += dy;

                    for (cc = 0; cc < cols; cc++){

                        dx = colwidths[cc];
                        tableWidth += dx;

                        cx = ((rr*cols)+cc);

                        if (this.has(cx)){

                            Component c = this.get(cx);
                            Rectangle2D.Float cb = c.getBoundsVector();
                            c.setBoundsVector(new TableCell(rr,cc,xx,yy,cb.width,cb.height));
                            c.relocated();

                            xx += dx;
                        }
                        else
                            break definition;
                    }
                    xx = cs;
                    yy += dy;
                }

                final Rectangle2D.Float bounds = this.getBoundsVector();
                bounds.width = (float)tableWidth;
                bounds.height = (float)tableHeight;
                this.setBoundsVector(bounds);
            }
        }
    }
    public final int getCols(){
        return this.cols;
    }
    public final TableSmall setCols(int cols){
        if (0 < cols){
            this.cols = cols;

            this.modified();
        }
        return this;
    }
    public final TableSmall setCols(Integer cols){
        if (null != cols)
            return this.setCols(cols.intValue());
        else
            return this;
    }
    public float getCellSpacing(){
        return this.cellSpacing;
    }
    public TableSmall setCellSpacing(float cellSpacing){
        if (0.0 <= cellSpacing){
            this.cellSpacing = cellSpacing;

            this.modified();
        }
        return this;
    }
    public TableSmall setCellSpacing(Float cellSpacing){
        if (null != cellSpacing)
            return this.setCellSpacing(cellSpacing.floatValue());
        else
            return this;
    }

    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("cols",this.getCols());

        thisModel.setValue("cellspacing",this.getCellSpacing());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        this.setCols( (Integer)thisModel.getValue("cols",Integer.class));

        this.setCellSpacing( thisModel.getValue("cellspacing",Float.class));

        Component.Tools.DecodeComponents(this,thisModel);

        return true;
    }
}
