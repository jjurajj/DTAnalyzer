package singleCase;


import decisionTree.Proposition;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juraj
 */
@ManagedBean (name ="CaseEvaluation", eager = true)
//@ViewScoped
@SessionScoped

public class CaseEvaluation {
    
    public boolean diagnosed;                                       //Dijagnosticira li se case?
    public boolean correct;                                         //Dijagnosticira li se tocno?
    public String end_node;                                         //Konacni cvor za case
    public ArrayList<Proposition> path = new ArrayList<>();         // Niz propozicija po kojima se dijagnosticira
    public ArrayList<ArrayList<String>> diags_per_node = new ArrayList<>();  // Za svaku propoziciju popis available/pozeljnih dijagnoza
    public ArrayList<Double> price_per_node = new ArrayList<>();    // Cijena po cvoru
    
    ///////////////////////////////////////////////
    // Konstruktori
    ///////////////////////////////////////////////

    public CaseEvaluation(boolean diagnosed, boolean correct, String end_concept) {
        this.diagnosed = diagnosed;
        this.correct = correct;
        this.end_node = end_concept;
    }
    public CaseEvaluation() {
        this.diagnosed = false;
        this.correct = false;
        this.end_node = "Undefined";
    }  

    ///////////////////////////////////////////////
    // Getteri i setteri
    ///////////////////////////////////////////////

    public ArrayList<Proposition> getPath() {
        return path;
    }
    public void setPath(ArrayList<Proposition> path) {
        this.path = path;
    }
    public boolean isDiagnosed() {
        return diagnosed;
    }
    public void setDiagnosed(boolean diagnosed) {
        this.diagnosed = diagnosed;
    }
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getEnd_node() {
        return end_node;
    }
    public void setEnd_node(String end_node) {
        this.end_node = end_node;
    }
    public boolean isCorrect() {
        return correct;
    }
    
}
