package collaborativeplatform;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.xerces.impl.dv.util.Base64;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class CollaborativePlatformInterface1
{
    private final String oauthRequestTokenURL = "http://www.granatum.org/bscw/bscw.cgi?op=OAuth";
    private final String oauthAuthTokenURL    = "http://www.granatum.org/bscw/bscw.cgi?op=OAuth";
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

    
    public CollaborativePlatformInterface1()
    {
        this.password = null;
        this.username = null;
    }    
    
    public CollaborativePlatformInterface1(String password, String username)
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
        
    public String retrieveMIMEType(String objectID)
    throws XmlRpcException
    {
        return (this.retrieveSpecificAttribute(objectID, "type")).split(";")[0];
    }    
    
    public String retrieveSIOCfromPlatform(String objectID)
    throws MalformedURLException, IOException
    {
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
        
        return responseMsg;
    }
    
    public String retrieveFileCategorization(String objectID)
    throws XmlRpcException
    {
        return this.retrieveSpecificAttribute(objectID, "cat");        
    }
     
    
    public String retrieveSpecificAttribute(String objectID, String attributeName)
    throws XmlRpcException
    {
        params = new Object[]{new String(""+objectID), new Object[]{new String(attributeName)}};
        Object[] objArray = (Object[]) client.execute("get_attributes", params);
        HashMap objectDescDictionary = (HashMap) objArray[0];
        
        return (String)objectDescDictionary.get(attributeName);
    }

    public String uploadFIleFromURL(String objectID, String name, String url)
    throws XmlRpcException
    {
        params = new Object[]{new String(objectID), new String(name), new String(url)};
        Object newObjectID = (Object) client.execute("uploadurl", params);
        
        return (String)newObjectID;
    }
        
    public String storeAnnotation(String objectID, String subject, String body, String tags, boolean replyTo, String ldbs)
    throws XmlRpcException            
    {       
        System.out.println(objectID +" "+ subject + " " + body + " " + tags);
        params = new Object[]{objectID, subject, body, ldbs, new Boolean(replyTo), tags};
        Object[] obj = (Object[])client.execute("addannotation", params); 
        
        return (String)obj[0];
    }

    public String editAnnotation(String objectID, String subject, String body, String tags, String lbds)
    throws XmlRpcException            
    {       
        params = new Object[]{objectID, subject, body, lbds, tags};
        Object[] obj = (Object[])client.execute("editannotation", params); 
        
        return (String)obj[0];
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
}
