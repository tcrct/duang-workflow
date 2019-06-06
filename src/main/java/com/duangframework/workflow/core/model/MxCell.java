package com.duangframework.workflow.core.model;

import java.io.Serializable;

/**
 * mxGraph-->mxCell节点
 *
 * @author laotang
 * @date 2019-06-05
 */
public class MxCell implements INode {

    public String style;
    private String parent;
    private String vertex;
    private String edge;
    private String source;
    private String target;

    public MxCell() {
    }

    public MxCell(String style, String parent, String vertex, String edge, String source, String target) {
        this.style = style;
        this.parent = parent;
        this.vertex = vertex;
        this.edge = edge;
        this.source = source;
        this.target = target;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getVertex() {
        return vertex;
    }

    public void setVertex(String vertex) {
        this.vertex = vertex;
    }

    public String getEdge() {
        return edge;
    }

    public void setEdge(String edge) {
        this.edge = edge;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "MxCell{" +
                "style='" + style + '\'' +
                ", parent='" + parent + '\'' +
                ", vertex='" + vertex + '\'' +
                ", edge='" + edge + '\'' +
                ", source='" + source + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}