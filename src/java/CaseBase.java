/**
 * Ovo bi trebalo ili ucitati gotovu tablicu parametara vrijednosti i diajagnoza iz nekog fajla
 * ili ju obnoviti ako ne postoji ili na zahtjev (ako se sadrzaj baze promijenio)
 * 
 * I onda dalje ovo sluzi za vrednovanje korisnickog stabla jer sadrzi sirove info iz caseova
 * pa bi trebalo ici najbrze.
 * 
 * @author juraj
 */

// Klasa CaseBase koja je objekt koji se sastoji od ucitanih caseova
// Nad ovim objektom se može radit vrednovanje caseova(u ovisnosti o zadanom DTu)
import java.io.IOException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@ManagedBean (name ="CaseBase", eager = true)
@SessionScoped
public class CaseBase {
    
    public String url = "http://diana.zesoi.fer.hr/~jpetrovic/case_repository/car_starting/";
    public ArrayList<Case> cases = new ArrayList<Case>();
    
    public void initialize(String url) throws IOException {
        this.url = url;
        initialize();
    }
    
    public void initialize() throws IOException {
        
        ArrayList<String> case_list = listCaseBase(this.url);
        for (String case_link : case_list) {
            Case new_case = new Case();
            new_case.initializeCase(case_link.concat("case.txt"));
            cases.add(new_case);
        }
    }
    
    // Ovo vraća popis URL direktorija u svakom od kojih se nalazi 1 case, ocekivano imena case.txt i eventualno s popratnim materijalima
    public ArrayList<String> listCaseBase(String url) {
        
        ArrayList<String> lista_caseova = new ArrayList<>();
        
        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");                 //http://jsoup.org/cookbook/extracting-data/example-list-links
            for (Element link : links) {
                if (link.attr("href").equals(link.text())) lista_caseova.add(this.url.concat(link.attr("href")));
            } 
        } catch (IOException e) {};
    
        return lista_caseova;
    }
    
    
    
    
    /* Ovo ucita svaki case.txt fajl iz zadane liste case URLova i spremi ga lokalno u fajl
    public void parseCaseBase(ArrayList<String> caseovi, String case_file_name) throws MalformedURLException {
    
        for (String case_file : caseovi) {
            
            String file_link = case_file.concat("/").concat(case_file_name);
            String case_text = readCase(file_link);
        }
    }*/
    
    // Provrti case kroz DT pomocu njihovih HashMapova i start nodea DTa
    // Tu mogu dobit da: case ima 1 dijagnozu, case ima vise dijagnoza, case zapne u cvoru
    
    
    
    public ArrayList<Case> getCaseovi() {
        return this.cases;
    }
    
    public Case getSingleCase(int i) {
        return this.cases.get(i);
    }
    
    public void setCases(ArrayList<Case> caseovi) {
        this.cases = caseovi;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}

