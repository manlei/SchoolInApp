package cn.edu.nju.cs.seg.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by fwz on 2017/7/8.
 */

@Entity
@Table(name = "avatar")
public class Avatar implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String md5;

    private String suffix;

    public Avatar() {
    }

    public Avatar(String md5, String suffix) {
        this.md5 = md5;
        this.suffix = suffix;
    }

    public long getId() {
        return id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Avatar avatar = (Avatar) o;

        return id == avatar.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
