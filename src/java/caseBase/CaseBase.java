package caseBase;

/**
 * Ova klasa predstavlja bazu caseova i sadrzi
 * - popis urlova baza (1 ili vise ili edfaultna na Diani)
 * - listu svih caseova ucitanih iz tih baza
 * 
 * Osim postavljanja i vracanja toga, moze se vratiti i popis dijagnoza
 * u koje se klasificiraju ucitani caseovi.
 * 
 * Moze se dobit i lista caseova za pojedinu bazu odnosno URL
 * 
 * @author juraj
 */

import decisionTree.DT;
import singleCase.Dijagnoza;
import singleCase.Case;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@ManagedBean (name = "CaseBase", eager = true)
@ViewScoped
public class CaseBase {
    
    public ArrayList<String> url = new ArrayList<>();
    public ArrayList<Case> cases = new ArrayList<>();
    public ArrayList<Case> display_cases = new ArrayList<>();
    
    // Inicijalizacija bez argumenta znaci da ce se postaviti defaultna baza na Diani
    public void initialize() throws IOException {
        this.url.add("http://diana.zesoi.fer.hr/~jpetrovic/case_repository/car_starting/");
        initialize(this.url);
    }
    
    // Inicijalizacija listom baza. Samo izvrtimo inicijalizaciju za svaku redom
    public void initialize(ArrayList<String> list_of_bases) {
    
        for (String url : list_of_bases) {
            try {
                initialize(url);
            } catch (IOException ex) {
                Logger.getLogger(CaseBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }
    
    //Dodavanje svih caseova s linka u CaseBase i pocetna obradba
    public void initialize(String url) throws IOException {
        
        String case_file_name = "case.txt";
        this.url.add(url);
        ArrayList<String> case_list = listCaseBase(url);
        for (String case_link : case_list) {
            Case new_case = new Case();
            new_case.initializeCase( case_link.concat(case_file_name) );
            
            this.cases.add(new_case);
            
        }
        this.display_cases = this.cases;
    }
    
    // Ovo vraća ArrayList popis URL direktorija u svakom od kojih se nalazi 1 case
    // Ocekivano ime casea je case.txt i eventualno su u folderu i popratni materijali
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
    
    // Evaluacija baze na stablu. 
    public void evaluateBase (DT tree) {
    
            for (int i=0; i<this.cases.size(); i++) {
                this.cases.get(i).evaluation = tree.runCase(this.cases.get(i));
            }
    }
    
    public int myIndexOf(String url) {
        for (int i=0; i<this.cases.size(); i++)
            if (this.cases.get(i).url == url)
                return i;
        return -1;
    }
    
    // Vraca ArrayList dijagnoza u koje se klasificiraju ucitani caseovi
    public ArrayList<String> getDiagnosesList () {
        
        ArrayList<String> diagnoses_list = new ArrayList<>();
        for (Case temp_case : this.cases)
            for (Dijagnoza temp_diag : temp_case.diagnoses) 
                if ((temp_diag.isCorrect()) && (diagnoses_list.contains(temp_diag.name)))
                    diagnoses_list.add(temp_diag.name);
        return diagnoses_list;
    }

    
    public void setDisplayCases(ArrayList<Case> display_cases) {
        this.display_cases = display_cases;
    }
    
    public Case getCase(int i) {
        return this.cases.get(i);
    }
    public ArrayList<Case> getCases() {
        return this.cases;
    }
    public ArrayList<String> getURL() {
        return this.url;
    }
    public void setCases(ArrayList<Case> cases) {
        this.cases = cases;
    }
    public void setUrl(ArrayList<String> url) {
        this.url = url;
    }

}
