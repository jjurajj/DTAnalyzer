package decisionTree;

import java.util.ArrayList;
import singleCase.Case;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juraj
 * Ovaj objekt ima liste caseova koji su TP/NP/FP/FN za dijagnozu name
*/

public class DiagnosisCount {

    public String name;
    
    public ArrayList<Case> total  = new ArrayList<>();
    public ArrayList<Case> TP  = new ArrayList<>();
    public ArrayList<Case> TN  = new ArrayList<>();
    public ArrayList<Case> FP  = new ArrayList<>();
    public ArrayList<Case> FN  = new ArrayList<>();
    public ArrayList<Case> undiagnosed  = new ArrayList<>();

    public DiagnosisCount(String name) {
        this.name = name;
    }
    
    

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Case> getTotal() {
        return total;
    }
    public void setTotal(ArrayList<Case> total) {
        this.total = total;
    }
    public ArrayList<Case> getTP() {
        return TP;
    }
    public void setTP(ArrayList<Case> TP) {
        this.TP = TP;
    }
    public ArrayList<Case> getTN() {
        return TN;
    }
    public void setTN(ArrayList<Case> TN) {
        this.TN = TN;
    }
    public ArrayList<Case> getFP() {
        return FP;
    }
    public void setFP(ArrayList<Case> FP) {
        this.FP = FP;
    }
    public ArrayList<Case> getFN() {
        return FN;
    }
    public void setFN(ArrayList<Case> FN) {
        this.FN = FN;
    }
    public ArrayList<Case> getUndiagnosed() {
        return undiagnosed;
    }
    public void setUndiagnosed(ArrayList<Case> undiagnosed) {
        this.undiagnosed = undiagnosed;
    }
        
}
