/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startValidator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="BaseSelector", eager = true)
@SessionScoped
public class BaseSelector {

    public String link;

    public String getLink() {
        return link;
    }

    public void setLink(String base_link) {
        this.link = base_link;
    }
    
}
