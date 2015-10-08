package daoImpl;

import dao.DbDao;
//import javafx.scene.control.Alert;
import model.Company;
import model.CompanyExchange;
import others.Parser;
//import others.MyAlerts;

import javax.persistence.TypedQuery;
import javax.swing.*;
import java.util.List;

/**
 * Created by odin on 15.04.15.
 */
public class DbCompany implements DbDao {

    public boolean saveCompany(Company company) {
        try {
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

    public boolean saveCompanyExchange(CompanyExchange companyExchange) {
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            entityManager.persist(companyExchange);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            //MyAlerts.showAlert("Error!", "Can't save company!", Alert.AlertType.ERROR, "Error!");
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }

    }

    public boolean updateCompany(Company company) {
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            entityManager.merge(company);
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

    public Boolean booleanFindCompany(String companyName) {
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
            return true;
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            return false;
        }
    }

    public Boolean checkCompaniesCount() {
        List<Company> user = null;
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            TypedQuery<Company> typedQuery = entityManager.createQuery(
                    "select u from Company u", Company.class);
            //typedQuery.setParameter("companyName", companyName);
            user = typedQuery.getResultList();
            entityManager.getTransaction().commit();
            int companyCount = Parser.getCountOfSiteCompany();
            if(user.size() == companyCount){
                JOptionPane.showMessageDialog(null, "Need to update companies.");
                return true;
            }else{

                return false;
            }
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            return false;
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

    public List<Company> loadAllCompanies() {
        TypedQuery<Company> createQuery = entityManager.createQuery(
                "select t from Company t", Company.class);
        List<Company> resultCompanyList = createQuery.getResultList();
        return resultCompanyList;
    }

    public List<Company> loadAllCompaniesOnLetter(String letter) {
        TypedQuery<Company> createQuery = entityManager.createQuery(
                "select t from Company t where t.companyName like :x", Company.class);
        createQuery.setParameter("x", letter.toUpperCase()+"%");
        List<Company> resultCompanyList = createQuery.getResultList();
        return resultCompanyList;
    }

}
