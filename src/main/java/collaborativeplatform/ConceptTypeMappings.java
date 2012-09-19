package collaborativeplatform;
import xmlhandlers.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class ConceptTypeMappings
{
    private final String path = "http://192.168.1.201:8080/SemanticSocialAnnotator/pub/ConceptTypeMappings.xml";
    private SAXParserFactory factory;
    private SAXParser saxParser;
    private ConceptTypeSaxHandler handler;
        
    public ConceptTypeMappings()
    throws ParserConfigurationException, SAXException
    {
        factory = SAXParserFactory.newInstance();
        saxParser = factory.newSAXParser();
        handler = new ConceptTypeSaxHandler();
    }
    
    public void parse()
    throws SAXException, IOException
    {
       saxParser.parse(path, handler);        
    }

    public String getConcept(String type)
    {
        String s = null;
        String[] typeParts = type.split("/");
        
        s = handler.getMappings().get(typeParts[0]);
        System.out.println(handler.getMappings());
        if(s==null)
            s = handler.getMappings().get("default");

        return s;
    }

    public static void main(String[] args)
    {
        try
        {
            ConceptTypeMappings cm = new ConceptTypeMappings();
            cm.parse();

            System.out.println(cm.getConcept("Article_import"));
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }
}
