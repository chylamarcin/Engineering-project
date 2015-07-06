package daoImpl;

import dao.DbDao;
//import javafx.scene.control.Alert;
import model.Company;
//import others.MyAlerts;

import javax.persistence.TypedQuery;

/**
 * Created by odin on 15.04.15.
 */
public class DbCompany implements DbDao {

    public boolean saveCompany(Company company) {
        try {//
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.persist(company);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            //MyAlerts.showAlert("Error!", "Can't save company!", Alert.AlertType.ERROR, "Error!");
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }

    }

    public Company findCompany(String companyName) {
        Company user = null;
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            TypedQuery<Company> typedQuery = entityManager.createQuery(
                    "select u from Company u where u.companyName = :companyName", Company.class);
            typedQuery.setParameter("companyName", companyName);
            user = typedQuery.getSingleResult();
            entityManager.getTransaction().commit();
            return user;
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            return null;
        }
    }

    public Company findCompanyById(long id) {
        Company company = null;
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            TypedQuery<Company> typedQuery = entityManager.createQuery(
                    "select u from Company u where u.id = :id", Company.class);
            typedQuery.setParameter("id", id);
            company = typedQuery.getSingleResult();
            entityManager.getTransaction().commit();
            return company;
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            return null;
        }
    }

}
