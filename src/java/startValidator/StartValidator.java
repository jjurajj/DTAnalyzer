/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startValidator;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import treeDrawer.WindowSize;

/**
 *
 * @author juraj
 */

@ManagedBean(name="StartValidator")
@ViewScoped

public class StartValidator {

        public void check(WindowSize window, boolean file_validator) {
        
            if ((file_validator == true) && (window.getHeight()>0) && (window.getWidth()>0)) {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                try {
                    ec.redirect(ec.getRequestContextPath() + "/analysis.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(StartValidator.class.getName()).log(Level.SEVERE, null, ex);
                }

            
            }
                
        }
}
