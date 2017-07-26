package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Comment;
import cn.edu.nju.cs.seg.pojo.Essay;
import cn.edu.nju.cs.seg.pojo.SupportEssays;
import cn.edu.nju.cs.seg.service.CommentService;
import cn.edu.nju.cs.seg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Clypso on 2017/5/5.
 */

public class EssayDaoImpl implements EssayDao {

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    

    @Override
    public Essay findEssayById(int id) {
        Session session = sessionFactory.openSession();
        Essay ret;
        session.beginTransaction();
        ret = (Essay) session.createQuery(
                "from Essay where id = " + id)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return ret;
    }

    @Override
    public List<Essay> findAllEssays() {
        List<Essay> essays;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        essays = session.createQuery("from Essay ").list();
        session.getTransaction().commit();
        session.close();
        return essays;
    }

    @Override
    public List<Essay> findEssaysByStudioId(int studioId) {
        Session session = sessionFactory.openSession();
        List<Essay> essays;
        session.beginTransaction();

        essays = session.createQuery(
                "select a from Essay a where a.studio.id = " + studioId)
                .list();

        session.getTransaction().commit();
        session.close();

        return essays;
    }

    @Override
    public Essay add(Essay essay) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        essay.getStudio().setEssaysNumber(essay.getStudio().getEssaysNumber() + 1);
        session.update(essay.getStudio());

        session.persist(essay);
        //session.flush();


        session.getTransaction().commit();
        session.close();

        return essay;
    }

    @Override
    public boolean remove(int id) {
        boolean flag = false;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Essay essay = (Essay) session.createQuery(
                "from Essay where id = " + id)
                .uniqueResult();

        if (essay != null) {
            List<Comment> comments = session.createQuery(
                    "from Comment where essay.id = " + id)
                    .list();
            for (Comment comment : comments) {
                session.remove(comment);
            }
            List<SupportEssays> supportEssays = session.createQuery(
                    "from SupportEssays where essay.id = " + id)
                    .list();
            for (SupportEssays se : supportEssays) {
                session.remove(se);
            }
            essay.getStudio().setEssaysNumber(essay.getStudio().getEssaysNumber() - 1);
            session.update(essay.getStudio());
            session.remove(essay);
            flag = true;
        }
        session.getTransaction().commit();
        session.close();
        return flag;
    }

    @Override
    public int incrementEssayHeat(int essayId) {
        int heat = 0;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Essay essay = (Essay) session.createQuery(
                "from Essay where id = " + essayId)
                .uniqueResult();


        if (essay != null) {
            heat = essay.getHeat() + 1;
            essay.setHeat(heat);
            session.update(essay);
        } else {
            heat = -1;
        }
        session.getTransaction().commit();

        session.close();
        return heat;
    }


}
