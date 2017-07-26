package cn.edu.nju.cs.seg.pojo;

import javax.persistence.*;
//<<<<<<< HEAD
//=======
import java.io.Serializable;
//>>>>>>> dev_fwz
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fwz on 2017/4/22.
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username = "";
    private int avatar = 1;
    private String email = "";
    private String phone = "";
    private String bio = "";
    private String sex = "unknown";
    private int age = 0;
    private String department = "";
    private String location = "";
    private String password = "";

    private int answersNumber = 0;
    private int questionsNumber = 0;

    //studios that user join in
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Studio> studios = new ArrayList<Studio>();


    //favorite questions
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Question> questions = new ArrayList<Question>();

    //favorite answers
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Answer> answers = new ArrayList<Answer>();

    //favorite essays
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Essay> essays = new ArrayList<Essay>();

    @OneToMany(mappedBy = "questionSupporter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportQuestions> supportQuestions = new ArrayList<>();


    @OneToMany(mappedBy = "essaySupporter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportEssays> supportEssays = new ArrayList<>();



    public User() {
    }


    public User(String email, String password) {
        this.email = email == null ? "" : email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAnswersNumber() {
        return answersNumber;
    }

    public void setAnswersNumber(int answersNumber) {
        this.answersNumber = answersNumber;
    }

    public int getQuestionsNumber() {
        return questionsNumber;
    }

    public void setQuestionsNumber(int questionsNumber) {
        this.questionsNumber = questionsNumber;
    }

    public List<Studio> getStudios() {
        return studios;
    }

    public void setStudios(List<Studio> studios) {
        this.studios = studios;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Essay> getEssays() {
        return essays;
    }

    public void setEssays(List<Essay> essays) {
        this.essays = essays;
    }

    public List<SupportQuestions> getSupportQuestions() {
        return supportQuestions;
    }

    public void setSupportQuestions(List<SupportQuestions> supportQuestions) {
        this.supportQuestions = supportQuestions;
    }

    public List<SupportEssays> getSupportEssays() {
        return supportEssays;
    }

    public void setSupportEssays(List<SupportEssays> supportEssays) {
        this.supportEssays = supportEssays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!email.equals(user.email)) return false;
        return phone != null ? phone.equals(user.phone) : user.phone == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + email.hashCode();
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }
}
