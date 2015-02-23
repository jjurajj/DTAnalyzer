package singleCase;


import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Blondie
 */
public class Dijagnoza {
    
    public String name;
    public boolean correct;

    public Dijagnoza(String name, boolean correct) {
        this.name = name;
        this.correct = correct;
    }

    public Dijagnoza() {
        this.name = "Undefined";
        this.correct = false;
    }
    
    public boolean isCorrect() {
        return this.correct;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
    
}