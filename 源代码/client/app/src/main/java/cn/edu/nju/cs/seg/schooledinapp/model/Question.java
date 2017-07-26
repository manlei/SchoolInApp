package cn.edu.nju.cs.seg.schooledinapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Created by luping on 2017/5/11.
 */

public class Question implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("heat")
    private int heat;

    @SerializedName("supports")
    private int supports;

    @SerializedName("answers")
    private int answers;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("audio_url")
    private String audioUrl;

    @SerializedName("directed_to")
    private String directdeTo;

    @SerializedName("url")
    private String url;

    @SerializedName("answer_url")
    private String answerUrl;

    @SerializedName("directed_to_url")
    private String directedToUrl;

    @SerializedName("created_at")
    private long createdAt;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getHeat() { return heat; }

    public void setHeat(int heat) { this.heat = heat; }

    public int getSupports() { return supports; }

    public void setSupports(int supports) { this.supports = supports; }

    public int getAnswers() { return answers; }

    public void setAnswers(int answers) { this.answers = answers; }

    public String getTitle() { return  title; }

    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getDirectdeTo() { return directdeTo; }

    public void setDirectdeTo(String directdeTo) { this.directdeTo = directdeTo;}

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getAnswerUrl() { return answerUrl; }

    public void setAnswerUrl(String answerUrl) { this.answerUrl = answerUrl; }

    public String getDirectedToUrl() { return directedToUrl; }

    public void setDirectedToUrl(String directedToUrl) { this.directedToUrl = directedToUrl; }

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


