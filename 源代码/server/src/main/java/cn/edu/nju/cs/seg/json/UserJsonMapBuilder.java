package cn.edu.nju.cs.seg.json;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.pojo.User;

import java.util.Map;

/**
 * Created by fwz on 2017/5/30.
 */
public class UserJsonMapBuilder implements JsonMapResponseBuilder {

    private User user;

    public UserJsonMapBuilder(User user) {
        this.user = user;
    }

    @Override
    public Map<String, Object> getSimpleMap() {
        Map<String, Object> map = new JsonMapBuilder()
                .append("id", user.getId())
                .append("name", user.getUsername())
                .append("answers", user.getAnswersNumber())
                .append("url", ServerConfig.SERVER_BASE_URL
                        + "/users/" + user.getId())
                .append("avatar_url", ServerConfig.SERVER_BASE_URL
                        + "/avatars/" + user.getAvatar())
                .append("bio", user.getBio())
                .getMap();
        return map;
    }

    @Override
    public Map<String, Object> getComplexMap() {
        Map<String, Object> map = new JsonMapBuilder()
                .append("id", user.getId())
                .append("name", user.getUsername())
                .append("answers", user.getAnswersNumber())
                .append("questions", user.getQuestionsNumber())
                .append("favorite_answers", 0)
                .append("favorite_questions", 0)
                .append("favorite_essays", 0)
                .append("studios", 0)
                .append("url", ServerConfig.SERVER_BASE_URL
                        + "/users/" + user.getId())
                .append("avatar_url", ServerConfig.SERVER_BASE_URL
                        + "/avatars/" + user.getAvatar())
                .append("questions_url", ServerConfig.SERVER_BASE_URL
                        + "/users/" + user.getId() + "/questions")
                .append("answers_url", ServerConfig.SERVER_BASE_URL
                        + "/users/" + user.getId() + "/answers")
                .append("favorite_questions_url", ServerConfig.SERVER_BASE_URL
                        + "/users/" + user.getId() + "/favorites/questions")
                .append("favorite_answers_url", ServerConfig.SERVER_BASE_URL
                        + "/users/" + user.getId() + "/favorites/answers")
                .append("favorite_essays_url", ServerConfig.SERVER_BASE_URL
                        + "/users/" + user.getId() + "/favorites/essays")
                .append("studios_url", ServerConfig.SERVER_BASE_URL
                        + "/users/" + user.getId() + "/studios")
                .append("email", user.getEmail())
                .append("phone", user.getPhone())
                .append("bio", user.getBio())
                .append("sex", user.getSex())
                .append("age", user.getAge())
                .append("department", user.getDepartment())
                .append("location", user.getLocation())
                .getMap();
        return map;
    }
}
