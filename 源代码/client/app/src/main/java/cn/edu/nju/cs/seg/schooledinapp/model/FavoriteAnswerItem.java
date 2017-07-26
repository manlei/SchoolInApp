package cn.edu.nju.cs.seg.schooledinapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FavoriteAnswerItem implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("answerer")
    private String answerer;

    @SerializedName("type")
    private String type;

    @SerializedName("audio_url")
    private String audioUrl;

    @SerializedName("content")
    private String content;

    @SerializedName("question_title")
    private String questionTitle;

    @SerializedName("comments")
    private int comments;

    @SerializedName("url")
    private String url;

    @SerializedName("answerer_url")
    private String answererUrl;

    @SerializedName("answerer_avatar_url")
    private String answererAvatarUrl;

    @SerializedName("question_url")
    private String questionUrl;

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

    public String getAnswerer() {
        return answerer;
    }

    public void setAnswerer(String answerer) {
        this.answerer = answerer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAnswererUrl() {
        return answererUrl;
    }

    public void setAnswererUrl(String answererUrl) {
        this.answererUrl = answererUrl;
    }

    public String getAnswererAvatarUrl() {
        return answererAvatarUrl;
    }

    public void setAnswererAvatarUrl(String answererAvatarUrl) {
        this.answererAvatarUrl = answererAvatarUrl;
    }

    public String getQuestionUrl() {
        return questionUrl;
    }

    public void setQuestionUrl(String questionUrl) {
        this.questionUrl = questionUrl;
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
