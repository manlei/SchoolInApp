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
@Table(name = "answer")
public class Answer implements Serializable {

    @Transient
    public static final int TYPE_TEXT = 0; //文本
    @Transient
    public static final int TYPE_AUDIO = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int type;
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "text")
    @Column(name = "content", nullable = false)
    private String content = "";
    private int commentsNumber = 0;
    private long createdAt;


    @ManyToOne
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "QUESTION_ID_FK"))
    private Question question;

    //answer author
    @ManyToOne
    @JoinColumn(name = "answerer_id", foreignKey = @ForeignKey(name = "ANSWERER_ID_FK"))
    private User answerer;

//    @ManyToOne
//    @JoinColumn(name = "directed_to_id", foreignKey = @ForeignKey(name = "DIRECTED_ID_FK"))
//    private Studio studio;

    //favorite
    @ManyToMany(mappedBy = "answers")
    private List<User> users = new ArrayList<User>();





    public Answer() {
        this.createdAt = System.currentTimeMillis();

    }

    public Answer(String content, User answerer, Question question, int type) {
        this.type = type;
        this.content = content == null ? "" : content;
        this.question = question;
        this.answerer = answerer;
        this.createdAt = System.currentTimeMillis();

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(int commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getAnswerer() {
        return answerer;
    }

    public void setAnswerer(User answerer) {
        this.answerer = answerer;
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

        Answer answer = (Answer) o;

        return id == answer.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
