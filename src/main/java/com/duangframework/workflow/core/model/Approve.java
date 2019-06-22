package com.duangframework.workflow.core.model;

import com.duangframework.mvc.annotation.Bean;
import com.duangframework.vtor.annotation.FieldName;
import com.duangframework.vtor.annotation.NotEmpty;
import com.duangframework.vtor.annotation.Pattern;

import java.util.List;

/**
 * 审批人，抄送人内容信息
 * @author laotang
 * @date 2019-6-22
 */
@Bean
public class Approve {

    /**
     * 审批人ID
     */
    @NotEmpty
    private List<String> approveUserId;

    /**
     * 设置审批人的类型
     * 用户：user
     * 岗位：post，如果这个选项，则要根据post与user的关联，找出对应的user设置到approveUserId集合中
     */
    @NotEmpty
    @FieldName(label = "审批人类型")
    @Pattern(regexp = "(^user$)|(^post$)", message = "请选择指定用户或岗位")
    private String approveType = "user";

    /**
     * 岗位ID
     */
    private String postId;

    /**
     * 节点说明
     * 为方便扩展，将原XML数据保存
     */
    @FieldName(label = "说明")
    @NotEmpty
    private String description;

    /**
     * 审批方式 and 还是 or, 默认and
     */
    @NotEmpty
    @FieldName(label = "审批方式")
    @Pattern(regexp = "(^and$)|(^or$)", message = "请选择 and 或 or ")
    private String approveModel = "and";

    /**
     * 是否是抄送节点，如果是则别起线程发送消息到指定的用户
     * 与审核人节点的判断方式一致
     */
    private boolean isCc = false;

    public Approve(List<String> approveUserId, String approveType, String postId, String description, String approveModel) {
        this.approveUserId = approveUserId;
        this.approveType = approveType;
        this.postId = postId;
        this.description = description;
        this.approveModel = approveModel;
    }

    public Approve(List<String> approveUserId, String approveType, String postId, String description, String approveModel, boolean isCc) {
        this.approveUserId = approveUserId;
        this.approveType = approveType;
        this.postId = postId;
        this.description = description;
        this.approveModel = approveModel;
        this.isCc = isCc;
    }

    public Approve() {
    }

    public List<String> getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(List<String> approveUserId) {
        this.approveUserId = approveUserId;
    }

    public String getApproveType() {
        return approveType;
    }

    public void setApproveType(String approveType) {
        this.approveType = approveType;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApproveModel() {
        return approveModel;
    }

    public void setApproveModel(String approveModel) {
        this.approveModel = approveModel;
    }

    public boolean isCc() {
        return isCc;
    }

    public void setCc(boolean cc) {
        isCc = cc;
    }

    @Override
    public String toString() {
        return "ApproveDto{" +
                "approveUserId=" + approveUserId +
                ", approveType='" + approveType + '\'' +
                ", postId='" + postId + '\'' +
                ", description='" + description + '\'' +
                ", approveModel='" + approveModel + '\'' +
                ", isCc='" + isCc + '\'' +
                '}';
    }
}
