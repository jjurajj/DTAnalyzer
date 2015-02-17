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

    public String diagnosis;
    public int total;
    public int correct;
    public int diagnosed;
    public int incorrect;   // sve ovo trebaju postat liste odgovarajucih caseova

    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public int getCorrect() {
        return correct;
    }
    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public void setCorrect(int correct) {
        this.correct = correct;
    }
    public int getDiagnosed() {
        return diagnosed;
    }
    public void setDiagnosed(int diagnosed) {
        this.diagnosed = diagnosed;
    }
    
}
