/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package displayDetails;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author juraj
 */

@ManagedBean (name ="CaseDetails", eager = true)
//@ViewScoped
@SessionScoped

public class CaseDetails {
    
    boolean display;
    int id;

    public CaseDetails(boolean display, int id) {
        this.display = display;
        this.id = id;
    }

    public CaseDetails() {
        boolean display=false;
        int id=0;
    }
    
    public void addDisplay() {
        this.display = true;
    }
    public void setAll(boolean display, int id) {
        this.display = display;
        this.id = id;
    }
    public boolean isDisplay() {
        return display;
    }
    public void setDisplay(boolean display) {
        this.display = display;
    }
    public int getId() {
        this.id = this.id + 1;
        return this.id - 1;
    }
    public void setId(int id) {
        this.id = id;
    }
    
}
