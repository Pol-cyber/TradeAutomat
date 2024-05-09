package com.yarem.dbConnect;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSessionFactory {

    private static final SessionFactory sessionFactory = createSessionFactory();

    public static SessionFactory createSessionFactory(){
        try{
            return new MetadataSources(new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml")
                    .build()).buildMetadata().buildSessionFactory();

        } catch (Exception e){
            System.err.println("При створенні SessionFactory виникла помилка");
            throw new RuntimeException("Failed to initialize Hibernate Session Factory", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

    public static void close() {
        sessionFactory.close();
    }
}
