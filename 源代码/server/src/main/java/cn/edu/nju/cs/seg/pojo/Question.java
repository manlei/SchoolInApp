package cn.edu.nju.cs.seg.pojo;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by fwz on 2017/4/25.
 */
@Entity
@Table(name = "question")
public class Question implements Serializable {

    @Transient
    public static final int TYPE_TEXT = 0; //文本
    @Transient
    public static final int TYPE_AUDIO = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title = "";

    @Basic(fetch = FetchType.LAZY)
    @Type(type = "text")
    @Column(name = "content", nullable = false)
    private String content = "";

    private int heat = 0;
    private int type;
    private int supportsNumber = 0;
    private int answersNumber = 0;
    private long createdAt;


    @ManyToOne
    @JoinColumn(name = "asker_id", foreignKey = @ForeignKey(name = "ASKER_ID_FK"))
    private User user;      //提问者


    @ManyToOne
    @JoinColumn(name = "directed_to_id", foreignKey = @ForeignKey(name = "DIRECTED_TO_ID_FK"))
    private Studio studio;      //提问指定的工作室

    //favorite
    @ManyToMany(mappedBy = "questions")
    private List<User> users = new ArrayList<User>();

    //support
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportQuestions> supportQuestions = new ArrayList<>();



    public Question() {
        this.createdAt = System.currentTimeMillis();
    }

    public Question(String title, String content, User user, Studio studio, int type) {
        this.type = type;
        this.title = title == null ? "" : title;
        this.content = content == null ? "" : content;
        this.user = user;
        this.studio = studio;
        createdAt = System.currentTimeMillis();
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getSupportsNumber() {
        return supportsNumber;
    }

    public void setSupportsNumber(int supportsNumber) {
        this.supportsNumber = supportsNumber;
    }

    public int getAnswersNumber() {
        return answersNumber;
    }

    public void setAnswersNumber(int answers) {
        this.answersNumber = answers;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<SupportQuestions> getSupportQuestions() {
        return supportQuestions;
    }

    public void setSupportQuestions(List<SupportQuestions> supportQuestions) {
        this.supportQuestions = supportQuestions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return id == question.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

