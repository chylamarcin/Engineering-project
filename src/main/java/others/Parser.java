package others;

import dao.DbDao;
import daoImpl.DbCompany;
import model.Company;
import model.CompanyExchange;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by odin on 27.05.15.
 */
public class Parser implements DbDao {

    DbCompany dbCompany = new DbCompany();


    public static void getTitle() {
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
                //System.out.println(line);
                buf.append(line+"\n");
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

        //Element content = doc.getElementById()
//        HashMap<String, String> tableMap = new HashMap<String, String>();
        LinkedList<String> listOfCompanyNames = new LinkedList<String>();
        Elements tabElements = doc.getElementsByClass("colWalor");
        for (Element tabElem : tabElements) {
            String companyName = tabElem.text();
            listOfCompanyNames.add(companyName);
        }

        LinkedList<String> listOfCompanyValues = new LinkedList<String>();
        Elements tabElementsCurse = doc.getElementsByClass("colKurs");
        for (Element tabCurseElem : tabElementsCurse) {
            String companyValue = tabCurseElem.text();
            listOfCompanyValues.add(companyValue);
        }

        listOfCompanyNames.removeFirst();
        listOfCompanyValues.removeFirst();

        for (int i = 0; i < listOfCompanyNames.size(); i++) {
            Company company = new Company();
            company.setCompanyName(listOfCompanyNames.get(i));
            CompanyExchange companyExchange = new CompanyExchange();
            companyExchange.setIdCompany(company.getId());
            companyExchange.setValue(listOfCompanyValues.get(i));
        }
    }




}
