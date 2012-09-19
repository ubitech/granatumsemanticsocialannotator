package importdata;

import java.util.Collection;
import collaborativeplatform.*;
import xmlhandlers.SPARQLResponseSaxHandler;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
//import org.apache.xerces.impl.dtd.models.CMAny;

public class DataCloudSPARQLInterface
extends DataCloudInterface
{
    private String query;
    private CategoryMappings catMappings;
    private SAXParserFactory factory;
    private SAXParser saxParser;
    private SPARQLResponseSaxHandler handler;    
    
    public DataCloudSPARQLInterface(String concept, CategoryMappings catMappings)
    {
        super(concept);
        super.endpointURL = "http://srvgal78.deri.ie:8080/Granatum/sparql?output=xml&query=";
        
        try
        {
            this.catMappings = catMappings;
//            catMappings = new CategoryMappings();
//            catMappings.parse();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            System.exit(1);
        }

        this.query = catMappings.getSPARQL(concept);
        
        this.query = this.query.replaceAll(" ", "%20");
        this.query = this.query.replaceAll("\\+", "%20");
        this.query = this.query.replaceAll("#", "%23");
        this.query = this.query.replaceAll("\\[", "%5B");
        this.query = this.query.replaceAll("\\]", "%5D");        
        System.out.println("QUERY= " + this.query);
 
    }
    
    public Collection getAssociatedEntities()
    throws Throwable
    {
        SPARQLCacheEndpoint sparqlCache = SPARQLCacheEndpoint.getCacheEndpoint();
        StringBuffer msgsock = new StringBuffer();
        Collection collection = null;
        String line;
                    
        String responseMsg;

        // check if it is already cached
        if((collection=sparqlCache.getCached(query))!=null)
        {
            System.out.println("Cache hit for query=" + query);
            return collection;
        }
        
        URL targetURL = new URL(super.endpointURL + query);
        URLConnection connection = targetURL.openConnection();
        connection.setDoOutput(true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); 

        while((line = reader.readLine()) != null)
            msgsock.append(line+"\n");

        responseMsg = msgsock.toString().trim();
        reader.close();

        factory = SAXParserFactory.newInstance();
        saxParser = factory.newSAXParser();
        handler = new SPARQLResponseSaxHandler();
        saxParser.parse(new ByteArrayInputStream(responseMsg.getBytes()), handler);
        collection = handler.getResults().entrySet();
        
        // cache result
        sparqlCache.cache(query, collection);
        System.out.println("Cache miss - Storing result for query=" + query);
        
        return collection;
    }
    
    public static void main(String[] args)
    {
        try {        
            CategoryMappings cm = new CategoryMappings();
            cm.parse();
        
            DataCloudSPARQLInterface d = new DataCloudSPARQLInterface("ChemopreventiveAgent", cm);
            //DataCloudSPARQLInterface d2 = new DataCloudSPARQLInterface("Molecule", cm);

            
            System.out.println(d.getAssociatedEntities());
            System.out.println(d.getAssociatedEntities().size());
            //System.out.println(d2.getAssociatedEntities().size());              
            //Collection c = d2.getAssociatedEntities();
            //c.addAll(d.getAssociatedEntities());
            //System.out.println(c.size());
            
        } catch (Throwable ex) {
            Logger.getLogger(DataCloudSPARQLInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
