package others;

import controllers.Controller;
import dao.DbDao;
import daoImpl.DbCompany;
import model.Company;
import model.CompanyExchange;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by odin on 27.05.15.
 */
public class Parser implements DbDao {

    public static Document getSiteDoc() {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        String html = null;

        try {
            url = new URL("http://www.bankier.pl/gielda/notowania/akcje");
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));
            StringBuilder buf = new StringBuilder();

            while ((line = br.readLine()) != null) {
                buf.append(line + "\n");
            }
            html = buf.toString();

        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }

        Document doc = Jsoup.parse(html);
        return doc;
    }

    public static int getCountOfSiteCompany() {
        Document doc = getSiteDoc();
        Elements tabElements = doc.getElementsByClass("colWalor");
        return tabElements.size() - 1;
    }

    public static void getCompanies() {
        DbCompany dbCompany = new DbCompany();
        //Download page source

        Document doc = getSiteDoc();

        LinkedList<String> listOfCompanyNames = new LinkedList<String>();
        Elements tabElements = doc.getElementsByClass("colWalor");

        for (int i = 1; i < tabElements.size(); i++) {
            String companyName = tabElements.get(i).text();
            listOfCompanyNames.add(companyName);
        }

        for (int i = 0; i < listOfCompanyNames.size(); i++) {
            //System.out.println(listOfCompanyNames.size());
            Boolean companyName = false;
            companyName = dbCompany.booleanFindCompany(listOfCompanyNames.get(i));
            if (companyName == false) {
                Company company = new Company();
                company.setCompanyName(listOfCompanyNames.get(i));
                dbCompany.saveCompany(company);
            }
        }
    }

    public static void getValues() {

        Document doc = getSiteDoc();
        DbCompany dbCompany = new DbCompany();
        List<Company> listOfCompany = dbCompany.loadAllCompanies();

        LinkedList<String> listOfCompanyValues = new LinkedList<String>();
        Elements tabElementsCurse = doc.getElementsByClass("colKurs");
        Date date = new Date();

        for (int i = 1; i < tabElementsCurse.size(); i++) {
            String companyValue = tabElementsCurse.get(i).text();
            listOfCompanyValues.add(companyValue);
        }

        for (int i = 0; i < listOfCompanyValues.size(); i++) {
            //System.out.println(listOfCompanyValues.size());
            Company company = listOfCompany.get(i);

            CompanyExchange companyExchange = new CompanyExchange();
            companyExchange.setValue(listOfCompanyValues.get(i));
            companyExchange.setCompany(company);
            companyExchange.setDate(date);
            company.addExchange(companyExchange);

            dbCompany.saveCompanyExchange(companyExchange);
            System.out.println((double) i / (double) listOfCompanyValues.size());

            Controller.progressBar.setProgress(0.56);

        }

    }

}
