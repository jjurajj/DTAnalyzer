package org.primefaces.showcase.view.data.diagram;
 
import java.util.Random;
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
 
@ManagedBean(name = "diagramFlowChartView")
@ViewScoped
public class FlowChartView {
     
    public DefaultDiagramModel model;
 
    @PostConstruct
    public void init() {
        model = new DefaultDiagramModel();
        model.setMaxConnections(100);
        
        FlowChartConnector connector = new FlowChartConnector();
        connector.setPaintStyle("{strokeStyle:'#C7B097',lineWidth:3}");
        model.setDefaultConnector(connector);
         
        Element start = new Element("Fight <wbr>for\n<br><br />\r\n your dream", "25em", "1em");
        start.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
        
        Element trouble = new Element("Do you meet some trouble?", "20em", "18em");
        trouble.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
        trouble.addEndPoint(new BlankEndPoint(EndPointAnchor.CONTINUOUS_LEFT));
        trouble.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
         
        Element giveup = new Element("Do you give up?", "20em", "30em");
        giveup.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP_LEFT));
        giveup.addEndPoint(new BlankEndPoint(EndPointAnchor.LEFT));
        giveup.addEndPoint(new BlankEndPoint(EndPointAnchor.RIGHT));
         
        Element succeed = new Element("Succeed sdf sdfg sdfg sd fgs df gs dfgsdfgs", "50em", "18em");
        succeed.addEndPoint(new BlankEndPoint(EndPointAnchor.LEFT));
        succeed.setStyleClass("ui-diagram-success");
         
        Element fail = new Element("Fail\\u200B sd fgs--&#8203;<br />-- dfg. Sd fg. Sdfg <wbr>sdf g sdf gsd fg dfs dfg s dfgsdfsg.", "50em", "30em");
        fail.addEndPoint(new BlankEndPoint(EndPointAnchor.LEFT));
        fail.setStyleClass("ui-diagram-fail");
        
        model.addElement(start);
        model.addElement(trouble);
        model.addElement(giveup);
        model.addElement(succeed);
        model.addElement(fail);
                 
        model.connect(createConnection(start.getEndPoints().get(0), trouble.getEndPoints().get(0), null));
        model.connect(createConnection(trouble.getEndPoints().get(1), giveup.getEndPoints().get(0), "Yes"));
        model.connect(createConnection(trouble.getEndPoints().get(2), succeed.getEndPoints().get(0), "No"));
        model.connect(createConnection(giveup.getEndPoints().get(2), fail.getEndPoints().get(0), "Yes"));
        
        model.removeElement(giveup);
    }
     
    public void addNode() {
        
        model = new DefaultDiagramModel();
        model.setMaxConnections(100);
        
        FlowChartConnector connector = new FlowChartConnector();
        connector.setPaintStyle("{strokeStyle:'#C7B097',lineWidth:3}");
        model.setDefaultConnector(connector);
         
        Element start = new Element("Fight <wbr>for\n<br><br />\r\n your dream", "25em", "1em");
        start.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
        
        Element trouble = new Element("Do you meet some trouble?", "20em", "18em");
        trouble.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
        trouble.addEndPoint(new BlankEndPoint(EndPointAnchor.CONTINUOUS_LEFT));
        trouble.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
         
        Element giveup = new Element("Do you give up?", "20em", "30em");
        giveup.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP_LEFT));
        giveup.addEndPoint(new BlankEndPoint(EndPointAnchor.LEFT));
        giveup.addEndPoint(new BlankEndPoint(EndPointAnchor.RIGHT));
         
        Element succeed = new Element("Succeed sdf sdfg sdfg sd fgs df gs dfgsdfgs", "50em", "18em");
        succeed.addEndPoint(new BlankEndPoint(EndPointAnchor.LEFT));
        succeed.setStyleClass("ui-diagram-success");
         
        Element fail = new Element("Fail\\u200B sd fgs--&#8203;<br />-- dfg. Sd fg. Sdfg <wbr>sdf g sdf gsd fg dfs dfg s dfgsdfsg.", "50em", "30em");
        fail.addEndPoint(new BlankEndPoint(EndPointAnchor.LEFT));
        fail.setStyleClass("ui-diagram-fail");

        Element start2 = new Element("sadfasdg s gsd r\n your dream", "5em", "10em");
        start2.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
        
        Element start3 = new Element("sdfgsd sd fgsdf sdfg sdf", "12em", "20em");
        start3.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
        
        model.addElement(start);
        model.addElement(trouble);
        model.addElement(giveup);
        model.addElement(succeed);
        model.addElement(fail);
        model.addElement(start3);
        model.addElement(start2);
                 
        model.connect(createConnection(start.getEndPoints().get(0), trouble.getEndPoints().get(0), null));
        model.connect(createConnection(trouble.getEndPoints().get(1), giveup.getEndPoints().get(0), "Yes"));
        model.connect(createConnection(trouble.getEndPoints().get(2), succeed.getEndPoints().get(0), "No"));
        model.connect(createConnection(giveup.getEndPoints().get(2), fail.getEndPoints().get(0), "Yes"));
        
      
        
    }
    
    public DiagramModel getModel() {
        return model;
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