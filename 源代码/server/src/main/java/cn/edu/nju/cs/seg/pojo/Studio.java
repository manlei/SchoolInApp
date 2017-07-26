package cn.edu.nju.cs.seg.pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clypso on 2017/4/27.
 */

@Entity
@Table(name = "studio")
public class Studio  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name = "";
    private int membersNumber = 0;
    private int questionsNumber = 0;
    private int answersNumber = 0;
    private int avatar = 1;
    private int essaysNumber = 0;

    private String bio = "";
    private int topQuestionId;
    private int topEssayId;

    @ManyToOne
    @JoinColumn(name = "manager_id", foreignKey = @ForeignKey(name = "MANAGER_ID_FK"))
    private User manager = null;

    //studio members
    @ManyToMany(mappedBy = "studios")
    private List<User> users = new ArrayList<User>();

    public Studio() {

    }

    public Studio(String name, User manager) {
        this.name = name == null ? "" : name;

        this.manager = manager;
    }

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

    public int getMembersNumber() {
        return membersNumber;
    }

    public void setMembersNumber(int membersNumber) {
        this.membersNumber = membersNumber;
    }

    public int getQuestionsNumber() {
        return questionsNumber;
    }

    public void setQuestionsNumber(int questionsNumber) {
        this.questionsNumber = questionsNumber;
    }

    public int getAnswersNumber() {
        return answersNumber;
    }

    public void setAnswersNumber(int answersNumber) {
        this.answersNumber = answersNumber;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }


    public int getEssaysNumber() {
        return essaysNumber;
    }

    public void setEssaysNumber(int essaysNumber) {
        this.essaysNumber = essaysNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getTopQuestionId() {
        return topQuestionId;
    }

    public void setTopQuestionId(int topQuestionId) {
        this.topQuestionId = topQuestionId;
    }

    public int getTopEssayId() {
        return topEssayId;
    }

    public void setTopEssayId(int topEssayId) {
        this.topEssayId = topEssayId;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Studio studio = (Studio) o;

        return id == studio.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
