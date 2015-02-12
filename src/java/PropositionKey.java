/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juraj
 */
public class PropositionKey {
    
    public String concept;
    public String value;

    public PropositionKey(String concept, String value) {
        this.concept = concept;
        this.value = value;
    }

    public PropositionKey() {
    }

    public String getConcept() {
        return concept;
    }
    public void setConcept(String concept) {
        this.concept = concept;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
