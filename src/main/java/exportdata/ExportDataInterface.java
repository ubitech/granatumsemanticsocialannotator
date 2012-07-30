package exportdata;

public abstract class ExportDataInterface
{
    protected String username;
    protected String resourceURL;
    protected String topicURLs;
    protected String content;
    protected String containerURL;
    protected String elementType; // whether it is a post, article etc


    public ExportDataInterface(String username, String resourceURL, String topicURLs, String content, String containerURL, String elementType) {
        this.username = username;
        this.resourceURL = resourceURL;
        this.topicURLs = topicURLs;
        this.content = content;
        this.containerURL = containerURL;
        this.elementType = elementType;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }
    
    
    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }
    
    public String getContainerURL() {
        return containerURL;
    }

    public void setContainerURL(String containerURL) {
        this.containerURL = containerURL;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopicURLs() {
        return topicURLs;
    }

    public void setTopicURLs(String topicURLs) {
        this.topicURLs = topicURLs;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }        
    
    public abstract String exportSIOCDataSPARQL();
    
    public abstract String exportSIOCDataXML();    
}
