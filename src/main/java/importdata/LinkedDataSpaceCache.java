/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importdata;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

/**
 *
 * @author user
 */
@WebServlet(name = "LinkedDataSpaceCache", urlPatterns = {"/LinkedDataSpaceCache"})
public class LinkedDataSpaceCache extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String operation = null;
        
        try
        {
            operation = request.getParameter("op");
            
            if(operation!=null)
            {
                if(operation.equals("makeSuggestions"))
                    this.makeSuggestions(request, response);
                else if(operation.equals("annotate"))
                    this.annotate(request, response);                
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

    private void annotate(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        System.out.println(request.getParameterMap());
        System.out.println(request.getParameter("selections"));
    }
    
    private void makeSuggestions(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        PrintWriter out = null;
        String term = null;
        
        out = response.getWriter();
        term = request.getParameter("term");
        response.setContentType("application/json;charset=UTF-8");        
        
        JSONObject json = new JSONObject();        
        LinkedList coll = new LinkedList();        
        HashMap<String, String> keyValPair = new HashMap<String, String>();
        keyValPair.put("name", "panos");
        json.put("name", "panos");
        keyValPair.put("name", "testing");
        json.put("name", "testing");        
     
        out.println(request.getParameter("callback") + "([" + json.toString() + "," + json.toString() + "])");
        out.flush();        
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    private void forwardToPage(String url, HttpServletRequest req, HttpServletResponse resp)
    throws IOException, ServletException
    {
        RequestDispatcher dis;
        
        dis = getServletContext().getRequestDispatcher(url);
        dis.forward(req, resp);
        
        return;
    }      
    
    public static void main(String[] args)
    {
        JSONObject json = new JSONObject();        
        LinkedList coll = new LinkedList();        
        HashMap<String, String> keyValPair = new HashMap<String, String>();
        keyValPair.put("username", "panos");
        json.put("username", "panos");
        coll.add(keyValPair);        
        keyValPair.put("username", "testing");
        coll.add(keyValPair);
        json.put("username", "testing");        

        json.put("people", coll);
        
        System.out.println(json.toString());        
    }
}
