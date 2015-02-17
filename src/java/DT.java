/**
 * @author juraj
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean (name ="DT", eager = true)
@SessionScoped

public class DT implements Serializable {
    
    public String start_node;
    public ArrayList<Proposition> propositions = new ArrayList<>(); 
    public ArrayList<String> diagnoses = new ArrayList<>();
    public HashMap<PropositionKey, String> propositionsMap= new HashMap<>();
    
    public void initializeDT(String DT_text_file) {
  
      ArrayList<String> left_concepts = new ArrayList<>();      // koncepti s lijeve strane
      ArrayList<String> right_concepts = new ArrayList<>();     // koncepti s desne strane
      String tree_text = DT_text_file;                           // plain text propozicije stabla s \t
      
      String separator = "\r\n";                                // odredi newline znak
      if (tree_text.contains("\r\n")) {separator = "\r\n";}
      else if (tree_text.contains("\n")) {separator = "\n";}
      else if (tree_text.contains("\r")) {separator = "\r";}
      else {System.out.println("Unknown newline character!");} 
      
      String[] lines = tree_text.split(separator);              //splitaj tekst u linije propozicije
      
      for (String line : lines) {                               
          
          String[] p_split = line.split("\t");                  // svaku liniju pretvori u C1-L-C2
          left_concepts.add(p_split[0]);
          right_concepts.add(p_split[2]);
          
          this.propositions.add(new Proposition(p_split[0], p_split[1], p_split[2]));
          PropositionKey key = new PropositionKey(p_split[0], p_split[1]);
          this.propositionsMap.put(key, p_split[2]);            // propozicije stavi i u hashmapu
      }
      
      for (String concept : left_concepts )                     // Odredimo korijen stabla
          if (right_concepts.contains(concept) == false) {this.setStart_node(concept);}
      
      ArrayList<String> leaves = new ArrayList<>();             // listovi stabla
      for (String concept : right_concepts)                     // su cvorovi koji nikad nisu lijevi
          if (left_concepts.contains(concept) == false) leaves.add(concept);
      this.setDiagnoses(leaves);
      
    }
    
    public String getStart_node() {
        return start_node;
    }
    public ArrayList<Proposition> getPropositions() {
        return propositions;
    }
    public ArrayList<String> getDiagnoses() {
        return diagnoses;
    }
    public HashMap<PropositionKey, String> getPropositionsMap() {
        return propositionsMap;
    }
        
    public void setStart_node(String start_node) {
        this.start_node = start_node;
    }
    public void setDiagnoses(ArrayList<String> diagnoses) {
        this.diagnoses = diagnoses;
    }
    public void setPropositions(ArrayList<Proposition> propositions) {
        this.propositions = propositions;
    }
    public void setPropositionsMap (HashMap<PropositionKey, String> propositionsMap) {
        this.propositionsMap = propositionsMap;
    }
    
    // Ovo vraća propozicije koje imaju zadani pocetni koncept
    public ArrayList<Proposition> getNextConcepts(String start_concept){
    
        ArrayList<Proposition> related_props = new ArrayList<>();
        
        for (int i = 0; i < this.propositions.size(); i++)
            if (this.propositions.get(i).getConcept_one().equals(start_concept))
                related_props.add(this.propositions.get(i));
        return related_props;
    }
    
    // Ovo vraca listove u koje se moze doci iz trenutnog cvora. Reimplementirat s hashmapom
    public ArrayList<String> getLeavesConcepts(String start_concept){

        ArrayList<String> available_leaves = new ArrayList<>();
        ArrayList<Proposition> related_props = getNextConcepts(start_concept);
        
        // Ako se radi o listu
        if (related_props.size() == 0) {
            available_leaves.add(start_concept);
            return available_leaves;
        }
        
        // Ako ima nekih podcvorova
        while (related_props.size() > 0){
            // Razvij jedan cvor: makni ga iz liste i dodaj njegovu djecu
            Proposition current_prop = related_props.get(0);
            related_props.remove(current_prop);
            ArrayList<Proposition> new_props = getNextConcepts(current_prop.concept_two);
            if (new_props.size() == 0)
                available_leaves.add(current_prop.concept_two);
            else
                related_props.addAll(getNextConcepts(current_prop.concept_two));
        }
        return available_leaves;

    }
    
}