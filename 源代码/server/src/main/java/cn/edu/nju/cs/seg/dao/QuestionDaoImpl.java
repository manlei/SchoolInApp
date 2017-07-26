package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.*;
import cn.edu.nju.cs.seg.service.AnswerService;
import cn.edu.nju.cs.seg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by fwz on 2017/5/1.
 */
public class QuestionDaoImpl implements QuestionDao {

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    

    @Override
    public boolean remove(int id) {
        boolean flag = false;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Question question = (Question) session.createQuery("from Question where id = " + id)
                .uniqueResult();
        if (question != null) {
            List<Answer> answers = session.createQuery(
                    "from Answer where question.id = " + id)
                    .list();
            for (Answer answer : answers) {
                AnswerService.remove(answer.getId());
            }
            List<SupportQuestions> supportQuestions = session.createQuery(
                    "from SupportQuestions where question.id = " + id)
                    .list();
            for (SupportQuestions sq : supportQuestions) {
                session.remove(sq);
            }
            List<Notification> notifications = session.createQuery(
                    "from Notification where question.id = " + id)
                    .list();
            for (Notification notification : notifications) {
                session.remove(notification);
            }
            question.getUser().setQuestionsNumber(question.getUser().getQuestionsNumber() - 1);
            question.getStudio().setQuestionsNumber(question.getStudio().getQuestionsNumber() - 1);
            session.update(question.getStudio());
            session.update(question.getUser());
            session.delete(question);
            flag = true;
        }
        session.getTransaction().commit();
        session.close();
        return flag;
    }

    @Override
    public Question add(Question question) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        question.getUser().setQuestionsNumber(question.getUser().getQuestionsNumber() + 1);
        question.getStudio().setQuestionsNumber(question.getStudio().getQuestionsNumber() + 1);
        session.update(question.getStudio());
        session.update(question.getUser());
        session.persist(question);
        //session.flush();
        session.getTransaction().commit();
        session.close();
        return question;
    }

    @Override
    public Question findQuestionById(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Question result = (Question) session.createQuery(
                "from Question where id = " + id)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public List<Question> findAll() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Question").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Question findQuestionByTitle(String title) {
        Question q = null;

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        q = (Question) session.createQuery(
                "from Question where title = " + "\'" + title + "\'")
                .uniqueResult();

        session.getTransaction().commit();
        session.close();

        return q;
    }

    @Override
    public List<Question> findQuestionsByUserId(int userId) {

        List<Question> questions = null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        questions = session.createQuery(
                "select q from Question q where q.user.id = " + userId)
                .list();

        session.getTransaction().commit();
        session.close();
        return questions;
    }

    @Override
    public List<Question> findQuestionsByStudioId(int studioId) {
        List<Question> questions = null;
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        questions = session.createQuery(
                "select q from Question q where q.studio.id = " + studioId)
                .list();

        session.getTransaction().commit();
        session.close();
        return questions;
    }

    @Override
    public int incrementQuestionHeat(int questionId) {

        int heat = 0;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Question question = (Question) session.createQuery("from Question where id = " + questionId).uniqueResult();


        if (question != null) {
            heat = question.getHeat() + 1;
            question.setHeat(heat);
            session.update(question);
            //session.flush();
        } else {
            heat = -1;
        }
        session.getTransaction().commit();

        session.close();
        return heat;
    }

}
