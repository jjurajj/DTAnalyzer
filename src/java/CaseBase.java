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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@ManagedBean (name = "CaseBase", eager = true)
@ViewScoped
public class CaseBase {
    
    public String url = "http://diana.zesoi.fer.hr/~jpetrovic/case_repository/car_starting/";
    public ArrayList<Case> cases  = new ArrayList<>();
    public ArrayList<String> case_list  = new ArrayList<>();
    
    public void initialize(String url) throws IOException {
        this.url = url;
        initialize();
    }
    
    public void initialize() throws IOException {
        
        //ArrayList<String> case_list =
        listCaseBase();
        for (String case_link : this.case_list) {
            Case new_case = new Case();
            new_case.initializeCase( case_link.concat("case.txt") );
            this.cases.add(new_case);
        }
    }
    
    // Ovo vraća popis URL direktorija u svakom od kojih se nalazi 1 case, ocekivano imena case.txt i eventualno s popratnim materijalima
    public void listCaseBase() {
        
        String url = this.url;
        ArrayList<String> lista_caseova = new ArrayList<>();
        
        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");                 //http://jsoup.org/cookbook/extracting-data/example-list-links
            for (Element link : links) {
                if (link.attr("href").equals(link.text())) lista_caseova.add(this.url.concat(link.attr("href")));
            } 
        } catch (IOException e) {};
        this.case_list = lista_caseova;
//        return lista_caseova;
    }
    public ArrayList<String> listCaseBase(String url) {
        
        ArrayList<String> lista_caseova = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");                 //http://jsoup.org/cookbook/extracting-data/example-list-links
            for (Element link : links) {
                if (link.attr("href").equals(link.text())) lista_caseova.add(url.concat(link.attr("href")));
            } 
        } catch (IOException e) {};
        return lista_caseova;
    }
    
    // Ovo tu evaluira stablo na sebi odnosno na bazi caseova
    public void evaluateTreeOnBase (DT tree) {
    
        //preciznost/odziv za svaku dijagnozu
        // Za dijagnoze iz stabla napravit vektor s njihovim countovima u case bazi
        // isti takav vektor jos nadopunimo iz evaluateCase rezultata
        ArrayList<DiagnosisCount> diagnosis_count = new ArrayList<>();
        
        for (Case temp_case : this.cases) {
            for (Dijagnoza temp_diag : temp_case.diagnoses)     // Prodi sve dijagnoze jer moze biti vise tocnih
                if (temp_diag.isCorrect()) {                    // Ako je dijagnoza tocna, onda to apdejtaj u total countu
                                                                // Odmah ispitaj i kako se tja case klasificira pa za azuriraj
                                                                    // ako se tocno klasificira
                                                                    // ako se ne klasificira i gdje zapinje
                                                                    // ako se krivo klasificira i kuda.
                    // 
                }
            
        }
        
        
    
    
    }
    
    
    public Case getCase(int i) {
        return this.cases.get(i);
    }
    public ArrayList<Case> getCases() {
        return this.cases;
    }
    public String getUrl() {
        return this.url;
    }
    public ArrayList<String> getCase_list() {
        return case_list;
    }
    
    public void setCases(ArrayList<Case> cases) {
        this.cases = cases;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setCase_list(ArrayList<String> case_list) {
        this.case_list = case_list;
    }

}
