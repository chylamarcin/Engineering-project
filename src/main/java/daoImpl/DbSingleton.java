package daoImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by odin on 16.04.15.
 */
public final class DbSingleton {

    private static DbSingleton instance = null;
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;


    private DbSingleton() {
        entityManagerFactory = Persistence
                .createEntityManagerFactory("myDatabase");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public static synchronized DbSingleton getInstance() {
        if (instance == null) {
            instance = new DbSingleton();
        }
        return instance;
    }



    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void closeConnetion() {
        entityManager.close();
        entityManagerFactory.close();
    }
}
