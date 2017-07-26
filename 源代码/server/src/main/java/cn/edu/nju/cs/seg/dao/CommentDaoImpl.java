package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Comment;

import cn.edu.nju.cs.seg.pojo.Notification;
import cn.edu.nju.cs.seg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Clypso on 2017/5/5.
 */

public class CommentDaoImpl implements CommentDao {

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();



    @Override
    public Comment findCommentById(int id) {
        Comment ret = null;
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        ret = (Comment) session.createQuery(
                "from Comment where id = " + id)
                .uniqueResult();

        session.getTransaction().commit();
        session.close();
        return ret;
    }

    @Override
    public Comment add(Comment comment) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        if (comment.getTarget() == Comment.COMMENT_FOR_ANSWER) {
            comment.getAnswer().setCommentsNumber(comment.getAnswer().getCommentsNumber() + 1);
            session.update(comment.getAnswer());
        } else {
            comment.getEssay().setCommentsNumber(comment.getEssay().getCommentsNumber() + 1);
            session.update(comment.getEssay());
        }


        session.persist(comment);

        session.getTransaction().commit();
        session.close();
        return comment;
    }

    @Override
    public boolean remove(int id) {
        boolean flag = false;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Comment comment = (Comment) session.createQuery(
                "from Comment where id = "+ id)
                .uniqueResult();
        if (comment != null) {
            if (comment.getTarget() == Comment.COMMENT_FOR_ANSWER) {
                comment.getAnswer().setCommentsNumber(comment.getAnswer().getCommentsNumber() - 1);
                session.update(comment.getAnswer());
            } else {
                comment.getEssay().setCommentsNumber(comment.getEssay().getCommentsNumber() - 1);
                session.update(comment.getEssay());
            }
            session.delete(comment);
            List<Notification> notifications = session.createQuery(
                    "from Notification where comment.id = " + id)
                    .list();
            for (Notification notification : notifications) {
                session.remove(notification);
            }
            flag = true;
        }
        session.getTransaction().commit();
        session.close();
        return flag;
    }

    @Override
    public List<Comment> findAllComments() {
        Session session = sessionFactory.openSession();;
        session.beginTransaction();
        List<Comment> result = session.createQuery("from Comment ").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }


    @Override
    public List<Comment> findCommentsByAnswerId(int answerId) {
        List<Comment> comments;
        Session session = sessionFactory.openSession();;
        session.beginTransaction();

        comments = session.createQuery(
                "select a from Comment a where a.answer.id = " + answerId)
                .list();

        session.getTransaction().commit();
        session.close();

        return comments;
    }

    @Override
    public List<Comment> findCommentsByEssayId(int essayId) {
        List<Comment> comments;
        Session session = sessionFactory.openSession();;
        session.beginTransaction();

        comments = session.createQuery(
                "select a from Comment a where a.essay.id = " + essayId)
                .list();

        session.getTransaction().commit();
        session.close();

        return comments;
    }



}
