package cn.edu.nju.cs.seg.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Clypso on 2017/6/16.
 */

@Entity
@Table(name = "notification")
public class Notification implements Serializable {

    @Transient
    public static final int NEW_QUESTION = 0; //推送给studio
    @Transient
    public static final int NEW_ANSWER = 1;
    @Transient
    public static final int NEW_COMMENT = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int type;
    private int isRead = 0;
    @ManyToOne
    @JoinColumn(name = "notify_target_id", foreignKey = @ForeignKey(name = "TARGET_ID_FK"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "notify_answered_id", foreignKey = @ForeignKey(name = "ANSWERED_ID_FK"))
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "notify_invited_id", foreignKey = @ForeignKey(name = "INVITED_ID_FK"))
    private Question question;

    @ManyToOne
    @JoinColumn(name = "notify_commented_id", foreignKey = @ForeignKey(name = "COMMENTED_ID_FK"))
    private Comment comment;


    public Notification() {
    }

    public Notification(User user, Question question) {
        this.type =  NEW_QUESTION;
        this.user = user;
        this.question = question;
    }

    public Notification(User user, Answer answer) {
        this.type =  NEW_ANSWER;
        this.user = user;
        this.answer = answer;

    }

    public Notification(User user, Comment comment) {
        this.type =  NEW_COMMENT;
        this.user = user;
        this.comment = comment;

    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
