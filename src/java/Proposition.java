/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juraj
 */
public class Proposition {
    
    String concept_one;
    String concept_two;
    String link;

    public Proposition(String concept_one, String concept_two, String link) {
        this.concept_one = concept_one;
        this.concept_two = concept_two;
        this.link = link;
    }
    
    public String getConcept_one() {
        return concept_one;
    }

    public void setConcept_one(String concept_one) {
        this.concept_one = concept_one;
    }

    public String getConcept_two() {
        return concept_two;
    }

    public void setConcept_two(String concept_two) {
        this.concept_two = concept_two;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

