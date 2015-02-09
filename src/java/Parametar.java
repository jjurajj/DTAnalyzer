
import java.util.Date;
import java.util.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jpetrovic
 */
public class Parametar {
    String name, synonims, real_value, assigned_value;
    double weight, price;
    Date occurrence;
    
    HashMap<String,String> dijagnoze;
        
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSynonims() {
        return synonims;
    }

    public void setSynonims(String synonims) {
        this.synonims = synonims;
    }

    public String getReal_value() {
        return real_value;
    }

    public void setReal_value(String real_value) {
        this.real_value = real_value;
    }

    public String getAssigned_value() {
        return assigned_value;
    }

    public void setAssigned_value(String assigned_value) {
        this.assigned_value = assigned_value;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Date occurrence) {
        this.occurrence = occurrence;
    }
    
    
}