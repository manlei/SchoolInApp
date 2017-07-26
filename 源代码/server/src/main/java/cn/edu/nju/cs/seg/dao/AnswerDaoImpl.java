package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Answer;
import cn.edu.nju.cs.seg.pojo.Comment;
import cn.edu.nju.cs.seg.pojo.Notification;
import cn.edu.nju.cs.seg.service.CommentService;
import cn.edu.nju.cs.seg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by fwz on 2017/5/1.
 */
public class AnswerDaoImpl implements AnswerDao {

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public boolean remove(int id) {
        boolean flag = false;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Answer answer = (Answer) session.createQuery(
                "from Answer where id = " + id )
                .uniqueResult();
        if (answer != null) {
            List<Comment> comments = session.createQuery(
                    "from Comment where answer.id = " + id)
                    .list();
            for (Comment comment : comments) {
                CommentService.remove(comment.getId());
//                session.remove(comment);
            }
            List<Notification> notifications = session.createQuery(
                    "from Notification where answer.id = " + id)
                    .list();
            for (Notification notification : notifications) {
                session.remove(notification);
            }

            answer.getQuestion().setAnswersNumber(answer.getQuestion().getAnswersNumber() - 1);
            answer.getAnswerer().setAnswersNumber(answer.getAnswerer().getAnswersNumber() - 1);
            answer.getQuestion().getStudio().setAnswersNumber(
                    answer.getQuestion().getStudio().getAnswersNumber() - 1);
            session.update(answer.getQuestion().getStudio());
            session.update(answer.getQuestion());
            session.update(answer.getAnswerer());
            session.remove(answer);
            flag = true;
        }
        session.getTransaction().commit();
        session.close();
        return flag;
    }

    @Override
    public Answer add(Answer answer) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        answer.getQuestion().setAnswersNumber(answer.getQuestion().getAnswersNumber() + 1);
        answer.getAnswerer().setAnswersNumber(answer.getAnswerer().getAnswersNumber() + 1);
        answer.getQuestion().getStudio().setAnswersNumber(
                answer.getQuestion().getStudio().getAnswersNumber() + 1);
        session.update(answer.getQuestion().getStudio());
        session.update(answer.getQuestion());
        session.update(answer.getAnswerer());

        session.persist(answer);

        session.getTransaction().commit();
        session.close();
        return answer;

    }

    @Override
    public Answer findAnswerById(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Answer result = (Answer) session.createQuery( "from Answer where id = " + id )
                .uniqueResult();
        session.getTransaction().commit();

        session.close();
        return result;
    }

    @Override
    public List<Answer> findAll() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery( "from Answer" ).list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public List<Answer> findAnswersByQuestionId(int questionId) {
        List<Answer> answers = null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        answers = session.createQuery(
                "select a from Answer a where a.question.id = " + questionId)
                .list();

        session.getTransaction().commit();
        session.close();
        return answers;
    }

    @Override
    public List<Answer> findAnswersByStudioId(int studioId) {
        List<Answer> answers = null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        answers = session.createQuery("select a from Answer a where a.studio.id = " + studioId).list();

        session.getTransaction().commit();
        session.close();
        return answers;
    }

    @Override
    public List<Answer> findAnswersByUserId(int userId) {
        List<Answer> answers = null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        answers = session.createQuery(
                "select a from Answer a where a.answerer.id = " + userId)
                .list();

        session.getTransaction().commit();
        session.close();
        return answers;
    }



}
