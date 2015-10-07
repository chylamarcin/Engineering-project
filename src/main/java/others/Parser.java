package others;

        import dao.DbDao;
        import daoImpl.DbCompany;
        import model.Company;
        import model.CompanyExchange;
        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;
        import org.jsoup.nodes.Element;
        import org.jsoup.select.Elements;

        import javax.swing.*;
        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.text.DateFormat;
        import java.util.*;
        import java.text.SimpleDateFormat;
        import java.util.jar.JarOutputStream;

/**
 * Created by odin on 27.05.15.
 */
public class Parser implements DbDao {


    public static void getTitle() {

        DbCompany dbCompany = new DbCompany();
        //Download page source
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

            Document doc = Jsoup.parse(html);

            LinkedList<String> listOfCompanyNames = new LinkedList<String>();
            LinkedList<String> listOfCompanyValues = new LinkedList<String>();
            Elements tabElements = doc.getElementsByClass("colWalor");
            Elements tabElementsCurse  = doc.getElementsByClass("colKurs");


            for (int i = 1; i < tabElements.size(); i++) {
                String companyName = tabElements.get(i).text();
                String companyValue = tabElementsCurse.get(i).text();
                listOfCompanyNames.add(companyName);
                listOfCompanyValues.add(companyValue);
            }

            Date date = new Date();

            ArrayList<Company> companies = new ArrayList<Company>();

            for (int i = 0; i < listOfCompanyNames.size(); i++) {
                Company company = new Company();
                company.setCompanyName(listOfCompanyNames.get(i));
                companies.add(company);
            }

            for (int i = 0; i < listOfCompanyNames.size(); i++) {
                Boolean companyName = false;
                companyName = dbCompany.booleanFindCompany(listOfCompanyNames.get(i));
                if (companyName == false) {
                    dbCompany.saveCompany(companies.get(i));
                }
            }

            for (int i = 0; i < listOfCompanyNames.size(); i++) {
                Company company = dbCompany.findCompany(listOfCompanyNames.get(i));

                CompanyExchange companyExchange = new CompanyExchange();
                companyExchange.setValue(listOfCompanyValues.get(i));
                companyExchange.setCompany(company);
                companyExchange.setDate(date);
                company.addExchange(companyExchange);

                if (company.getCompanyName() == dbCompany.findCompany(listOfCompanyNames.get(i)).getCompanyName()) {
                    dbCompany.updateCompany(company);
                    dbCompany.saveCompanyExchange(companyExchange);
                } else {
                    dbCompany.saveCompany(company);
                    dbCompany.saveCompanyExchange(companyExchange);
                }

            }

        }  catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Nie wykryto po??czenia z internetem.");
        }

    }

}
