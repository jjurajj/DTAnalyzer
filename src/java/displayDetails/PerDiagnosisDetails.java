package displayDetails;

import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import singleCase.Case;

@ManagedBean (name ="PerDiagnosisDetails", eager = true)
@ViewScoped

/**
 *
 * @author juraj
 */
public class PerDiagnosisDetails {
    public String info = new String();
    public ArrayList<Case> display_cases = new ArrayList<>();
    public ArrayList<Boolean> display = new ArrayList<Boolean>();

    public PerDiagnosisDetails() {}
    
    public void setAll(ArrayList<Case> display_cases, String info) {
        this.info=info;
        
        this.display_cases.clear();
        this.display_cases.addAll(display_cases);
        
        this.display.clear();
        for (Case display_case : display_cases) {
            this.display.add(false);
        }
    }
    public void setSingle(Case display_case, String info) {
        this.info=info;
        this.display_cases.clear();
        this.display_cases.add(display_case);
        this.display.clear();
        this.display.add(true);
    }
 
    public void setInfo(String info) { this.info = info; }
    public void setDisplayCases(ArrayList<Case> display_cases) { this.display_cases = display_cases; }
    public void setDisplay(ArrayList<Boolean> display) { this.display = display; }    
    
    public void toggleDisplay (Case current_case) {
        for (int i = 0; i < this.display_cases.size(); i++) {
            if (this.display_cases.get(i).equals(current_case)) {
                    this.display.set(i, !this.display.get(i));
                    break;
            }
        }
    }
    
    public boolean getDisplay (Case current_case) {
        for (int i = 0; i < this.display_cases.size(); i++) {
            if (this.display_cases.get(i).equals(current_case)) {
                    return this.display.get(i);
            }
        }
        return false;
    }
    public String getInfo() { return info; }
    public ArrayList<Case> getDisplayCases() { return display_cases; }
    public ArrayList<Boolean> getDisplay() { return display; }

}





