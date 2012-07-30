package collaborativeplatform;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xerces.impl.dv.util.Base64;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import importdata.*;
import collaborativeplatform.*;
import exportdata.ExportDataSIOC;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Collection;
import java.util.LinkedList;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xmlrpc.XmlRpcException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import mocktesting.DataCloudSPARQLMock;
import net.sf.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import xmlhandlers.XMLRPCResponseHandler;


public class AnnotateResource
extends HttpServlet
{
    private UserAuthentication userAuth;
    private SPARQLCacheEndpoint sparqlCache;
    private ConceptTypeMappings conceptTypeMappings;
    private CategoryMappings categoryMappings;
    private CacheEndpoint ce;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        String operation = null;
        String action = null;

        try
        {
            operation = request.getParameter("op");
            action = request.getParameter("action");
            
            if(operation!=null)
            {
                if(action==null)
                {
                    CollaborativePlatformInterface collPlatform = new CollaborativePlatformInterface();
                    collPlatform.requestTokensOAuth();
                    response.sendRedirect(collPlatform.getOauthAuthTokenURL()
                                        + "&oauth_token=" + collPlatform.getOauthToken() + "&"
//                                        + "a1=" + java.net.URLEncoder.encode(Base64.encode((collPlatform.getOauthToken()).getBytes("UTF-8"))) + "&"
//                                        + "a2=" + java.net.URLEncoder.encode(Base64.encode((collPlatform.getOauthTokenSecret()).getBytes("UTF-8"))) + "&"
                                        + "oauth_callback=" + java.net.URLEncoder.encode(request.getRequestURL().toString() + "?" + request.getQueryString() + "&a1=" + Base64.encode((collPlatform.getOauthToken()).getBytes("UTF-8")) + "&a2=" + Base64.encode((collPlatform.getOauthTokenSecret()).getBytes("UTF-8")) + "&action=" + operation));
                }
                else if(operation.equals("create")  || (operation.equals("edit")) || (operation.equals("replyto")))
                    this.displayAnnotation(request, response, operation);
                else if(operation.equals("headers"))                
                    this.showHeaders(request, response);
                else if(operation.equals("createAnnotation") || operation.equals("replytoAnnotation"))
                    this.createAnnotation(request, response);
                else if(operation.equals("editAnnotation"))
                    this.editAnnotation(request, response);
                else if(operation.equals("makeSuggestions"))
                    this.makeSuggestions(request, response);      
                else if(operation.equals("provideSIOC"))
                    this.provideSIOC(request, response);                 
                else
                    this.forwardToPage("/errorPage.jsp?errormsg=Wrong%20Operation", request, response);
            }
        } 
        catch(Throwable t)
        {            
            t.printStackTrace();
            this.forwardToPage("/errorPage.jsp?errormsg=" + t.getMessage(), request, response);
        }
    }

    public void init()
    throws ServletException
    {
        ce = new CacheEndpoint();
        
        userAuth = new UserAuthentication();
/*        
        DataCloudSPARQLMock mock = DataCloudSPARQLMock.getMock();        
        try {
            categoryMappings = new CategoryMappings();
            categoryMappings.parse();
        } catch (Throwable t) {
            Logger.getLogger(AnnotateResource.class.getName()).log(Level.SEVERE, null, t);
        }
  */      
        try {
            conceptTypeMappings = new ConceptTypeMappings();
            conceptTypeMappings.parse();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AnnotateResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(AnnotateResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
                Logger.getLogger(AnnotateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        sparqlCache = SPARQLCacheEndpoint.getCacheEndpoint();
    }

    private void provideSIOC(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SQLException
    {
        HttpSession session = request.getSession();
        String sioc = (String)session.getAttribute(request.getParameter("id"));
        PrintWriter out = response.getWriter();
        response.setContentType("text/xml; charset=UTF-8");
        
        out.write(sioc);
        out.flush();
        out.close();
    }    

    private void showHeaders(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SQLException
    {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            String title = "Servlet Example: Showing Request Headers";
            out.println("<BODY BGCOLOR=\"#FDF5E6\">\n" +
                        "<H1 ALIGN=CENTER>" + title + "</H1>\n" +
                        "<B>Request Method: </B>" +
                        request.getMethod() + "<BR>\n" +
                        "<B>Request URI: </B>" +
                        request.getRequestURI() + "<BR>\n" +
                        "<B>Request Protocol: </B>" +
                        request.getProtocol() + "<BR><BR>\n" +
                        "<TABLE BORDER=1 ALIGN=CENTER>\n" +
                        "<TR BGCOLOR=\"#FFAD00\">\n" +
                        "<TH>Header Name<TH>Header Value");
                Enumeration headerNames = request.getHeaderNames();
                while(headerNames.hasMoreElements()) {
                  String headerName = (String)headerNames.nextElement();
                  out.println("<TR><TD>" + headerName);
                  out.println("    <TD>" + request.getHeader(headerName));
                  System.out.println(headerName + " " + request.getHeader(headerName));
                }

            out.println("</TABLE>\n</BODY></HTML>");        
    }
    
    private void makeSuggestions(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SQLException
    {
        PrintWriter out = null;
        String term = null;
        HttpSession session = request.getSession();
        String jsonString = new String("");
        JSONObject json = new JSONObject();
        HashMap<String,String> suggestions = new HashMap<String, String>();
        HashMap<String,String> sessionsuggestions = (HashMap<String,String>)session.getAttribute("suggestions");
        
        if(sessionsuggestions == null)
            sessionsuggestions = new HashMap<String, String>();
        
        out = response.getWriter();
        term = request.getParameter("term");
        suggestions.putAll(ce.getSuggestions(term));
        sessionsuggestions.putAll(suggestions);
        session.setAttribute("suggestions", sessionsuggestions);
        HashMap<String,String> x = (HashMap<String,String>) session.getAttribute("suggestions");
        System.out.println("haspmap=" + x);
        response.setContentType("application/json;charset=UTF-8");
                
        Iterator iter = suggestions.keySet().iterator();
        
        while(iter.hasNext())
        {
            String s = (String)iter.next();
            String name = s.split("\\$")[0];
            System.out.println(name);
            String className = s.split("\\$")[1];

            // correct format just for json            
            json.put("name", formalize(name));
            json.put("classname", className);            
            jsonString += json.toString() + ",";
        }

        out.println(request.getParameter("callback") + "([" + jsonString.substring(0, jsonString.length()-1) + "])");
        out.flush();        
    }    
    
    private String formalize(String s)
    {
        s = s.replace(':', ' ');        
        
        return s;
    }
    /*
    protected void getRestConcepts(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, XmlRpcException, Throwable
    {   
        HttpSession session = request.getSession(true);
        PrintWriter out = response.getWriter();
        Iterator categories = categoryMappings.getCategories();
        String cat = null;
        
        Collection queryResult = null;
        Iterator queryResultIterator = null;
        DataCloudSPARQLInterface sparqlInterface;

        // in cases like this, we will retrieve 
        // everything associated with a category
        // regardless of the mime type
        String fileMIMEtype = new String("default");        
        response.setContentType("text/xml; charset=UTF-8");
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        out.write("<tree id=\"0\">");
        // populate the result xml
        while(categories.hasNext())
        {
            cat = (String)categories.next();
            out.write("<item text=\"" + cat + "\" id=\"" + cat + "\" nocheckbox=\"true\">");

//            sparqlInterface = new DataCloudSPARQLInterface(fileMIMEtype, cat, categoryMappings);
            sparqlInterface = new DataCloudSPARQLInterface(cat, categoryMappings);            
            queryResult = sparqlInterface.getAssociatedEntities();
            queryResultIterator = queryResult.iterator();

            while(queryResultIterator.hasNext())
            {
                String concept = (String)queryResultIterator.next();
                out.write("<item text=\"" + concept + "\" id=\"" + concept + "\"/>");
            }

            out.write("</item>");
        }

        out.write("</tree>");        
        out.flush();
    }    
    
    protected void getConcepts(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, XmlRpcException, Throwable
    {   
        HttpSession session = request.getSession(true);
        Collection queryResult = null;
        Iterator queryResultIterator = null;
        PrintWriter out = response.getWriter();
        DataCloudSPARQLInterface sparqlInterface;
        String fileMIMEtype = (String)session.getAttribute("filemimetype");
        String category = request.getParameter("category");
        System.out.println(fileMIMEtype + " " + category);
        
//        sparqlInterface = new DataCloudSPARQLInterface(fileMIMEtype, category, categoryMappings);
        
        sparqlInterface = new DataCloudSPARQLInterface(category, categoryMappings);
        queryResult = sparqlInterface.getAssociatedEntities();
        
        response.setContentType("text/xml; charset=UTF-8");
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        out.write("<rows>\n");
        queryResultIterator = queryResult.iterator();
        
        // populate the result xml
        while(queryResultIterator.hasNext())
        {
            String concept = (String)queryResultIterator.next();
            out.write("<row id=\""  + concept + "\"><cell>" + concept + "</cell><cell>0</cell></row>\n");
        }
        
        out.write("</rows>\n");                
        out.flush();
    }

    protected void getMockConcepts(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, XmlRpcException, Throwable
    {   
        HttpSession session = request.getSession(true);
        PrintWriter out = response.getWriter();
        DataCloudSPARQLMock mock = DataCloudSPARQLMock.getMock();
        String fileMIMEtype = (String)session.getAttribute("filemimetype");
        String category = request.getParameter("category");
        
        response.setContentType("text/xml; charset=UTF-8");
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        out.write("<tree id=\"0\">");
        HashMap<String,HashMap> clist = mock.getHashMap("default_" + category);
        Iterator iterA = clist.keySet().iterator();
        
        // populate the result xml
        while(iterA.hasNext())
        {
            String c = (String)iterA.next();
            out.write("<item text=\"" + c + "\" id=\"" + c  + "\" nocheckbox=\"true\">");

            HashMap<String,String> listmap = clist.get(c);
            Iterator iterB = listmap.keySet().iterator();

            while(iterB.hasNext())
            {
                String concept = (String)iterB.next();
                out.write("<item text=\"" + concept + "\" id=\"" + listmap.get(concept) + "\"/>");
            }
            
            out.write("</item>");
        }
        
        out.write("</tree>\n");
        out.flush();
    }    
    */


    protected void editAnnotation(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, XmlRpcException
    {
        HttpSession session = request.getSession(true);
        String SIOCdata = null;
        String newObjectID = null;
        String filemimetype = null;
        String backObj  = (String)request.getParameter("backObj");
        String password = (String)session.getAttribute("password");
        String username = (String)session.getAttribute("username");
        String userID   = (String)session.getAttribute("userID");
        String category = (String)request.getParameter("category");
        String objectID = (String)request.getParameter("objectID");
        String subject  = (String)request.getParameter("subject");
        String body     = new String(" ");        
        String lbds     = new String("http://www.testref.com");
        String topics   = new String("");
        HashMap<String,String> suggestions = (HashMap<String,String>)session.getAttribute("suggestions");        
        int statusCode = 0;
        String accessToken = null;
        String accessTokenSecret = null;
        
        topics = new String(getURIs((String)request.getParameter("selections"), suggestions)+ " ");
        CollaborativePlatformInterface collPlatform = new CollaborativePlatformInterface();
        System.out.println("--- " + request.getParameter("a1") + " " + request.getParameter("a2"));
        
        System.out.println(new String(Base64.decode(request.getParameter("a1"))) + " " + new String(Base64.decode(request.getParameter("a2"))));
        collPlatform.requestAccessOAuth(new String(Base64.decode(request.getParameter("a1"))), new String(Base64.decode(request.getParameter("a2"))));
        accessToken = collPlatform.getOauthTokenAccess();
        accessTokenSecret = collPlatform.getOauthTokenSecretAccess();
        
        System.out.println(accessToken + " " + accessTokenSecret);
        filemimetype = collPlatform.retrieveMIMEType(objectID, accessToken, accessTokenSecret);

        topics+=conceptTypeMappings.getConcept(filemimetype);        
        newObjectID = collPlatform.editAnnotation(objectID, subject, body, topics, lbds, accessToken, accessTokenSecret);
        session.setAttribute("suggestions", null);

//        SIOCdata = collPlatform.retrieveSIOCfromPlatform(newObjectID, accessToken, accessTokenSecret);
//        System.out.println(SIOCdata);
//        session.setAttribute(newObjectID, SIOCdata);
//        collPlatform.uploadFileFromURL("10210", (newObjectID+".rdf"), "http://94.75.243.141:8080/SemanticSocialAnnotator/AnnotateResource?op=provideSIOC&action=sioc&id="+newObjectID, accessToken, accessTokenSecret);
        
        response.sendRedirect(backObj);        
    }     
      
    protected void createAnnotation(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, XmlRpcException
    {
        HttpSession session = request.getSession(true);
        String SIOCdata = null;
        String newObjectID = null;
//        String filemimetype = (String)session.getAttribute("filemimetype");
        String filemimetype = null;
        String replyToOp = (String)session.getAttribute("replyto");
        String backObj  = (String)request.getParameter("backObj");        
        String password = (String)session.getAttribute("password");
        String username = (String)session.getAttribute("username");
        String userID   = (String)session.getAttribute("userID");
        String category = (String)request.getParameter("category");
        String objectID = (String)request.getParameter("objectID");
        String subject  = (String)request.getParameter("subject");
        HashMap<String,String> suggestions = (HashMap<String,String>)session.getAttribute("suggestions");
        String body     = new String(" ");                
        String lbds     = new String("http://www.testref.com");
        String topics   = new String("");
        String accessToken = null;
        String accessTokenSecret = null;
        String homeFolder = null;
        int statusCode = 0;
        boolean replyTo = false;

        System.out.println(" Selections=" + (String)request.getParameter("selections"));
        topics = new String(getURIs((String)request.getParameter("selections"), suggestions));
        System.out.println(topics);
        
        if(replyToOp.equals("replyto"))
            replyTo = true;

        CollaborativePlatformInterface collPlatform = new CollaborativePlatformInterface();
        System.out.println("--- " + request.getParameter("a1") + " " + request.getParameter("a2"));
        
        System.out.println(new String(Base64.decode(request.getParameter("a1"))) + " " + new String(Base64.decode(request.getParameter("a2"))));
        collPlatform.requestAccessOAuth(new String(Base64.decode(request.getParameter("a1"))), new String(Base64.decode(request.getParameter("a2"))));

        accessToken       = collPlatform.getOauthTokenAccess();
        accessTokenSecret = collPlatform.getOauthTokenSecretAccess();
        
        System.out.println(accessToken + " " + accessTokenSecret);
        filemimetype = collPlatform.retrieveMIMEType(objectID, accessToken, accessTokenSecret);

        topics+=conceptTypeMappings.getConcept(filemimetype);
        
        System.out.println(" -FileMimeType=" + filemimetype);
        newObjectID = collPlatform.storeAnnotation(objectID, subject, body, topics, replyTo, lbds, accessToken, accessTokenSecret);
        session.setAttribute("suggestions", null);
        
        homeFolder = collPlatform.getHomeDirectory(accessToken, accessTokenSecret);
       
        SIOCdata = collPlatform.retrieveSIOCfromPlatform(newObjectID, accessToken, accessTokenSecret);
        
        System.out.println(homeFolder);
        System.out.println(SIOCdata);
        System.out.println("http://www.granatum.org/bscw/bscw.cgi/"+newObjectID+"?op=sioc.get_sioc");        
        collPlatform.uploadFileFromURL(homeFolder, (newObjectID+".rdf"), "http://www.granatum.org/bscw/bscw.cgi/"+newObjectID+"?op=sioc.get_sioc", accessToken, accessTokenSecret);

        if(newObjectID==null)
        {
            this.forwardToPage("/errorPage.jsp?errormsg=Error%20uploading%20sioc%20" + statusCode, request, response);
            return;
        }   
        
        response.sendRedirect(backObj);        
    }    


    
    private String getURIs(String selection, HashMap<String,String> suggestions)
    {
        String topics = new String("");
        String[] selectionArray = selection.split(",");
        
        System.out.println(selection);
        System.out.println(suggestions);

        for(int i=0;i<selectionArray.length;i++)
        {
            topics += suggestions.get(selectionArray[i].substring(0,selectionArray[i].length()-2)) + " ";
            System.out.println(" --- " + selectionArray[i].substring(0,selectionArray[i].length()-2) + " " + suggestions.get(selectionArray[i].substring(0,selectionArray[i].length()-2)));
        }        
        
        return topics;
    }    
    
    protected void displayAnnotation(HttpServletRequest request, HttpServletResponse response, String mode)
    throws ServletException, IOException, XmlRpcException, Throwable
    {
        String objectID = null;
        String backObj = null;
        String userID = null;
        String username = null;
        String password = null;
        String fileMIMEtype = new String("null");
        Collection queryResult;
        String category = null;
        String op = null;
        HttpSession session = request.getSession(true);
        HashMap<String,String> suggestions = (HashMap<String,String>)session.getAttribute("suggestions");
        CollaborativePlatformInterface collPlatform;
        String accessToken = null;
        String accessTokenSecret = null;
        
        objectID = request.getParameter("objectID");
        userID  = request.getParameter("userID");
        backObj = request.getParameter("back_obj");
        op = request.getParameter("op");

        if(suggestions!=null)
            suggestions.clear();
        else
            suggestions = new HashMap<String, String>();
        
        session.setAttribute("suggestions", suggestions);

        /*
        username = new String(userAuth.getUsername(userID));
        password = new String(userAuth.getPassword(userID));
                
        if(!userAuth.authenticateUser(username, password))
        {
            this.forwardToPage("/errorPage.jsp?errormsg=Error%20on%20login", request, response);
            return;
        }

        session.setAttribute("userID",   userID);
        session.setAttribute("username", username);
        session.setAttribute("password", password);
                      
        collPlatform = new CollaborativePlatformInterface(password, username);
         * 
         */
/*               
        collPlatform = new CollaborativePlatformInterface();
        System.out.println("--- " + request.getParameter("a1") + " " + request.getParameter("a2"));
        
        System.out.println(new String(Base64.decode(request.getParameter("a1"))) + " " + new String(Base64.decode(request.getParameter("a2"))));
        collPlatform.requestAccessOAuth(new String(Base64.decode(request.getParameter("a1"))), new String(Base64.decode(request.getParameter("a2"))));

        accessToken       = collPlatform.getOauthTokenAccess();
        accessTokenSecret = collPlatform.getOauthTokenSecretAccess();
        
        System.out.println(accessToken + " " + accessTokenSecret);
        
        fileMIMEtype = collPlatform.retrieveMIMEType(objectID, accessToken, accessTokenSecret);
*/
        // bind data before displaying the page
//        session.setAttribute("filemimetype", fileMIMEtype);
//        System.out.println("fileMIMEtype=" + fileMIMEtype);
        session.setAttribute("replyto", op);
        
        this.forwardToPage("/annotateResource2.jsp?mode=" + mode + "Annotation&back_obj=" + backObj + "&objectID=" + objectID, request, response);
    }
    
    private void forwardToPage(String url, HttpServletRequest req, HttpServletResponse resp)
    throws IOException, ServletException
    {
        RequestDispatcher dis;
        
        dis = getServletContext().getRequestDispatcher(url);
        dis.forward(req, resp);
        
        return;
    }    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo()
    {
        return "Granatum Annotator";
    }
    
    public static void main(String args[])
    {
        System.out.println(new String(Base64.decode("MTIyMzAtMTM0MjQzMzY4OS0yMzc2MzQ2")));
    }
}
