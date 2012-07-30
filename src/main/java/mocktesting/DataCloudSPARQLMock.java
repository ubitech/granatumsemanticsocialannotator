package mocktesting;

import importdata.CategoryMappings;
import java.util.HashMap;
import java.util.LinkedList;


public class DataCloudSPARQLMock
{
    private HashMap<String,HashMap> concepts;
    private HashMap<String,HashMap> clists;
    public static DataCloudSPARQLMock mock = null;
    
    public static DataCloudSPARQLMock getMock()
    {
        if(mock==null)
            mock = new DataCloudSPARQLMock();
        
        return mock;
    }    
    
    public DataCloudSPARQLMock()
    {
        concepts = new HashMap<String,HashMap>();
        clists   = new HashMap<String,HashMap>();
        
        HashMap<String,String> llA = new HashMap<String, String>();
        llA.put("Actin", "http://deri.ie.testtest.ir/Actin");
        llA.put("Coronin", "http://deri.ie.testtest.ir/Coronin");
        llA.put("Keratin", "http://deri.ie.testtest.ir/Keratin");
        llA.put("Collagen", "http://deri.ie.testtest.ir/Collagen");
        llA.put("Elastin", "http://deri.ie.testtest.ir/Elastin");
        llA.put("Protein S", "http://deri.ie.testtest.ir/ProteinS");
        llA.put("Protein C", "http://deri.ie.testtest.ir/ProteinC");
        llA.put("Protein Z", "http://deri.ie.testtest.ir/ProteinZ");
        llA.put("Integrin", "http://deri.ie.testtest.ir/Integrin");
        llA.put("Ependymin", "http://deri.ie.testtest.ir/Ependymin");        
        clists.put("Protein Entities", llA);
        
        HashMap<String,String> llB = new HashMap<String, String>();
        llB.put("Induction", "http://deri.ie.testtest.ir/Induction");
        llB.put("Incubation", "http://deri.ie.testtest.ir/incubation");
        llB.put("Replication Process", "http://deri.ie.testtest.ir/replication");
        llB.put("Base Pair", "http://deri.ie.testtest.ir/basepair");
        llB.put("SuperCoiling", "http://deri.ie.testtest.ir/supercoiling");
        llB.put("Damage", "http://deri.ie.testtest.ir/damage");
        clists.put("DNA Entities", llB);

        HashMap<String,String> llC = new HashMap<String, String>();
        llC.put("Cell cycle", "http://deri.ie.testtest.ir/cellcycle");
        llC.put("Cell apoptosis", "http://deri.ie.testtest.ir/cellapoptosis");
        llC.put("Cell damage", "http://deri.ie.testtest.ir/celldamage");
        llC.put("Necrosis", "http://deri.ie.testtest.ir/necrosis");
        llC.put("Stenosis", "http://deri.ie.testtest.ir/stenosis");
        llC.put("Anakribeia", "http://deri.ie.testtest.ir/anakribeia");
        clists.put("Cell Entities", llC);        
        
        concepts.put("default_Experimental Data", clists);
        concepts.put("text/html_Experimental Data", clists);
        concepts.put("image/jpeg_Experimental Data", clists);
        concepts.put("image/tiff_Experimental Data", clists);
        
    }
    
    public HashMap getHashMap(String key)
    {
        System.out.println(key);
        return concepts.get(key);
    }
    
    public static void main(String[] args)
    {
        DataCloudSPARQLMock mock = DataCloudSPARQLMock.getMock();
        System.out.println(mock.getHashMap("default_Experimental Data"));
    }
}
