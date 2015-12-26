package dao;

import daoImpl.DbSingleton;

import javax.persistence.EntityManager;


/**
 * Created by odin on 15.04.15.
 */
public interface DbDao {
    DbSingleton db = DbSingleton.getInstance();
    EntityManager entityManager = db.getEntityManager();
}