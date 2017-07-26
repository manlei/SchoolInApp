package cn.edu.nju.cs.seg.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Created by fwz on 2017/4/22.
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
//        System.out.println("hibernate session building.");
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        // Create the SessionFactory from hibernate.cfg.xml
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();

    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
