package xmlhandlers;

import java.net.URLEncoder;
import java.util.HashMap;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SPARQLResponseSaxHandler
extends DefaultHandler
{
            HashMap<String, String> results = new HashMap();
            boolean urifl = false;
            boolean bindingfl = false;
            boolean literalfl = false;
            String uri, bindingName, literal;
            int i = 0;
            
            public HashMap<String,String> getResults()
            {
                return results;
            }
            
            public void startElement(String uri, String localName,String qName, 
                    Attributes atts) throws SAXException {

                    if (qName.equalsIgnoreCase("uri")) {
                        urifl = true;
                    }

                    if (qName.equalsIgnoreCase("literal"))
                    {
                        literalfl = true;
                    }
/*
 *                         int length = atts.getLength();
                        for (int i=0; i<length; i++) {
                            String name = atts.getQName(i);
                            String value = atts.getValue(i);
                        }
                        
                        bindingfl = true;
 */
            }
            
            public void endElement(String uri, String localName,
                    String qName) throws SAXException {

                    if(qName.equals("uri"))
                    {
                        System.out.println((i++) + " " + this.uri + " " + this.literal);
                        results.put(this.literal, this.uri);
                    }
            }

            
            public void characters(char ch[], int start, int length) throws SAXException {

                    if (urifl) {
                        uri = new String(ch, start, length);
                        urifl = false;
                    }

                    if (literalfl) {
                        literal = new String(ch, start, length);
                        literalfl = false;
                    }
            }
}