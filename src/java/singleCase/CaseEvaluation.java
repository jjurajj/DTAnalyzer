package singleCase;


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
    
    public boolean diagnosed;
    public boolean correct;
    public String end_node;

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
