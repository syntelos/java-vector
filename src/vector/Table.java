package vector;

import json.Json;
import json.ObjectJson;

import java.awt.geom.Rectangle2D;

/**
 * {@link Table} defines set of equally sized children.
 */
public class Table
    extends Container
{

    /**
     * Cell bounds and (row,col) position
     */
    public final class Cell
        extends Rectangle2D.Float
    {
        /**
         * Counting from zero, inclusive
         */
        public final int row, col;


        public Cell(int row, int col, double x, double y, double w, double h){
            super((float)x,(float)y,(float)w,(float)h);
            this.row = row;
            this.col = col;
        }
    }



    protected float cellSpacing;

    protected double cellWidth, cellHeight;

    protected int rows, cols;


    public Table(){
        super();
    }


    @Override
    public void modified(){
        this.layout();
    }
    @Override
    public void resized(){
        this.setBoundsVectorInit(this.getParentVector());
        this.layout();
    }
    public void layout(){

        final int count = this.count();
        final Rectangle2D.Float bounds = this.getBoundsVector();
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

                    c.setBoundsVector(new Table.Cell(rr,cc,xx,yy,this.cellWidth,this.cellHeight));

                    xx += dx;
                }
                else
                    break layout;
            }

            xx = x0;
            yy += dy;
        }
    }
    public float getCellSpacing(){
        return this.cellSpacing;
    }
    public Table setCellSpacing(float cellSpacing){
        if (0.0 <= cellSpacing){
            this.cellSpacing = cellSpacing;

            this.modified();
        }
        return this;
    }
    public Table setCellSpacing(Float cellSpacing){
        if (null != cellSpacing)
            return this.setCellSpacing(cellSpacing.floatValue());
        else
            return this;
    }

    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("cellspacing",this.getCellSpacing());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        this.setCellSpacing( thisModel.getValue("cellspacing",Float.class));

        Component.Tools.DecodeComponents(this,thisModel);

        return true;
    }
}
