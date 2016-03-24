package decisionTree;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

/**
 * @author juraj
 */

@ManagedBean (name ="Uploader", eager = true)
@SessionScoped

public class Uploader implements Serializable {
  public Part file;
  public String fileContent;

    // Ovo najprije validira fajl (velicinu, txt, jel prazan) i ako je OK onda ga aplouda i parsa u stablo
  public void validateFile(FacesContext ctx, UIComponent comp, Object value) throws IOException {
    List<FacesMessage> msgs = new ArrayList<FacesMessage>();
    this.file = (Part)value;

    if (file.getSize() > 1024*1024) { // 1 MB je upload limit
        msgs.add(new FacesMessage("File too big! Max upload limit: 1 MB."));
    } else if (!msgs.isEmpty()) {
        throw new ValidatorException(msgs);
    } else {                                   // Ako je validacija prosla OK
        try {
            readFile();
            
            // Redirectanje. Prije se to radilo ovak: <h:commandButton value="Upload" action="analysis?faces-redirect=true">
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/faces/analysis.xhtml");
    
        } catch (IOException e1) {
            msgs.add(new FacesMessage("Error reading file stream."));
        }
 
    }
    
    }
  
  public void readFile() {
    List<FacesMessage> msgs = new ArrayList<FacesMessage>();
    try {
        this.fileContent = new Scanner(this.file.getInputStream()).useDelimiter("\\A").next();
    } catch (IOException e) {
        msgs.add(new FacesMessage("Error reading file stream."));
    }
    
  }
  
  public Part getFile() {
    return file;
  }
 
  public void setFile(Part file) {
    this.file = file;
  }

  public String getFileContent() {
    return fileContent;
  }

  public void setFileContent(String fileContent) {
    this.fileContent = fileContent;
  }

}