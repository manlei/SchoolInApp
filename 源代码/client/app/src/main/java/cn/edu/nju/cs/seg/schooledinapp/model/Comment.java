package cn.edu.nju.cs.seg.schooledinapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment implements Serializable {

    @SerializedName("parent_id")
    private int parentId;

    @SerializedName("content")
    private String content;

    @SerializedName("parent_type")
    private String parentType;

    @SerializedName("commenter_password")
    private String commenterPassword;

    @SerializedName("commenter_email_or_phone")
    private String commenterEmailOrPhone;


    public int getParentId() { return parentId; }

    public void setParentId(int parentId) { this.parentId = parentId; }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParentType() { return parentType; }

    public void setParentType(String parentType) { this.parentType = parentType; }

    public String getCommenterPassword() { return commenterPassword; }

    public void setCommenterPassword(String commenterPassword) { this.commenterPassword = commenterPassword; }

    public String getCommenterEmailOrPhone() { return commenterEmailOrPhone; }

    public void setCommenterEmailOrPhone(String commenterEmailOrPhone) { this.commenterEmailOrPhone = commenterEmailOrPhone; }

}
