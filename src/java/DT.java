/**
 * @author juraj
 */

import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean (name ="DT", eager = true)
@SessionScoped

public class DT implements Serializable {
    
    public String start_node;
    public ArrayList<Proposition> propositions = new ArrayList<>(); 
    public ArrayList<String> diagnoses = new ArrayList<>();

public void initializeDT(String fileContent) {
  
      // tu bi trebalo ucitat fajl liniju po liniju, provjerit jel okej 
      // sadrzaj je u fileContent
      DT MyTree = new DT();
      
      ArrayList<Proposition> props = new ArrayList<>(); 
      ArrayList<String> left_concepts = new ArrayList<>(), right_concepts = new ArrayList<>();
      
      String line = "", tree_text = fileContent;
      
      // Pohvatamo sve propozicije
      while (!tree_text.contentEquals("")) {
          
          Proposition prop = new Proposition();
          
          if (tree_text.contains("\r\n")) {
              line = tree_text.substring(0,tree_text.indexOf("\r\n"));
              tree_text = tree_text.substring(tree_text.indexOf("\r\n")+2);
          } else if (tree_text.contains("\n")) {
              line = tree_text.substring(0,tree_text.indexOf("\n"));
              tree_text = tree_text.substring(tree_text.indexOf("\n")+1);
          } else { System.out.println("Unknown newline character!"); }
          
          prop.concept_one = line.substring(0,line.indexOf("\t"));
          prop.link = line.substring(line.indexOf("\t")+1,line.lastIndexOf("\t"));
          prop.concept_two = line.substring(line.lastIndexOf("\t")+1);
          
          left_concepts.add(prop.concept_one);
          right_concepts.add(prop.concept_two);
          
          props.add(prop);
      }
      MyTree.setPropositions(props);
      
      // Odredimo korijen
      int i = 0;
      while (right_concepts.contains(left_concepts.get(i++))) {}
      if (right_concepts.contains(left_concepts.get(i-1))== false) {
              MyTree.setStart_node(left_concepts.get(i-1));
          }
      else {
          // Error - nema korijenskog cvora
      }
      
      // Odredimo listove - pregledavamo sve desne cvorove i gledamo koji od njih ni jednom nije lijevi
      ArrayList<String> leaves = new ArrayList<>();
      for (i=0; i<right_concepts.size(); i++)
          if (left_concepts.contains(right_concepts.get(i)) == false)
              leaves.add(right_concepts.get(i));
      MyTree.setDiagnoses(leaves);
      
      this.start_node = MyTree.start_node;
      this.propositions = MyTree.propositions;
      this.diagnoses = MyTree.diagnoses;
      
    }

    public String getStart_node() {
        return start_node;
    }

    public void setStart_node(String start_node) {
        this.start_node = start_node;
    }

    public ArrayList<Proposition> getPropositions() {
        return propositions;
    }

    public void setPropositions(ArrayList<Proposition> propositions) {
        this.propositions = propositions;
    }

    public ArrayList<String> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(ArrayList<String> diagnoses) {
        this.diagnoses = diagnoses;
    }
    
    // Ovo vraća propozicije koje imaju zadani pocetni koncept
    public ArrayList<Proposition> getNextConcepts(String start_concept){
    
        ArrayList<Proposition> related_props = new ArrayList<>();
        
        for (int i = 0; i < this.propositions.size(); i++)
            if (this.propositions.get(i).getConcept_one().equals(start_concept))
                related_props.add(this.propositions.get(i));
        return related_props;
    }
    
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