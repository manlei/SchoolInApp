package cn.edu.nju.cs.seg.json;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.pojo.Essay;

import java.util.Map;

/**
 * Created by fwz on 2017/5/30.
 */
public class EssayJsonMapBuilder implements JsonMapResponseBuilder {
    private Essay essay;

    public EssayJsonMapBuilder(Essay essay) {
        this.essay = essay;
    }

    @Override
    public Map<String, Object> getSimpleMap() {
        Map<String, Object> map = new JsonMapBuilder()
                .append("id", essay.getId())
                .append("title", essay.getTitle())
                .append("type", essay.getType() == Essay.TYPE_TEXT ? "text" : "audio")
                .append("heat", essay.getHeat())
                .append("supports", essay.getSupportsNumber())
                .append("comments", essay.getCommentsNumber())
                .append("studio", essay.getStudio().getName())
                .append("url", ServerConfig.SERVER_BASE_URL
                        + "/essays/" + essay.getId())
                .append("studio_url", ServerConfig.SERVER_BASE_URL
                        + "/studios/" + essay.getStudio().getId())
                .append("comments_url", ServerConfig.SERVER_BASE_URL
                        + "/essays/" + essay.getId() + "/comments")
                .append("created_at", essay.getCreatedAt())
                .getMap();

        return map;

    }

    @Override
    public Map<String, Object> getComplexMap() {
        Map<String, Object> map = new JsonMapBuilder()
                .append("id", essay.getId())
                .append("type", essay.getType() == Essay.TYPE_TEXT ? "text" : "audio")
                .append("title", essay.getTitle())
                .append(essay.getType() == Essay.TYPE_TEXT ? "content" : "audio_url",
                        essay.getContent())
                .append("heat", essay.getHeat())
                .append("supports", essay.getSupportsNumber())
                .append("comments", essay.getCommentsNumber())
                .append("studio", essay.getStudio().getName())
                .append("studio_bio", essay.getStudio().getBio())
                .append("url", ServerConfig.SERVER_BASE_URL
                        + "/essays/" + essay.getId())
                .append("studio_url", ServerConfig.SERVER_BASE_URL
                        + "/studios/" + essay.getStudio().getId())
                .append("studio_avatar_url", ServerConfig.SERVER_BASE_URL
                        + "/avatars/" + essay.getStudio().getAvatar())
                .append("comments_url", ServerConfig.SERVER_BASE_URL
                        + "/essays/" + essay.getId() + "/comments")
                .append("created_at", essay.getCreatedAt())
                .getMap();
        return map;
    }
}
