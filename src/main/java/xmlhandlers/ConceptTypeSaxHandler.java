package xmlhandlers;

import java.net.URLEncoder;
import java.util.HashMap;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ConceptTypeSaxHandler
extends DefaultHandler
{
            HashMap<String,String> mappings = new HashMap<String, String>();
            boolean typefl = false;
            boolean conceptfl = false;
            String type, concept;
            
            public HashMap<String,String> getMappings()
            {
                return mappings;
            }
            
            public void startElement(String uri, String localName,String qName, 
                    Attributes attributes) throws SAXException {

                    if (qName.equalsIgnoreCase("type")) {
                            typefl = true;
                    }

                    if (qName.equalsIgnoreCase("concept")) {
                            conceptfl = true;
                    }
            }

            
            // we assume that the XML is fairly simple and canonical,
            // always in the form: mappings/type first
            // followed by mapping/concept
            
            public void endElement(String uri, String localName,
                    String qName) throws SAXException {

                    if(qName.equals("concept"))
                    {
                        mappings.put(type, concept);
                    }

                    if(qName.equals("type"))
                    {
                    }                    
            }
                   
            public void characters(char ch[], int start, int length) throws SAXException {

                    if(typefl)
                    {
                            type = new String(ch, start, length);
                            typefl = false;
                    }

                    if(conceptfl)
                    {
                            concept = new String(ch, start, length);
                            conceptfl = false;
                    }
            }
}
