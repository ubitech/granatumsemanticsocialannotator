package importdata;

import xmlhandlers.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class CategoryMappings
{
    private final String path = "http://localhost:8080/SemanticSocialAnnotator/pub/CategoriesMappings.xml";
    private SAXParserFactory factory;
    private SAXParser saxParser;
    private CategoriesSaxHandler handler;
        
    public CategoryMappings()
    throws ParserConfigurationException, SAXException
    {
        factory = SAXParserFactory.newInstance();
        saxParser = factory.newSAXParser();
        handler = new CategoriesSaxHandler();
    }
    
    public void parse()
    throws SAXException, IOException
    {
       saxParser.parse(path, handler);        
    }

    public String getSPARQL(String category)
    {
        return handler.getCategories().get(category).get("sparql");
    }

    public String getRestful(String concept)
    {
        return handler.getCategories().get(concept).get("restful");
    }    
    
    public Iterator<String> getCategories()
    {
        return handler.getCategories().keySet().iterator();
    }
    
    public static void main(String[] args)
    {
        try
        {
            CategoryMappings cm = new CategoryMappings();
            cm.parse();

            System.out.println(cm.getRestful("Molecule"));
            System.out.println(cm.getCategories());
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }
}
