package server.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtils {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private HibernateUtils() {
        super();
    }

    private static SessionFactory buildSessionFactory() {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder() //
                .configure("hibernate.cfg.xml") // Load hibernate.cfg.xml from resource folder by default
                .build();
        Metadata metadata = null;
        try {
             metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return metadata.getSessionFactoryBuilder().build();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void close() {
        getSessionFactory().close();
    }
    public static boolean isClosed() {
        return getSessionFactory().isClosed();

    }
}