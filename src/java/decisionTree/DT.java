package decisionTree;

/**
 * @author juraj
 * klasa koja opisuje DT koje ucita korisnik
 * Pretpostavke:
 * Stablo ima jedan početni čvor
 *  Čvorovi stabla zadani su s IDjem. zato jer se mogu ponavljati nazivi. 
*/

import c45.ParameterGainRatio;
import singleCase.Dijagnoza;
import singleCase.CaseEvaluation;
import singleCase.Case;
import caseBase.CaseBase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import singleCase.Parametar;

@ManagedBean (name ="DT", eager = true)
@ViewScoped

public class DT implements Serializable {
    
    public String start_node;                                                       // ID početnog čvora
    public ArrayList<Proposition> propositions = new ArrayList<>();                 // Lista svih propozicija (ID, vrijednost linka, ID)
    //public ArrayList<Proposition> active_tree = new ArrayList<>();                  // Reprezentacija stabla za aktivni prikaz
    
    public ArrayList<String> diagnoses = new ArrayList<>();                         // Lista ID-jeva dijagnoza
    public HashMap<PropositionKey, String> propositionsMap= new HashMap<>();        // HashMap: ID koncepta + value -> ID idućeg koncepta
    public HashMap<String, ArrayList<String>> reachable_diagnoses = new HashMap<>();// HashMap: ID koncepta -> Sve dostupne dijagnoze
    public HashMap<String, String> concepts_map= new HashMap<>();                   // HashMap: ID koncepta/dijagnoze -> Ime koncepta/dijagnoze

    public DT() {}
    public DT copyTree(DT tree) {
        
        DT new_tree = new DT();
        new_tree.start_node = new String(tree.getStartNodeID());
        new_tree.propositions = new ArrayList(tree.getPropositions());
        
        new_tree.diagnoses = new ArrayList( tree.getDiagnoses());
        new_tree.propositionsMap = new HashMap(tree.getPropositionsMap());
        new_tree.reachable_diagnoses = new HashMap(tree.getReachableDiagnoses());
        new_tree.concepts_map = new HashMap(tree.getConceptsMap());
        
        return new_tree;
    }
    
    
    
    public void initializeFromFile(String DT_text_file) {
  
      // Inicijalizacija stabla na temelju tekstualne datoteke (propozicije ili CXL)
        
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
          
          String concept_list=(String) tree_text.subSequence(tree_text.indexOf("<concept-list>")+14,tree_text.indexOf("</concept-list>"));
          String tmp_concept_lines=concept_list.substring(concept_list.indexOf("<concept id="),concept_list.lastIndexOf("/>")+2);
          String[] concept_lines=tmp_concept_lines.split("/>");   
          
          String link_list=(String) tree_text.subSequence(tree_text.indexOf("<linking-phrase-list")+20,tree_text.indexOf("</linking-phrase-list>"));
          String tmp_link_lines = link_list.substring(link_list.indexOf("<linking-phrase id="), link_list.lastIndexOf("/>")+2);
          String[] link_lines=tmp_link_lines.split("/>");   
          
          String connection_list=(String) tree_text.subSequence(tree_text.indexOf("<connection-list>")+17,tree_text.indexOf("</connection-list>"));
          String tmp_connection_lines = connection_list.substring(connection_list.indexOf("<connection id="),connection_list.lastIndexOf("/>")+2);
          String[] connection_lines=tmp_connection_lines.split("/>");
          
          // Purge connections for loops
          
          
          for (String line : concept_lines) {
              if (line.contains("id=")) {
                  String id=(String) line.subSequence(line.indexOf("id=\"")+4, line.indexOf("\" "));
                  String label=(String) line.subSequence(line.indexOf("label=\"")+7, line.lastIndexOf("\""));
                  this.concepts_map.put(id, label);
              }
          }

          HashMap<String, String> links_map= new HashMap<>();
          for (String line : link_lines) {
              if (line.contains("id=")) {
                  String id=(String) line.subSequence(line.indexOf("id=\"")+4, line.indexOf("\" "));
                  String label=(String) line.subSequence(line.indexOf("label=\"")+7, line.lastIndexOf("\""));
                  links_map.put(id, label);
              }
          }
          
          ArrayList<String> left_con = new ArrayList<>();
          ArrayList<String> right_con = new ArrayList<>();
          String[] tmp_connections = connection_lines;
          for (String line : connection_lines) {
              if (line.contains("id=")) {
                  String from=(String) line.subSequence(line.indexOf("from-id=\"")+9, line.indexOf("\" to-id"));
                  String to=(String) line.subSequence(line.indexOf("to-id=\"")+7, line.lastIndexOf("\""));
                  // Check for loops (errors most likely)
                  boolean loop = false;
                  for (String tmp_line : tmp_connections)
                      if ((tmp_line.indexOf("to-id=\"".concat(from))>0) && (tmp_line.indexOf("from-id=\"".concat(to))>0))
                          loop=true;
                  if (loop) {
                      //links_map.remove(from);
                  } else {
                      left_con.add(from);
                      right_con.add(to);
                  }
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
          
          
          int k=0;
      }
      
      //for (String concept : left_concepts )                     // Odredimo korijen stabla
      //    if (right_concepts.contains(concept) == false) {this.setStartNode(concept);}
      
      /*ArrayList<String> leaves = new ArrayList<>();             // listovi stabla
      for (String concept : right_concepts)                     // su cvorovi koji nikad nisu lijevi
          if ((left_concepts.contains(concept) == false) && (leaves.contains(concept)==false)) leaves.add(concept);
      this.setDiagnoses(leaves);
      
      // reachable diagnoses
      */
      
      this.setDiagnoses(this.determineDiagnoses(this.propositions));
      this.setStartNode(this.determineStartNode(this.propositions));
      
      setReachableDiagnoses(start_node);
      
      //expandLeaves();
    }

    // U svaki initializer treba dodat da se azuriraju IDjevi SVIH koncepata
    
    private ArrayList<String> determineDiagnoses(ArrayList<Proposition> tree_propositions) {
        ArrayList<String> left_concepts = new ArrayList<>(), right_concepts = new ArrayList<>(), diagnoses = new ArrayList<>();
        for (Proposition prop : tree_propositions) {
            left_concepts.add(prop.getConcept_one());
            right_concepts.add(prop.getConcept_two());
        }
        for (String concept : right_concepts)
            if ((left_concepts.contains(concept) == false) && (diagnoses.contains(concept)==false))
                diagnoses.add(concept);
        return diagnoses;
    }
    private String determineStartNode(ArrayList<Proposition> tree_propositions) {
        ArrayList<String> left_concepts = new ArrayList<>(), right_concepts = new ArrayList<>();
        for (Proposition prop : tree_propositions) {
            left_concepts.add(prop.getConcept_one());
            right_concepts.add(prop.getConcept_two());
        }
        for (String concept : left_concepts )                     // Odredimo korijen stabla
            if (right_concepts.contains(concept) == false)
                return concept;
        return "Undefined";
    }
    
    
    public void initializeOptimal(CaseBase base, HashMap<String, String> concepts_map) {
    
        setConceptsMap(concepts_map);
        ArrayList<Proposition> c45_propositions = this.C45(base.cases);

        setPropositions(c45_propositions);
        setStartNode(this.determineStartNode(c45_propositions));
        setDiagnoses(this.determineDiagnoses(c45_propositions));
        for (Proposition prop : c45_propositions) this.propositionsMap.put(new PropositionKey (prop.getConcept_one(), prop.getLink()), prop.getConcept_two());
        setReachableDiagnoses(this.getStartNodeID());
    
    }
    
    public ArrayList<DiagnosisCount> evaluateTreeDecision (CaseBase base) {
    // Analiza stabla na tebelju baze caseova vraca liste caseova (N, TP, FP, TN, FN, undiag) za svaku dostupnu dijagnozu
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
                    //CaseEvaluation temp_eval = temp_case.evaluateCase(this);
                    CaseEvaluation temp_eval = temp_case.evaluation;
                            
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
    private ArrayList<String> setReachableDiagnoses (String node) {
        // Rekurzija koja postavi hashmap reachable_diagnoses za svu djecu zadanog cvora. Poziva se iz inicijalizatora DTa
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
    public CaseEvaluation runCase (Case current_case) {
        // vracam niz propozicija i za svaku popis ostavljenih dobrih dijagnoza, dobro i krivo iskljucenih
        // pretpostavka: CB ima samo caseove koji se odnose na dijagnoze iz stabla

       // Tu bi trebao ici po propozicijama i za svaki novi cvor
       // naci listu caseova koji imaju do sad utvrdene parametre
       // naci popis dijagnoza tih caseova
       ArrayList<Proposition> prop_sequence = new ArrayList<>();
       CaseEvaluation case_eval = new CaseEvaluation();
       String next_concept = this.start_node;
       
       // key mi je za odredivanje iduceg cvora stabla (par trenutni cvor i vrijednost tog parametra u caseu)
       PropositionKey key;
       key = getCaseIndependentPropositionKey(next_concept, current_case.parametersMap.get(getNodeNameFromID(next_concept)));
       
       // Prodemo po stablu dok ima sljedeceg koncepta u stablu i dok case ima parametar (kljuc) za njega
       String end_concept=next_concept;
       while ((next_concept != null) && (key.concept != null)) {
           
           // Sad prolazim po caseu. Tu radim sve sto trebam raditi:
           // Sumiram cijenu
           // Pamtim koje su jos moguce / potrebne / discardible dijagnoze

           String prevoius_concept = next_concept;
           end_concept=next_concept;
           // Odredi iduci koncept
           case_eval.end_node = getNodeNameFromID(next_concept);                                                      // Do kud je case dosao
           key = getCaseIndependentPropositionKey(next_concept, current_case.parametersMap.get(getNodeNameFromID(next_concept)));   // Za taj cvor ispitaj vrijednost caseu
           next_concept = this.propositionsMap.get(key);                                                    // Provjeri postoji li za to u stablu iduci cvor
            
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
           case_eval.path_name.add(new Proposition(getNodeNameFromID(key.concept), key.value, getNodeNameFromID(next_concept)));
           
           case_eval.diags_per_node.add(reachable_diagnoses);
            
        }
        
        //ako je cvor u kojem je klasifikacija stala jedan od terminalnih cvorova stabla
        if (this.diagnoses.contains(end_concept)) {
            case_eval.diagnosed = true;
            // tu je greska...
            for (Dijagnoza temp_diag : current_case.diagnoses)
                if (temp_diag.getName().equals(case_eval.end_node))
                    case_eval.correct = temp_diag.isCorrect();
            
        }

        return case_eval;
    }
    private PropositionKey getCaseIndependentPropositionKey(String concept, String value) {

        PropositionKey real_key = new PropositionKey(concept, value);
        if ((concept == null) || (value == null) || (this.propositionsMap.containsKey(real_key))) return real_key;
        else {
            for (PropositionKey key : this.propositionsMap.keySet())
                if (key.concept.equals(concept))
                    if (key.value.toLowerCase().trim().equals(value.toLowerCase().trim()))
                        value=key.value;
            return new PropositionKey(concept, value);
        }
    }
    public ArrayList<String> getElements(String text, String element) {
        ArrayList elements = new ArrayList<>();
        String end_indicator="\"/>";
        
        while (text.indexOf("<".concat(element)) > -1) {
            int start_index=text.indexOf("<".concat(text));
            int end_index=text.indexOf(end_indicator)+end_indicator.length();
            elements.add(text.substring(start_index,end_index));
            text=text.substring(end_index);
        }
        
        return elements;
        
    }
    public ArrayList<Proposition> getChildrenPropositions(String concept_ID) {
        ArrayList children_propositions = new ArrayList<>();
        for (Proposition proposition : this.propositions)
            if (proposition.getConcept_one().equals(concept_ID))
                    children_propositions.add(proposition);
        return children_propositions;
    }
    public int getNodeDepth(String node) {

        int depth=0;
        ArrayList<String> active_nodes=new ArrayList<>();
        active_nodes.add(this.start_node);
        ArrayList<Integer> all_depths=new ArrayList<Integer>();
        
        while (active_nodes.size()>0) {
            ArrayList<String> temp_active_nodes=new ArrayList<>();
            depth++;
            for (String active_node : active_nodes)
                for (Proposition proposition : this.propositions)
                    if (proposition.concept_one.equals(active_node))
                        temp_active_nodes.add(proposition.concept_two);
                
            if (temp_active_nodes.contains(node))
                all_depths.add(depth);
            active_nodes=temp_active_nodes;
        }
        
        Integer final_depth=0;
        for (Integer max_depth : all_depths)
            if (max_depth>final_depth)
                final_depth=max_depth;
        
        return final_depth;
    }
    private ArrayList<String> getLeaves(ArrayList<Proposition> propositions) {
    // Prima listu propozicija, vraca listu IDjeva cvorova koji su listovi (ili praznu listu)
        // Listovi su koncepti koji se u propozicijama nalaze samo s desne strane
        // Tu U listu dodam desni i maknem lijevi cvor za sve propozicije
        
        ArrayList<String> leaves = new ArrayList<>();
        if (propositions.size()==0) return leaves;
        
        for (Proposition proposition : propositions) {
            leaves.add(proposition.concept_two);
            leaves.remove(proposition.concept_one);
        }
        
        return leaves;
    }
    public ArrayList<String> getReachableDiagnoses (String node_ID) {
        // Vraca sve dijagnoze u koje se moze doci iz trenutnog cvora
        ArrayList<String> diagnoses_list = new ArrayList<>();
        diagnoses_list.add(node_ID);                               // na pocetku lista ima samo pocetni cvor
        String current_node = node_ID;
        while (current_node != null) {                          // dok ima cvorova u listi
            for (Proposition temp_prop : this.propositions) {   // nadi svu djecu trenutnog cvora i dodaj ih u listu
                if (temp_prop.concept_one.equals(current_node))
                    diagnoses_list.add(temp_prop.concept_two);
            }
            diagnoses_list.remove(current_node);                // makni trenutni cvor iz liste
            current_node = null;                                // postavi tenutni cvor na null
            for (String temp_node : diagnoses_list) {           // ako u listi postoji jos cvor koji nije terminalni, onda je on iduci
                if (!this.diagnoses.contains(temp_node))
                    current_node=temp_node;
            }
        }
        
        return diagnoses_list;
    }
    public ArrayList<String> getNextConcepts(String start_concept){
        // Ovo vraća cvorove djecu od zadanog cvora
        ArrayList<String> related_concepts = new ArrayList<>();
        for (Proposition temp_prop : this.propositions)
            if (temp_prop.getConcept_one().equals(start_concept))
                related_concepts.add(temp_prop.concept_two);
        return related_concepts;
    }
    public boolean checkReachability(String concept_ID, String diagnosis_name) {
        // Can the target diagnosis be reached from the concept?
        if (this.getReachableDiagnoses(concept_ID).contains(this.getNodeIDFromName(diagnosis_name)))
            return true;
        else
            return false;
    }

    public ArrayList<Proposition> C45 (ArrayList<Case> cases_list) {
        
            ArrayList<Proposition> c45_propositions = new ArrayList<>();
            ArrayList<ArrayList<Case>> active_case_subsets = new ArrayList<>();
            active_case_subsets.add(cases_list);
            
            while (active_case_subsets.size()>0) {

                ArrayList<Case> current_case_set = active_case_subsets.get(0);
                active_case_subsets.remove(0);

                if (getUniqueDiagnosesOfCaseList(current_case_set).equals(1)) // Ako ima samo jedna dijagnoza
                    return c45_propositions;
                ParameterGainRatio best_splitting_node = getBestSplittingNode(current_case_set);
                ArrayList<String> best_splitting_node_values = getUniqueValuesOfNode(current_case_set, best_splitting_node.getName());
                
                // Particioniraj skup primjera u odnosu na te vrijednosti i dodaj te skupove u listu primjera a makni nulti element liste
                for (String current_unique_value : best_splitting_node_values) {
                    ArrayList<Case> new_subset = new ArrayList<>();
                    for (Case temp_case : current_case_set)
                        if (temp_case.GetParameterAssignedValueByName(best_splitting_node.getName()).equals(current_unique_value))
                            new_subset.add(temp_case);
                    
                    // Provjeri jesu li sad u new_subset primjeri iz bar dve klase
                    if (getUniqueDiagnosesOfCaseList(new_subset).size()>1) {
                        c45_propositions.add(new Proposition(best_splitting_node.getName(), current_unique_value, getBestSplittingNode(new_subset).getName()));
                        active_case_subsets.add(new_subset);
                    } else c45_propositions.add(new Proposition(best_splitting_node.getName(), current_unique_value, new_subset.get(0).getCorrectDiagnosis().getName()));
                }
            }

            // Convert names to IDs
            ArrayList<Proposition> c45_propositions_ID = new ArrayList<>();
            for (Proposition prop : c45_propositions) {
                String concept_one_ID = "";
                for (String key : this.concepts_map.keySet())
                    if (this.concepts_map.get(key).equals(prop.concept_one))
                            concept_one_ID = key;
                String concept_two_ID = "";
                for (String key : this.concepts_map.keySet())
                    if (this.concepts_map.get(key).equals(prop.concept_two))
                            concept_two_ID = key;
                c45_propositions_ID.add(new Proposition( concept_one_ID, prop.link, concept_two_ID));
            }
            return c45_propositions_ID;
    }
    
    public ParameterGainRatio getBestSplittingNode (ArrayList<Case> cases_list) {
        
        ParameterGainRatio best_splitting_node = new ParameterGainRatio();
        if (cases_list.size() == 0)
            return null;
        // Svi caseovei moraju imati sve parametre definirane pa stoga prvi case ima sve
        Double max_gain_ratio = 0.0;
        for (Parametar current_parameter : cases_list.get(0).getParameters()) {
            Double current_gain_ratio = GR(cases_list,current_parameter);
            if (current_gain_ratio > max_gain_ratio) {
                max_gain_ratio = current_gain_ratio;
                best_splitting_node.setName(current_parameter.getName());
                best_splitting_node.setGain_ratio(current_gain_ratio);
            }
        }
        return best_splitting_node;
    }
    
    public HashMap<String, Double> calculateGRForCasesList (ArrayList<Case> cases_list) {
        HashMap<String, Double> info_gains = new HashMap<String, Double>();
        if (cases_list.size() == 0)
            return null;
        for (Parametar current_parameter : cases_list.get(0).getParameters())
            info_gains.put(current_parameter.getName(), GR(cases_list,current_parameter));
        return info_gains;
    }
    
    private Double SI(ArrayList<Case> P, Parametar a) {
        Double SI_value = 0.0;
        ArrayList<String> unique_a_values = getUniqueValuesOfNode(P, a.getName());
        for (String temp_unique_value : unique_a_values) {
            Double Pv = 0.0;
            for (Case temp_case : P)
                if (temp_case.GetParameterAssignedValueByName(a.getName()).equals(temp_unique_value))
                    Pv = Pv + 1.0;
            Double ratio = Pv/P.size();
            SI_value = SI_value - ratio*Math.log10(ratio)/Math.log10(2.0);
        }
        return SI_value;
    }
    private Double E(ArrayList<Case> P) {
        Double E_value = 0.0;
        ArrayList<String> unique_diagnosis_values = getUniqueDiagnosesOfCaseList(P);
        for (String temp_unique_value : unique_diagnosis_values) {
            Double pv = 0.0;
            for (Case temp_case : P)
                if (temp_case.getCorrectDiagnosis().getName().equals(temp_unique_value))
                    pv = pv + 1.0;
            Double ratio = pv/P.size();
            E_value = E_value - ratio*Math.log10(ratio)/Math.log10(2.0);
        }
        return E_value;
    }
    private Double IG(ArrayList<Case> P, Parametar a) {
        Double IG_value = 0.0;
        
        ArrayList<String> unique_a_values = getUniqueValuesOfNode(P, a.getName());
        for (String temp_unique_value : unique_a_values) {
            Double Pv = 0.0;
            ArrayList<Case> Pv_cases = new ArrayList<>();
            for (Case temp_case : P)
                if (temp_case.GetParameterAssignedValueByName(a.getName()).equals(temp_unique_value)) {
                    Pv = Pv + 1.0;
                    Pv_cases.add(temp_case);
                }
            IG_value = IG_value - Pv*E(Pv_cases)/P.size();
        }
        IG_value = IG_value + E(P);
        return IG_value;
    }
    private Double GR(ArrayList<Case> P, Parametar a) {
        //return SI(P,a)/IG(P,a);
        return IG(P,a);
    }
    private ArrayList<String> getUniqueDiagnosesOfCaseList(ArrayList<Case> cases_list) {
        ArrayList<String> unique_diagnosis_list = new ArrayList<>();
        for (Case temp_case : cases_list)
            if (! unique_diagnosis_list.contains(temp_case.getCorrectDiagnosis().getName()))
                unique_diagnosis_list.add(temp_case.getCorrectDiagnosis().getName());
        return unique_diagnosis_list;
    }
    private ArrayList<String> getUniqueValuesOfNode (ArrayList<Case> cases_list, String parameter_name) {
        ArrayList<String> all_values = new ArrayList<>();
        for (Case temp_case : cases_list) {
            String value = temp_case.GetParameterAssignedValueByName(parameter_name);
            if  (! all_values.contains(value))
                all_values.add(value);
        }
        return all_values;
    }
        
    
    // Ime vs. ID dekoding
    public String getStartNodeName() { return this.getConceptsMap().get(this.start_node); }
    public String getStartNodeID() { return this.start_node; }
    public String getNodeNameFromID (String id) {
        String name = this.concepts_map.containsKey(id) ? this.concepts_map.get(id) : "Concetp name not found.";
        return name;
    }
    public String getNodeIDFromName(String name) {
        for (String key : this.concepts_map.keySet())
            if (this.concepts_map.get(key).equals(name))
                return key;
        return "Node ID not found for ";
    }
    public Proposition decodeProposition(Proposition p) {
        Proposition decoded = new Proposition();
        decoded.setConcept_one(this.getNodeNameFromID(p.getConcept_one()));
        decoded.setConcept_two(this.getNodeNameFromID(p.getConcept_two()));
        decoded.setLink(p.getLink());
        return decoded;
    }
        
    // Parsing errors info
    public ArrayList<String> getOrphanConcepts() {
        ArrayList<String> orphans = new ArrayList<>();
        for (String concept_id : this.concepts_map.keySet()) {
            boolean contains = false;
            for (Proposition proposition : this.propositions)
                if (proposition.getConcept_one().equals(concept_id)) contains=true;
            if (this.diagnoses.contains(concept_id)) contains=true;
            if (contains==false) orphans.add(concept_id);
        }
        return orphans;
    }
    public ArrayList<String> getUnrecognizedConcepts (ArrayList<Case> case_list) {
        ArrayList<String> unrecognized_concepts = new ArrayList<>();
        for (String key : this.concepts_map.keySet()) {
            boolean contains=false;
            for (Case single_case : case_list)
                for (Parametar single_parameter : single_case.getParameters())
                    if (single_parameter.getName().equals(this.getNodeNameFromID(key)))
                        contains=true;
            if (this.diagnoses.contains(key)) contains=true;
            if (contains==false)
                unrecognized_concepts.add(key);
        }
        return unrecognized_concepts;   
    }
    public ArrayList<String> getUnrecognizedDiagnoses (ArrayList<Case> case_list) {
        ArrayList<String> unrecognized_diagnoses = new ArrayList<>();
        for (String tree_diagnosis : this.getDiagnoses()) {
            boolean contains=false;
            for (Case single_case : case_list)
                for (Dijagnoza case_diagnosis : single_case.getDiagnoses())
                    if (case_diagnosis.getName().equals(this.getNodeNameFromID(tree_diagnosis)))
                        contains=true;
            if (contains==false)
                unrecognized_diagnoses.add(tree_diagnosis);
        }
        return unrecognized_diagnoses;   
    }
    
    // Getters
    public ArrayList<Proposition> getPropositions() { return propositions; }
    public ArrayList<String> getDiagnoses() { return diagnoses; }
    public HashMap<PropositionKey, String> getPropositionsMap() { return propositionsMap; }
    public HashMap<String, ArrayList<String>> getReachableDiagnoses() { return reachable_diagnoses; }
    //public ArrayList<Proposition> getActiveTree() { return active_tree; }
    public HashMap<String, String> getConceptsMap() { return concepts_map; }
    
    // Setters
    //public void setActiveTree(ArrayList<Proposition> active_tree) { this.active_tree = active_tree; }
    public void setConceptsMap(HashMap<String, String> concepts_map) { this.concepts_map = concepts_map; }
    public void setReachableDiagnoses(HashMap<String, ArrayList<String>> reachable_diagnoses) { this.reachable_diagnoses = reachable_diagnoses; }
    public void setStartNode(String start_node) { this.start_node = start_node; }
    public void setDiagnoses(ArrayList<String> diagnoses) { this.diagnoses = diagnoses; }
    public void setPropositions(ArrayList<Proposition> propositions) { this.propositions = propositions; }
    public void setPropositionsMap (HashMap<PropositionKey, String> propositionsMap) { this.propositionsMap = propositionsMap; }

}