package exportdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

public class ExportDataSIOC
extends ExportDataInterface
{
    private String namespaces  = "xmlns:sioc=\"http://rdfs.org/sioc/ns#\" " + 
                                 "xmlns:ns0=\"http://rdfs.org/sioc/access#\" " + 
                                 "xmlns:ns1=\"http://rdfs.org/sioc/types#\" " +
                                 "xmlns:ns2=\"http://rdfs.org/sioc/services#\" " +
                                 "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" " +
                                 "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"";

    private String sparqlPrefixes = "prefix sioc: <http://rdfs.org/sioc/ns#>\n" +
                                    "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                    "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                                    "prefix xsd: <http://www.w3.org/2001/XMLSchema#>";

    private String exportData = null;
    
    private final int connectionTimeout = 10000;
    
    private final String platformURL = "http://chem.deri.ie/granatum/api";
    
    private String authToken = "UGFuYWdpb3RpczpxMnczZTRy";    

    private String objectID;
    
    public ExportDataSIOC()
    {
        super(null,null,null,null,null,null);
    }
    
    public ExportDataSIOC(String username, String resourceURL, String topicURLs, String content, String containerURL, String elementType) {
        super(username, resourceURL, topicURLs, content, containerURL, elementType);
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }    
    
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(String namespaces) {
        this.namespaces = namespaces;
    }

    public String getSparqlPrefixes() {
        return sparqlPrefixes;
    }

    public void setSparqlPrefixes(String sparqlPrefixes) {
        this.sparqlPrefixes = sparqlPrefixes;
    }   
    
    public String exportSIOCDataXML()
    {
        String[] tagArray = topicURLs.replace(' ', ',').split(",");
        
        this.exportData = new String("<sioc:" + elementType + " rdf:about=\"" + resourceURL + "\" " + this.namespaces + ">\n" +
                                     "\t<sioc:has_container rdf:resource=\"" + containerURL + "\"/>\n" +
                                     "\t<sioc:has_creator>\n" +
                                     "\t\t<sioc:UserAccount rdf:about=\"" + username + "\"/>\n" +
                                     "\t</sioc:has_creator>\n" +
                                     "\t<sioc:content>" + content + "</sioc:content>\n");

        for(int i=0;i<tagArray.length;i++)
            this.exportData = new String(exportData + "\t<sioc:topic rdf:resource=\"" + 
                                         tagArray[i] + "\" rdfs:label=\"" + i + "\"/>\n");

        this.exportData += "</sioc:" + elementType + ">\n";
        
        return exportData;
    }
       
    public String exportSIOCDataSPARQL()
    {
        String[] tagArray = topicURLs.replace(' ', ',').split(",");
        
        this.exportData = new String(sparqlPrefixes + "insert { \n" + 
                                     "sioc:Item rdf:about \"" + resourceURL + "\"^^xsd:string;\n" +
                                     "  sioc:has_container rdf:about \"" + containerURL + "\"^^xsd:string;\n");

        for(int i=0;i<tagArray.length;i++)
            this.exportData = new String(exportData + "  sioc:topic rdf:resource=\"" + 
                                         tagArray[i] + "\"^^xsd:string;\n");
        
        this.exportData = new String(exportData + "  sioc:content \"" + content + "\"^^xsd:string.\n}");
        
        return exportData;
    }

    public void setExportData(String exportData)
    {
        this.exportData = exportData;
    }
    
    public int uploadSIOC()
    throws UnsupportedEncodingException, IOException
    {   
        String filename = new String(objectID + "_"+((int)(Math.random()*10000))+ ".rdf");
        int statusCode = 0;
        
        // the headers try to immitate how a CURL request occurs
        // STUB - will simplify the data being sent
        HttpURLConnection con = (HttpURLConnection) new URL(this.platformURL).openConnection();          
        con.setRequestMethod("PUT");
        con.setDoOutput(true);
        con.setRequestProperty("User-Agent","curl/7.20.1 (x86_64-unknown-linux-gnu) libcurl/7.20.1 OpenSSL/1.0.0 zlib/1.2.3 libidn/1.15 libssh2/1.2.2_DEV");            
        con.setRequestProperty("Host","chem.deri.ie");
        con.setRequestProperty("Accept", "*/*");
        con.setRequestProperty("Authorization", "Basic " + authToken);
        con.setRequestProperty("Expect","100-continue");
        con.setRequestProperty("Content-Type", "multipart/form-data");
        con.setRequestProperty("Content-Disposition", "form-data; filename=" + filename);
        con.setRequestProperty("Content-Type", "text/plain");
        con.setRequestProperty("Connection", "close");
        con.connect();            
                    
        PrintWriter out = new PrintWriter(con.getOutputStream());
        out.println();
        out.println(this.exportData);
        out.flush();
        out.close();

        statusCode = con.getResponseCode();
        BufferedReader is = new BufferedReader(new InputStreamReader(con.getInputStream()));
   
        while(is.ready())
            System.out.println(is.readLine());

        con.disconnect();
                
        return statusCode;
    }
                
    public static void main(String[] args)
    {
        ExportDataSIOC exportDataSIOC = new ExportDataSIOC("panos", 
                                                           "http://www.xsd.uoc.gr",
                                                           new String("http://www.xsd.uoc.bk http://www.xsd.uoc.bk"),
                                                           "test test etst",
                                                           "http://www.billiejean.com",
                                                           "post");

        exportDataSIOC.setObjectID("424");
        try {
            System.out.println(exportDataSIOC.uploadSIOC());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ExportDataSIOC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportDataSIOC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

