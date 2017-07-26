package cn.edu.nju.cs.seg.schooledinapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FavoriteEssayItem implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("heat")
    private int heat;

    @SerializedName("supports")
    private int supports;

    @SerializedName("comments")
    private int comments;

    @SerializedName("studio")
    private String studio;

    @SerializedName("url")
    private String url;

    @SerializedName("studio_url")
    private String studioUrl;

    @SerializedName("comments_url")
    private String commentsUrl;

    @SerializedName("created_at")
    private long createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getSupports() {
        return supports;
    }

    public void setSupports(int supports) {
        this.supports = supports;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStudioUrl() {
        return studioUrl;
    }

    public void setStudioUrl(String studioUrl) {
        this.studioUrl = studioUrl;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

}
