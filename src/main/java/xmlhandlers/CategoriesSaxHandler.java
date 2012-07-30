package xmlhandlers;

import java.net.URLEncoder;
import java.util.HashMap;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CategoriesSaxHandler
extends DefaultHandler
{
            HashMap<String, HashMap<String,String>> categories = new HashMap();
            HashMap<String,String> mappings;
            boolean namefl = false;
            boolean restfulfl = false;
            boolean sparqlfl = false;
            boolean restfulbindingsfl = false;
            boolean sparqlbindingsfl = false;            
            String name, sparql, restfull, restfulbindings, sparqlbindings;
            private boolean inEntity;
            private String entityName;
            
            public HashMap<String,HashMap<String,String>> getCategories()
            {
                return categories;
            }
            
            public void startElement(String uri, String localName,String qName, 
                    Attributes attributes) throws SAXException {

                    if (qName.equalsIgnoreCase("name")) {
                            namefl = true;
                    }

                    if (qName.equalsIgnoreCase("sparql")) {
                            sparqlfl = true;
                    }

                    if (qName.equalsIgnoreCase("sparqlbindings")) {
                            sparqlbindingsfl = true;
                    }                    

                    if (qName.equalsIgnoreCase("restful")) {
                            restfulfl = true;
                    }

                    if (qName.equalsIgnoreCase("restfulbindings")) {
                            restfulbindingsfl = true;
                    }

                    if (qName.equalsIgnoreCase("mapping")) {
                            mappings = new HashMap<String, String>();
                    }                    
                    
                    // SAX has a problem reading character "<"
                    // Solutions: a) a hack in SAX parser or
                    // b) url-coding that character at the input file
            }

            
            // we assume that the XML is fairly simple and canonical,
            // always in the form: mappings/mapping/mimetype first
            // followed by mappings/mapping/sparql
            
            public void endElement(String uri, String localName,
                    String qName) throws SAXException {

                    if(qName.equals("mapping"))
                    {
                        categories.put(name, mappings);
                    }

                    if(qName.equals("sparql"))
                    {
                        mappings.put(qName, sparql);                        
                    }

                    if(qName.equals("sparqlbindings"))
                    {
                        mappings.put(qName, sparqlbindings);                        
                    }                    

                    if(qName.equals("restful"))
                    {
                        mappings.put(qName, restfull);
                    }

                    if(qName.equals("restfulbindings"))
                    {
                        mappings.put(qName, restfulbindings);
                    }                                        
            }
                   
            public void characters(char ch[], int start, int length) throws SAXException {

                    if (namefl) {
                            name = new String(ch, start, length);
                            namefl = false;
                    }

                    if (sparqlfl) {
                            sparql = new String(ch, start, length);
                            sparqlfl = false;
                    }

                    if (sparqlbindingsfl) {
                            sparqlbindings = new String(ch, start, length);
                            sparqlbindingsfl = false;
                    }                    

                    if (restfulfl) {
                            restfull = new String(ch, start, length);
                            restfulfl = false;
                    }

                    if (restfulbindingsfl) {
                            restfulbindings = new String(ch, start, length);
                            restfulbindingsfl = false;
                    }                    
            }
}
