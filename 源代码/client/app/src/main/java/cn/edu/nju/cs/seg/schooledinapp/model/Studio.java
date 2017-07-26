package cn.edu.nju.cs.seg.schooledinapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Studio implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("manager")
    private String manager;

    @SerializedName("members")
    private int members;

    @SerializedName("questions")
    private int questions;

    @SerializedName("essays")
    private int essays;

    @SerializedName("url")
    private String url;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("manager_url")
    private String managerUrl;

    @SerializedName("manager_avatar_url")
    private String managerAvatarUrl;

    @SerializedName("members_url")
    private String membersUrl;

    @SerializedName("questions_url")
    private String questionsUrl;

    @SerializedName("essays_url")
    private String essaysUrl;

    @SerializedName("top_question_url")
    private String topQuestionUrl;

    @SerializedName("top_essay_url")
    private String topEssayUrl;

    @SerializedName("bio")
    private String bio;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getEssays() {
        return essays;
    }

    public void setEssays(int essays) {
        this.essays = essays;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getManagerUrl() {
        return managerUrl;
    }

    public void setManagerUrl(String managerUrl) {
        this.managerUrl = managerUrl;
    }

    public String getManagerAvatarUrl() {
        return managerAvatarUrl;
    }

    public void setManagerAvatarUrl(String managerAvatarUrl) {
        this.managerAvatarUrl = managerAvatarUrl;
    }

    public String getMembersUrl() {
        return membersUrl;
    }

    public void setMembersUrl(String membersUrl) {
        this.membersUrl = membersUrl;
    }

    public String getQuestionsUrl() {
        return questionsUrl;
    }

    public void setQuestionsUrl(String questionsUrl) {
        this.questionsUrl = questionsUrl;
    }

    public String getEssaysUrl() {
        return essaysUrl;
    }

    public void setEssaysUrl(String essaysUrl) {
        this.essaysUrl = essaysUrl;
    }

    public String getTopQuestionUrl() {
        return topQuestionUrl;
    }

    public void setTopQuestionUrl(String topQuestionUrl) {
        this.topQuestionUrl = topQuestionUrl;
    }

    public String getTopEssayUrl() {
        return topEssayUrl;
    }

    public void setTopEssayUrl(String topEssayUrl) {
        this.topEssayUrl = topEssayUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
