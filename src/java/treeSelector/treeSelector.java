/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeSelector;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "TreeSelector")
@ViewScoped

public class treeSelector {
    
    public String algorithm="";
    public Boolean weighted_attributes;

    public treeSelector(String algorithm) {
        this.algorithm = algorithm;
    }

    public void initialize(String initial_algorithm) {
        if (this.algorithm.isEmpty()) this.algorithm = initial_algorithm;
    }
    
    public treeSelector() {
    }

    public Boolean getWeighted_attributes() {
        return weighted_attributes;
    }

    public void setWeighted_attributes(Boolean weighted_attributes) {
        this.weighted_attributes = weighted_attributes;
    }
    
    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public treeSelector(Boolean weighted_attributes) {
        this.weighted_attributes = weighted_attributes;
    }

    public treeSelector(String algorithm, Boolean weighted_attributes) {
        this.algorithm = algorithm;
        this.weighted_attributes = weighted_attributes;
    }
    
    
}
