package displayDetails;

import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import singleCase.Case;

@ManagedBean (name ="PerDiagnosisDetails", eager = true)
@ViewScoped

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juraj
 */
public class PerDiagnosisDetails {
    public String info = new String();
    public ArrayList<Case> display_cases = new ArrayList<>();

    public PerDiagnosisDetails() {
    }

    public void setAll(ArrayList<Case> display_cases, String info) {
        this.info=info;
        this.display_cases=display_cases;
    }
    
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ArrayList<Case> getDisplay_cases() {
        return display_cases;
    }

    public void setDisplay_cases(ArrayList<Case> display_cases) {
        this.display_cases = display_cases;
    }
}





