package com.duangframework.workflow.core.model;

import java.io.Serializable;

/**
 *  mxGraph-->mxGeometry节点
 *
 * @author laotang
 * @date 2019-06-05
 */
public class MxGeometry implements INode {

    private String x;
    private String y;
    private String width;
    private String height;
    private String as;
    private String relative;

    public MxGeometry() {
    }

    public MxGeometry(String x, String y, String width, String height, String as, String relative) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.as = as;
        this.relative = relative;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public String getRelative() {
        return relative;
    }

    public void setRelative(String relative) {
        this.relative = relative;
    }

    @Override
    public String toString() {
        return "MxGeometry{" +
                "x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", as='" + as + '\'' +
                ", relative='" + relative + '\'' +
                '}';
    }
}
