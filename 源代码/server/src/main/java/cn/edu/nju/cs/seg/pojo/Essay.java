package cn.edu.nju.cs.seg.pojo;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clypso on 2017/5/5.
 */

@Entity
@Table(name = "essay")
public class Essay implements Serializable {

    @Transient
    public static final int TYPE_TEXT = 0; //文本
    @Transient
    public static final int TYPE_AUDIO = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title = "";
    private int type;
    private int heat = 0;
    private int supportsNumber = 0;
    private int commentsNumber = 0;
    private long createdAt;

    @Basic(fetch = FetchType.LAZY)
    @Type(type = "text")
    @Column(name = "content", nullable = false)
    private String content = "";



    @ManyToOne
    @JoinColumn(name = "author_studio_id", foreignKey = @ForeignKey(name = "AUTHOR_STUDIO_ID_FK"))
    private Studio studio;

    //favorite
    @ManyToMany(mappedBy = "essays")
    private List<User> users = new ArrayList<User>();

    //support
    @OneToMany(mappedBy = "essay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportEssays> supportEssays = new ArrayList<>();


    public Essay(){
        createdAt = System.currentTimeMillis();
    }

    public Essay(String title, String content, Studio studio, int type) {
        this.type = type;
        createdAt = System.currentTimeMillis();
        this.title = title;
        this.studio = studio;
        this.content = content == null ? "" : content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

        Essay essay = (Essay) o;

        return id == essay.id;
    }

    @Override
    public int hashCode() {
        return id;

    }
}
