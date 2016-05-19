/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeDrawer;

import decisionTree.DT;
import decisionTree.Proposition;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.DiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.connector.FlowChartConnector;
import org.primefaces.model.diagram.endpoint.BlankEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.primefaces.model.diagram.overlay.LabelOverlay;
 
@ManagedBean(name = "treeDrawer")
@ViewScoped
public class treeDrawer {
     
    private DefaultDiagramModel model;
    private DT tree;
    private ArrayList<Proposition> active_tree = new ArrayList<>();             // Reprezentacija stabla za aktivni prikaz
    
    public void init(DT decision_tree) {
        
        this.tree = decision_tree;
        
        model = new DefaultDiagramModel();
        model.setMaxConnections(-1);
         
        FlowChartConnector connector = new FlowChartConnector();
        connector.setPaintStyle("{strokeStyle:'#C7B097',lineWidth:2}");
        model.setDefaultConnector(connector);
         
        Element start = new Element(tree.getStartNodeName(),"600px", "20px");
        start.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
        start.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));            // Samo da svi čvorovi imaju iste anchore
        start.setId(tree.getStartNodeID());
        model.addElement(start);
    }
    
    private ArrayList<String> getLeaves() {
        ArrayList<String> leaves = new ArrayList<>();
        if (this.active_tree.size()==0) return leaves;
        for (Proposition proposition : this.active_tree) {
            leaves.add(proposition.getConcept_two());
            leaves.remove(proposition.getConcept_one());
        }
        return leaves;
    }
 
    public DiagramModel getModel() {
        return this.model;
    }

   
    public void addNode() {
        Element tmp_element = new Element("ASDASDA", "5em", "1em");
        tmp_element.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
        tmp_element.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
        this.model.addElement(tmp_element);
    }
    
    public void addPropositionToGraph(Proposition proposition) {
        
        Element new_child = new Element(tree.getNodeNameFromID(proposition.getConcept_two()));
        new_child.setId(proposition.getConcept_two());
        new_child.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
        new_child.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
                    
        for (Element node : model.getElements())
            if (node.getId().equals(proposition.getConcept_one())) 
                model.connect(createConnection(node.getEndPoints().get(1), new_child.getEndPoints().get(0), proposition.getLink()));
        model.addElement(new_child);
        
    }
    
    public void expandLeaves() {
        /** Ako nema propozicija u active tree-u, dodaj sve propozicije koje idu iz pocetnog cvora.
         * Inace, nadi sve listove active tree-a
         * Za svaku propoziciju stabla, ako je prvi koncept propozicije medu listovima, dodaj propoziciju
        */
        
        ArrayList<Proposition> new_propositions = new ArrayList<>(); 
        ArrayList<String> leaves = getLeaves();

        for (Proposition proposition : tree.propositions)
            if ((this.active_tree.size()==0) & (proposition.getConcept_one().equals(tree.getStartNodeID())) || (leaves.contains(proposition.getConcept_one()))) {
                addPropositionToGraph(proposition);
                new_propositions.add(proposition);
            }
        this.active_tree.addAll(new_propositions);
    }
       
    public void pruneLeaves() {
    // Ukloni listove iz active_tree stabla
    /* Ako stablo ima samo korijenski cvor onda ne radi nista
     * Inace nadi sve listove u stablu i ukloni sve propozicije kojima
    */
        ArrayList new_active_tree = new ArrayList<>();
        ArrayList<String> leaves = getLeaves();
        Integer max_depth=0;
        
        for (String leaf : leaves) {
            Integer temp_depth=getNodeDepth(leaf);
            if (max_depth<temp_depth)
                max_depth=temp_depth;
        }

        for (Proposition proposition : this.active_tree) {
            String concept_two = proposition.getConcept_two();
            if ((!leaves.contains(concept_two)) || (getNodeDepth(concept_two)<max_depth)) {
                new_active_tree.add(proposition);
            }
            else {
                this.model.removeElement(this.model.findElement(concept_two));
            }
        }
        
        this.active_tree=new_active_tree;
    }

        public int getNodeDepth(String node) {

        int depth=0;
        ArrayList<String> active_nodes=new ArrayList<>();
        active_nodes.add(tree.getStartNodeName());
        ArrayList<Integer> all_depths=new ArrayList<Integer>();
        
        while (active_nodes.size()>0) {
            ArrayList<String> temp_active_nodes=new ArrayList<>();
            depth++;
            for (String active_node : active_nodes)
                for (Proposition proposition : tree.propositions)
                    if (proposition.getConcept_one().equals(active_node))
                        temp_active_nodes.add(proposition.getConcept_two());
                
            if (temp_active_nodes.contains(node))
                all_depths.add(depth);
            active_nodes=temp_active_nodes;
        }
        
        Integer final_depth=0;
        for (Integer max_depth : all_depths)
            if (max_depth>final_depth)
                final_depth=max_depth;
        
        return final_depth;
    }

    
    private Connection createConnection(EndPoint from, EndPoint to, String label) {
        Connection conn = new Connection(from, to);
        conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));
         
        if(label != null) {
            conn.getOverlays().add(new LabelOverlay(label, "flow-label", 0.5));
        }
         
        return conn;
    }
}
