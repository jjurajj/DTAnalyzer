/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeDrawer;

import decisionTree.DT;
import decisionTree.Proposition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.diagram.DefaultDiagramModel;
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
    public int concept_height=90, concept_width=320;
    public int offset_height=10, offset_width=10;
    public int canvas_width, canvas_height;
    public DefaultDiagramModel model = new DefaultDiagramModel();               // Treee to be represented
    public ArrayList<NodeInfo> arranged_elements = new ArrayList<>();
    public HashMap<String, NodeInfo> node_info_map= new HashMap<>();   
    public DT tree;
    public ArrayList<Proposition> active_tree = new ArrayList<>();              // Reprezentacija stabla za aktivni prikaz
    public ArrayList<Integer> nodes_per_level = new ArrayList<>();
    public ArrayList<String> centered_nodes = new ArrayList<>();
    
    public void resetTree(){
        while (this.active_tree.size()>0)
            pruneLeaves();
    }
    
    public void initialize(DT decision_tree) {this.initialize(decision_tree,default_width,default_height);}
    public void initialize(DT decision_tree, int height, int width) {
        
        // If this is the tree initialization:
        if (this.model.getElements().isEmpty()) {
            
            this.active_tree.clear();
            this.nodes_per_level.clear();
            
            this.tree = decision_tree;
            this.canvas_width=width;
            this.canvas_height=height;
            
            this.default_width=width;
            this.default_height=height;
        
            this.model = new DefaultDiagramModel();
            this.model.setMaxConnections(-1);
         
            FlowChartConnector connector = new FlowChartConnector();
            connector.setPaintStyle("{strokeStyle:'#C7C7C7',lineWidth:2}");
            model.setDefaultConnector(connector);
        
            Element start = new Element(tree.getStartNodeName(), Integer.toString((canvas_width)/2).concat("px"), "10px");
            start.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
            start.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
            start.setId(tree.getStartNodeID());
            start.setStyleClass("ui-diagram-start");
            model.addElement(start);
            buildFullTree();
            saveNodeLocations();
            
        } else { //Otherwise just rescale tree based on the x value
            this.canvas_width=width;
            this.canvas_height=height;
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
    public void adjustWithLeaves(String node_ID) {
        // Dohvati svu djecu ovog cvora
        ArrayList<String> children = new ArrayList<>();
        for (Proposition prop : this.tree.getPropositions())
            if (prop.getConcept_one().equals(node_ID)) {
                children.add(prop.getConcept_two());
                if (this.centered_nodes.contains(prop.getConcept_two()) == false)
                    adjustWithLeaves(prop.getConcept_two());
                this.centered_nodes.add(prop.getConcept_two());
            }
        int sum_x=0;
        for (Element node : this.model.getElements())
            if (children.contains(node.getId()))
                sum_x=sum_x+Integer.parseInt(node.getX().substring(0,node.getX().length()-2));
        if (children.size()>0) sum_x=sum_x/children.size();
        for (Element node : this.model.getElements())
            if (node.getId().equals(node_ID)) {
                node.setX(Integer.toString(sum_x).concat("px"));
                this.centered_nodes.add(node.getId());
            }
        
    }
    
    public void addNode(String name, String x, String y, String style, String id) {
        Element tmp_element = new Element(name, x, y);
        tmp_element.setStyleClass(style);
        tmp_element.setId(id);
        tmp_element.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
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
        ArrayList<Proposition> new_propositions = getNextPropositions();
        if (new_propositions.size()>0) {
            active_tree.addAll(new_propositions);
            nodes_per_level.add(new_propositions.size());
            for (Proposition proposition : new_propositions)
                addPropositionToGraph(proposition);            
        }
        loadNodeLocations();
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
    
    private void saveNodeLocations() {
        for (Element node : this.model.getElements()) {
            node_info_map.put(node.getId(), new NodeInfo(node.getX(), node.getY(), node.getStyleClass()));
        }
    }
    private void loadNodeLocations() {
        for (Element node : this.model.getElements()) {
            NodeInfo info = this.node_info_map.get(node.getId());
            node.setX(info.getX());
            node.setY(info.getY());
        }
    }
    private void loadNodeStyles(String style) {
        for (Element node : this.model.getElements())
            if (style.equals("current"))
                node.setStyleClass(this.node_info_map.get(node.getId()).getCurrent_style());
            else if (style.equals("defualt"))
                node.setStyleClass(this.node_info_map.get(node.getId()).getDefault_style());
    }
    private void saveNodeStyles() {
        for (Element node : this.model.getElements()) {
            NodeInfo info = this.node_info_map.get(node.getId());
            this.node_info_map.remove(node.getId());
            this.node_info_map.put(node.getId(), new NodeInfo(node.getX(), node.getY(), info.getDefault_style(), node.getStyleClass()));
        }
    }
    public void resetNodeStyles() {
        for (Element temp_element : this.model.getElements()) {
            String style = temp_element.getStyleClass();
            int end = style.indexOf("-selected");
            if (end>0) temp_element.setStyleClass(style.substring(0, end));
        }
    }
    
    public void runCase(Case target_case) {
        buildFullTree();
        markCasePath(target_case);
    }
    // Edits element styles on the case path
    public void markCasePath(Case target_case) {
        
        resetNodeStyles();
        this.stored_connections=this.model.getConnections();                    // Store connections
        
        ArrayList<Connection> remove_connections = new ArrayList<>();
        ArrayList<Connection> add_connections = new ArrayList<>();
        
        String correct_diagnosis_ID = this.tree.getNodeIDFromName(target_case.getCorrectDiagnosis().getName());
        String style_label = "-selected";
        
        for (Proposition prop : target_case.getEvaluation().getPath())
            for (Element node_one : this.model.getElements())
                if (prop.getConcept_one().equals(node_one.getId())) {           // If the node_one is on the classify path:
                    
                    // If its on the wrong path or if its and incorrect dignosis change style label (for ever)
                    if ((this.tree.getDiagnoses().contains(prop.getConcept_one()) == true) && (prop.getConcept_one().equals(correct_diagnosis_ID) == false) || (this.tree.getReachableDiagnoses(prop.getConcept_one()).contains(correct_diagnosis_ID) == false) && (this.tree.getDiagnoses().contains(prop.getConcept_one()) == false))
                            style_label = "-selected-false";
                    node_one.setStyleClass(node_one.getStyleClass().concat(style_label));
                    
                    for (Element node_two : this.model.getElements())
                        if ((prop.getConcept_two() != null) && (prop.getConcept_two().equals(node_two.getId())))
                            for (Connection conn : this.model.getConnections())
                                if ((node_one.getEndPoints().contains(conn.getSource())) && (node_two.getEndPoints().contains(conn.getTarget()))) {
                                    
                                    Connection new_conn = new Connection(conn.getSource(), conn.getTarget());
                                    new_conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));
                                    new_conn.getOverlays().add(new LabelOverlay(prop.getLink(), "flow-label-selected", 0.5));
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
        toggleLegend();
        toggleLegend();
        
    }
    
    public void toggleLegend() {

        ArrayList<String> legend = new ArrayList<String>(Arrays.asList("csr", "icsp", "isr", "ucs", "start", "end", "decision", "cse"));
        boolean add_legend = true;
        ArrayList<String> nodes_to_remove = new ArrayList<>();
        for (Element node : this.model.getElements()) if (legend.contains(node.getId())) nodes_to_remove.add(node.getId());
        for (String node_ID : nodes_to_remove)
            this.model.removeElement(this.model.findElement(node_ID));
        
        if (nodes_to_remove.isEmpty()) {
            boolean correct_path=false, unreached_correct=false, incorrect_path=false, correct_end=false, incorrect_solution=false, start_node=false, decision_node=false, end_node=false;
            for (Element node : this.model.getElements())
                if (node.getStyleClass().equals("ui-diagram-end-selected-true")) unreached_correct=true;
                else if (node.getStyleClass().equals("ui-diagram-element-selected")) correct_path=true;
                else if (node.getStyleClass().equals("ui-diagram-end-selected")) correct_end=true;
                else if (node.getStyleClass().equals("ui-diagram-end-selected-false")) incorrect_solution=true;
                else if (node.getStyleClass().equals("ui-diagram-element-selected-false")) incorrect_path=true;
                else if (node.getStyleClass().equals("ui-diagram-start")) start_node=true;
                else if (node.getStyleClass().equals("ui-diagram-element")) decision_node=true;
                else if (node.getStyleClass().equals("ui-diagram-end")) end_node=true;
            
            int i=0;
            if (start_node) addNode("Start node", Integer.toString(canvas_width-300).concat("px"), Integer.toString(i++*65).concat("px"), "ui-diagram-start", "start");
            if (decision_node) addNode("Decision node", Integer.toString(canvas_width-300).concat("px"), Integer.toString(i++*65).concat("px"), "ui-diagram-element", "decision");
            if (end_node) addNode("End node", Integer.toString(canvas_width-300).concat("px"), Integer.toString(i++*65).concat("px"), "ui-diagram-end", "end");
            if (correct_path) addNode("Correct case-solving path (correct solution reachable)", Integer.toString(canvas_width-300).concat("px"), Integer.toString(i++*65).concat("px"), "ui-diagram-element-selected", "csr");
            if (correct_end) addNode("Correct end node", Integer.toString(canvas_width-300).concat("px"), Integer.toString(i++*65).concat("px"), "ui-diagram-end-selected", "cse");
            if (incorrect_path) addNode("Incorrect case-solving path (correct solution unreachable)", Integer.toString(canvas_width-300).concat("px"), Integer.toString(i++*65).concat("px"), "ui-diagram-element-selected-false", "icsp");
            if (incorrect_solution) addNode("Incorrect solution reached", Integer.toString(canvas_width-300).concat("px"), Integer.toString(i++*65).concat("px"), "ui-diagram-end-selected-false", "isr");
            if (unreached_correct) addNode("Unreached correct solution", Integer.toString(canvas_width-300).concat("px"), Integer.toString(i++*65).concat("px"), "ui-diagram-end-selected-true", "ucs");
        }
    }
    
    // Builds complete tree with default styles and arranges elements visually
    public void buildFullTree() {
        while (active_tree.size()>0) { pruneLeaves(); }                         // First reduce tree maximally to remove styles
        do  {
            ArrayList<Proposition> new_propositions = getNextPropositions();    // Get all next level propositions
            if (new_propositions.size()>0) {                                    
                active_tree.addAll(new_propositions);                           // Add them to active tree
                nodes_per_level.add(new_propositions.size());                   // Add them to nodes per level
                for (Proposition proposition : new_propositions)                // Add new propositions (create nodes and links)
                    addPropositionToGraph(proposition);
            }
        } while (active_tree.size() != tree.propositions.size());
        arrangeNodesOnScreen();
    }
    private ArrayList<Proposition> getNextPropositions() {
    
        ArrayList<Proposition> new_propositions = new ArrayList<>(); 
        if (this.active_tree.size()==0) {
            new_propositions=this.tree.getChildrenPropositions(this.tree.getStartNodeID());
        } else {
            for (int i = active_tree.size() - nodes_per_level.get(nodes_per_level.size() - 1); i < active_tree.size(); i++)
                new_propositions.addAll(this.tree.getChildrenPropositions(active_tree.get(i).getConcept_two()));
        }
        return new_propositions;
    }
    private void arrangeNodesOnScreen() {
        
        for (Element node : model.getElements())            // Onda nadi taj cvor u grafu
            node.setY(Integer.toString((tree.getNodeDepth(node.getId()))*(concept_height+offset_height)).concat("px"));
        this.centered_nodes.clear();                                            // Clear centered nodes list
        int leaves_number = this.getTree().getDiagnoses().size();               // Count leaves
        int x = default_width/2 - (leaves_number/2)*concept_width - (leaves_number-1)*offset_width/2;
        
        ArrayList<Integer> leaves_indexes = new ArrayList<>();
        for (Element node : this.model.getElements())                           // Distribute leaves evenly
            if (this.tree.getDiagnoses().contains(node.getId())) {
                node.setX(Integer.toString(x).concat("px"));
                centered_nodes.add(node.getId());
                x=x+concept_width+offset_width;
            }
        
        adjustWithLeaves(this.tree.getStartNodeID());                           // Center all other nodes according to veaves
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
    public Integer getScreen_x() {return canvas_width;}
    public void setScreen_x(Integer canvas_width) {this.canvas_width = canvas_width;}
    public Integer getScreen_y() {return canvas_height;}
    public void setScreen_y(Integer canvas_height) {this.canvas_height = canvas_height;}

    public DefaultDiagramModel getModel() {
        return model;
    }

    public HashMap<String, NodeInfo> getNode_info_map() {
        return node_info_map;
    }

    public void setNode_info_map(HashMap<String, NodeInfo> node_info_map) {
        this.node_info_map = node_info_map;
    }

    public List<Connection> getStored_connections() {
        return stored_connections;
    }

    public void setStored_connections(List<Connection> stored_connections) {
        this.stored_connections = stored_connections;
    }

    public int getDefault_height() {
        return default_height;
    }

    public void setDefault_height(int default_height) {
        this.default_height = default_height;
    }

    public int getDefault_width() {
        return default_width;
    }

    public void setDefault_width(int default_width) {
        this.default_width = default_width;
    }

    public int getConcept_height() {
        return concept_height;
    }

    public void setConcept_height(int concept_height) {
        this.concept_height = concept_height;
    }

    public int getConcept_width() {
        return concept_width;
    }

    public void setConcept_width(int concept_width) {
        this.concept_width = concept_width;
    }

    public int getOffset_height() {
        return offset_height;
    }

    public void setOffset_height(int offset_height) {
        this.offset_height = offset_height;
    }

    public int getOffset_width() {
        return offset_width;
    }

    public void setOffset_width(int offset_width) {
        this.offset_width = offset_width;
    }

    public int getCanvas_width() {
        return canvas_width;
    }

    public void setCanvas_width(int canvas_width) {
        this.canvas_width = canvas_width;
    }

    public int getCanvas_height() {
        return canvas_height;
    }

    public void setCanvas_height(int canvas_height) {
        this.canvas_height = canvas_height;
    }

    public ArrayList<NodeInfo> getArranged_elements() {
        return arranged_elements;
    }

    public void setArranged_elements(ArrayList<NodeInfo> arranged_elements) {
        this.arranged_elements = arranged_elements;
    }

    public ArrayList<String> getCentered_nodes() {
        return centered_nodes;
    }

    public void setCentered_nodes(ArrayList<String> centered_nodes) {
        this.centered_nodes = centered_nodes;
    }

}
