package cn.edu.nju.cs.seg.json;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.pojo.Comment;

import java.util.Map;

/**
 * Created by fwz on 2017/5/31.
 */
public class CommentJsonMapBuilder implements JsonMapResponseBuilder {

    private Comment comment;

    public CommentJsonMapBuilder(Comment comment) {
        this.comment = comment;
    }

    @Override
    public Map<String, Object> getSimpleMap() {
        Map<String, Object> map = new JsonMapBuilder()
                .append("id", comment.getId())
                .append("commenter", comment.getUser().getUsername())
                .append("content", comment.getContent())
                .append("url", ServerConfig.SERVER_BASE_URL
                        + "/comments/" + comment.getId())
                .append("commenter_url", ServerConfig.SERVER_BASE_URL
                        + "/users/" + comment.getUser().getId())
                .append("commenter_avatar_url", ServerConfig.AVATARS_BASE_URL
                         + comment.getUser().getAvatar())
                .append("created_at", comment.getCreatedAt())
                .getMap();
        return map;
    }

    @Override
    public Map<String, Object> getComplexMap() {
        Map<String, Object> map = getSimpleMap();
        if (comment.getTarget() == Comment.COMMENT_FOR_ANSWER) //answer
            map.put("parent_url", ServerConfig.SERVER_BASE_URL
                    + "/answers/" + comment.getAnswer().getId());
        else //essay
            map.put("parent_url", ServerConfig.SERVER_BASE_URL
                    + "/essays/" + comment.getEssay().getId());
        return map;
    }
}
