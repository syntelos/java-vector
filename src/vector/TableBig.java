package vector;

import json.Json;
import json.ObjectJson;

import java.awt.geom.Rectangle2D;

/**
 * {@link TableBig} defines set of equally sized children.
 */
public class TableBig
    extends Container
{

    protected float cellSpacing;

    protected double cellWidth, cellHeight;

    protected int rows, cols;

    protected boolean fixed = false;


    public TableBig(){
        super();
    }


    @Override
    public void modified(){
        super.modified();

        this.layout();
    }
    @Override
    public void resized(){
        super.resized();

        if (!this.fixed){
            this.setBoundsVectorInit(this.getParentVector());
        }
        this.layout();
    }
    protected void layout(){

        final int count = this.count();
        if (0 < count){
            final Bounds bounds = this.getBoundsVector();
            if (!bounds.isEmpty()){
                if (2 > count){
                    this.cols = 1;
                    this.rows = 1;
                }
                else {
                    this.cols = (int)Math.ceil(Math.sqrt(count));
                    this.rows = (count/this.cols);
                }
                final double ww = ((double)bounds.width-((double)this.cellSpacing*(double)(this.cols+1)));
                final double hh = ((double)bounds.height-((double)this.cellSpacing*(double)(this.rows+1)));

                this.cellWidth = (ww / (double)this.cols);
                this.cellHeight = (hh / (double)this.rows);


                final double x0 = this.cellSpacing;
                final double dx = (x0+this.cellWidth);
                final double dy = (x0+this.cellHeight);

                double xx = x0, yy = x0;

                int  rr = 0, cc = 0, cx;

                layout:
                for (rr = 0; rr < this.rows; rr++){

                    for (cc = 0; cc < this.cols; cc++){

                        cx = ((rr*this.cols)+cc);

                        if (this.has(cx)){

                            Component c = this.get(cx);

                            c.setBoundsVector(new TableCell(rr,cc,xx,yy,this.cellWidth,this.cellHeight));
                            c.resized();

                            xx += dx;
                        }
                        else
                            break layout;
                    }

                    xx = x0;
                    yy += dy;
                }
            }
        }
    }
    public final boolean isFixed(){
        return this.fixed;
    }
    public final Boolean getFixed(){
        return this.fixed;
    }
    public final TableBig setFixed(boolean fixed){
        this.fixed = fixed;
        return this;
    }
    public final TableBig setFixed(Boolean fixed){
        if (null != fixed){
            this.fixed = fixed;

            this.modified();
        }
        return this;
    }
    public float getCellSpacing(){
        return this.cellSpacing;
    }
    public TableBig setCellSpacing(float cellSpacing){
        if (0.0 <= cellSpacing){
            this.cellSpacing = cellSpacing;

            this.modified();
        }
        return this;
    }
    public TableBig setCellSpacing(Float cellSpacing){
        if (null != cellSpacing)
            return this.setCellSpacing(cellSpacing.floatValue());
        else
            return this;
    }

    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("cellspacing",this.getCellSpacing());

        thisModel.setValue("fixed",this.getFixed());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        this.setCellSpacing( thisModel.getValue("cellspacing",Float.class));

        this.setFixed( (Boolean)thisModel.getValue("fixed"));

        Component.Tools.DecodeComponents(this,thisModel);

        return true;
    }
}
