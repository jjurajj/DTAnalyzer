package decisionTree;

/**
 * @author juraj
 * klasa koja opisuje DT koje ucita korisnik
 */

import singleCase.Dijagnoza;
import singleCase.CaseEvaluation;
import singleCase.Case;
import caseBase.CaseBase;
import static com.sun.corba.se.impl.util.Utility.printStackTrace;
import com.sun.faces.util.CollectionsUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean (name ="DT", eager = true)
@SessionScoped


// Pretpostavke:
// Stablo ima jedan početni čvor
// Smiju li se čvorovi ponavljati? Da, ali ako ih labeliram s #1 #2 #3 itd. I onda to treba implementirat u vrednovanje.
public class DT implements Serializable {
    
    public String start_node;                                                       // početni čvor
    public ArrayList<Proposition> propositions = new ArrayList<>();                 // lista svih propozicija
    public ArrayList<String> diagnoses = new ArrayList<>();                         // lista dijagnoza
    public HashMap<PropositionKey, String> propositionsMap= new HashMap<>();        // HashMap: ključ(string+value) -> idući čvor
    public HashMap<String, ArrayList<String>> reachable_diagnoses = new HashMap<>();// HashMap: trenutni cvor -> sve dostupne dijagnoze
    public HashMap<String, String> concepts_map= new HashMap<>();                     // HashMap: ključ(string+value) -> idući čvor
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Inicijalizacija stabla na temelju tekstualne datoteke koja ga opisuje
    public void initializeDT(String DT_text_file) {
  
      String tree_text = DT_text_file;                                      // plain text propozicije stabla s \t
      String input_type="txt";
      
      if (tree_text.contains("concept-list")) {input_type="cxl";}           // odredi format inputa
      
      ArrayList<String> left_concepts = new ArrayList<>();      // koncepti s lijeve strane (za odredivanje pocetnog cvora)
      ArrayList<String> right_concepts = new ArrayList<>();     // koncepti s desne strane
      
      String separator = "\r\n";                                // odredi newline znak
      if (tree_text.contains("\r\n")) {separator = "\r\n";}
      else if (tree_text.contains("\n")) {separator = "\n";}
      else if (tree_text.contains("\r")) {separator = "\r";}
      else {System.out.println("Unknown newline character!");} 
      
      this.propositions.clear();
      
      if (input_type=="txt") {
      
          String[] lines = tree_text.split(separator);              //splitaj tekst u linije propozicije

          for (String line : lines) {                               
            String[] p_split = line.split("\t");                  // svaku liniju pretvori u C1-L-C2
            left_concepts.add(p_split[0]);
            right_concepts.add(p_split[2]);
          
            this.propositions.add(new Proposition(p_split[0], p_split[1], p_split[2]));
            PropositionKey key = new PropositionKey(p_split[0], p_split[1]);
            this.propositionsMap.put(key, p_split[2]);            // propozicije stavi i u hashmapu
            
            if (!this.concepts_map.containsKey(p_split[0]))
                this.concepts_map.put(p_split[0],p_split[0]);
            if (!this.concepts_map.containsKey(p_split[2]))
                this.concepts_map.put(p_split[2],p_split[2]);
          }
      
      } else if (input_type=="cxl") {                             // Ako je cxl file onda nadi linije koje mi trebaju
          // concept-list 
          // linking-phrase-list
          // connection-list
          
          tree_text=tree_text.replace("&#xa;"," ");
          
          String concept_list=(String) tree_text.subSequence(tree_text.indexOf("<concept-list>")+15,tree_text.indexOf("</concept-list>")-1);
          String tmp_concept_lines=concept_list.substring(concept_list.indexOf("<concept id="),concept_list.lastIndexOf("/>")+2);
          String[] concept_lines=tmp_concept_lines.split(separator);   
          
          String link_list=(String) tree_text.subSequence(tree_text.indexOf("<linking-phrase-list")+21,tree_text.indexOf("</linking-phrase-list>")-1);
          String tmp_link_lines = link_list.substring(link_list.indexOf("<linking-phrase id="), link_list.lastIndexOf("/>")+2);
          String[] link_lines=tmp_link_lines.split(separator);   
          
          String connection_list=(String) tree_text.subSequence(tree_text.indexOf("<connection-list>")+18,tree_text.indexOf("</connection-list>")-1);
          String tmp_connection_lines = connection_list.substring(connection_list.indexOf("<connection id="),connection_list.lastIndexOf("/>")+2);
          String[] connection_lines=tmp_connection_lines.split(separator);
          
          for (String line : concept_lines) {
              if (line.contains("concept id=")) {
                  String id=(String) line.subSequence(line.indexOf("id=\"")+4, line.indexOf("\" "));
                  String label=(String) line.subSequence(line.indexOf("label=\"")+7, line.indexOf("\"/>"));
                  this.concepts_map.put(id, label);
              }
          }

          HashMap<String, String> links_map= new HashMap<>();
          for (String line : link_lines) {
              if (line.contains("linking-phrase id=")) {
                  String id=(String) line.subSequence(line.indexOf("id=\"")+4, line.indexOf("\" "));
                  String label=(String) line.subSequence(line.indexOf("label=\"")+7, line.indexOf("\"/>"));
                  links_map.put(id, label);
              }
          }
          
          ArrayList<String> left_con = new ArrayList<>();
          ArrayList<String> right_con = new ArrayList<>();
          for (String line : connection_lines) {
              if (line.contains("connection id=")) {
                  String from=(String) line.subSequence(line.indexOf("from-id=\"")+9, line.indexOf("\" to-id"));
                  String to=(String) line.subSequence(line.indexOf("to-id=\"")+7, line.indexOf("\"/>"));
                  left_con.add(from);
                  right_con.add(to);
              }
          }
          
          // pohranjujem propozicije u obliku concept1_id-link_name-concept2_id
          for (int i=0; i<left_con.size(); i++) {           // Prodi kroz sve lijeve koncepte
              String left=left_con.get(i);
              if (this.concepts_map.containsKey(left)) {    // Ako se s lijeve strane nalazi koncept a ne link
                  String right=right_con.get(left_con.indexOf(right_con.get(i)));
                  
                  left_concepts.add(left);
                  right_concepts.add(right);
              
                  this.propositions.add(new Proposition(left, links_map.get(right_con.get(i)), right));
                  PropositionKey key = new PropositionKey(left, links_map.get(right_con.get(i)));
                  this.propositionsMap.put(key, right);      // propozicije stavi i u hashmapu
              }
          }
      }
      
      for (String concept : left_concepts )                     // Odredimo korijen stabla
          if (right_concepts.contains(concept) == false) {this.setStart_node(concept);}
      
      ArrayList<String> leaves = new ArrayList<>();             // listovi stabla
      for (String concept : right_concepts)                     // su cvorovi koji nikad nisu lijevi
          if (left_concepts.contains(concept) == false) leaves.add(concept);
            this.setDiagnoses(leaves);
      
      // reachable diagnoses
      setReachableDiagnoses(start_node);
          
    }
    
    // Rekurzija koja postavi hashmap reachable_diagnoses za svu djecu zadanog cvora
    public ArrayList<String> setReachableDiagnoses (String node) {
        
        // Ako se radi o cvoru koji je konacna dijagnoza onda ga vrati
        if (this.diagnoses.contains(node)) {                        
            ArrayList<String> temp_list = new ArrayList();
            temp_list.add(node);
            return temp_list;
        // Ako nije terminalni cvor onda su njegove reachable dijagnoze one od sve njegove djece
        } else {
            ArrayList<String> next_nodes = new ArrayList<>();
            ArrayList<String> reachable_diagnoses = new ArrayList<>();
            next_nodes = getNextConcepts(node);                             // popis djece
            for (String child : next_nodes) {                               // za svako dijete
                ArrayList<String> temp = new ArrayList<>();
                temp = setReachableDiagnoses(child);                        // dobij dijagnoze za to dijete
                for (String temp_diag : temp) {                             // sve te dijagnoze dodaj u trenutne dijagnoze
                    if (! reachable_diagnoses.contains(temp_diag))
                        reachable_diagnoses.add(temp_diag);
                }
            }
            this.reachable_diagnoses.put(node, reachable_diagnoses);        // dodaj to u hashmap
            return reachable_diagnoses;
        }
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Analiza stabla na tebelju baze caseova
    // Vraca liste caseova (N, TP, FP, TN, FN, undiag) za svaku dijagnozu dostupnu u bazi
    public ArrayList<DiagnosisCount> evaluateTreeDecision (CaseBase base) {
    
        ArrayList<DiagnosisCount> diagnosis_count_al = new ArrayList<>();
        ArrayList<String> excluded = new ArrayList<>();
        HashMap<String, DiagnosisCount> diagnosis_count = new HashMap<>();
        
        for (Case temp_case : base.cases) {
            
            // Za case ispitamo sve njegove dijagnoze jer moze biti vise tocnih
            for (Dijagnoza temp_diag : temp_case.diagnoses) {
                
                // za svaku tocnu (ocekivano jedinu tocnu dijagnozu)
                if (temp_diag.isCorrect()) {
                    
                    // Statistika po dijagnozama
                    // ako ta dijagnoza vec postoji u hashmapu dodaj ju i potom u svakom slucaju dodaj joj trenutni case u total
                    if (!diagnosis_count.containsKey(temp_diag.name)) {     
                        diagnosis_count.put(temp_diag.name, new DiagnosisCount(temp_diag.name));
                    }
                    diagnosis_count.get(temp_diag.name).total.add(temp_case);
                    
                    // Evaluacija casea
                    // Evaluiraj trenutni case i povecaj odgovarajuce vrijednosti kod klasifikacije (N, Tp, TN, FP, FN, undiagnosed)
                    CaseEvaluation temp_eval = temp_case.evaluateCase(this);
                    if (!temp_eval.diagnosed) {                                             //ako se ne klsificira dodaj case u undiagnosed
                        diagnosis_count.get(temp_diag.name).undiagnosed.add(temp_case);     // i provjeri jel ta dijagnoza uopce postoji u stablu
                        if ((!this.diagnoses.contains(temp_diag.name)) && (!excluded.contains(temp_diag.name)))
                            excluded.add(temp_diag.name);
                    } else if (temp_eval.correct) {                                         // ako se klasifcira i to tocno dodaj u TP
                        diagnosis_count.get(temp_diag.name).TP.add(temp_case);
                    } else {                                                                 // ako se krivo klasificira
                        diagnosis_count.get(temp_diag.name).FN.add(temp_case);               // dodaj u FN za trenutnu dijagnozu
                        if (!diagnosis_count.containsKey(temp_eval.end_node)) {     
                            diagnosis_count.put(temp_eval.end_node, new DiagnosisCount(temp_eval.end_node));
                        }
                        diagnosis_count.get(temp_eval.end_node).FP.add(temp_case);           // povecaj FP za dijagnozu di se krivo klasificira
                    }
                    
                }
            }
        }
        
        // Sad mozda pretovrit Hashmap u araylist da lakse s njm baratam na webu http://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
        for (DiagnosisCount value : diagnosis_count.values()) {
            diagnosis_count_al.add(value);
        }

        return diagnosis_count_al;
    }

    // vracam niz propozicija i za svaku popis ostavljenih dobrih dijagnoza, dobro i krivo iskljucenih
    // pretpostavka: CB ima samo caseove koji se odnose na dijagnoze iz stabla
    public CaseEvaluation runCase (Case current_case) {
    
       // Tu bi trebao ici po propozicijama i za svaki novi cvor
       // naci listu caseova koji imaju do sad utvrdene parametre
       // naci popis dijagnoza tih caseova
       ArrayList<Proposition> prop_sequence = new ArrayList<>();
       CaseEvaluation case_eval = new CaseEvaluation();
       String next_concept = this.start_node;
       
       // key mi je za odredivanje iduceg cvora stabla (par trenutni cvor i vrijednost tog parametra u caseu)
       PropositionKey key; 
       key = new PropositionKey(next_concept, current_case.parametersMap.get(next_concept));

       // Prodemo po stablu dok ima sljedeceg koncepta u stablu i dok case ima parametar (kljuc) za njega
       while ((next_concept != null) && (key.concept != null)) {
           
           // Sad prolazim po caseu. Tu radim sve sto trebam raditi:
           // Sumiram cijenu
           // Pamtim koje su jos moguce / potrebne / discardible dijagnoze

           String prevoius_concept = next_concept;
           // Odredi iduci koncept
           case_eval.end_node = next_concept;                                                      // Do kud je case dosao
           key = new PropositionKey(next_concept, current_case.parametersMap.get(next_concept));   // Za taj cvor ispitaj vrijednost caseu
           next_concept = this.propositionsMap.get(key);                                           // Provjeri postoji li za to u stablu iduci cvor
            
           // Za taj koncept dohvati moguce dijagnoze na temelju stabla
           ArrayList<String> reachable_diagnoses = new ArrayList<>();
           reachable_diagnoses = this.reachable_diagnoses.get(next_concept);
           // A sad za taj skup parametara dohvati iz baze caseova popis mogucih dijagnoza
           ArrayList<String> possible_diagnoses = new ArrayList<>();
           possible_diagnoses = this.reachable_diagnoses.get(next_concept);      // ovo treba iskodirati
           
           // Dodaj u case eval:
           // Trenutnu propoziciju
           // Trenutno moguce dijagnoze
           // Potrebne dijagnoze. Tu jos treba rijesit uniju, razliku u presjek
           case_eval.path.add(new Proposition(key.concept, key.value, next_concept));
           case_eval.diags_per_node.add(reachable_diagnoses);
            
        }
        
        //ako je cvor u kojem je klasifikacija stala jedan od terminalnih cvorova stabla
        if (this.diagnoses.contains(case_eval.end_node)) {
            case_eval.diagnosed = true;
            // tu je greska...
            for (Dijagnoza temp_diag : current_case.diagnoses)
                if (temp_diag.getName().equals(case_eval.end_node))
                    case_eval.correct = temp_diag.isCorrect();
            
        }

        return case_eval;
    }
    
    // Vraca sve dijagnoze u koje se moze doci iz trenutnog cvora
    public ArrayList<String> getPossibleDiagnoses (String node) {
    
        ArrayList<String> diagnoses_list = new ArrayList<>();
        diagnoses_list.add(node);                               // na pocetku lista ima samo pocetni cvor
        String current_node = node;
        while (current_node != null) {                          // dok ima cvorova u listi
            for (Proposition temp_prop : this.propositions) {   // nadi svu djecu trenutnog cvora i dodaj ih u listu
                if (temp_prop.concept_one == current_node)
                    diagnoses_list.add(temp_prop.concept_two);
            }
            diagnoses_list.remove(current_node);                // makni trenutni cvor iz liste
            current_node = null;                                // postavi tenutni cvor na null
            for (String temp_node : diagnoses_list) {           // ako u listi postoji jos cvor koji nije terminalni, onda je on iduci
                if (!this.diagnoses.contains(temp_node))
                    current_node = temp_node;
            }
        }
        
        return diagnoses_list;
    }

    // Ovo vraća cvorove djecu od zadanog cvora
    public ArrayList<String> getNextConcepts(String start_concept){
        ArrayList<String> related_concepts = new ArrayList<>();
        for (Proposition temp_prop : this.propositions)
            if (temp_prop.getConcept_one().equals(start_concept))
                related_concepts.add(temp_prop.concept_two);
        return related_concepts;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getteri i setteri od svojstava klase:
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
    public HashMap<String, ArrayList<String>> getReachable_diagnoses() {
        return reachable_diagnoses;
    }
    public String getName(String id) {
        if (this.concepts_map.containsKey(id))
            return this.concepts_map.get(id);
        else
            return "Concetp name not found.";
    }
    
    public void setReachable_diagnoses(HashMap<String, ArrayList<String>> reachable_diagnoses) {
        this.reachable_diagnoses = reachable_diagnoses;
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
    
}