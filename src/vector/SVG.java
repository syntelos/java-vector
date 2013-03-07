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

import json.Json;
import json.ObjectJson;

import java.net.URL;

/**
 * <h3>JSON</h3>
 * 
 * <p> Read or write the <b>vector</b> scene graph.  </p>
 * 
 * <h3>XML</h3>
 * 
 * <p> Use {@link SVGParser} to read an SVG document from a URL into
 * the <b>vector</b> scene graph, for example 
 * 
 * <pre>
 * URL url = ...;
 * 
 * SVG svg = new SVG(url);
 * 
 * this.add(svg); // (network read svg &amp; build scene graph)
 * </pre>
 * </p>
 *
 * @see SVGParser
 */
public class SVG
    extends Container
{

    protected URL source;

    protected Bounds viewbox;

    protected Boolean pathContent;


    /**
     * Build or read scene from JSON 
     */
    public SVG(){
        super();
    }
    /**
     * Use SVGParser to build scene from init
     */
    public SVG(URL u){
        super();
        this.source = u;
    }


    @Override
    public void init(){

        super.init();

        this.parseSVG();
    }
    @Override
    public void destroy(){
        super.destroy();

        this.viewbox = null;
        this.pathContent = null;
    }
    @Override
    public void modified(){

        this.parseSVG();

        super.modified();
    }
    public SVG parseSVG(){
        if (null != this.source && this.isEmpty()){

            final SVGParser p = this.createSVGParser();

            if (p.parse())
                this.parseOk(p);
            else
                this.parseEr(p);
        }
        return this;
    }
    public SVGParser createSVGParser(){

        return (new SVGParser(this));
    }
    public void parseOk(SVGParser p){

        this.outputScene();
    }
    public void parseEr(SVGParser p){
    }
    public boolean hasViewbox(){

        return (null != this.viewbox);
    }
    public Bounds getViewbox(){

        Bounds viewbox = this.viewbox;
        if (null != viewbox)
            return viewbox.clone();
        else
            return null;
    }
    public SVG setViewbox(Bounds bounds){

        this.viewbox = bounds;

        return this;
    }
    public URL getSource(){

        return this.source;
    }
    public SVG setSource(URL u){

        this.source = u;

        return this;
    }
    public SVG setSource(String uri){
        try {
            Display display = this.getRootContainer();
            if (display.hasDocumentVector()){

                Document document = display.getDocumentVector();

                this.source = document.reference(uri);
            }
            else {
                try {
                    this.source = new URL(uri);
                }
                catch (Exception exc){

                    throw new IllegalArgumentException(uri,exc);
                }
            }
        }
        catch (Exception exc){

            throw new IllegalArgumentException(uri,exc);
        }
        return this;
    }
    public boolean hasPathContent(){
        return (null != this.pathContent);
    }
    public Boolean getPathContent(){
        return this.pathContent;
    }
    public boolean isPathContent(){
        return (null != this.pathContent && this.pathContent.booleanValue());
    }
    public SVG setPathContent(Boolean booleanValue){
        this.pathContent = booleanValue;
        return this;
    }
    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("source",this.getSource());

        if (this.hasPathContent()){
            thisModel.setValue("path-content",this.getPathContent());
        }
        if (this.hasViewbox()){
            thisModel.setValue("viewbox",this.getViewbox());
        }

        return thisModel;
    }
    public boolean fromJson(Json thisModel){
        super.fromJson(thisModel);

        this.setSource((String)thisModel.getValue("source"));
        this.setPathContent(thisModel.getValue("path-content",Boolean.class));
        this.setViewbox(thisModel.getValue("viewbox",Bounds.class));

        return true;
    }

}
