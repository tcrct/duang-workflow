package com.duangframework.workflow.core.model;


/**
 * @author laotang
 */
public class Element implements INode {

    private String id;
    private String label;
    private String description;
    private String href;

    public Element() {
    }

    public Element(String id, String label, String description, String href) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "Element{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
