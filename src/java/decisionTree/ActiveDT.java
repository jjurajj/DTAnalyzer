/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisionTree;

import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean (name ="ActiveDT", eager = true)
@SessionScoped
/**
 *
 * @author juraj
 */
public class ActiveDT {
    
    public DT DT = new DT();

    public void activeDT(String start_node) {
        this.DT.start_node=start_node;
    }

    // Prune tree - find all leaf nodes and remove them
    public void pruneTree() {
        ArrayList<Proposition> new_propositions = new ArrayList<>();
        for (Proposition proposition : this.DT.propositions) {
            String right_concept=proposition.concept_two;
            boolean is_leaf=true;
            for (Proposition proposition_check : this.DT.propositions)
                if (proposition_check.concept_one == right_concept)
                    is_leaf=false;
            if (!is_leaf) new_propositions.add(proposition);
        }
        this.DT.propositions=new_propositions;
    }

    public void expandTree() {
        ArrayList<String> leaves=new ArrayList<>();
        for (Proposition proposition : this.DT.propositions) {
            String right_concept=proposition.concept_two;
            boolean is_leaf=true;
            for (Proposition proposition_check : this.DT.propositions)
                if (proposition_check.concept_one == right_concept)
                    is_leaf=false;
            if (is_leaf) leaves.add(right_concept);
        }
        
    }

    
    
    
    // Podrezuje ili expandira ActiveDT stablo ovisno o tome je li zadani cvor razvijen ili list
    public void pruneInclude (String concept_id) {
        
        boolean prune=false;
        for (Proposition proposition : this.DT.propositions)
            if (proposition.concept_one==concept_id) prune=true;
        
        if (prune) { // Izbaci sve propozicije koje imaju ovaj cvor s lijeve strane i sve ulancane s tom
            
        } else {    // Ako je ovaj cvor vec razvijen
            
        }
    }
    
    public DT getTree() {
        return DT;
    }
    public void setTree(DT tree) {
        this.DT = DT;
    }
    
    
}

