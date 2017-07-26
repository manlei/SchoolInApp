package cn.edu.nju.cs.seg.pojo;

import javax.persistence.*;

/**
 * Created by Clypso on 2017/5/4.
 */

@Entity
@Table(name = "comment")
public class Comment {

    @Transient
    public static final int COMMENT_FOR_ANSWER = 0;

    @Transient
    public static final int COMMENT_FOR_ESSAY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private long createdAt;
    private String content = "";
    private int target;//0: studio 1: question 2: answer 3: essay

    //comment author
    @ManyToOne
    @JoinColumn(name = "commenter_id", foreignKey = @ForeignKey(name = "COMMENTER_ID_FK"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_answer_id", foreignKey = @ForeignKey(name = "COMMENT_ANSWER_ID_FK"))
    private Answer answer;


    @ManyToOne
    @JoinColumn(name = "comment_essay_id", foreignKey = @ForeignKey(name = "COMMENT_ESSAY_ID_FK"))
    private Essay essay;


    public Comment() {
        createdAt = System.currentTimeMillis();
    }

    public Comment(User user, String content, Answer answer) {
        this.user = user;
        this.content = content == null ? "" : content;
        this.answer = answer;
        this.target = COMMENT_FOR_ANSWER;
        this.createdAt = System.currentTimeMillis();
    }

    public Comment(User user, String content, Essay essay) {
        this.user = user;
        this.content = content == null ? "" : content;
        this.essay = essay;
        target = COMMENT_FOR_ESSAY;
        createdAt = System.currentTimeMillis();

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }


    public Essay getEssay() {
        return essay;
    }

    public void setEssay(Essay essay) {
        this.essay = essay;
    }


}
