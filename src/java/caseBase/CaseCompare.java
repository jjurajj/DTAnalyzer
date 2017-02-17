/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caseBase;

import java.util.Objects;

/**
 *
 * @author juraj
 */
public class CaseCompare {
    
    public String parameter_name;
    public String parameter_value;

    public CaseCompare(String parameter_name, String parameter_value) {
        this.parameter_name = parameter_name;
        this.parameter_value = parameter_value;
    }

    public String getParameter_name() {
        return parameter_name;
    }

    public void setParameter_name(String parameter_name) {
        this.parameter_name = parameter_name;
    }

    public String getParameter_value() {
        return parameter_value;
    }

    public void setParameter_value(String parameter_value) {
        this.parameter_value = parameter_value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CaseCompare other = (CaseCompare) obj;
        if (!Objects.equals(this.parameter_name, other.parameter_name)) {
            return false;
        }
        if (!Objects.equals(this.parameter_value, other.parameter_value)) {
            return false;
        }
        return true;
    }

    
    
    
}
