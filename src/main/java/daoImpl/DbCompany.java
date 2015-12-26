package daoImpl;

import dao.DbDao;
import model.Company;
import model.CompanyExchange;
import model.PredictedExchange;
import others.ExceptionAlert;
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

    /**
     * Method to save company in database.
     *
     * @param company
     * @return True if company was added and false if not.
     */
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
            ExceptionAlert alert = new ExceptionAlert("Can't save company", e);
            return false;
        }

    }

    /**
     * Method to save company exchange in database.
     *
     * @param companyExchange
     * @return True if company exchange was added and false if not.
     */
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
            ExceptionAlert alert = new ExceptionAlert("Can't save company exchange", e);
            return false;
        }

    }

    /**
     * Method to save predicted company exchange in databse.
     * @param predictedExchange
     * @return True if predicted exchange was added and false if not.
     */
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
            ExceptionAlert alert = new ExceptionAlert("Can't save predicted exchange", e);
            return false;
        }

    }

    /**
     * Method to find company in database by name.
     * @param companyName
     * @return Comapny if company with that name exist in databse or null if not.
     */
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
            //ExceptionAlert alert = new ExceptionAlert("Can't find company", ex);
            return null;
        }
    }

    /**
     * Method to check if company exist in databse by name.
     * @param companyName
     * @return True if company with that name exist in database and false if not.
     */
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
            //ExceptionAlert alert = new ExceptionAlert("Can't find company", ex);
            return false;
        }
    }

    /**
     * Method to find list of predicted exchange of company in database by company id.
     * @param companyId
     * @return List of predicted exchange if it is in databse and null if not.
     */
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
            //ExceptionAlert alert = new ExceptionAlert("Can't find company", ex);
            return null;
        }
    }

    /**
     * Method to delete associations between company and company exchanges by id.
     * @param id
     * @return True if delete was complete and false if not.
     */
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
            //ExceptionAlert alert = new ExceptionAlert("Can't delete company", ex);
            return false;
        }
    }

    /**
     * Method to delete company from database by id.
     * @param id
     * @return Return true if delete was complete and false if not.
     */
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
            //ExceptionAlert alert = new ExceptionAlert("Can't delete company", ex);
            return false;
        }
    }

    /**
     * Method to check if companies cout is the same at site and database.
     * @return True if count of companies is the same in database and at site and false if not.
     */
    public Boolean checkCompaniesCount() {
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            Query query = entityManager.createNativeQuery("Select count(companyName) from company");
            int companyCountAtDatabase = Integer.valueOf(query.getSingleResult().toString());
            int companyCountAtSite = Parser.getCountOfSiteCompany();

            if (companyCountAtDatabase == companyCountAtSite) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ExceptionAlert alert = new ExceptionAlert("Can't check companies count", ex);
            entityManager.getTransaction().rollback();
            return false;
        }
    }

    /**
     * Method to find company in database by id.
     * @param id
     * @return Company if company with this id exist in database and false if not.
     */
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
            ExceptionAlert alert = new ExceptionAlert("Can't find company", ex);
            return null;
        }
    }

    /**
     * Method to get list of all companies from database.
     * @return List of companies from database.
     */
    public List<Company> loadAllCompanies() {
        TypedQuery<Company> createQuery = entityManager.createQuery(
                "select t from Company t", Company.class);
        List<Company> resultCompanyList = createQuery.getResultList();
        return resultCompanyList;
    }

    /**
     * Method to get list of all companies sorted by alphabet.
     * @return List of companies sorted by alphabet.
     */
    public List<Company> loadAlphabetCompanies() {
        TypedQuery<Company> createQuery = entityManager.createQuery(
                "select t from Company t order by t.companyName", Company.class);
        List<Company> resultCompanyList = createQuery.getResultList();
        return resultCompanyList;
    }

    /**
     * Method to load all companies whos names start at some letter.
     * @param letter
     * @return List of company whos names start at letter.
     */
    public List<Company> loadAllCompaniesOnLetter(String letter) {
        TypedQuery<Company> createQuery = entityManager.createQuery(
                "select t from Company t where t.companyName like :x", Company.class);
        createQuery.setParameter("x", letter.toUpperCase() + "%");
        List<Company> resultCompanyList = createQuery.getResultList();
        return resultCompanyList;
    }
}
