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

import posra.Address;
import posra.Employee;
import posra.dataaccess.DB;

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
    Employee emp = createEmployee();
 // Get Gson object
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String fileData = new String(Files.readAllBytes(Paths.get("Employee.txt")));
 // parse json string to object
    Employee emp1 = gson.fromJson(fileData, Employee.class);
    String jsonFromFile = gson.toJson(emp1);
    String jsonEmp = gson.toJson(emp);
    out.println("And here is a sample json serialized employee java object:\n\n " + jsonEmp);
    out.println("\n\nAnd here is a sample json deserialized employee java object from employee.txt in eclipse root dir:\n\n " + jsonFromFile);
  }
  
  protected Employee createEmployee() {
	  
      Employee emp = new Employee();
      emp.setId(100);
      emp.setName("David");
      emp.setPermanent(false);
      emp.setPhoneNumbers(new long[] { 123456, 987654 });
      emp.setRole("Manager");

      Address add = new Address();
      add.setCity("Bangalore");
      add.setStreet("BTM 1st Stage");
      add.setZipcode(560100);
      emp.setAddress(add);

      List<String> cities = new ArrayList<String>();
      cities.add("Los Angeles");
      cities.add("New York");
      emp.setCities(cities);

      Map<String, String> props = new HashMap<String, String>();
      props.put("salary", "1000 Rs");
      props.put("age", "28 years");
      emp.setProperties(props);

      return emp;
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