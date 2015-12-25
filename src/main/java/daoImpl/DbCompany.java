package daoImpl;

import dao.DbDao;
import javafx.scene.control.Alert;
import model.Company;
import model.CompanyExchange;
import model.PredictedExchange;
import others.Parser;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

//import javafx.scene.control.Alert;
//import others.MyAlerts;


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
            entityManager.getTransaction().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Can't save company " + e.getMessage());
            alert.showAndWait();
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
            if (entityManager.getTransaction() != null) {
                entityManager.getTransaction().rollback();
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Can't save exchange" + e.getMessage());
            alert.showAndWait();
            return false;
        }

    }

    public boolean savePredictedCompanyExchange(PredictedExchange predictedExchange) {
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            entityManager.persist(predictedExchange);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction() != null) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Can't save predicted exchange " + e.getMessage());
            alert.showAndWait();
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
            entityManager.getTransaction().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Can't update company " + e.getMessage());
            alert.showAndWait();
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

    public List<PredictedExchange> findPredictedExchange(Long companyId) {
        List<PredictedExchange> predictedExchanges = null;
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            TypedQuery<PredictedExchange> typedQuery = entityManager.createQuery(
                    "select u from PredictedExchange u where u.company.id = :companyId", PredictedExchange.class);
            typedQuery.setParameter("companyId", companyId);
            entityManager.getTransaction().commit();
            return predictedExchanges = typedQuery.getResultList();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            return null;
        }
    }

    public boolean deleteAssociationsById(Long id) {
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            Query query = entityManager.createNativeQuery("DELETE FROM companyExchange WHERE companyId = ?1");
            query.setParameter(1, id);
            query.executeUpdate();
            return true;
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Can't delete company " + ex.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    public boolean deteleteCompanyById(Long id) {
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            Query query = query = entityManager.createNativeQuery("DELETE FROM test.company WHERE id=?1");
            query.setParameter(1, id);
            query.executeUpdate();
            return true;
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Can't update company " + ex.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    public Boolean checkCompaniesCount() {

        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }


            Query query = entityManager.createNativeQuery("Select count(companyName) from company");
            int companyCountAtDatabase = Integer.valueOf(query.getSingleResult().toString());
            int companyCountAtSite = Parser.getCountOfSiteCompany();

            if (companyCountAtDatabase == companyCountAtSite) {
                System.out.println(companyCountAtDatabase + " " + companyCountAtSite);
                System.out.println("true");
                return true;
            } else {
                System.out.println(companyCountAtDatabase + " " + companyCountAtSite);
                System.out.println("false");
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("spam");
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

    public List<Company> loadAlphabetCompanies() {
        TypedQuery<Company> createQuery = entityManager.createQuery(
                "select t from Company t order by t.companyName", Company.class);
        List<Company> resultCompanyList = createQuery.getResultList();
        return resultCompanyList;
    }

    public List<Company> loadAllCompaniesOnLetter(String letter) {
        TypedQuery<Company> createQuery = entityManager.createQuery(
                "select t from Company t where t.companyName like :x", Company.class);
        createQuery.setParameter("x", letter.toUpperCase() + "%");
        List<Company> resultCompanyList = createQuery.getResultList();
        return resultCompanyList;
    }


}
