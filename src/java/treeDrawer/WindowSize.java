package treeDrawer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name="WindowSize", eager = true)
@SessionScoped

public class WindowSize implements Serializable {
    
    public int height;
    public int width;

    public void initialize() {}

    public int getHeight() {return height;}
    public void setHeight(int height) {this.height = height;}
    public int getWidth() {return width;}
    public void setWidth(int width) {this.width = width;}
   
}
