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
import javax.faces.bean.ManagedBean;
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
import singleCase.Case;

@ManagedBean(name = "TreeDrawer")
@ViewScoped

public class TreeGraph {
    
    public List<Connection> stored_connections;                                 // Since I can't edit labels manually
    public int default_height=800, default_width=1200;
    public DefaultDiagramModel model;
    public DT tree;
    public ArrayList<Proposition> active_tree = new ArrayList<>();             // Reprezentacija stabla za aktivni prikaz
    public ArrayList<Integer> nodes_per_level = new ArrayList<>();
    public Integer screen_x, screen_y, step_y=90, x_offset=10;
    
    public void resetTree(){
        while (this.active_tree.size()>1)
            pruneLeaves();
    }
    
    public void initialize(DT decision_tree) {this.initialize(decision_tree,default_width,default_height);}
    public void initialize(DT decision_tree, int height, int width) {
        
        // Do a tree reset and set the start node
        if (active_tree.size() == 0) {
            
            this.active_tree.clear();
            this.nodes_per_level.clear();
            
            this.tree = decision_tree;
            this.screen_x=width;
            this.screen_y=height;
            
            this.default_width=width;
            this.default_height=height;
        
            this.model = new DefaultDiagramModel();
            this.model.setMaxConnections(-1);
         
            FlowChartConnector connector = new FlowChartConnector();
            connector.setPaintStyle("{strokeStyle:'#9C9FA1',lineWidth:2}");
            model.setDefaultConnector(connector);
        
            Element start = new Element(tree.getStartNodeName(), Integer.toString((screen_x)/2).concat("px"), "10px");
            start.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
            start.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
            start.setId(tree.getStartNodeID());
            start.setStyleClass("ui-diagram-start");
            model.addElement(start);
            this.buildFullTree();
            
        } else { //Otherwise just rescale tree based on the x value
            this.screen_x=width;
            this.screen_y=height;
            // Za sve cvorove skaliraj x koordinatu
        }
        
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
    public void visualArrange() {
        // Centriraj korijen
        // Dohvati sve cvorove i-te razine
        // Za svakog provjeri koliko ima konačno listova i postavi mu x koordinatu u tom omjeru
        // na kraju provedi algoritam za pretraživanje u dubinu i sortiranje na temelju toga.
        
        
        
    
    }
 
    public void addNode(String name) {
        Element tmp_element = new Element(name, "5em", "1em");
        tmp_element.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
        tmp_element.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
        this.model.addElement(tmp_element);
    }
    public void addPropositionToGraph(Proposition proposition) {
        
        // Definicija novog koncepta koji se dodaje u graf
        Element new_child = new Element(tree.getNodeNameFromID(proposition.getConcept_two()));
        new_child.setId(proposition.getConcept_two());
        new_child.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
        new_child.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
        if (tree.diagnoses.contains(proposition.getConcept_two()))
            new_child.setStyleClass("ui-diagram-end");
        else
            new_child.setStyleClass("ui-diagram-element");
        
        // Nađi roditelja po IDju i poveži ga s čvorom
        for (Element node : model.getElements())
            if (node.getId().equals(proposition.getConcept_one())) 
                model.connect(createConnection(node.getEndPoints().get(1), new_child.getEndPoints().get(0), proposition.getLink()));
        
        model.addElement(new_child);
        
    }
    public void expandLeaves() {
    
        ArrayList<Proposition> new_propositions = new ArrayList<>(); 
        
        // Razvij korijesnki cvor ili sve listove redom kojim se javljaju
        if (this.active_tree.size()==0) {
            new_propositions=this.tree.getChildrenPropositions(this.tree.getStartNodeID());
        } else {
            for (int i = active_tree.size() - nodes_per_level.get(nodes_per_level.size() - 1); i < active_tree.size(); i++)
                new_propositions.addAll(this.tree.getChildrenPropositions(active_tree.get(i).getConcept_two()));
        }
        
        // Dodaj sve nove propozicije, njihov broj, i onda svaku novu dodaj u graf
        if (new_propositions.size()>0) {
            active_tree.addAll(new_propositions);
            nodes_per_level.add(new_propositions.size());
            for (Proposition proposition : new_propositions)
                addPropositionToGraph(proposition);
        }
        
        ArrayList<String> leaves = getLeaves();
        // Sad ih treba poravnati ali po redu kojim se javljaju
        int x_scale = 1;
        for (Proposition proposition : new_propositions)                 // Prodi redom kroz sve propozicije
            if (leaves.contains(proposition.getConcept_two()))      // Ako je desni koncept list
                for (Element node : model.getElements())            // Onda nadi taj cvor u grafu
                    if (node.getId().equals(proposition.getConcept_two())) {
                        node.setX(Integer.toString(x_scale++*screen_x/(leaves.size()+1)).concat("px"));                                // Po x osi
                        node.setY(Integer.toString((tree.getNodeDepth(node.getId()))*step_y).concat("px")); // Po y osi
                    }
        
    } 
    public void pruneLeaves() {
        
        ArrayList<Proposition> new_active_tree = new ArrayList<>();
        for (int i=0; i<active_tree.size(); i++ )
            if (i<active_tree.size() - nodes_per_level.get(nodes_per_level.size()-1))
                new_active_tree.add(active_tree.get(i));
            else
                this.model.removeElement(this.model.findElement(active_tree.get(i).getConcept_two()));
            
        active_tree=new_active_tree;
        nodes_per_level.remove(nodes_per_level.size()-1);
    }
    
    public void runCase(Case target_case) {
        buildFullTree();
        markCasePath(target_case);
    }
    
    public void resetStyles() {
    
        for (Element temp_element : this.model.getElements()) {
            String style = temp_element.getStyleClass();
            int end = style.indexOf("-selected");
            if (end>0) temp_element.setStyleClass(style.substring(0, end));
        }

        //List<Connection> rem_conns = this.model.getConnections();
        //for (Connection rem_conn : rem_conns) this.model.disconnect(rem_conn);
        //for (Connection add_conn : this.stored_connections) this.model.connect(add_conn);

    }
    
    public void markCasePath(Case target_case) {
        
        // Store connections
        this.stored_connections=this.model.getConnections();
        
        ArrayList<Connection> remove_connections = new ArrayList<>();
        ArrayList<Connection> add_connections = new ArrayList<>();
        
        String correct_diagnosis_ID = this.tree.getNodeIDFromName(target_case.getCorrectDiagnosis().getName());
        String style_label = "-selected";
        
        // Change Element styles
        for (Proposition prop : target_case.getEvaluation().getPath())
            for (Element node_one : this.model.getElements())
                if (prop.getConcept_one().equals(node_one.getId())) {
                    
                    // If its on the wrong path or if its and incorrect dignosis change style label (for ever)
                    if ((this.tree.getDiagnoses().contains(prop.getConcept_one()) == true) && (prop.getConcept_one().equals(correct_diagnosis_ID) == false) || (this.tree.getReachableDiagnoses(prop.getConcept_one()).contains(correct_diagnosis_ID) == false) && (this.tree.getDiagnoses().contains(prop.getConcept_one()) == false))
                            style_label = style_label.concat("-false");
                    node_one.setStyleClass(node_one.getStyleClass().concat(style_label));
                    
                    for (Element node_two : this.model.getElements())
                        if ((prop.getConcept_two() != null) && (prop.getConcept_two().equals(node_two.getId())))
                            for (Connection conn : this.model.getConnections())
                                if ((node_one.getEndPoints().contains(conn.getSource())) && (node_two.getEndPoints().contains(conn.getTarget()))) {
                                    
                                    Connection new_conn = new Connection(conn.getSource(), conn.getTarget());
                                    new_conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));
                                    new_conn.getOverlays().add(new LabelOverlay(prop.getLink(), "flow-label".concat(style_label), 0.5));
                                    remove_connections.add(conn);
                                    add_connections.add(new_conn);
                                    
                                }       
                }
        
        // If the case was not correctly solved, mark the correct end
        if (target_case.getEvaluation().isCorrect() == false)
            for (Element node_one : this.model.getElements())
                if (node_one.getId().equals(correct_diagnosis_ID)) {
                    node_one.setStyleClass(node_one.getStyleClass().concat("-selected-true"));
                    break;
                }

        for (Element node_one : this.model.getElements())
            if (node_one.getStyleClass().contains("selected") == false)
                node_one.setStyleClass(node_one.getStyleClass().concat("-lite"));

        
        for (Connection temp_conn : remove_connections) this.model.disconnect(temp_conn);
        for (Connection temp_connection2 : add_connections) this.model.connect(temp_connection2);
            
    }
    
    public void buildFullTree() {
        
        while (active_tree.size()>0) {
            pruneLeaves();
        }
        
        ArrayList<Proposition> last_propositions = new ArrayList<>();
        do  {
            expandLeaves();
        } while (active_tree.size() != tree.propositions.size());
        resetStyles();
    
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
    public Connection createConnection(EndPoint from, EndPoint to, String label) {
        Connection conn = new Connection(from, to);
        conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));
         
        if(label != null) {
            conn.getOverlays().add(new LabelOverlay(label, "flow-label", 0.5));
        }
         
        return conn;
    }

    public void setModel(DefaultDiagramModel model) {this.model = model;}
    public DT getTree() {return tree;}
    public void setTree(DT tree) {this.tree = tree;}
    public ArrayList<Proposition> getActive_tree() {return active_tree;}
    public void setActive_tree(ArrayList<Proposition> active_tree) {this.active_tree = active_tree;}
    public ArrayList<Integer> getNodes_per_level() {return nodes_per_level;}
    public void setNodes_per_level(ArrayList<Integer> nodes_per_level) {this.nodes_per_level = nodes_per_level;}
    public Integer getScreen_x() {return screen_x;}
    public void setScreen_x(Integer screen_x) {this.screen_x = screen_x;}
    public Integer getScreen_y() {return screen_y;}
    public void setScreen_y(Integer screen_y) {this.screen_y = screen_y;}
    public Integer getStep_y() {return step_y;}
    public void setStep_y(Integer step_y) {this.step_y = step_y;}
    public Integer getX_offset() {return x_offset;}
    public void setX_offset(Integer x_offset) {this.x_offset = x_offset;}

}
