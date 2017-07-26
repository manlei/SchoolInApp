package cn.edu.nju.cs.seg.service;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.ServerContext;
import cn.edu.nju.cs.seg.dao.QuestionDaoImpl;
import cn.edu.nju.cs.seg.pojo.Notification;
import cn.edu.nju.cs.seg.pojo.Question;
import cn.edu.nju.cs.seg.pojo.Studio;
import cn.edu.nju.cs.seg.pojo.User;
import cn.edu.nju.cs.seg.util.JPushUtil;
import cn.edu.nju.cs.seg.util.LuceneIndexUtil;
import cn.jpush.api.push.model.PushPayload;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;
import java.util.List;

/**
 * Created by fwz on 2017/5/1.
 */
public class QuestionService {
    private static QuestionDaoImpl dao = new QuestionDaoImpl();


    public static boolean remove(int id) {
        Question question = QuestionService.findQuestionById(id);
        IndexWriter indexWriter = null;
        try {
            indexWriter = LuceneIndexUtil.getIndexWriter();
            LuceneIndexUtil.deleteQuestion(indexWriter, question);
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dao.remove(id);
    }

    public static List<Question> findAllQuestions() {
        return dao.findAll();
    }


    public static int add(Question question) {
        Question q = dao.add(question);
        if (q != null) {

            IndexWriter indexWriter = null;
            try {
                indexWriter = LuceneIndexUtil.getIndexWriter();
                LuceneIndexUtil.addQuestion(indexWriter, q);
                indexWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (ServerConfig.NOTIFICATION) {

                Studio studio = q.getStudio();
                List<User> members = StudioService.findUsersByStudioId(studio.getId());
                for (User u : members) {
                    Notification notification = new Notification(
                            u, q);
                    NotificationService.add(notification);
                    if (ServerContext.logins.containsKey(u.getId() + "")) {
                        PushPayload payload = JPushUtil
                                .buildPushObject_android_alias_msg(u.getId() + "");
                        JPushUtil.sendPush(payload);
                    }
                }
            }

            return q.getId();
        }
        return 0;
    }

    public static Question findQuestionByTitle(String title) {
        return dao.findQuestionByTitle(title);
    }

    public static Question findQuestionById(int questionId) {
        return dao.findQuestionById(questionId);
    }

    public static List<Question> findQuestionsByUserId(int userId) {

        return dao.findQuestionsByUserId(userId);

    }

    public static List<Question> findQuestionsByStudioId(int studioId) {
        return dao.findQuestionsByStudioId(studioId);
    }

    public static int incrementQuestionHeat(int questionId) {
        return dao.incrementQuestionHeat(questionId);
    }

}

