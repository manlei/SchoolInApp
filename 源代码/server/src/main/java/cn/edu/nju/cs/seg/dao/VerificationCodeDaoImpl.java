package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.VerificationCode;
import cn.edu.nju.cs.seg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by fwz on 2017/5/14.
 */
public class VerificationCodeDaoImpl implements VerificationCodeDao {

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    

    @Override
    public void add(VerificationCode code) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(code);

        session.getTransaction().commit();
        session.close();
    }

    @Override
    public VerificationCode getCodeByEmailOrPhone(String emailOrPhone) {
        VerificationCode code = null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        code = (VerificationCode) session.createQuery(
                "from VerificationCode where emailOrPhone = " + "\'" + emailOrPhone + "\'")
                .uniqueResult();

        session.getTransaction().commit();
        session.close();
        return code;
    }

    @Override
    public void remove(String emailOrPhone) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        VerificationCode code = (VerificationCode) session.createQuery("from VerificationCode where emailOrPhone = " + "\'" + emailOrPhone + "\'").uniqueResult();
        if (session != null) {
            session.remove(code);
        }

        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(VerificationCode code) {
        if (code != null) {

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            session.update(code);

            session.getTransaction().commit();
            session.close();
        }
    }
}
