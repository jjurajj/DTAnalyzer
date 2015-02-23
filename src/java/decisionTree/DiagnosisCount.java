package decisionTree;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juraj
 */
public class DiagnosisCount {

    public String name;
    public int N;       // Koliko ukupno caseova ima ovu dijagnozu? 
    public int TP;     // Koliko caseova koji iamju ovu dijagnozu se ispravno klasificira?
    public int FP;   // Koliko caseova koji imaju ovu dijagnoze se dijagnosticira? (Ne zapinje)
    public int TN;
    public int FN;
    public int undiagnosed;

    public DiagnosisCount() {
        this.name = "";
        this.N = 0;
        this.TP = 0;
        this.FP = 0;
        this.TN = 0;
        this.FN = 0;
        this.undiagnosed = 0;
    }
    
    public DiagnosisCount(String name, int N, int TP, int FP, int TN, int FN, int undiagnosed) {
        this.name = name;
        this.N = N;
        this.TP = TP;
        this.FP = FP;
        this.TN = TN;
        this.FN = FN;
        this.undiagnosed = undiagnosed;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getN() {
        return N;
    }
    public void setN(int N) {
        this.N = N;
    }
    public int getTP() {
        return TP;
    }
    public void setTP(int TP) {
        this.TP = TP;
    }
    public int getFP() {
        return FP;
    }
    public void setFP(int FP) {
        this.FP = FP;
    }
    public int getTN() {
        return TN;
    }
    public void setTN(int TN) {
        this.TN = TN;
    }
    public int getFN() {
        return FN;
    }
    public void setFN(int FN) {
        this.FN = FN;
    }
    public int getUndiagnosed() {
        return undiagnosed;
    }
    public void setUndiagnosed(int undiagnosed) {
        this.undiagnosed = undiagnosed;
    }
    
}
