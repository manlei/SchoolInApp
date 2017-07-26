package cn.edu.nju.cs.seg.schooledinapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Created by luping on 2017/5/16.
 */

public class Essay implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("heat")
    private int heat;

    @SerializedName("supports")
    private int supports;

    @SerializedName("comments")
    private int comments;

    @SerializedName("title")
    private String title;

    @SerializedName("type")
    private String type;

    @SerializedName("audio_url")
    private String audioUrl;

    @SerializedName("content")
    private String content;

    @SerializedName("studio")
    private String studio;

    @SerializedName("studio_bio")
    private String studioBio;

    @SerializedName("url")
    private String url;

    @SerializedName("studio_url")
    private String studioUrl;

    @SerializedName("studio_avatar_url")
    private String studioAvatarUrl;

    @SerializedName("comments_url")
    private String commentsUrl;

    @SerializedName("created_at")
    private long createdAt;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getHeat() { return heat; }

    public void setHeat(int heat) { this.heat = heat; }

    public int getSupports() { return supports; }

    public void setSupports(int supports) { this.supports = supports; }

    public int getComments() { return comments; }

    public void setComments(int comments) { this.comments = comments; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getStudio() { return studio; }

    public void setStudio(String studio) { this.studio = studio; }

    public String getStudioBio() { return studioBio; }

    public void setStudioBio(String studioBio) { this.studioBio = studioBio; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getStudioUrl() { return studioUrl; }

    public void setStudioUrl(String studioUrl) { this.studioUrl = studioUrl; }

    public String getStudioAvatarUrl() { return studioAvatarUrl; }

    public void setStudioAvatarUrl(String studioAvatarUrl) { this.studioAvatarUrl = studioAvatarUrl; }

    public String getCommentsUrl() { return commentsUrl; }

    public void setCommentsUrl(String commentsUrl) { this.commentsUrl = commentsUrl; }

    public long getCreatedAt() { return createdAt; }

    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}
