package com.duangframework.workflow.core.model;


import com.duangframework.kit.ToolsKit;
import com.duangframework.utils.Assert;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author laotang
 */
public abstract class BaseElement implements INode {

    /**
     *
     */
    private String[] NAMES = {"description", "name"};

    private String id;
    private String label;
    private String description;
    private String href;

    public BaseElement() {
    }

    public BaseElement(String id, String label, String description, String href) {
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

    public void parse(org.w3c.dom.Node node) throws Exception {

        Assert.isTrue(null != node, "node is null");

        NamedNodeMap namedNodeMap = node.getAttributes();
        // 提取ID
        Node idAttribute = namedNodeMap.getNamedItem("id");
        Assert.isTrue(null != idAttribute, "id must exist");
        String id = idAttribute.getNodeValue();
        Assert.hasText(id, "id is empty");
        this.id = id;

        // description属性可选
        String description = null;

        for(String item : NAMES) {
            Node descriptionAttribute = namedNodeMap.getNamedItem(item);
            if (ToolsKit.isNotEmpty(descriptionAttribute)) {
                description = descriptionAttribute.getNodeValue();
                break;
            }
        }
        this.description = description;

    }

    @Override
    public String toString() {
        return "BaseElement{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
