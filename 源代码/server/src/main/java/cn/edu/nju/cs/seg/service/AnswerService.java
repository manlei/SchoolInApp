package cn.edu.nju.cs.seg.service;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.ServerContext;
import cn.edu.nju.cs.seg.dao.AnswerDaoImpl;
import cn.edu.nju.cs.seg.pojo.Answer;
import cn.edu.nju.cs.seg.pojo.Notification;
import cn.edu.nju.cs.seg.util.JPushUtil;
import cn.jpush.api.push.model.PushPayload;

import java.util.List;

/**
 * Created by fwz on 2017/5/1.
 */
public class AnswerService {
    private static AnswerDaoImpl dao = new AnswerDaoImpl();



    public static boolean remove(int id) {
        return dao.remove(id);
    }

    public static List<Answer> findAllAnswers() {
        return dao.findAll();
    }


    public static int add(Answer answer) {
        Answer a = dao.add(answer);
        if (a == null) {
            return 0;
        }
        if (ServerConfig.NOTIFICATION) {
            Notification notification = new Notification(
                    answer.getQuestion().getUser(), answer);
            NotificationService.add(notification);
            if (ServerContext.logins.containsKey(
                    answer.getQuestion().getUser().getId() + "")) {
                PushPayload payload = JPushUtil.buildPushObject_android_alias_msg(
                        answer.getQuestion().getUser().getId() + "");
                JPushUtil.sendPush(payload);
            }
        }
        return a.getId();
    }

    public static Answer findAnswerById(int answerId) {
        return dao.findAnswerById(answerId);
    }

    public static List<Answer> findAnswersByUserId(int userId) {
        return dao.findAnswersByUserId(userId);

    }

    public static List<Answer> findAnswersByStudioId(int studioId) {
        return dao.findAnswersByStudioId(studioId);
    }

    public static List<Answer> findAnswersByQuestionId(int questionId) {
        return dao.findAnswersByQuestionId(questionId);
    }




}
