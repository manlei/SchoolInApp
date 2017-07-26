package cn.edu.nju.cs.seg.schooledinapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by luping on 2017/6/10.
 */

public class UserSupportItem implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("type")
    private String type = "essay";

    @SerializedName("title")
    private String title;

    @SerializedName("heat")
    private int heat;

    @SerializedName("supports")
    private int supports;

    @SerializedName("answers")
    private int answers;

    @SerializedName("directed_to")
    private String directedTo;

    @SerializedName("url")
    private String url;

    @SerializedName("answer_url")
    private String answerUrl;

    @SerializedName("create_at")
    private long createdAt;

    @SerializedName("comments")
    private int comments;

    @SerializedName("studio")
    private String studio;

    @SerializedName("studio_url")
    private String studioUrl;

    @SerializedName("comments_url")
    private String commentsUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getAnswers() {
        return answers;
    }

    public void setAnswers(int answers) {
        this.answers = answers;
    }

    public String getDirectedTo() {
        return directedTo;
    }

    public void setDirectedTo(String directedTo) {
        this.directedTo = directedTo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAnswerUrl() {
        return answerUrl;
    }

    public void setAnswerUrl(String answerUrl) {
        this.answerUrl = answerUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
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

}
