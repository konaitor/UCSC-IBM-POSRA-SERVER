package posra.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import posra.dataaccess.DB;
import posra.dataaccess.Polymer;
import posra.dataaccess.PolymerHome;

/**
 * Servlet implementation class FileCounter
 */

public class PolymerServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
       
  int count;
  private DB dao;

  public void init() throws ServletException {
    dao = new DB();
    try {
      count = dao.getCount();
    } catch (Exception e) {
      getServletContext().log("An exception occurred in FileCounter", e);
      throw new ServletException("An exception occurred in FileCounter"
          + e.getMessage());
    }
  }
  
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	  
	  
	  
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Set a cookie for the user, so that the counter does not increate
    // every time the user press refresh
    HttpSession session = request.getSession(true);
    // Set the session valid for 5 secs
    session.setMaxInactiveInterval(5);
    response.setContentType("text/plain");
    PrintWriter out = response.getWriter();
   
    if (session.isNew()) {
      count++;
    }
    
    out.println("This site has been accessed " + count + " times.");
    
    // Sample json
    
 // Get Gson object
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
   
    Polymer poly = createPolymer();
    String jsonPolymer = gson.toJson(poly, Polymer.class);
    out.println("And here is a sample json serialized polymer java object:\n\n " + jsonPolymer);

  }
  
  protected Polymer createPolymer() {
	  
	  Polymer p = new Polymer();
	  p.setName("test");
	  //PolymerHome ph = new PolymerHome();
	  
      return p;
  }
  
  public void destroy() {
    super.destroy();
    try {
      dao.save(count);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

} 