package cn.edu.nju.cs.seg.json;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.pojo.Studio;

import java.util.Map;

/**
 * Created by fwz on 2017/5/31.
 */
public class StudioJsonMapBuilder implements JsonMapResponseBuilder {

    private Studio studio;

    public StudioJsonMapBuilder(Studio studio) {
        this.studio = studio;
    }

    @Override
    public Map<String, Object> getSimpleMap() {
        Map<String, Object> map = new JsonMapBuilder()

                .append("id", studio.getId())
                .append("name", studio.getName())
                .append("manager", studio.getManager().getUsername())
                .append("members", studio.getMembersNumber())
                .append("questions", studio.getQuestionsNumber())
                .append("essays", studio.getEssaysNumber())
                .append("url", ServerConfig.SERVER_BASE_URL
                        + "/studios/" + studio.getId())
                .append("avatar_url", ServerConfig.AVATARS_BASE_URL
                        + studio.getAvatar())
                .append("bio", studio.getBio())
                .getMap();

        return map;
    }

    @Override
    public Map<String, Object> getComplexMap() {
        Map<String, Object> map = new JsonMapBuilder()
                .append("id", studio.getId())
                .append("members", studio.getMembersNumber())
                .append("questions", studio.getQuestionsNumber())
                .append("answers", studio.getAnswersNumber())
                .append("essays", studio.getEssaysNumber())
                .append("name", studio.getName())
                .append("url", ServerConfig.SERVER_BASE_URL + "/studios/" + studio.getId())
                .append("manager", studio.getManager().getUsername())
                .append("avatar_url", ServerConfig.AVATARS_BASE_URL
                        + studio.getAvatar())
                .append("manager_url", ServerConfig.SERVER_BASE_URL
                        + "/users/" + studio.getManager().getId())
                .append("manager_avatar_url", ServerConfig.SERVER_BASE_URL
                        + "/avatars/" + studio.getManager().getAvatar())
                .append("members_url", ServerConfig.SERVER_BASE_URL
                        + "/studios/" + studio.getId() + "/members")
                .append("questions_url", ServerConfig.SERVER_BASE_URL
                        + "/studios/" + studio.getId() + "/questions")//as above
                .append("essays_url", ServerConfig.SERVER_BASE_URL
                        + "/studios/" + studio.getId() + "/essays")//as above
                .append("top_question_url", ServerConfig.SERVER_BASE_URL
                        + "questions/" + studio.getTopQuestionId())
                .append("top_essay_url", ServerConfig.SERVER_BASE_URL
                        + "/questions" + studio.getTopEssayId())
                .append("bio", studio.getBio())
                .getMap();
        return map;
    }
}
