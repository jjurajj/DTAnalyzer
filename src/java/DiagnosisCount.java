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

    public int total;       // Koliko ukupno caseova ima ovu dijagnozu? 
    public int correct;     // Koliko caseova koji iamju ovu dijagnozu se ispravno klasificira?
    public int diagnosed;   // Koliko caseova koji imaju ovu dijagnoze se dijagnosticira? (Ne zapinje)

    public DiagnosisCount() {
        this.correct = 0;
        this.diagnosed = 0;
        this.total = 0;
    }

    public DiagnosisCount(int total, int correct, int diagnosed) {
        this.total = total;
        this.correct = correct;
        this.diagnosed = diagnosed;
    }

    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public int getCorrect() {
        return correct;
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
