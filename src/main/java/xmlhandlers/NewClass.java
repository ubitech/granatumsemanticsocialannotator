package xmlhandlers;

import java.io.InputStream;
import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;


public class NewClass {
    
    public static void main(String[] args)
    {
        SAXParserFactory factory;
        SAXParser saxParser;
        XMLRPCResponseHandler handler;        
        factory = SAXParserFactory.newInstance();
        String xmlData = "<?xml version='1.0'?><methodResponse><value><string>12865</string></value></methodResponse>";
        try
        {
            saxParser = factory.newSAXParser();
            handler = new XMLRPCResponseHandler();
            saxParser.parse(new InputSource(new StringReader(xmlData)), handler);
            System.out.println(handler.getValues());
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }
}
