package cn.edu.nju.cs.seg.schooledinapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OnlineUser extends User {

    @SerializedName("alias")
    private String alias;

    @SerializedName("tags")
    private List<String> tags;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}
