package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Notification;
import cn.edu.nju.cs.seg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Clypso on 2017/6/24.
 */
public class NotificationDaoImpl implements NotificationDao {

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public void remove(int id) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Notification notification = (Notification) session.createQuery(
                "from Notification where id = " + id)
                .uniqueResult();
        if (notification != null) {
            session.delete(notification);
        }
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Notification add(Notification notification) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.persist(notification);

        session.getTransaction().commit();
        session.close();
        return notification;
    }

    @Override
    public Notification update(Notification notification) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(notification);

        session.getTransaction().commit();
        session.close();
        return notification;
    }

    @Override
    public Notification findNotificationById(int id) {
        Notification notification = null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        notification = (Notification) session.createQuery(
                "from Notification where id = " + id)
                .uniqueResult();


        session.getTransaction().commit();
        session.close();
        return notification;
    }

    @Override
    public List<Notification> findNotificationsByUserId(int userId) {

        List<Notification> notifications = null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        notifications = session.createQuery(
                "select n from Notification n where n.user.id = " + userId)
                .list();

        session.getTransaction().commit();
        session.close();
        return notifications;

    }

}
