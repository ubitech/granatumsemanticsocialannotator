package xmlhandlers;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XMLRPCResponseHandler
extends DefaultHandler
{
            private SAXParserFactory factory;
            private SAXParser saxParser;
            private CategoriesSaxHandler handler;    
            LinkedList<String> values = new LinkedList<String>();
            boolean strfl = false;
            String str;
            
            public LinkedList<String> getValues()
            {
                return values;
            }
            
            public void startElement(String uri, String localName,String qName, 
                    Attributes attributes) throws SAXException {

                    if (qName.equalsIgnoreCase("string")) {
                            strfl = true;
                    }

            }
                       
            public void endElement(String uri, String localName,
                    String qName) throws SAXException {

                    if(qName.equals("string"))
                    {
                        values.add(str);
                    }

            }
                   
            public void characters(char ch[], int start, int length) throws SAXException {

                    if (strfl) {
                            str = new String(ch, start, length);
                            strfl = false;
                    }
            }
}
