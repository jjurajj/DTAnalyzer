/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c45;

/**
 *
 * @author juraj
 */
public class ParameterGainRatio {

    public String name;
    public Double gain_ratio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getGain_ratio() {
        return gain_ratio;
    }

    public void setGain_ratio(Double gain_ratio) {
        this.gain_ratio = gain_ratio;
    }

    public ParameterGainRatio(String name, Double gain_ratio) {
        this.name = name;
        this.gain_ratio = gain_ratio;
    }

    public ParameterGainRatio() {
    }
    
    
}
