package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Avatar;
import cn.edu.nju.cs.seg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by fwz on 2017/7/8.
 */
public class AvatarDaoImpl implements AvatarDao {

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Avatar findAvatarById(long id) {
        Avatar avatar = null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        avatar = (Avatar) session.createQuery("from Avatar where id = " + id)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return avatar;
    }

    @Override
    public Avatar findAvatarByMd5(String md5) {

        Avatar avatar = null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("from Avatar where md5 = " + "\'" + md5 + "\'")
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return avatar;

    }

    @Override
    public Avatar add(Avatar avatar) {
        Avatar ret = null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(avatar);
        session.getTransaction().commit();
        session.close();
        return avatar;
    }
}
