package cn.edu.nju.cs.seg.schooledinapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by luping on 2017/5/17.
 */

public class AnswerCommentItem implements Serializable{

    @SerializedName("id")
    private int id;

    @SerializedName("commenter")
    private String commenter;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    @SerializedName("comment_url")
    private String commentUrl;

    @SerializedName("commenter_avatar_url")
    private String commenterAvatarUrl;

    @SerializedName("created_at")
    private long createdAt;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getCommenter() { return commenter; }

    public void setCommenter(String commenter) { this.commenter = commenter; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getCommentUrl() { return commentUrl; }

    public void setCommentUrl(String commentUrl) { this.commentUrl = commentUrl; }

    public String getCommenterAvatarUrl() { return commenterAvatarUrl; }

    public void setCommenterAvatarUrl(String commenterAvatarUrl) { this.commenterAvatarUrl = commenterAvatarUrl; }

    public long getCreatedAt() { return createdAt; }

    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
