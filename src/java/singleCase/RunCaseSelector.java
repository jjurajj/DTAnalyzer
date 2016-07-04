/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package singleCase;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author juraj
 */

@ManagedBean (name ="RunCaseSelector", eager = true)
//@ViewScoped
@SessionScoped

public class RunCaseSelector {

    public int caseID;

    public void init() {}
    
    public int getCaseID() { return caseID; }

    public void setCaseID(int caseID) { this.caseID = caseID; }

}
