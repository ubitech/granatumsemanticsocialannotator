package importdata;

import java.util.Collection;

public abstract class DataCloudInterface
{
    public String endpointURL = null;
    protected String concept;

    public DataCloudInterface(String concept) {
        this.concept = concept;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }
    
    public abstract Collection getAssociatedEntities() throws Throwable;
}
