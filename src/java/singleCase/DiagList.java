/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package singleCase;

import java.util.ArrayList;

/**
 *
 * @author juraj
 */
public class DiagList {

    public enum Status {
        correct, falsely_discarded, to_be_discarded 
    }

    public ArrayList<String> name;
    public ArrayList<Status> status;

    
    
    
    ////////////////////////////////////
    // Getteri i setteri
    ////////////////////////////////////
    
    public ArrayList<String> getName() {
        return name;
    }
    public void setName(ArrayList<String> name) {
        this.name = name;
    }
    public ArrayList<Status> getStatus() {
        return status;
    }
    public void setStatus(ArrayList<Status> status) {
        this.status = status;
    }
    
}
