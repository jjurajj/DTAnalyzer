/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knowledgeModel;

import caseBase.CaseBase;
import decisionTree.DT;
import decisionTree.Proposition;
import decisionTree.PropositionKey;
import decisionTree.Tree;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import singleCase.Case;

/**
 *
 * @author juraj
 */
public class KnowledgeModel {
    
    public Tree user_tree = new Tree();
    public Tree optimal_tree = new Tree();
    public ArrayList<Proposition> active_tree = new ArrayList<>();
    
    public CaseBase case_base = new CaseBase();
    
    public ArrayList<String> diagnoses = new ArrayList<>();                         // Lista ID-jeva dijagnoza
    public HashMap<String, String> concepts_map= new HashMap<>();                   // HashMap: ID koncepta/dijagnoze -> Ime koncepta/dijagnoze
    
    public void initialize(String DT_text_file, String case_base_URL) throws IOException {
    
        this.user_tree.initialize(DT_text_file);
        this.case_base.initialize(case_base_URL);
        
        this.setDiagnoses(this.getUser_tree().getDiagnoses());
        this.setConcepts_map(this.getUser_tree().getConceptsMap());
        
        
        
        // dovoljno je za prvi case provjeriti postoje li u mapi
        
        for (Case temp_case : this.case_base.getCases()) {
            
        concepts_map.
        
        }
        
        
        
        // sad prodi po bazi i za svaki atribut i dijagnozu provjeri 
        // Sad treba apdejtati popis koncepata i dijagnoza
        
        
        
        
        // Initialize user_tree
        // Update/add diagnoses, attributes in map, based on existing
         
        
    }

    public Tree getUser_tree() {
        return user_tree;
    }

    public void setUser_tree(Tree user_tree) {
        this.user_tree = user_tree;
    }

    public Tree getOptimal_tree() {
        return optimal_tree;
    }

    public void setOptimal_tree(Tree optimal_tree) {
        this.optimal_tree = optimal_tree;
    }

    public ArrayList<Proposition> getActive_tree() {
        return active_tree;
    }

    public void setActive_tree(ArrayList<Proposition> active_tree) {
        this.active_tree = active_tree;
    }

    public CaseBase getCase_base() {
        return case_base;
    }

    public void setCase_base(CaseBase case_base) {
        this.case_base = case_base;
    }

    public ArrayList<String> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(ArrayList<String> diagnoses) {
        this.diagnoses = diagnoses;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<String> attributes) {
        this.attributes = attributes;
    }

    public HashMap<String, String> getConcepts_map() {
        return concepts_map;
    }

    public void setConcepts_map(HashMap<String, String> concepts_map) {
        this.concepts_map = concepts_map;
    }
    
    
    
    
    
}
