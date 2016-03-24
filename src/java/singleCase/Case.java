package singleCase;

/*
 * Ovo je klasa za case.
 *
 * Tu definiram parametre koje case ima i omogucavam da se case ucita iz txt fajla. 
 *
 * Ovo bi trebala bit robustna klasa za ucitavanje casea, a poslije iz njega uzimam
 * specificno elemente koji mi trebaju za pojedinu primjenu.
 */

import decisionTree.PropositionKey;
import decisionTree.DT;
import java.util.ArrayList;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import java.util.Scanner;
import javax.faces.bean.SessionScoped;

@ManagedBean (name ="Case", eager = true)
//@ViewScoped
@SessionScoped
public class Case implements Serializable {

    //private static final long serialVersionUID = 1L;
    
    // Parametri casea:
    public String id, url, text, introduction, task, explanation;
    public ArrayList<Dijagnoza> diagnoses = new ArrayList<>();      // DijagnozE - u kojem vec obliku - ukljucuju info o tocnosti
    public ArrayList<Parametar> parameters = new ArrayList<>();     // Parametri
    public HashMap<String,String> parametersMap = new HashMap<>();  // HMap parametara <ime vrijednost> za parametre casea
    public CaseEvaluation evaluation = new CaseEvaluation();
    
    
    /////////////////////////////////////////
    //Konktrukcija i inicijalizacija
    /////////////////////////////////////////
    
    // Prazni konstruktor
    public Case() {
    
        //this = Case("http://diana.zesoi.fer.hr/~jpetrovic/case_repository/car_starting/case_1/case.txt");
        //Case("http://diana.zesoi.fer.hr/~jpetrovic/case_repository/car_starting/case_1/case.txt");
    }

    // Inicijalizacija na temelju defaultnog linka ili zadanog
    public void initialize() {
    }
    public void initializeCase(String url) throws IOException {

        this.url = url;
    
        try{
            
            String case_file;                           //ucitaj cijeli fajl u case_file varijablu         
            InputStream inputStream = new URL(this.url).openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream ));
            case_file = new Scanner(reader).useDelimiter("\\A").next();                         // Sad imamo case fajl
            this.text = case_file;
            
            //Tagovi prve razine: introduction, task, parameters, diagnoses, explanation
            String id = parseTag(case_file, "ID").get(0);
            String introduction = parseTag(case_file, "Introduction").get(0);
            String task = parseTag(case_file, "Task").get(0);
            String parameters = parseTag(case_file, "Parameters").get(0);
            String diagnoses = parseTag(case_file, "Diagnoses").get(0);
            String explanation = parseTag(case_file, "Explanation").get(0);

            //ID
            this.id =id;
            
            //Introduction
            this.introduction =introduction;
            
            //Task
            this.task = task;
        
            // Parameters
            ArrayList<String> temp_parameters = parseTag(case_file, "Parameter");                         // Ove stavke mogu imati vise elemenata
            for (String temp_parameter : temp_parameters) {
           
                Parametar temp_parametar = new Parametar();
       
                temp_parametar.setName(parseTag(temp_parameter, "Name").get(0));
                temp_parametar.setSynonims(parseTag(temp_parameter, "Synonims").get(0));
                temp_parametar.setReal_value(parseTag(temp_parameter, "Real value").get(0));
                temp_parametar.setAssigned_value(parseTag(temp_parameter, "Assigned value").get(0));
                try {
                    temp_parametar.setWeight(new BigDecimal(Float.parseFloat(parseTag(temp_parameter, "Weight").get(0))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());  //http://stackoverflow.com/questions/2808535/round-a-double-to-2-significant-figures-after-decimal-point
                } catch (NullPointerException|NumberFormatException e2) {
                    temp_parametar.setWeight(-999);
                }
                try {
                    temp_parametar.setPrice(new BigDecimal(Float.parseFloat(parseTag(temp_parameter, "Price").get(0))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());  //http://stackoverflow.com/questions/2808535/round-a-double-to-2-significant-figures-after-decimal-point
                } catch (NullPointerException|NumberFormatException e2) {
                    temp_parametar.setPrice(-999);
                }       
                    
                this.parameters.add(temp_parametar);
                
                parametersMap.put(temp_parametar.name, temp_parametar.assigned_value);
            }
            
            //Diagnoses
            ArrayList<String> temp_diagnoses = parseTag(diagnoses, "Diagnosis");                           // Ove stavke mogu imati vise elemenata
            for (String current_diagnosis : temp_diagnoses) {
                Dijagnoza temp_dijagnoza = new Dijagnoza();
                temp_dijagnoza.setName(parseTag(current_diagnosis, "Name").get(0));
                String correct = parseTag(current_diagnosis, "Correct").get(0);
                if (correct.toLowerCase().contains("true") || correct.toLowerCase().contains("yes")) {
                    temp_dijagnoza.setCorrect(true);
                }
                this.diagnoses.add(temp_dijagnoza);
            }
            
            //Explanation
            this.explanation = explanation;
            
        } catch(Exception ex) {
            System.out.println("Error reading file '" + "'");					
        }

    }

    // Tag parser. Tragi vrijednost taga u datoteci (Stringu)
    private ArrayList<String> parseTag(String text, String tag) {
        String temp_text = text;
        String start_tag = "<" + tag + ">";
        String end_tag = "</" + tag + ">";
        ArrayList<String> tag_values = new ArrayList<>();
        while (temp_text.indexOf(start_tag) != -1){
            int start_index = temp_text.indexOf(start_tag)+ start_tag.length();
            int end_index = temp_text.indexOf(end_tag);
            String value = temp_text.substring(start_index, end_index);
            temp_text = temp_text.substring(end_index+end_tag.length());
            tag_values.add(value);
        }
        if (tag_values.isEmpty()) {
            tag_values.add("Undefined");
        }
        return tag_values;
    }

    
    
    /////////////////////////////////////////
    // Evaluacija casea
    /////////////////////////////////////////
    
    public CaseEvaluation evaluateCase(DT tree) {
        
        CaseEvaluation eval = new CaseEvaluation();
        
        String next_concept = tree.start_node;                                                  // pocetni cvor stabla
        PropositionKey key; 
        key = new PropositionKey(next_concept, parametersMap.get(next_concept));
        
        // Provrti case po stablu
        while ((next_concept != null) && (key.concept != null)) {                       // prodi po stablu: sumiraj cijene i dodi do kraja
            
            // Odredi iduci koncept
            eval.end_node = next_concept;                                               // Odi u iduci cvor stabla
            key = new PropositionKey(next_concept, parametersMap.get(next_concept));    // Za taj cvor ispitaj vrijednost caseu
            next_concept = tree.propositionsMap.get(key);                               // Provjeri postoji li za to u stablu iduci cvor
            
            // Za taj koncept odredi moguce dijagnoze
            ArrayList<String> possible_diagnoses = tree.getPossibleDiagnoses(next_concept);
            
            
            
        }
        
        //ako je cvor u kojem je klasifikacija stala jedan od terminalnih cvorova stabla
        if (tree.diagnoses.contains(eval.end_node)) {
            eval.diagnosed = true;
            // tu je greska...
            for (Dijagnoza temp_diag : this.diagnoses)
                if (temp_diag.getName().equals(eval.end_node))
                    eval.correct = temp_diag.isCorrect();
            
        }
        
        return eval;
    }
    
    
    
    
    /////////////////////////////////////////
    // Getteri i setteri
    /////////////////////////////////////////
    
    public void setUrl(String url) {
        this.url = url;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public void setTask(String task) {
        this.task = task;
    }
    public void setParameters(ArrayList<Parametar> parameters) {
        this.parameters = parameters;
    }
    public void setDiagnoses(ArrayList<Dijagnoza> diagnoses) {
        this.diagnoses = diagnoses;
    }
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setParametersMap(HashMap<String, String> parametersMap) {
        this.parametersMap = parametersMap;
    }
    public void setEvaluation(CaseEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    
    // Vraća PRVU točnu dijagnozu za case ili prazni objekt Dijagnoza ako nista ne nade
    public Dijagnoza getCorrectDiagnosis() {
    
        for (Dijagnoza correct_diagnosis : this.diagnoses ) 
            if (correct_diagnosis.correct) return correct_diagnosis;
        
        // Ako nista nisi nasao onda
        Dijagnoza error_diagnosis = new Dijagnoza();
        return error_diagnosis;
    }
            
    public CaseEvaluation getEvaluation() {
        return evaluation;
    }
    public String getUrl() {
        return url;
    }
    public String getIntroduction() {
        return introduction;
    }
    public String getTask() {
        return task;
    }
    public ArrayList<Parametar> getParameters() {
        return this.parameters;
    }
    public ArrayList<Dijagnoza> getDiagnoses() {
        return this.diagnoses;
    }
    public String getExplanation() {
        return explanation;
    }
    public String getText() {
        return text;
    }
    public HashMap<String, String> getParametersMap() {
        return parametersMap;
    }
    
    
    
    
    /*public String getCasePlainText() throws MalformedURLException {
        String text = "";
        URL case_url;
        InputStream stream = null;
        DataInputStream dis;
        String line;
      
        try {   //http://alvinalexander.com/java/edu/pj/pj010011
            case_url = new URL(this.url);
            stream = case_url.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(case_url.openStream()));
            while ((line = in.readLine()) != null) {
                text = text.concat(line).concat("\r\n");
            }
            in.close();
            return text;
        }
        catch (MalformedURLException URLe) {
            return "NULL";
        }
        catch (IOException IOe) {
            return "NULL";
        }
    }*/

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Case other = (Case) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
