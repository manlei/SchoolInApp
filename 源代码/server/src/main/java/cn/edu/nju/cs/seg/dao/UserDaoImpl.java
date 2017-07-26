package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.*;
import cn.edu.nju.cs.seg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fwz on 2017/4/22.
 */
public class UserDaoImpl implements UserDao {

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public boolean remove(int id) {
        User user;
        boolean flag = false;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        user = (User) session.createQuery("from User where id = " + id).uniqueResult();
        if (user != null) {
            session.delete(user);
            flag = true;
        }
        session.getTransaction().commit();
        session.close();
        return flag;
    }

    @Override
    public User add(User user) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(user);
        //session.flush();

        session.getTransaction().commit();
        session.close();
        return user;
    }

    @Override
    public void addFavoriteQuestion(int userId, int questionId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery(
                "from User where id = " + userId)
                .uniqueResult();
        Question question = (Question) session.createQuery(
                "from Question where id = " + questionId)
                .uniqueResult();
        if (!user.getQuestions().contains(question)) {
            user.getQuestions().add(question);
            question.getUsers().add(user);
            session.update(user);
            session.update(question);
            //session.flush();
        }

        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void addFavoriteAnswer(int userId, int answerId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery(
                "from User where id = " + userId)
                .uniqueResult();
        Answer answer = (Answer) session.createQuery(
                "from Answer where id = " + answerId)
                .uniqueResult();

        if (!user.getAnswers().contains(answer)) {
            user.getAnswers().add(answer);
            answer.getUsers().add(user);
            session.update(user);
            session.update(answer);
            session.flush();
        }

        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void addFavoriteEssay(int userId, int essayId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery(
                "from User where id = " + userId)
                .uniqueResult();
        Essay essay = (Essay) session.createQuery(
                "from Essay where id = " + essayId)
                .uniqueResult();

        if (!user.getEssays().contains(essay)) {

            user.getEssays().add(essay);
            essay.getUsers().add(user);
            session.update(user);
            session.update(essay);
            session.flush();
        }

        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void addSupportQuestion(int userId, int questionId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery(
                "from User where id = " + userId)
                .uniqueResult();
        Question question = (Question) session.createQuery(
                "from Question where id = " + questionId)
                .uniqueResult();
        SupportQuestions existSq = (SupportQuestions) session.createQuery(
                "from SupportQuestions where questionSupporter.id = " + userId +
                        "and question.id = " + questionId)
                .uniqueResult();

        if (user != null && question != null && existSq == null) {
            SupportQuestions sq = new SupportQuestions(user, question);
            sq.setQuestion(question);
            sq.setQuestionSupporter(user);
            user.getSupportQuestions().add(sq);
            question.getSupportQuestions().add(sq);
            question.setSupportsNumber(question.getSupportsNumber() + 1);
            session.persist(sq);
            session.update(user);
            session.update(question);
        }


        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void addSupportEssay(int userId, int essayId) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery(
                "from User where id = " + userId)
                .uniqueResult();
        Essay essay = (Essay) session.createQuery(
                "from Essay where id = " + essayId)
                .uniqueResult();

        SupportEssays existSe = (SupportEssays) session.createQuery(
                "from SupportEssays where essaySupporter.id = " + userId +
                        "and essay.id = " + essayId)
                .uniqueResult();

        if (user != null && essay != null && existSe == null) {
            SupportEssays se = new SupportEssays(user, essay);
            se.setEssay(essay);
            se.setEssaySupporter(user);
            user.getSupportEssays().add(se);
            essay.getSupportEssays().add(se);
            essay.setSupportsNumber(essay.getSupportsNumber() + 1);
            session.persist(se);
            session.update(user);
            session.update(essay);
        }


        session.getTransaction().commit();
        session.close();
    }


    @Override
    public void updateUser(User user) {
        if (user != null) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            session.update(user);

            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public User findUserById(int id) {
        Session session = sessionFactory.openSession();
        User ret = null;
        session.beginTransaction();
        ret = (User) session.createQuery("from User where id = " + id).uniqueResult();
        session.getTransaction().commit();
        session.close();
        return ret;
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery("from User").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public User findUserByPhone(String phone) {
        if (phone != null) {

            User u = null;

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            u = (User) session.createQuery(
                    "from User where phone = " + "\'" + phone + "\'")
                    .uniqueResult();

            session.getTransaction().commit();
            session.close();

            return u;
        }
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        if (email != null) {
            User u = null;
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            u = (User) session.createQuery(
                    "from User where email = " + "\'" + email + "\'")
                    .uniqueResult();

            session.getTransaction().commit();
            session.close();
            return u;
        }
        return  null;
    }

    @Override
    public List<Studio> findStudiosByUserId(int userId) {
        List<Studio> studios;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery(
                "select u from User u left join fetch u.studios where u.id = "
                        + userId)
                .uniqueResult();

        studios = user.getStudios();
        session.getTransaction().commit();
        session.close();
        return studios;
    }

    @Override
    public List<Question> findFavoriteQuestionsByUserId(int userId) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery(
                "select u from User u left join fetch u.questions where u.id = " + userId)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return user.getQuestions();
    }

    @Override
    public List<Answer> findFavoriteAnswersByUserId(int userId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery("select u from User u left join fetch u.answers where u.id = " + userId).uniqueResult();

        session.getTransaction().commit();
        session.close();
        return user.getAnswers();
    }

    @Override
    public List<Essay> findFavoriteEssaysByUserId(int userId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery(
                "select u from User u left join fetch u.essays where u.id = " + userId)
                .uniqueResult();

        session.getTransaction().commit();
        session.close();
        return user.getEssays();
    }

    @Override
    public List<Question> findSupportQuestionsByUserId(int userId) {
        List<Question> questions = new ArrayList<Question>();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<SupportQuestions> sqs = session.createQuery(
                "from SupportQuestions s where s.questionSupporter.id = " + userId)
                .list();
        for (int i = sqs.size() - 1; i >= 0; i--) {
            questions.add(sqs.get(i).getQuestion());
        }

        session.getTransaction().commit();
        session.close();
        return questions;
    }

    @Override
    public List<Essay> findSupportEssaysByUserId(int userId) {
        List<Essay> essays = new ArrayList<Essay>();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<SupportEssays> ses = session.createQuery(
                "from SupportEssays s where s.essaySupporter.id = " + userId)
                .list();
        for (int i = ses.size() - 1; i >= 0; i--) {
            essays.add(ses.get(i).getEssay());
        }

        session.getTransaction().commit();
        session.close();
        return essays;
    }

    @Override
    public List<Object> findSupportsByUserId(int userId, int offset, int limit) {
        List<Object> supports = new ArrayList<Object>();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<SupportQuestions> sqs = session.createQuery(
                "from SupportQuestions s where s.questionSupporter.id = " + userId)
                .list();

        List<SupportEssays> ses = session.createQuery(
                "from SupportEssays s where s.essaySupporter.id = " + userId)
                .list();

        int max1 = sqs.size() - 1, max2 = ses.size() - 1;
        int i = 0, j = 0;
        while (i <= max1 && j <= max2 && i + j < offset) {
            if (sqs.get(max1 - i).getCreatedAt() > ses.get(max2 - j).getCreatedAt()) {
                i++;
            } else {
                j++;
            }
        }
        while (i > max1 && j <= max2 && i + j < offset) {
            j++;
        }
        while (j > max2 && i <= max1 && i + j < offset) {
            i++;
        }

        if (i <= max1 && j <= max2) { //questions and essay
            while (i <= max1 && j <= max2 && i + j < offset + limit) {
                if (sqs.get(max1 - i).getCreatedAt() > ses.get(max2 - j).getCreatedAt()) {
                    Question question = (Question) session.createQuery(
                            "from Question where id = "
                                    + sqs.get(max1 - i).getQuestion().getId())
                            .uniqueResult();
                    supports.add(question);
                    i++;
                } else {
                    Essay essay = (Essay) session.createQuery(
                            "from Essay where id = "
                                    + ses.get(max2 - j).getEssay().getId())
                            .uniqueResult();
                    supports.add(essay);
                    j++;
                }
            }
            while (i > max1 && j <= max2 && i + j < offset + limit) {
                Essay essay = (Essay) session.createQuery(
                        "from Essay where id = "
                                + ses.get(max2 - j).getEssay().getId())
                        .uniqueResult();
                supports.add(essay);
                j++;
            }
            while (j > max2 && i <= max1 && i + j < offset + limit) {
                Question question = (Question) session.createQuery(
                        "from Question where id = "
                                + sqs.get(max1 - i).getQuestion().getId())
                        .uniqueResult();
                supports.add(question);
                i++;
            }
        } else if (i >= max1 && j <= max2) { //all is essay
            for (; j <= max2 && j + i < offset + limit; j++) {
                Essay essay = (Essay) session.createQuery(
                        "from Essay where id = "
                                + ses.get(max2 - j).getEssay().getId())
                        .uniqueResult();
                supports.add(essay);
            }

        } else if (j >= max2 && i <= max1) { //all is questions
            for (; i <= max1 && j + i < offset + limit; i++) {
                Question question = (Question) session.createQuery(
                        "from Question where id = "
                                + sqs.get(max1 - i).getQuestion().getId())
                        .uniqueResult();
                supports.add(question);
            }
        }

        session.getTransaction().commit();
        session.close();
        return supports;

    }

    @Override
    public void removeFavoriteQuestion(int userId, int questionId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery(
                "select u from User u left join fetch u.questions where u.id = " + userId)
                .uniqueResult();
        Question question = (Question) session.createQuery(
                "select q from Question q left join fetch q.users where q.id = " + questionId)
                .uniqueResult();

        if (user != null && question != null) {
            question.getUsers().remove(user);
            user.getQuestions().remove(question);
            session.update(user);
            session.update(question);
        }

        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void removeFavoriteAnswers(int userId, int answerId) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery("select u from User u left join fetch u.questions where u.id = " + userId).uniqueResult();
        Answer answer = (Answer) session.createQuery("select a from Answer a left join fetch a.users where a.id = " + answerId).uniqueResult();

        if (user != null && answer != null) {
            answer.getUsers().remove(user);
            user.getAnswers().remove(answer);
            session.update(user);
            session.update(answer);
        }

        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void removeFavoriteEssays(int userId, int essayId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery("select u from User u left join fetch u.questions where u.id = " + userId).uniqueResult();
        Essay essay = (Essay) session.createQuery("select e from Essay e left join fetch e.users where e.id = " + essayId).uniqueResult();

        if (user != null && essay != null) {
            essay.getUsers().remove(user);
            user.getEssays().remove(essay);
            session.update(user);
            session.update(essay);
        }

        session.getTransaction().commit();
        session.close();
    }


}
