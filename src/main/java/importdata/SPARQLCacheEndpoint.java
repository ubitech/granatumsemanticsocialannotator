package importdata;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

// Singleton class
// Cache for all requests, from all users
public class SPARQLCacheEndpoint
{
    private HashMap<String,Collection> cache;
    public static SPARQLCacheEndpoint c = null;
    private long expireDate;
    private final int expireMillieSeconds = 1000 * 60 * 60; // an hour
    
    public static SPARQLCacheEndpoint getCacheEndpoint()
    {
        if(c==null)
            c = new SPARQLCacheEndpoint();
        
        return c;
    }
    
    public SPARQLCacheEndpoint()
    {
        cache = new HashMap<String,Collection>();
        expireDate = new Date().getTime() + expireMillieSeconds;
    }
    
    public synchronized Collection getCached(String key)
    {
        return cache.get(key);
    }
    
    public synchronized void cache(String key, Collection value)
    {
        long currDate = new Date().getTime();
        System.out.println("exp="+expireDate);
        System.out.println("cur="+currDate);

        if(currDate>=expireDate)
        {
            cache = null;
            cache = new HashMap<String, Collection>();
        }
        
        cache.put(key, value);
    }
    
    public static void main(String[] args)
    {
        LinkedList<String> ll = new LinkedList<String>();
        ll.add("val1");
        SPARQLCacheEndpoint s = SPARQLCacheEndpoint.getCacheEndpoint();
        s.cache("test1", ll);
        s.cache("test2", ll);        
        System.out.println(s.getCached("test1"));        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SPARQLCacheEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        s.cache("test3",ll);
        System.out.println("-" + s.getCached("test1"));
    }
}
