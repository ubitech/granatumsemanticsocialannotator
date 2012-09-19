package importdata;

import db.dbConnector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CacheEndpoint
{
    private static CacheEndpoint c;
    private dbConnector db;

    public CacheEndpoint()
    {
        db = new dbConnector();
        try {
            db.dbOpen();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    public HashMap<String,String> getSuggestions(String pattern)
    throws SQLException
    {
        ResultSet rs = null;
        int i = 1;
        HashMap<String,String> hashmap = new HashMap<String, String>();
        
//        rs = db.dbQuery("(select object, subject from ChemopreventiveAgent where predicate=\"http://www.w3.org/2000/01/rdf-schema#label\" and object like \"%" + pattern + "%\") union (select object, subject from molecule where predicate like \"%title%\" and object like \"%" + pattern + "%\" and char_length(object)<50);");
        rs = db.dbQuery("(select name, uri, \"ChemopreventiveAgent\" as \"class\" from chemopreventiveagent where name like \"%" + pattern + "%\" limit 0,10) union (select name, uri, \"Molecule\" as \"class\" from molecule where name like \"%" + pattern + "%\" and char_length(name)<50 limit 0,10);");        
        //        rs = db.dbQuery("(select name, uri, \"ChemopreventiveAgent\" as \"class\" from chemopreventiveagent where name like \"%" + pattern + "%\") union (select name, uri, \"Molecule\" as \"class\" from molecule where name like \"%" + pattern + "%\" and char_length(name)<50);");        
        System.out.println("(select name, uri, \"ChemopreventiveAgent\" as \"class\" from chemopreventiveagent where name like \"%" + pattern + "%\" limit 0,10) union (select name, uri, \"Molecule\" as \"class\" from molecule where name like \"%" + pattern + "%\" and char_length(name)<50 limit 0,10);");
//        rs = db.dbQuery("select name, uri from ChemopreventiveAgent where name like \"%" + pattern + "%\";");                
//        rs = db.dbQuery("select object, subject from molecule where predicate like \"%title%\" and object like \"%" + pattern + "%\" and char_length(object)<50;");        

        while(rs.next())
        {
//            hashmap.put(rs.getString("object"), rs.getString("subject"));
            hashmap.put(rs.getString("name") + "$" + rs.getString("class"), rs.getString("uri"));
        }

        rs.close();
        return hashmap;
    }

    public void close()
    throws SQLException
    {
        db.dbClose();
    }
    
    public static void main(String[] args)
    {
        CacheEndpoint ce = new CacheEndpoint();
        
        try {
            System.out.println(ce.getSuggestions("asp"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
