/*
 * Ovo je klasa za case.
 * Tu definiram parametre koje case ima i omogucavam da se case ucita iz txt fajla. 
 */

import java.util.ArrayList;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import javax.faces.bean.ManagedBean;
import java.util.Scanner;
import javax.faces.bean.SessionScoped;

@ManagedBean (name ="assessmentcase", eager = true)
//@ViewScoped
@SessionScoped
public class Case implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // Parametri casea:
    String url;                                                     // URL case.txt fajla
    String introduction, task;                                      // Uvod i zadatak
    ArrayList<Dijagnoza> diagnoses = new ArrayList<>();                                               // Dijagnoza u kojem vec obliku
    ArrayList<Parametar> parameters = new ArrayList<>();             // Parametri

    public ArrayList<String> ParseTag(String text, String tag) {
        String temp_text = text;
        String start_tag = "<" + tag + ">";
        String end_tag = "</" + tag + ">";
        ArrayList<String> tag_values = new ArrayList<>();
        while (temp_text.indexOf(start_tag) != -1){
            String value = text.substring(text.indexOf(start_tag)+ start_tag.length(), text.indexOf(end_tag.length()) - 1);
            tag_values.add(value);
        }
        return tag_values;
    }

    
    public void SetCase(String url) throws IOException {

        url = url+"/case_1/case.txt";
        this.url = url;
    
        String case_file;                           //ucitaj cijeli fajl u case_file varijablu         
    
        try{
    
            InputStream inputStream = new URL(this.url).openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream ));
            case_file = new Scanner(reader).useDelimiter("\\A").next();                         // Sad imamo case fajl
        
            this.introduction = ParseTag(case_file, "Introduction").get(0);                            // Ovo su stavke s jedinstvenom string vrijednoscu
            this.task = ParseTag(case_file, "Task").get(0);                                            // Ovo su stavke s jedinstvenom string vrijednoscu
        
            ArrayList<String> temp_diagnoses = ParseTag(case_file, "Diagnoses");                           // Ove stavke mogu imati vise elemenata
            for (int i = 0; i < temp_diagnoses.size(); i++) {
            
                Dijagnoza temp_dijagnoza = new Dijagnoza();
            
                temp_dijagnoza.setName(ParseTag(temp_diagnoses.get(i), "Name").get(0));
                temp_dijagnoza.setCorrect(ParseTag(temp_diagnoses.get(i), "Correct").get(0));
            
                this.diagnoses.add(temp_dijagnoza);
            }

            ArrayList<String> temp_parameters = ParseTag(case_file, "Parameters");                         // Ove stavke mogu imati vise elemenata
            for (int i = 0; i < temp_parameters.size(); i++) {
           
                Parametar temp_parametar = new Parametar();
           
                temp_parametar.setName(ParseTag(temp_parameters.get(i), "Name").get(0));
                temp_parametar.setSynonims(ParseTag(temp_parameters.get(i), "Synonims").get(0));
                temp_parametar.setReal_value(ParseTag(temp_parameters.get(i), "Real value").get(0));
                temp_parametar.setAssigned_value(ParseTag(temp_parameters.get(i), "Assigned value").get(0));
                temp_parametar.setWeight(new BigDecimal(Float.parseFloat(ParseTag(temp_parameters.get(i), "Weight").get(0))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());  //http://stackoverflow.com/questions/2808535/round-a-double-to-2-significant-figures-after-decimal-point
                temp_parametar.setPrice(new BigDecimal(Float.parseFloat(ParseTag(temp_parameters.get(i), "Price").get(0))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());  //http://stackoverflow.com/questions/2808535/round-a-double-to-2-significant-figures-after-decimal-point

                this.parameters.add(temp_parametar);
            }
        } catch(Exception ex) {
            System.out.println("Error reading file '" + "'");					
        }

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


}
