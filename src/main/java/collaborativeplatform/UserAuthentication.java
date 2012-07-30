package collaborativeplatform;

import java.util.HashMap;

public class UserAuthentication
{
    private HashMap<String,String> acceptedAccounts;
    private HashMap<String,String> acceptedPasswords;

    public UserAuthentication()
    {
        this.acceptedPasswords = new HashMap<String, String>();
        this.acceptedAccounts  = new HashMap<String, String>();
        
        this.acceptedAccounts.put("2953", "panhas");
        this.acceptedAccounts.put("5019", "dummyuserA");
        this.acceptedAccounts.put("5563", "SteveMiller");
        this.acceptedAccounts.put("5386", "MaryJones");
        this.acceptedAccounts.put("5339", "JohnSmith");        
/*
        this.acceptedAccounts.put("5671", "SteveMiller");
        this.acceptedAccounts.put("5611", "MaryJones");
        this.acceptedAccounts.put("5731", "JohnSmith");
 */       
        this.acceptedPasswords.put("panhas",  "stakaman");
        this.acceptedPasswords.put("dummyuserA", "dummyuserA");
        this.acceptedPasswords.put("SteveMiller", "#stevemiller#");
        this.acceptedPasswords.put("MaryJones", "#maryjones#");
        this.acceptedPasswords.put("JohnSmith", "#johnsmith#");        
    }   
    
    public String getUsername(String userID)
    {
        return this.acceptedAccounts.get(userID);
    }

    public String getPassword(String userID)
    {
        return this.acceptedPasswords.get(this.acceptedAccounts.get(userID));
    }    
    
    public boolean authenticateUser(String username, String password)
    {
        String pass = this.acceptedPasswords.get(username);
        if(pass==null)
            return false;
        else
            return(pass.equals(password));
    }
    
    public static void main(String args[])
    {
        UserAuthentication ua = new UserAuthentication();
        System.out.println(ua.authenticateUser("dummyuserA", "dummyuserA"));
    }
}
