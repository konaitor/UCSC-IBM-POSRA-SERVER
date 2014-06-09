package posra.servlets;

import java.io.*;
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
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import posra.dataaccess.Polymer;
import posra.dataaccess.PolymerHome;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class FileCounter
 */

@MultipartConfig
public class PolymerServlet extends HttpServlet {

  //private final static String OSRA_LIB = "libosra_java";
  static {
    System.setProperty("line.separator","\n"); // so we won't have to mess with DOS line endings ever again
    // load the shared osra library.
    // NOTE that this is currently going to fail on 32bit cygwin using a 64bit VM under 64bit Eclipse!!
    // workarounds: - use a 32bit VM under 32bit Eclipse
    //              - try to build posra (and all its prereqs) under 64bit cygwin - some likelihood to fail with current immature state of 64bit cygwin  
    //try {  
    //  System.loadLibrary(OSRA_LIB);
    //  System.out.println("done loading '" + OSRA_LIB + "'!");
    //} catch (Error e) {
    //  System.err.println("Error loading native library '" + OSRA_LIB + "'; java.library.path=" + System.getProperty("java.library.path"));
    //  throw e; // re-throw
    //}
  }

  private static final long serialVersionUID = 1L;
       
  int count;
  private String filePath, osraPath;
  private File imagePath; 

  public void init() throws ServletException {
    filePath = getServletContext().getInitParameter("image_location");
    osraPath = getServletContext().getInitParameter("osra_location");
    imagePath = new File(filePath);
    
    if (!new File(osraPath).exists()) {
      System.err.println("POSRA binary '" + osraPath + "' not found!"); System.exit(1);
    }
  }

  protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    out.print("<!DOCTYPE html>\n<html lang=\"en\">");
    out.print("<head>\n\t<meta charset=\"utf-8\"/>\n\t<title>P-OSRA</title>\n</head>");
    out.print("<body>");

    if (!imagePath.exists()) {
      if (!imagePath.mkdir()) {
        throw new IOException("\"images\" directory could not be made.");
      }
    }

    // reading through request
    for (Part part : request.getParts()) {

          String name = part.getName();

          InputStream is = request.getPart(name).getInputStream();

          //origFilePath is the path on the client of the file being uploaded
          String origFilePath = getUploadedFileName(part);

          //the "fileName" will be the local path (filePath) and just the file name of the uploaded file.
    	  File newFile = new File(origFilePath);
    	  String fileName = filePath + newFile.getName();
          
    	  //There is io here, a try/catch is needed in case there is a problem writing the file
    	  try{
        	  FileOutputStream fos = new FileOutputStream(fileName);
	          int data = 0;
	
	          while((data = is.read()) != -1) {
	
	          	fos.write(data);
	
	          }
	
	          fos.close();
	          is.close();

			  out.print("<h5>File Uploaded, me thinks</h5>");
			  out.print("<h5>New File's Path:</h5>");
			  out.print("<h5>" + fileName + "</h5>");
			  
			  // Add POSRA CALL
			  // "fileName" is fullPath to File On Server
			  // "newFile" is the Java File. 
			  //
        Process process = new ProcessBuilder(osraPath, "-r", "150", fileName).start();
        try (BufferedReader pbr = new BufferedReader(new InputStreamReader(process.getInputStream()))) { // stdout from process
          String line;
          while ((line = pbr.readLine()) != null) {
            System.out.println(line); break; // first line is SMILES
          }
          String SMILES = line;
          out.print("<h5>SMILES is " + SMILES + "</h5>");
          // ... process SMILES ...
        }
              
			}catch(IOException e){
				out.print("<h5>There was an error</h5>");
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
  
  // This is an unused method for now, but could be used for testing.
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Set a cookie for the user, so that the counter does not increate
    // every time the user presses refresh
    HttpSession session = request.getSession(true);
    // Set the session valid for 5 secs
    session.setMaxInactiveInterval(5);
    response.setContentType("text/plain");
    PrintWriter out = response.getWriter();
    
    // Sample json
    
 // Get Gson object
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    Polymer poly = createPolymer();
    poly.setName("Polystyrene");
    
    PolymerHome p = new PolymerHome();
    p.persist(poly);
   
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
  }

} 