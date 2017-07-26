package cn.edu.nju.cs.seg.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by fwz on 2017/5/18.
 */
@Entity
@Table(name = "supporter_questions")
public class SupportQuestions implements Serializable {
    @Id
    @ManyToOne
    private User questionSupporter;

    @Id
    @ManyToOne
    private Question question;

    private long createdAt;

    public SupportQuestions() {
        createdAt = System.currentTimeMillis();
    }

    public SupportQuestions(User user, Question question) {
        this.questionSupporter = questionSupporter;
        this.question = question;
        createdAt = System.currentTimeMillis();
    }


    public User getQuestionSupporter() {
        return questionSupporter;
    }

    public void setQuestionSupporter(User questionSupporter) {
        this.questionSupporter = questionSupporter;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupportQuestions support = (SupportQuestions) o;

        if (!questionSupporter.equals(support.questionSupporter)) return false;
        return question.equals(support.question);
    }

    @Override
    public int hashCode() {
        int result = questionSupporter.hashCode();
        result = 31 * result + question.hashCode();
        return result;
    }
}
