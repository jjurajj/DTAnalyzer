/**
 * Ovo bi trebalo ili ucitati gotovu tablicu parametara vrijednosti i diajagnoza iz nekog fajla
 * ili ju obnoviti ako ne postoji ili na zahtjev (ako se sadrzaj baze promijenio)
 * 
 * I onda dalje ovo sluzi za vrednovanje korisnickog stabla jer sadrzi sirove info iz caseova
 * pa bi trebalo ici najbrze.
 * 
 * @author juraj
 */

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.*;

@ManagedBean (name ="Infobase", eager = true)
@SessionScoped
public class Infobase {
    
    public String url = "http://diana.zesoi.fer.hr/~jpetrovic/case_repository/car_starting/";
    public ArrayList<Case> znanje = new ArrayList<>();
    public ArrayList<String> caseovi = new ArrayList<>();
    
    public void initialize(String url) {
        this.url = url;
        initialize();
    }
    
    public void initialize() {
        
        // provjeri jel u casebaseu ima index.txt file, ako ima parsaj ga, ak ne, pokreni metodu za generiranje pa ga onda parsaj
    
        try {
            Document doc = Jsoup.connect(this.url.concat("index.txt")).post();
        } catch(IOException e){
            // Ako nema indeks fajla onda treba proparsat caseove i generirat ga
            // To ide u odvojenu metodu da bi se moglo i nezavisno pozvat po potrebi
            //parseCaseBase(listCaseBase(this.url), "case.txt");
            // pozovi metodu za parsanje caseova
            // pozovi metodu za parsanje
        }

        // parsaj to sranje i izvuci info
    
    }
    
    // Ovo vraća popis URL direktorija u svakom od kojih se nalazi 1 case, ocekivano imena case.txt i eventualno s popratnim materijalima
    public ArrayList<String> listCaseBase(String url) {
        
        ArrayList<String> lista_caseova = new ArrayList<>();
        
        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");                 //http://jsoup.org/cookbook/extracting-data/example-list-links
            for (Element link : links) {
                if (link.attr("href").equals(link.text())) lista_caseova.add(this.url.concat(link.attr("href")));
            } 
        } catch (IOException e) {};
    
        return lista_caseova;
    }

    public String readCase(String url) throws MalformedURLException {
        String text = "";
        URL case_url;
        InputStream stream = null;
        DataInputStream dis;
        String line;
      
        try {   //http://alvinalexander.com/java/edu/pj/pj010011
            case_url = new URL(url);
            stream = case_url.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(case_url.openStream()));
            while ((line = in.readLine()) != null) {
                text = text.concat(line).concat("\r\n");
            }
            in.close();
            return text;
        }
        catch (MalformedURLException URLe) {
            return "NULL";
        }
        catch (IOException IOe) {
            return "NULL";
        }
    }
        
        
    // Ovo ucita svaki case.txt fajl iz zadane liste case URLova i spremi ga lokalno u fajl
    public void parseCaseBase(ArrayList<String> caseovi, String case_file_name) throws MalformedURLException {
    
        for (String case_file : caseovi) {
            
            String file_link = case_file.concat("/").concat(case_file_name);
            String case_text = readCase(file_link);
        }
    }
    
    public ArrayList<String> getCaseovi() {
        return caseovi;
    }
    public void setCaseovi(ArrayList<String> caseovi) {
        this.caseovi = caseovi;
    }
    public ArrayList<Case> getZnanje() {
        return znanje;
    }
    public void setZnanje(ArrayList<Case> znanje) {
        this.znanje = znanje;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}

