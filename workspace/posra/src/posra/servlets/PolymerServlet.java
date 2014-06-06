package posra.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import posra.dataaccess.DB;
import posra.dataaccess.Polymer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class FileCounter
 */

@MultipartConfig
public class PolymerServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
       
  int count;
  private DB dao;
  private boolean isMultipart;
  private String filePath;
  private int maxFileSize = 20 * 1024;
  private int maxMemSize = 4 * 1024;
  private File file ;

  public void init() throws ServletException {
    dao = new DB();
    try {
      count = dao.getCount();
    } catch (Exception e) {
      getServletContext().log("An exception occurred in FileCounter", e);
      throw new ServletException("An exception occurred in FileCounter"
          + e.getMessage());
    }
    
    filePath = getServletContext().getInitParameter("image_location");
  }
  
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	 PrintWriter out = response.getWriter();     
	 out.print("<!DOCTYPE html>\n<html lang=\"en\">");
	 out.print("<head>\n\t<meta charset=\"utf-8\"/>\n\t<title>P-OSRA</title>\n</head>"); 
	 out.print("<body>");
	  for(Part part : request.getParts()) {

          String name = part.getName();

          InputStream is = request.getPart(name).getInputStream();

          String origFilePath = getUploadedFileName(part);

    	  File newFile = new File(origFilePath);
    	  String fileName = filePath + newFile.getName();
          try{
        	  File fileDir = new File(filePath);
        	  File file = File.createTempFile("posra", ".tmp", fileDir);
        	  FileOutputStream fos = new FileOutputStream(fileName);
	          int data = 0;
	
	          while((data = is.read()) != -1) {
	
	          	fos.write(data);
	
	          }
	
	          fos.close();
	          is.close();

			  out.print("<h5>File Uploaded, me thinks</h5><br />");
			  out.print("<h5>New File's Path:</h5><br />");
			  out.print("<h5>" + fileName + "</h5><br />");
			}catch(Exception e){
				out.print("<h5>There was an error</h5><br />");
				out.print("Debug: <br />Uploaded File Name: " + origFilePath + "<br />");
				out.print("Destination Directory: " + filePath + "<br />");
			}
		  }  
  }	
  private String getUploadedFileName(Part p) {

      String file = "", header = "Content-Disposition";

      String[] strArray = p.getHeader(header).split(";");

      for(String split : strArray) {

          if(split.trim().startsWith("filename")) {

              file = split.substring(split.indexOf('=') + 1);

              file = file.trim().replace("\"", "");
          }

      }

      return file;

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