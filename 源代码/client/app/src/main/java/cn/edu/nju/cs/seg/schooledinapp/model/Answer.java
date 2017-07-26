package cn.edu.nju.cs.seg.schooledinapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by luping on 2017/5/12.
 */

public class Answer implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("comments")
    private int comments;

    @SerializedName("answerer")
    private String answerer;

    @SerializedName("type")
    private String type;

    @SerializedName("audio_url")
    private String audioUrl;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    @SerializedName("answerer_bio")
    private String answererBio;

    @SerializedName("question_title")
    private String questionTitle;

    @SerializedName("question_url")
    private String questionUrl;

    @SerializedName("answerer_url")
    private String answererUrl;

    @SerializedName("comments_url")
    private String commentsUrl;

    @SerializedName("answerer_avatar_url")
    private String answererAvatarUrl;

    @SerializedName("created_at")
    private long createdAt;

    public int getId() { return id; };

    public void setId(int id) { this.id = id; }

    public int getComments() { return comments; }

    public void setComments(int comments) { this.comments = comments; }

    public String getAnswerer() { return answerer; }

    public void setAnswerer(String answerer) { this.answerer = answerer; }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getAnswererBio() { return answererBio; }

    public void setAnswererBio(String answererBio) { this.answererBio = answererBio; }

    public String getQuestionTitle() { return questionTitle; }

    public void setQuestionTitle(String questionTitle) { this.questionTitle = questionTitle; }

    public String getCommentsUrl() { return commentsUrl; }

    public void setCommentsUrl() { this.commentsUrl = commentsUrl; }

    public String getQuestionUrl() { return questionUrl; }

    public void setQuestionUrl(String questionUrl) { this.questionUrl = questionUrl; }

    public String getAnswererAvatarUrl() { return answererAvatarUrl; }

    public void setAnswererAvatarUrl(String answererAvatarUrl) { this.answererAvatarUrl = answererAvatarUrl; }

    public String getAnswererUrl() { return answererUrl; }

    public void setAnswererUrl(String answererUrl) { this.answererUrl = answererUrl; }

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
