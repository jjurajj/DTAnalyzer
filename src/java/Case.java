/*
 * Ovo je klasa za case.
 *
 * Tu definiram parametre koje case ima i omogucavam da se case ucita iz txt fajla. 
 *
 * Ovo bi trebala bit robustna klasa za ucitavanje casea, a poslije iz njega uzimam
 * specificno elemente koji mi trebaju za pojedinu primjenu.
 */

import java.util.ArrayList;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import javax.faces.bean.ManagedBean;
import java.util.Scanner;
import javax.faces.bean.SessionScoped;

@ManagedBean (name ="assessmentcase", eager = true)
//@ViewScoped
@SessionScoped
public class Case implements Serializable {

    //private static final long serialVersionUID = 1L;
    
    // Parametri casea:
    public String url;
    public String text; // URL case.txt fajla
    public String introduction, task, explanation;                                      // Uvod i zadatak
    public ArrayList<Dijagnoza> diagnoses = new ArrayList<Dijagnoza>();                                               // Dijagnoza u kojem vec obliku
    public ArrayList<Parametar> parameters = new ArrayList<Parametar>();             // Parametri
    public HashMap<String, String> CaseMap= new HashMap<String, String>();

    public void initializeCase(String url) throws IOException {

        this.url = url;
    
        try{
            
            String case_file;                           //ucitaj cijeli fajl u case_file varijablu         
            InputStream inputStream = new URL(this.url).openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream ));
            case_file = new Scanner(reader).useDelimiter("\\A").next();                         // Sad imamo case fajl
            this.text = case_file;
            
            //Tagovi prve razine: introduction, task, parameters, diagnoses, explanation
            String introduction = parseTag(case_file, "Introduction").get(0);
            String task = parseTag(case_file, "Task").get(0);
            String parameters = parseTag(case_file, "Parameters").get(0);
            String diagnoses = parseTag(case_file, "Diagnoses").get(0);
            String explanation = parseTag(case_file, "Explanation").get(0);

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
                
                CaseMap.put(temp_parametar.name, temp_parametar.assigned_value);
            }
            
            //Diagnoses
            ArrayList<String> temp_diagnoses = parseTag(diagnoses, "Diagnosis");                           // Ove stavke mogu imati vise elemenata
            for (String current_diagnosis : temp_diagnoses) {
                Dijagnoza temp_dijagnoza = new Dijagnoza();
                temp_dijagnoza.setName(parseTag(current_diagnosis, "Name").get(0));
                temp_dijagnoza.setCorrect(parseTag(current_diagnosis, "Correct").get(0));
                this.diagnoses.add(temp_dijagnoza);
            }
            
            //Explanation
            this.explanation = explanation;
            
        } catch(Exception ex) {
            System.out.println("Error reading file '" + "'");					
        }

    }
    public String getCasePlainText() throws MalformedURLException {
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
    }

    public String evaluateCase(DT tree) {
        String diagnosis = "";
        String next_concept = tree.start_node;
        while (next_concept != null) {
            PropositionKey key = new PropositionKey(next_concept, CaseMap.get(next_concept));
            diagnosis = next_concept;
            next_concept = tree.DTMap.get(key);
        }
        return diagnosis;
    }
    
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
            tag_values.add("");
        }
        return tag_values;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public HashMap<String, String> getCaseMap() {
        return CaseMap;
    }

    public void setCaseMap(HashMap<String, String> CaseMap) {
        this.CaseMap = CaseMap;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
}
