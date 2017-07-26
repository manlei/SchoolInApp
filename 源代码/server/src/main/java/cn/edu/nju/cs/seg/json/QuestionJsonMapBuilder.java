package cn.edu.nju.cs.seg.json;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.pojo.Question;

import java.util.Map;

/**
 * Created by fwz on 2017/5/30.
 */
public class QuestionJsonMapBuilder implements JsonMapResponseBuilder {
    private Question question;


    public QuestionJsonMapBuilder(Question question) {
        this.question = question;
    }

    @Override
    public Map<String, Object> getSimpleMap() {
        Map<String, Object> map = new JsonMapBuilder()
                .append("id", question.getId())
                .append("type", question.getType() == Question.TYPE_TEXT ? "text" : "audio")
                .append("title", question.getTitle())
                .append("heat", question.getHeat())
                .append("supports", question.getSupportsNumber())
                .append("answers", question.getAnswersNumber())
                .append("directed_to", question.getStudio().getName())
                .append("directed_to_url", ServerConfig.SERVER_BASE_URL
                        + "/studios/" + question.getStudio().getId())
                .append("url", ServerConfig.SERVER_BASE_URL
                        + "/questions/" + question.getId())
                .append("answers_url", ServerConfig.SERVER_BASE_URL
                        + "/questions/" + question.getId() + "/answers")
                .append("created_at", question.getCreatedAt())
                .getMap();
        return map;
    }

    @Override
    public Map<String, Object> getComplexMap() {
        Map<String, Object> map = getSimpleMap();
        map.put(question.getType() == Question.TYPE_TEXT ? "content" : "audio_url",
                question.getContent());
        return map;
    }
}
