package others;

import model.Company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by odin on 27.05.15.
 */
public class Parser {


    public static void getTitle(String html) {
        Document doc = Jsoup.parse(html);
        String companyName;
        String companyValue;

        //Element content = doc.getElementById()
//        HashMap<String, String> tableMap = new HashMap<String, String>();
        LinkedList<String> listOfCompanyNames = new LinkedList<String>();
        Elements tabElements = doc.getElementsByClass("colWalor");
        for (Element tabElem : tabElements) {
            companyName = tabElem.text();
            listOfCompanyNames.add(companyName);
        }

        LinkedList<String> listOfCompanyValues = new LinkedList<String>();
        Elements tabElementsCurse = doc.getElementsByClass("colKurs");
        for (Element tabCurseElem : tabElementsCurse) {
            companyValue = tabCurseElem.text();
            listOfCompanyValues.add(companyValue);
        }

        listOfCompanyNames.removeFirst();
        listOfCompanyValues.removeFirst();

        for (int i = 0; i < listOfCompanyNames.size(); i++) {
            Company company = new Company();
            company.setCompanyName(listOfCompanyNames.get(i));
            //company.setListOfExchanges();
        }
    }
}
