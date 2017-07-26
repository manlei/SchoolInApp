package cn.edu.nju.cs.seg.schooledinapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchUserItem implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("answers")
    private int answers;

    @SerializedName("questions")
    private int questions;

    @SerializedName("favorite_questions")
    private int favoriteQuestions;

    @SerializedName("favorite_answers")
    private int favoriteAnswers;

    @SerializedName("favorite_essays")
    private int favoriteEssays;

    @SerializedName("studios")
    private int studios;

    @SerializedName("url")
    private String url;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("questions_url")
    private String questionsUrl;

    @SerializedName("answers_url")
    private String answersUrl;

    @SerializedName("favorite_questions_url")
    private String favoriteQuestionsUrl;

    @SerializedName("favorite_answers_url")
    private String favoriteAnswersUrl;

    @SerializedName("favorite_essays_url")
    private String favoriteEssaysUrl;

    @SerializedName("studios_url")
    private String studiosUrl;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("bio")
    private String bio;

    @SerializedName("sex")
    private String sex;

    @SerializedName("age")
    private int age;

    @SerializedName("department")
    private String department;

    @SerializedName("location")
    private String location;

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

    public int getAnswers() {
        return answers;
    }

    public void setAnswers(int answers) {
        this.answers = answers;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getFavoriteAnswers() {
        return favoriteAnswers;
    }

    public void setFavoriteAnswers(int favoriteAnswers) {
        this.favoriteAnswers = favoriteAnswers;
    }

    public int getFavoriteQuestions() {
        return favoriteQuestions;
    }

    public void setFavoriteQuestions(int favoriteQuestions) {
        this.favoriteQuestions = favoriteQuestions;
    }

    public int getFavoriteEssays() {
        return favoriteEssays;
    }

    public void setFavoriteEssays(int favoriteEssays) {
        this.favoriteEssays = favoriteEssays;
    }

    public int getStudios() {
        return studios;
    }

    public void setStudios(int studios) {
        this.studios = studios;
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

    public String getQuestionsUrl() {
        return questionsUrl;
    }

    public void setQuestionsUrl(String questionsUrl) {
        this.questionsUrl = questionsUrl;
    }

    public String getAnswersUrl() {
        return answersUrl;
    }

    public void setAnswersUrl(String answersUrl) {
        this.answersUrl = answersUrl;
    }

    public String getFavoriteQuestionsUrl() {
        return favoriteQuestionsUrl;
    }

    public void setFavoriteQuestionsUrl(String favoriteQuestionsUrl) {
        this.favoriteQuestionsUrl = favoriteQuestionsUrl;
    }

    public String getFavoriteAnswersUrl() {
        return favoriteAnswersUrl;
    }

    public void setFavoriteAnswersUrl(String favoriteAnswersUrl) {
        this.favoriteAnswersUrl = favoriteAnswersUrl;
    }

    public String getFavoriteEssaysUrl() {
        return favoriteEssaysUrl;
    }

    public void setFavoriteEssaysUrl(String favoriteEssaysUrl) {
        this.favoriteEssaysUrl = favoriteEssaysUrl;
    }

    public String getStudiosUrl() {
        return studiosUrl;
    }

    public void setStudiosUrl(String studiosUrl) {
        this.studiosUrl = studiosUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
