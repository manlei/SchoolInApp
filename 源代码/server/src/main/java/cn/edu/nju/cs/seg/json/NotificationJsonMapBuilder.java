package cn.edu.nju.cs.seg.json;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.pojo.Comment;
import cn.edu.nju.cs.seg.pojo.Notification;
import cn.edu.nju.cs.seg.pojo.Question;

import java.util.Map;

/**
 * Created by fwz on 2017/7/9.
 */
public class NotificationJsonMapBuilder implements JsonMapResponseBuilder {

    private Notification notification;

    public NotificationJsonMapBuilder(Notification notification) {
        this.notification = notification;
    }

    @Override
    public Map<String, Object> getSimpleMap() {
        Map<String, Object> map;
        if (notification.getType() == Notification.NEW_ANSWER) {
            map = new JsonMapBuilder()
                    .append("id", notification.getId())
                    .append("type", "answered")
                    .append("has_read", notification.getIsRead() == 1)
                    .append("answerer_name", notification.getAnswer().getAnswerer().getUsername())
                    .append("question_title", notification.getAnswer().getQuestion().getTitle())
                    .append("answerer_url", ServerConfig.SERVER_BASE_URL
                            + "/users/" + notification.getAnswer().getAnswerer().getId())
                    .append("question_url", ServerConfig.SERVER_BASE_URL
                            + "/questions/" + notification.getAnswer().getQuestion().getId())
                    .getMap();

        } else if (notification.getType() == Notification.NEW_QUESTION) {
            Question question = notification.getQuestion();
            map = new JsonMapBuilder()
                    .append("id", notification.getId())
                    .append("question_title", question.getTitle())
                    .append("asker_name", question.getUser().getUsername())
                    .append("asker_url", ServerConfig.SERVER_BASE_URL
                            + "/users/" + question.getUser().getId())
                    .append("type", "invited")
                    .append("has_read", notification.getIsRead() == 1)
                    .append("question_url", ServerConfig.SERVER_BASE_URL
                            + "/questions/" + question.getId())
                    .append("answers_url", ServerConfig.SERVER_BASE_URL
                            + "/questions/" + question.getId() + "/answers")
                    .getMap();
        } else {
            Comment comment = notification.getComment();
            String type, url;
            if (comment.getTarget() == Comment.COMMENT_FOR_ESSAY) {
                type = "essay";
                url = ServerConfig.SERVER_BASE_URL + "/essays/" + comment.getEssay().getId();
            } else {
                type = "answer";
                url = ServerConfig.SERVER_BASE_URL + "/answers/" + comment.getAnswer().getId();
            }
            map = new JsonMapBuilder()
                    .append("id", notification.getId())
                    .append("type", "commented")
                    .append("has_read", notification.getIsRead() == 1)
                    .append("parent_type", type)
                    .append("parent_url", url)
                    .getMap();
        }
        return map;
    }

    @Override
    public Map<String, Object> getComplexMap() {
        return getSimpleMap();
    }
}
