package collaborativeplatform;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.xerces.impl.dv.util.Base64;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import xmlhandlers.XMLRPCResponseHandler;

public class CollaborativePlatformInterface
{
    private final String oauthRequestTokenURL = "http://www.granatum.org/bscw/bscw.cgi?op=OAuth";
    private final String oauthAuthTokenURL    = "http://www.granatum.org/bscw/bscw.cgi?op=OAuth";
    private final String oauthExecTokenURL    = "http://www.granatum.org/bscw/bscw.cgi";
    
    private final String consumerKey    = "Ubitech_Annotator";
    private final String consumerSecret = "67f079a9d0538024956855912406435b";
    
    private String platformURL = "http://www.granatum.org/bscw/bscw.cgi";
    private String password;
    private String username;
    private XmlRpcClientConfigImpl config;
    private XmlRpcClient client;
    private Object[] params;
    private int oauthTimestamp = 1342167429;
    private int oauthTimedelta = 1;
    private String oauthTokenSecret = null;
    private String oauthToken = null;
    private String oauthTokenSecretAccess = null;
    private String oauthTokenAccess = null;

    public String getOauthTokenAccess() {
        return oauthTokenAccess;
    }

    public String getOauthTokenSecretAccess() {
        return oauthTokenSecretAccess;
    }
    
    public String getOauthAuthTokenURL() {
        return oauthAuthTokenURL;
    }

    public String getOauthRequestTokenURL() {
        return oauthRequestTokenURL;
    }
   
    public int getOauthTimedelta() {
        return oauthTimedelta;
    }

    public void setOauthTimedelta(int oauthTimedelta) {
        this.oauthTimedelta = oauthTimedelta;
    }

    public int getOauthTimestamp() {
        return oauthTimestamp;
    }

    public void setOauthTimestamp(int oauthTimestamp) {
        this.oauthTimestamp = oauthTimestamp;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getOauthTokenSecret() {
        return oauthTokenSecret;
    }

    public void setOauthTokenSecret(String oauthTokenSecret) {
        this.oauthTokenSecret = oauthTokenSecret;
    }

    
    public CollaborativePlatformInterface()
    {
        this.password = null;
        this.username = null;
    }    
    
    public CollaborativePlatformInterface(String password, String username)
    throws MalformedURLException
    {
        this.password = password;
        this.username = username;
        this.config = new XmlRpcClientConfigImpl();
        this.config.setServerURL(new URL(platformURL));
        this.config.setBasicUserName(this.username);
        this.config.setBasicPassword(this.password);
        client = new XmlRpcClient();

        client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
        client.setConfig(config);        
    }

    public String getPlatformURL()
    {
        return new String(this.platformURL);
    }
    
    public void shutdown()
    {
        client = null;
    }
    
    public int retrieveObjectID(String source)
    {
        return 0;
    }

    public void requestTokensOAuth()
    {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(oauthRequestTokenURL);

        while(oauthToken==null)
        {
                try {
                  method.setRequestHeader("Authorization", "OAuth oauth_signature=\"" + consumerSecret + "%26\",oauth_consumer_key=\"" + consumerKey + "\",oauth_signature_method=\"PLAINTEXT\",oauth_nonce=\"2376346\",oauth_timestamp=" + oauthTimestamp);                
                  System.out.println("AUTH= " + "OAuth oauth_signature=\"" + consumerSecret + "%26\",oauth_consumer_key=\"" + consumerKey + "\",oauth_signature_method=\"PLAINTEXT\",oauth_nonce=\"2376346\",oauth_timestamp=" + oauthTimestamp);
                  int statusCode = client.executeMethod(method);
                  byte[] responseBody = method.getResponseBody();
                  String reqString = new String(responseBody);

                  if (statusCode == 200)
                  {
                        oauthTokenSecret  = new String(reqString.split("&")[0].split("=")[1]);
                        oauthToken        = new String(reqString.split("&")[1].split("=")[1]);
                        System.out.println(oauthToken + " " + oauthTokenSecret);          
                  }
                  else if(statusCode == 401)
                  {
                        oauthTimestamp = Integer.parseInt((reqString.split(":")[0].split("=")[1]).substring(1, reqString.split(":")[0].split("=")[1].length()-1));
                        oauthTimedelta = Integer.parseInt(reqString.split(":")[1].trim().split(" ")[1]);
                        oauthTimestamp = Math.abs(oauthTimedelta) + oauthTimestamp + 1;
                        System.out.println("oauthTimestamp  " + oauthTimestamp);
                  }
                } 
                catch (HttpException e)
                {
                    System.err.println("Fatal protocol violation: " + e.getMessage());
                    e.printStackTrace();
                } 
                catch (IOException e) 
                {
                    System.err.println("Fatal transport error: " + e.getMessage());
                    e.printStackTrace();
                } 
                finally 
                {
                  // Release the connection.
                  method.releaseConnection();
                }
        }        
    }

    public void requestAccessOAuth(String oauthToken, String oauthTokenSecret)
    {
        HttpClient client = new HttpClient();
        GetMethod method  = new GetMethod(oauthRequestTokenURL);
        int statusCode = 0;
        
        while(statusCode!=200)
        {
                try
                {
                  method.setRequestHeader("Authorization", "OAuth oauth_signature=\"" + consumerSecret + "%26" + oauthTokenSecret + "\", oauth_consumer_key=\"" + consumerKey + "\",oauth_signature_method=\"PLAINTEXT\",oauth_nonce=\"2376347\",oauth_timestamp=" + oauthTimestamp + ",oauth_token=" + oauthToken);
                  System.out.println("OAuth oauth_signature=\"" + consumerSecret + "%26" + oauthTokenSecret + "\", oauth_consumer_key=\"" + consumerKey + "\",oauth_signature_method=\"PLAINTEXT\",oauth_nonce=\"2376347\",oauth_timestamp=" + oauthTimestamp + ",oauth_token=" + oauthToken);
                  statusCode = client.executeMethod(method);
                  byte[] responseBody = method.getResponseBody();
                  String reqString = new String(responseBody);
                  System.out.println("response = " + reqString);

                  if (statusCode == 200)
                  {
                        oauthTokenSecretAccess  = new String(reqString.split("&")[1].split("=")[1]);
                        oauthTokenAccess        = new String(reqString.split("&")[0].split("=")[1]);
                        System.out.println("oauthTokenAccess=" + oauthTokenAccess + "  oauthTokenSecretAccess=" + oauthTokenSecretAccess);
                  }
                  else if(statusCode == 401)
                  {
                        oauthTimestamp = Integer.parseInt((reqString.split(":")[0].split("=")[1]).substring(1, reqString.split(":")[0].split("=")[1].length()-1));
                        oauthTimedelta = Integer.parseInt(reqString.split(":")[1].trim().split(" ")[1]);
                        oauthTimestamp = Math.abs(oauthTimedelta) + oauthTimestamp + 1;
                        System.out.println(" -requestAccessOAuth");
                        
                  }
                } 
                catch (HttpException e)
                {
                    System.err.println("Fatal protocol violation: " + e.getMessage());
                    e.printStackTrace();
                } 
                catch (IOException e) 
                {
                    System.err.println("Fatal transport error: " + e.getMessage());
                    e.printStackTrace();
                } 
                finally 
                {
                  // Release the connection.
                  method.releaseConnection();
                }
        }        
    }
        
    public String retrieveMIMEType(String objectID, String accessToken, String accessTokenSecret)
    throws XmlRpcException
    {
        return (this.retrieveSpecificAttribute(objectID, "type", accessToken, accessTokenSecret)).split(";")[0];
    }    
        
    public String retrieveSIOCfromPlatform(String objectID, String accessToken, String accessTokenSecret)
    throws MalformedURLException, IOException
    {
        /*
        String responseMsg = null;
        String line = null;
        StringBuffer msgsock = new StringBuffer();        
        String base64credentials = null;
        
        URL targetURL = new URL(this.platformURL + "/" + objectID + "?op=sioc.get_sioc");
        URLConnection connection = targetURL.openConnection();
        base64credentials = Base64.encode((username + ":" + password).getBytes("UTF-8"));
        System.out.println(base64credentials.substring(0, base64credentials.length()) );
        connection.setRequestProperty("Authorization", "Basic " + base64credentials.substring(0, base64credentials.length()) ); 
        
        connection.setDoOutput(true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        
        while((line = reader.readLine()) != null)
            msgsock.append(line+"\n");

        responseMsg = msgsock.toString().trim();
        reader.close();
    */
        int statusCode = 0;
        String reqString = null;
        String respString = null;
                
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(this.platformURL + "/" + objectID + "?op=sioc.get_sioc");

        while(statusCode!=200 && statusCode != 500)
        {
            try
            {
              method.setRequestHeader("Content-Type", "text/xml");                
              method.setRequestHeader("User-Agent", "Apache XML RPC 3.1.3 (Jakarta Commons httpclient Transport)");
              method.setRequestHeader("Authorization", "OAuth oauth_nonce=\"1705890416\",oauth_consumer_key=\"Ubitech_Annotator\",oauth_signature_method=\"PLAINTEXT\",oauth_token=\"" + accessTokenSecret + "\",oauth_signature=\"" + consumerSecret + "%26"+ accessToken + "\",oauth_timestamp=" + oauthTimestamp );
              System.out.println("Access Exec = " + "OAuth oauth_nonce=\"1705890416\",oauth_consumer_key=\"Ubitech_Annotator\",oauth_signature_method=\"PLAINTEXT\",oauth_token=\"" + accessTokenSecret + "\",oauth_signature=\"" + consumerSecret + "%26"+ accessToken + "\",oauth_timestamp=" + oauthTimestamp );
             
              statusCode = client.executeMethod(method);
              byte[] responseBody = method.getResponseBody();
              respString = new String(responseBody);
              System.out.println(statusCode);
              System.out.println(respString);              
              if (statusCode == 200)
              {
              }
              else if(statusCode == 401)
              {
                    System.out.println("respString=" + respString);
                    oauthTimestamp = Integer.parseInt((reqString.split(":")[0].split("=")[1]).substring(1, reqString.split(":")[0].split("=")[1].length()-1));
                    oauthTimedelta = Integer.parseInt(reqString.split(":")[1].trim().split(" ")[1]);
                    oauthTimestamp = Math.abs(oauthTimedelta) + oauthTimestamp + 1;
              }
            }
            catch (HttpException e)
            {
                System.err.println("Fatal protocol violation: " + e.getMessage());
                e.printStackTrace();
            } 
        
            catch (IOException e)
            {
                System.err.println("Fatal transport error: " + e.getMessage());
                e.printStackTrace();
            } 
            finally
            {
                method.releaseConnection();
            }
        }        
                
        return respString;
    }
/*    
    public String retrieveFileCategorization(String objectID)
    throws XmlRpcException
    {
        return this.retrieveSpecificAttribute(objectID, "cat");        
    }
  */   
    public String retrieveSpecificAttribute(String objectID, String attributeName, String accessToken, String accessTokenSecret)
    throws XmlRpcException
    {
        int statusCode = 0;
        String reqString = null;
        String respString = null;
                
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(oauthExecTokenURL);

        while(statusCode!=200 && statusCode != 500)
        {
            try
            {
              method.setRequestHeader("Content-Type", "text/xml");                
              method.setRequestHeader("User-Agent", "Apache XML RPC 3.1.3 (Jakarta Commons httpclient Transport)");
              method.setRequestHeader("Authorization", "OAuth oauth_nonce=\"1705890416\",oauth_consumer_key=\"Ubitech_Annotator\",oauth_signature_method=\"PLAINTEXT\",oauth_token=\"" + accessTokenSecret + "\",oauth_signature=\"" + consumerSecret + "%26"+ accessToken + "\",oauth_timestamp=" + oauthTimestamp );
              System.out.println("Access Exec = " + "OAuth oauth_nonce=\"1705890416\",oauth_consumer_key=\"Ubitech_Annotator\",oauth_signature_method=\"PLAINTEXT\",oauth_token=\"" + accessTokenSecret + "\",oauth_signature=\"" + consumerSecret + "%26"+ accessToken + "\",oauth_timestamp=" + oauthTimestamp );

              reqString = new String("<?xml version='1.0' encoding='UTF-8'?>\n<methodCall>\n<methodName>get_attributes</methodName>\n"
                      + "<params>\n"
                      + "<param>\n<value>" + objectID + "</value>\n</param>\n"
                      + "<param>\n<value>\n<array>\n<data>\n<value>" + attributeName + "</value>\n</data>\n</array>\n</value></param>\n"
                      + "</params>\n" 
                      + "</methodCall>");
              
              method.setRequestEntity(new StringRequestEntity(reqString,"text/xml","UTF-8"));
              statusCode = client.executeMethod(method);
              byte[] responseBody = method.getResponseBody();
              respString = new String(responseBody);

              if (statusCode == 200)
              {
                    System.out.println("respString=" + respString);
              }
              else if(statusCode == 401)
              {
                    System.out.println("respString=" + respString);
                    oauthTimestamp = Integer.parseInt((reqString.split(":")[0].split("=")[1]).substring(1, reqString.split(":")[0].split("=")[1].length()-1));
                    oauthTimedelta = Integer.parseInt(reqString.split(":")[1].trim().split(" ")[1]);
                    oauthTimestamp = Math.abs(oauthTimedelta) + oauthTimestamp + 1;
              }
            }
            catch (HttpException e)
            {
                System.err.println("Fatal protocol violation: " + e.getMessage());
                e.printStackTrace();
            } 
        
            catch (IOException e)
            {
                System.err.println("Fatal transport error: " + e.getMessage());
                e.printStackTrace();
            } 
            finally
            {
                method.releaseConnection();
            }
        }
                
        return new String(getValueFromXMLResponse(respString, 0));   
    }

    public String uploadFileFromURL(String objectID, String name, String url, String accessToken, String accessTokenSecret)
    throws XmlRpcException
    {
        int statusCode = 0;
        String reqString = null;
        String respString = null;
                
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(oauthExecTokenURL);

        while(statusCode!=200 && statusCode != 500)
        {
            try
            {
              method.setRequestHeader("Content-Type", "text/xml");                
              method.setRequestHeader("User-Agent", "Apache XML RPC 3.1.3 (Jakarta Commons httpclient Transport)");
              method.setRequestHeader("Authorization", "OAuth oauth_nonce=\"1705890416\",oauth_consumer_key=\"Ubitech_Annotator\",oauth_signature_method=\"PLAINTEXT\",oauth_token=\"" + accessTokenSecret + "\",oauth_signature=\"" + consumerSecret + "%26"+ accessToken + "\",oauth_timestamp=" + oauthTimestamp );
              System.out.println("Access Exec = " + "OAuth oauth_nonce=\"1705890416\",oauth_consumer_key=\"Ubitech_Annotator\",oauth_signature_method=\"PLAINTEXT\",oauth_token=\"" + accessTokenSecret + "\",oauth_signature=\"" + consumerSecret + "%26"+ accessToken + "\",oauth_timestamp=" + oauthTimestamp );

              reqString = new String("<?xml version='1.0' encoding='UTF-8'?>\n<methodCall>\n<methodName>uploadurl</methodName>\n"
                      + "<params>\n"
                      + "<param>\n<value>" + objectID + "</value>\n</param>\n"
                      + "<param>\n<value>" + name + "</value>\n</param>\n"
                      + "<param>\n<value>" + url + "</value>\n</param>\n"
                      + "</params>\n" 
                      + "</methodCall>");
              
              method.setRequestEntity(new StringRequestEntity(reqString,"text/xml","UTF-8"));
              statusCode = client.executeMethod(method);
              byte[] responseBody = method.getResponseBody();
              respString = new String(responseBody);

              if (statusCode == 200)
              {
                    System.out.println("respString=" + respString);
              }
              else if(statusCode == 401)
              {
                    System.out.println("respString=" + respString);
                    oauthTimestamp = Integer.parseInt((reqString.split(":")[0].split("=")[1]).substring(1, reqString.split(":")[0].split("=")[1].length()-1));
                    oauthTimedelta = Integer.parseInt(reqString.split(":")[1].trim().split(" ")[1]);
                    oauthTimestamp = Math.abs(oauthTimedelta) + oauthTimestamp + 1;
              }
            }
            catch (HttpException e)
            {
                System.err.println("Fatal protocol violation: " + e.getMessage());
                e.printStackTrace();
            } 
        
            catch (IOException e)
            {
                System.err.println("Fatal transport error: " + e.getMessage());
                e.printStackTrace();
            } 
            finally
            {
                method.releaseConnection();
            }
        }
                
        return new String(getValueFromXMLResponse(respString, 0));
        
    }
/*    
    public String uploadFIleFromURL(String objectID, String name, String url)
    throws XmlRpcException
    {
        params = new Object[]{new String(objectID), new String(name), new String(url)};
        Object newObjectID = (Object) client.execute("uploadurl", params);
        
        return (String)newObjectID;
    }
  */      
    public String storeAnnotation(String objectID, String subject, String body, String tags, boolean replyTo, String ldbs, String accessToken, String accessTokenSecret)
    throws XmlRpcException            
    {
        int statusCode = 0;
        String reqString = null;
        String respString = null;
                
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(oauthExecTokenURL);

        while(statusCode!=200 && statusCode != 500)
        {
            try
            {
              method.setRequestHeader("Content-Type", "text/xml");                
              method.setRequestHeader("User-Agent", "Apache XML RPC 3.1.3 (Jakarta Commons httpclient Transport)");
              method.setRequestHeader("Authorization", "OAuth oauth_nonce=\"1705890416\",oauth_consumer_key=\"Ubitech_Annotator\",oauth_signature_method=\"PLAINTEXT\",oauth_token=\"" + accessTokenSecret + "\",oauth_signature=\"" + consumerSecret + "%26"+ accessToken + "\",oauth_timestamp=" + oauthTimestamp );
              System.out.println("Access Exec = " + "OAuth oauth_nonce=\"1705890416\",oauth_consumer_key=\"Ubitech_Annotator\",oauth_signature_method=\"PLAINTEXT\",oauth_token=\"" + accessTokenSecret + "\",oauth_signature=\"" + consumerSecret + "%26"+ accessToken + "\",oauth_timestamp=" + oauthTimestamp );

              reqString = new String("<?xml version='1.0' encoding='UTF-8'?>\n<methodCall>\n<methodName>addannotation</methodName>\n"
                      + "<params>\n"
                      + "<param>\n<value>" + objectID + "</value>\n</param>\n"
                      + "<param>\n<value>" + subject + "</value>\n</param>\n"
                      + "<param>\n<value>" + body + "</value>\n</param>\n"
                      + "<param>\n<value>" + ldbs + "</value>\n</param>\n"
                      + "<param>\n<value><boolean>" + ((replyTo==true) ? 1 : 0) + "</boolean></value>\n</param>\n"
                      + "<param>\n<value>" + tags + "</value>\n</param>\n"
                      + "</params>\n" 
                      + "</methodCall>");
              
              method.setRequestEntity(new StringRequestEntity(reqString,"text/xml","UTF-8"));
              statusCode = client.executeMethod(method);
              byte[] responseBody = method.getResponseBody();
              respString = new String(responseBody);

              if (statusCode == 200)
              {
                    System.out.println("respString=" + respString);
              }
              else if(statusCode == 401)
              {
                    System.out.println("respString=" + respString);
                    oauthTimestamp = Integer.parseInt((reqString.split(":")[0].split("=")[1]).substring(1, reqString.split(":")[0].split("=")[1].length()-1));
                    oauthTimedelta = Integer.parseInt(reqString.split(":")[1].trim().split(" ")[1]);
                    oauthTimestamp = Math.abs(oauthTimedelta) + oauthTimestamp + 1;
              }
            }
            catch (HttpException e)
            {
                System.err.println("Fatal protocol violation: " + e.getMessage());
                e.printStackTrace();
            } 
        
            catch (IOException e)
            {
                System.err.println("Fatal transport error: " + e.getMessage());
                e.printStackTrace();
            } 
            finally
            {
                method.releaseConnection();
            }
        }
                
        return new String(getValueFromXMLResponse(respString, 0));
    }

    public String getHomeDirectory(String accessToken, String accessTokenSecret)
    throws XmlRpcException            
    {
        int statusCode = 0;
        String reqString = null;
        String respString = null;
                
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(oauthExecTokenURL);

        while(statusCode!=200 && statusCode != 500)
        {
            try
            {
              method.setRequestHeader("Content-Type", "text/xml");                
              method.setRequestHeader("User-Agent", "Apache XML RPC 3.1.3 (Jakarta Commons httpclient Transport)");
              method.setRequestHeader("Authorization", "OAuth oauth_nonce=\"1705890416\",oauth_consumer_key=\"Ubitech_Annotator\",oauth_signature_method=\"PLAINTEXT\",oauth_token=\"" + accessTokenSecret + "\",oauth_signature=\"" + consumerSecret + "%26"+ accessToken + "\",oauth_timestamp=" + oauthTimestamp );
              System.out.println("Access Exec = " + "OAuth oauth_nonce=\"1705890416\",oauth_consumer_key=\"Ubitech_Annotator\",oauth_signature_method=\"PLAINTEXT\",oauth_token=\"" + accessTokenSecret + "\",oauth_signature=\"" + consumerSecret + "%26"+ accessToken + "\",oauth_timestamp=" + oauthTimestamp );

              reqString = new String("<?xml version='1.0' encoding='UTF-8'?>\n<methodCall>\n<methodName>get_attributes</methodName>\n"
                      + "<params/>\n" 
                      + "</methodCall>");
              
              method.setRequestEntity(new StringRequestEntity(reqString,"text/xml","UTF-8"));
              statusCode = client.executeMethod(method);
              byte[] responseBody = method.getResponseBody();
              respString = new String(responseBody);

              if (statusCode == 200)
              {
                    System.out.println("respString=" + respString);
              }
              else if(statusCode == 401)
              {
                    System.out.println("respString=" + respString);
                    oauthTimestamp = Integer.parseInt((reqString.split(":")[0].split("=")[1]).substring(1, reqString.split(":")[0].split("=")[1].length()-1));
                    oauthTimedelta = Integer.parseInt(reqString.split(":")[1].trim().split(" ")[1]);
                    oauthTimestamp = Math.abs(oauthTimedelta) + oauthTimestamp + 1;
              }
            }
            catch (HttpException e)
            {
                System.err.println("Fatal protocol violation: " + e.getMessage());
                e.printStackTrace();
            } 
        
            catch (IOException e)
            {
                System.err.println("Fatal transport error: " + e.getMessage());
                e.printStackTrace();
            } 
            finally
            {
                method.releaseConnection();
            }
        }
                
        return new String(getValueFromXMLResponse(respString, 0));
    }
    
    
    public String getValueFromXMLResponse(String xmlData, int index)
    {
        SAXParserFactory factory;
        SAXParser saxParser;
        XMLRPCResponseHandler handler = null;
        factory = SAXParserFactory.newInstance();
        
        try
        {
            saxParser = factory.newSAXParser();
            handler = new XMLRPCResponseHandler();
            saxParser.parse(new InputSource(new StringReader(xmlData)), handler);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }     
        
        return handler.getValues().get(index);
    }
    
    public String editAnnotation(String objectID, String subject, String body, String tags, String lbds, String accessToken, String accessTokenSecret)
    throws XmlRpcException            
    {       
        int statusCode = 0;
        String reqString = null;
        String respString = null;
                
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(oauthExecTokenURL);

        while(statusCode!=200 && statusCode != 500)
        {
            try
            {
              method.setRequestHeader("Content-Type", "text/xml");                
              method.setRequestHeader("User-Agent", "Apache XML RPC 3.1.3 (Jakarta Commons httpclient Transport)");
              method.setRequestHeader("Authorization", "OAuth oauth_nonce=\"1705890416\",oauth_consumer_key=\"Ubitech_Annotator\",oauth_signature_method=\"PLAINTEXT\",oauth_token=\"" + accessTokenSecret + "\",oauth_signature=\"" + consumerSecret + "%26"+ accessToken + "\",oauth_timestamp=" + oauthTimestamp );
              System.out.println("Access Exec = " + "OAuth oauth_nonce=\"1705890416\",oauth_consumer_key=\"Ubitech_Annotator\",oauth_signature_method=\"PLAINTEXT\",oauth_token=\"" + accessTokenSecret + "\",oauth_signature=\"" + consumerSecret + "%26"+ accessToken + "\",oauth_timestamp=" + oauthTimestamp );

              reqString = new String("<?xml version='1.0' encoding='UTF-8'?>\n<methodCall>\n<methodName>editannotation</methodName>\n"
                      + "<params>\n"
                      + "<param>\n<value>" + objectID + "</value>\n</param>\n"
                      + "<param>\n<value>" + subject + "</value>\n</param>\n"
                      + "<param>\n<value>" + body + "</value>\n</param>\n"
                      + "<param>\n<value>" + lbds + "</value>\n</param>\n"
                      + "<param>\n<value>" + tags + "</value>\n</param>\n"
                      + "</params>\n" 
                      + "</methodCall>");
              
              method.setRequestEntity(new StringRequestEntity(reqString,"text/xml","UTF-8"));
              statusCode = client.executeMethod(method);
              byte[] responseBody = method.getResponseBody();
              respString = new String(responseBody);

              if (statusCode == 200)
              {
                    System.out.println("respString=" + respString);
              }
              else if(statusCode == 401)
              {
                    System.out.println("respString=" + respString);
                    oauthTimestamp = Integer.parseInt((reqString.split(":")[0].split("=")[1]).substring(1, reqString.split(":")[0].split("=")[1].length()-1));
                    oauthTimedelta = Integer.parseInt(reqString.split(":")[1].trim().split(" ")[1]);
                    oauthTimestamp = Math.abs(oauthTimedelta) + oauthTimestamp + 1;
              }
            }
            catch (HttpException e)
            {
                System.err.println("Fatal protocol violation: " + e.getMessage());
                e.printStackTrace();
            } 
        
            catch (IOException e)
            {
                System.err.println("Fatal transport error: " + e.getMessage());
                e.printStackTrace();
            } 
            finally
            {
                method.releaseConnection();
            }
        }
                
        return new String(getValueFromXMLResponse(respString, 0));
        
    }    
    
    public HashMap retrieveListAttributes(int objectID, Object[] paramList)
    throws XmlRpcException
    {
        params = new Object[]{new String(""+objectID), paramList};
        Object[] objArray = (Object[]) client.execute("get_attributes", params);
        return (HashMap) objArray[0];
    }    
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public static void main(String[] args)
    {
        try {
            CollaborativePlatformInterface c = new CollaborativePlatformInterface();
            System.out.println(c.retrieveSIOCfromPlatform("13638", "4c3015550d243d5a6bfa2545b473d570", "fa33d7934013ecb9155712e7d0d29456%3a5386"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(CollaborativePlatformInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CollaborativePlatformInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
