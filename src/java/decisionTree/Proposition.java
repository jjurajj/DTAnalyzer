package decisionTree;

import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juraj
 * klasa propozicija. ima 
 */
public class Proposition {
    
    String concept_one;
    String concept_two;
    String link;

    public Proposition() {
        this.concept_one = "";
        this.link = "";
        this.concept_two = "";
    }
    
    public Proposition(String concept_one, String link, String concept_two) {
        this.concept_one = concept_one;
        this.link = link;
        this.concept_two = concept_two;
    }
    
    public void setConcept_one(String concept_one) {
        this.concept_one = concept_one;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public void setConcept_two(String concept_two) {
        this.concept_two = concept_two;
    }

    public String getConcept_one() {
        return concept_one;
    }
    public String getLink() {
        return link;
    }
    public String getConcept_two() {
        return concept_two;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Proposition other = (Proposition) obj;
        if (!Objects.equals(this.concept_one, other.concept_one)) {
            return false;
        }
        if (!Objects.equals(this.concept_two, other.concept_two)) {
            return false;
        }
        if (!Objects.equals(this.link, other.link)) {
            return false;
        }
        return true;
    }
    
    

}

