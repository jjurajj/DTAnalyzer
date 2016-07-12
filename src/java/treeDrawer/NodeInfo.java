/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeDrawer;

/**
 *
 * @author juraj
 */
public class NodeInfo {
    
    public String x, y, default_style, current_style;

    public NodeInfo(String x, String y, String default_style) {
        this.default_style = default_style;
        this.x = x;
        this.y = y;
    }

    public NodeInfo(String x, String y, String default_style, String current_style) {
        this.x = x;
        this.y = y;
        this.default_style = default_style;
        this.current_style = current_style;
    }
    
    

    public String getCurrent_style() {
        return current_style;
    }

    public void setCurrent_style(String current_style) {
        this.current_style = current_style;
    }
    
    public String getDefault_style() {
        return default_style;
    }

    public void setDefault_style(String default_style) {
        this.default_style = default_style;
    }
    
    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    
}
