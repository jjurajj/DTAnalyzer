package caseBase;

/**
 * Ova klasa predstavlja bazu caseova i sadrzi
 * - popis urlova baza (1 ili vise ili edfaultna na Diani)
 * - listu svih caseova ucitanih iz tih baza
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
import singleCase.Parametar;

@ManagedBean (name = "CaseBase", eager = true)
@ViewScoped

public class CaseBase {
    
    public ArrayList<String> url = new ArrayList<>();
    public ArrayList<Case> cases = new ArrayList<>();
    public ArrayList<Case> excluded_cases = new ArrayList<>();
    
    public void initialize() throws IOException {
        this.url.add("http://diana.zesoi.fer.hr/~jpetrovic/case_repository/car_starting/");
        initialize("http://diana.zesoi.fer.hr/~jpetrovic/case_repository/car_starting/");
    }
    public void initialize(String url) throws IOException {

        this.url = new ArrayList<>();
        this.cases = new ArrayList<>();

        String case_file_name = "case.txt";
        this.url.add(url);
        ArrayList<String> case_list = listCaseBase(url);
        for (String case_link : case_list) {
            Case new_case = new Case();
            new_case.initializeCase( case_link.concat(case_file_name) );
            
            this.cases.add(new_case);
            
        }
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
    
    public void purgeBase(DT tree) {
        ArrayList<Case> purged_case_list = new ArrayList<>();
        ArrayList<Case> excluded_cases = new ArrayList<>();
        for (Case temp_case : cases) {
            String diagnosis = temp_case.getDiagnoses().get(0).getName();
            for (String diagnosis_ID : tree.getDiagnoses())
                if (tree.getNodeNameFromID(diagnosis_ID).equals(diagnosis))
                    purged_case_list.add(temp_case);
                else
                    excluded_cases.add(temp_case);
        }
        this.cases=purged_case_list;
        this.excluded_cases=excluded_cases;
    }
    
    public void evaluateBase (DT tree) {
    
        purgeBase(tree); // Ovo izbacuje iz liste sve caseove koji imaju dijagnoze koje nisu pokrivene stablom
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
    public ArrayList<String> getDiagnosesList () {
        
        ArrayList<String> diagnoses_list = new ArrayList<>();
        for (Case temp_case : this.cases)
            for (Dijagnoza temp_diag : temp_case.diagnoses) 
                if ((temp_diag.isCorrect()) && (diagnoses_list.contains(temp_diag.name)))
                    diagnoses_list.add(temp_diag.name);
        return diagnoses_list;
    }
    public ArrayList<Case> getCasesWithDiagnosis(String diagnosis) {
        ArrayList<Case> list = new ArrayList<>();
        for (Case case_from_base : this.cases)
            if (case_from_base.getCorrectDiagnosis().getName().equals(diagnosis))
                list.add(case_from_base);
        return list;
    }
    public ArrayList<Case> getCasesWithParameters(ArrayList<Parametar> parameters) {
        ArrayList<Case> return_cases_list = new ArrayList<>();
        for (Case temp_case : this.cases)
            if (temp_case.containsParameterValues(parameters))
                return_cases_list.add(temp_case);
        return return_cases_list;
    }
    public ArrayList<Case> getCasesWithDiagnosisAndParameters(String diagnosis, ArrayList<Parametar> parameters) {
        ArrayList<Case> cases_with_diagnosis = getCasesWithDiagnosis(diagnosis);
        ArrayList<Case> cases_with_parameters = getCasesWithParameters(parameters);
        ArrayList<Case> cases_with_parameters_and_parameters = new ArrayList<>();
        for (Case temp_case : cases_with_diagnosis)
            if (cases_with_parameters.contains(temp_case))
                cases_with_parameters_and_parameters.add(temp_case);
        return cases_with_parameters_and_parameters;
    }

    public ArrayList<Case> getExcluded_cases() {
        return excluded_cases;
    }

    public void setExcluded_cases(ArrayList<Case> excluded_cases) {
        this.excluded_cases = excluded_cases;
    }
    
    
    public ArrayList<String> getCaseIDs() {
        ArrayList<String> list = new ArrayList<>();
        for (Case temp_case : this.getCases())
            list.add(temp_case.getID());
        return list;
    }
    public Case getCase(int i) {return this.cases.get(i);}
    public ArrayList<Case> getCases() {return this.cases;}
    public ArrayList<String> getURL() {return this.url;}
    public void setCases(ArrayList<Case> cases) {this.cases = cases;}
    public void setUrl(ArrayList<String> url) {this.url = url;}
    public Case getCaseByID(String id) {
        for (Case temp_case : this.getCases())
            if (temp_case.getID().equals(id))
                return temp_case;
        return null;
    }
}
