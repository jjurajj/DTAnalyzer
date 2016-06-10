package treeDrawer;

import decisionTree.DT;
import decisionTree.Proposition;
import java.util.ArrayList;
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
 
@ManagedBean(name = "StaticTree")
@ViewScoped

public class StaticTree {
    
    public DefaultDiagramModel model;
    public DT tree;
    public ArrayList<Proposition> active_tree = new ArrayList<>();             // Reprezentacija stabla za aktivni prikaz
    public ArrayList<Integer> nodes_per_level = new ArrayList<>();
    public Integer screen_x, screen_y, step_y=90, x_offset=50;
    
    public void initialize(DT decision_tree) {this.initialize(decision_tree,1200,800);}
    public void initialize(DT decision_tree, int height, int width) {
        
        active_tree = new ArrayList<>();
        nodes_per_level = new ArrayList<>();
         
        this.tree = decision_tree;
        this.screen_x=width;
        this.screen_y=height;
        
        model = new DefaultDiagramModel();
        model.setMaxConnections(-1);
         
        FlowChartConnector connector = new FlowChartConnector();
        connector.setPaintStyle("{strokeStyle:'#d8ccc0',lineWidth:2}");
        model.setDefaultConnector(connector);
        
        Element start = new Element(tree.getStartNodeName(), Integer.toString(screen_x/2).concat("px"), "10px");
        start.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
        start.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
        start.setId(tree.getStartNodeID());
        start.setStyleClass("ui-diagram-start");
        model.addElement(start);
        this.buildFullTree();
        
    }
    public DiagramModel getModel() {return this.model;}
    public void visualArrange() {}
    public void addPropositionToGraph(Proposition proposition) {
        
        // Definicija novog koncepta koji se dodaje u graf
        Element new_child = new Element(tree.getNodeNameFromID(proposition.getConcept_two()));
        new_child.setId(proposition.getConcept_two());
        new_child.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
        new_child.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
        if (tree.diagnoses.contains(proposition.getConcept_two()))
            new_child.setStyleClass("ui-diagram-end");
        
        
        // Nađi roditelja po IDju i poveži ga s čvorom
        for (Element node : model.getElements())
            if (node.getId().equals(proposition.getConcept_one())) 
                model.connect(createConnection(node.getEndPoints().get(1), new_child.getEndPoints().get(0), proposition.getLink()));
        
        model.addElement(new_child);
        
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
        active_tree.addAll(new_propositions);
        ArrayList<String> leaves = getLeaves();
        nodes_per_level.add(new_propositions.size());
        for (Proposition proposition : new_propositions)
            addPropositionToGraph(proposition);
        
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
    public void buildFullTree() {
        
        ArrayList<Proposition> last_propositions = new ArrayList<>();
        do  {
            expandLeaves();
        } while (active_tree.size() != tree.propositions.size());
    
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
