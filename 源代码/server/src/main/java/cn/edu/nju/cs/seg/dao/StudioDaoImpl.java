package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Essay;
import cn.edu.nju.cs.seg.pojo.Question;
import cn.edu.nju.cs.seg.pojo.Studio;
import cn.edu.nju.cs.seg.pojo.User;
import cn.edu.nju.cs.seg.service.EssayService;
import cn.edu.nju.cs.seg.service.QuestionService;
import cn.edu.nju.cs.seg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Clypso on 2017/5/4.
 */
public class StudioDaoImpl implements StudioDao {
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();


    @Override
    public boolean remove(int studioId) {
        Studio studio;
        boolean isRemove = false;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        studio = (Studio) session.createQuery(
                "select s from Studio s left join fetch s.users where s.id = "
                        + studioId)
                .uniqueResult();
        if (studio != null) {
            List<Question> questions = QuestionService.findQuestionsByStudioId(studioId);
            for (Question question : questions) {
                QuestionService.remove(question.getId());
            }
            List<Essay> essays = EssayService.findEssaysByStudioId(studioId);
            for (Essay essay : essays) {
                EssayService.remove(essay.getId());
            }
//            List<User> members = studio.getUsers();
//            for (User member : members) {
//                studio.getUsers().remove(member);
//                member.getStudios().remove(studio);
////                session.update(studio);
//                session.update(member);
//            }
            session.remove(studio);
            isRemove = true;
        }
        session.getTransaction().commit();
        session.close();
        return isRemove;
    }

    @Override
    public void removeMember(int studioId, int memberId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Studio studio = (Studio) session.createQuery(
                "select s from Studio s left join fetch s.users where s.id = " + studioId)
                .uniqueResult();
        User member = (User) session.createQuery(
                "select u from User u left join fetch u.studios where u.id = " + memberId)
                .uniqueResult();
        if (studio != null && member != null) {
            studio.getUsers().remove(member);
            member.getStudios().remove(studio);
            studio.setMembersNumber(studio.getMembersNumber() - 1);
            session.update(studio);
            session.update(member);
        }
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Studio add(Studio studio) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(studio);

        session.getTransaction().commit();
        session.close();

        return studio;
    }

    @Override
    public void addMembers(int studioId, int userId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery(
                "from User where id = " + userId)
                .uniqueResult();
        Studio studio = (Studio) session.createQuery(
                "from Studio where id = " + studioId)
                .uniqueResult();

        if (user != null && studio != null) {
            if (!user.getStudios().contains(studio)) {
                user.getStudios().add(studio);
            }
            if (!studio.getUsers().contains(user)) {
                studio.getUsers().add(user);
                studio.setMembersNumber(studio.getMembersNumber() + 1);
            }
            session.update(user);
            session.update(studio);
        }

        session.getTransaction().commit();

        session.close();
    }


    @Override
    public Studio findStudioById(int id) {
        Session session = sessionFactory.openSession();
        Studio ret = null;
        session.beginTransaction();

        ret = (Studio) session.createQuery(
                "from Studio where id = " + id)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return ret;
    }

    @Override
    public Studio findStudioByName(String name) {
        if (name != null) {
            Session session = sessionFactory.openSession();
            Studio ret = null;
            session.beginTransaction();

            ret = (Studio) session.createQuery(
                    "from Studio where name = " + "\'" + name + "\'")
                    .uniqueResult();

            session.getTransaction().commit();
            session.close();
            return ret;
        }
        return null;
    }

    @Override
    public List<Studio> findAll() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery(
                "from Studio ")
                .list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public void updateStudio(Studio studio) {
        if (studio != null) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            session.update(studio);

            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public List<Studio> findStudiosByUserId(int userId) {
        List<Studio> studios;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.createQuery(
                "select u from User u left join fetch u.studios where u.id = " + userId)
                .uniqueResult();

        studios = user.getStudios();
        session.getTransaction().commit();
        session.close();
        return studios;

    }


    @Override
    public List<User> findUsersByStudioId(int studioId) {
        List<User> users;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Studio studio = (Studio) session.createQuery(
                "select u from Studio u left join fetch u.users where u.id = " + studioId)
                .uniqueResult();

        users = studio.getUsers();
        session.getTransaction().commit();
        session.close();
        return users;
    }

}
