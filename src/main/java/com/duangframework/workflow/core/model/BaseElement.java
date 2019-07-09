package com.duangframework.workflow.core.model;


import com.duangframework.kit.ToolsKit;
import com.duangframework.utils.Assert;
import com.duangframework.workflow.utils.NodeEventEnum;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author laotang
 */
public abstract class BaseElement implements INode {


    private String id;
    private String label;
    private String description;
    private String href;
    /**标签名称*/
    private String name;
    /**
     * 是否真的被使用到了,没有使用的视为无效配置,会报异常
     */
    private boolean connected = false;


    public BaseElement() {
    }

    public BaseElement(String id, String label, String description, String href, String name) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.href = href;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void parse(org.w3c.dom.Node node) throws Exception {

        Assert.isTrue(null != node, "node is null");

        NamedNodeMap namedNodeMap = node.getAttributes();
        Assert.isTrue(null != namedNodeMap, "namedNodeMap is null");

        // 提取ID
        this.id = getValue(namedNodeMap, "id");
        Assert.hasText(id, "id is empty");
        // 提取label
        this.label = getValue(namedNodeMap, "label");
        // 提取description
        this.description = getValue(namedNodeMap, "description");
        // 提取href
        this.href = getValue(namedNodeMap, "href");
        // 标签名称
        this.name = node.getNodeName().trim();
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
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

    /**
     * 取元素值，确保值不为null
     * @param namedNodeMap
     * @param key
     * @return
     */
    private String getValue(NamedNodeMap namedNodeMap, String key) {
        Node attribute = namedNodeMap.getNamedItem(key);
        if (ToolsKit.isNotEmpty(attribute)) {
            String value = attribute.getNodeValue();
            if(ToolsKit.isNotEmpty(value)) {
                return value;
            }
        }
        return "";
    }
}
