package cn.edu.nju.cs.seg.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by fwz on 2017/5/23.
 */
@Entity
@Table(name = "supporter_essays")
public class SupportEssays implements Serializable {
    @Id
    @ManyToOne
    private User essaySupporter;

    @Id
    @ManyToOne
    private Essay essay;

    private long createdAt;

    public SupportEssays() {
    }

    public SupportEssays(User essaySupporter, Essay essay) {
        this.essaySupporter = essaySupporter;
        this.essay = essay;
        this.createdAt = System.currentTimeMillis();
    }

    public User getEssaySupporter() {
        return essaySupporter;
    }

    public void setEssaySupporter(User essaySupporter) {
        this.essaySupporter = essaySupporter;
    }

    public Essay getEssay() {
        return essay;
    }

    public void setEssay(Essay essay) {
        this.essay = essay;
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

        SupportEssays that = (SupportEssays) o;

        if (!essaySupporter.equals(that.essaySupporter)) return false;
        return essay.equals(that.essay);
    }

    @Override
    public int hashCode() {
        int result = essaySupporter.hashCode();
        result = 31 * result + essay.hashCode();
        return result;
    }
}
