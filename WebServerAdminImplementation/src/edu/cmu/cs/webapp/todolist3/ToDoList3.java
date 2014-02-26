/*
 * Student Name: Tsu-Hao Kuo
 * Andrew ID: tkuo
 * Date: Feb. 17, 2013
 * Course Number: 08764/15637
 */



package edu.cmu.cs.webapp.todolist3;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.text.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.cmu.cs.webapp.todolist3.dao.ItemDAO;
import edu.cmu.cs.webapp.todolist3.dao.MyDAOException;
import edu.cmu.cs.webapp.todolist3.dao.UserDAO;
import edu.cmu.cs.webapp.todolist3.databean.ItemBean;
import edu.cmu.cs.webapp.todolist3.databean.User;
import edu.cmu.cs.webapp.todolist3.formbean.IdForm;
import edu.cmu.cs.webapp.todolist3.formbean.ItemForm;
import edu.cmu.cs.webapp.todolist3.formbean.LoginForm;
import edu.cmu.cs.webapp.todolist3.formbean.RegisterForm;

public class ToDoList3 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private ItemDAO toDoListDAO;
	private UserDAO userDAO;
	//private int state = 1;
	
	public void init() throws ServletException {
		String jdbcDriverName = getInitParameter("jdbcDriver");
		String jdbcURL        = getInitParameter("jdbcURL");
		
		try {
			userDAO     = new UserDAO(jdbcDriverName,jdbcURL,"tkuo_user");
			toDoListDAO = new ItemDAO(jdbcDriverName,jdbcURL,"tkuo_item");
		} catch (MyDAOException e) {
			throw new ServletException(e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
               		
        if (session.getAttribute("user") == null) {
        	if (request.getParameter("button") == null && request.getParameter("button_register") == null) {
            	login(request, response);
            	return;
            } else if (request.getParameter("button") != null) {
            	if (request.getParameter("button").equals("Login")) {
            		login(request, response);
            		return;
            	} else if (request.getParameter("button").equals("Register")) {
            		register(request, response);
            		return;
            	}
            } else if ((request.getParameter("button_register") != null)) {
            	register(request, response);
            	return;
            }
        } else {
        	manageList(request,response);
        }
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request,response);
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		
    	List<String> errors = new ArrayList<String>();
   		
   		LoginForm form = new LoginForm(request);
    	
    	if (!form.isPresent()) {
    		outputLoginPage(response,form,null);
    		return;
    	}
    	
    	errors.addAll(form.getLoginValidationErrors());
   		if (errors.size() != 0) {
   			outputLoginPage(response,form,errors);
   	    	return;
   	    }
    	
   		try {
            User user;
            
       		if (form.getButton().equals("Register")) {
       			register(request, response);
       			return;
       		} else {
       			user = userDAO.read(form.getEmail_login());
		       	if (user == null) {
		       		errors.add("No such user");
		    		outputLoginPage(response,form,errors);
		    		return;
		       	}
		       	
		       	if (!form.getPassword_login().equals(user.getPassword())) {
			       		errors.add("Incorrect password");
			    		outputLoginPage(response,form,errors);
			    		return;
			    }
       		}       				
				
		
       		HttpSession session = request.getSession();
	       	session.setAttribute("user",user);
	       	manageList(request,response);
       	
       	} catch (MyDAOException e) {
       		errors.add(e.getMessage());
       		outputLoginPage(response,form,errors);
       	}
	}

	private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<String> errors = new ArrayList<String>();
   		
   		RegisterForm form = new RegisterForm(request);
   		
   		if (!form.isPresent()) {
    		outputRegisterPage(response,form,null);
    		return;
    	}
   		
   		errors.addAll(form.getRegisterValidationErrors());
   		if (errors.size() != 0) {
   			outputRegisterPage(response,form,errors);
   	    	return;
   	    }
   		
   		
   		
   		try {
            User user;
            if (form.getButton().equals("Register")) {
            	errors.addAll(form.getRegisterValidationErrors());
           		if (errors.size() != 0) {
           			outputRegisterPage(response,form,errors);
           	    	return;
           	    }
            	
           		user = new User();
           		user.setUserName(form.getUserName());
           		user.setUserFirstName(form.getUserFirstName());
           		user.setUserLastName(form.getUserLastName());
           		user.setUserEmail(form.getEmail_register());
           		user.setPassword(form.getPassword_register());
           		
           		user = userDAO.create(user);
           		if (user == null) {
           			errors.add("The E-mail has been registered. Please choose another E-mail address!");
           			outputRegisterPage(response,form,errors);
           			return;
           		}
           		
           		user = userDAO.read(form.getEmail_register());
           		
           		HttpSession session = request.getSession();
    	       	session.setAttribute("user",user);
    	       	manageList(request,response);
            }
            else {
            	outputRegisterPage(response,form,null);
            	return;
            	
            }
         } catch (MyDAOException e) {
       		errors.add(e.getMessage());
       		outputRegisterPage(response,form,errors);
       	}
	
	
	}
    private void manageList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Look at the what parameter to see what we're doing to the list
        String what = request.getParameter("what");
        int userId = ((User) request.getSession().getAttribute("user")).getUserID();
        
        if (what == null) {
        	// No change to list requested
        	outputToDoList(response, userId);
        	return;
        }
        
        if (what.equals("X")) {
        	processDelete(request,response);
        	return;
        }
        
        if (what.equals("Add")) {
        	processAdd(request,response);
        	return;
        }
        
        if (what.equals("Logout")) {
        	response.setHeader("Cache-Control", "nop-cache, no-store");
        	response.setHeader("Pragma", "no-cache");
        	
        	HttpSession session = request.getSession();
        	if (session != null) {
        		session.removeAttribute("user");
        		session.invalidate();
        	}
        	login(request, response);
        	return;
        }
        
        outputToDoList(response,"No such operation: "+what, userId);
	}
    
    private void processAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		List<String> errors = new ArrayList<String>();
   		
   		int userId = ((User)request.getSession().getAttribute("user")).getUserID();
   		
   		ItemForm form = new ItemForm(request);
   		
   		errors.addAll(form.getValidationErrors());
        if (errors.size() > 0) {
        	outputToDoList(response,errors, userId);
        	return;
        }

       	try {
       		ItemBean bean = new ItemBean();
       		bean.setItem(form.getItem());
       		 	
       		bean.setPrice(form.getPrice());
       		bean.setDescription(form.getDescription());
       		bean.setUserID(userId);
       		Date d = new Date();
       		SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
       		bean.setListingDate(ft.format(d).toString());
       		toDoListDAO.create(bean);
        	outputToDoList(response,"Item Added", userId);
        } catch (MyDAOException e) {
        	errors.add(e.getMessage());
	    	outputToDoList(response,errors, userId);
        }
	}
    
    private void processDelete(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
   		List<String> errors = new ArrayList<String>();
   		int userId = ((User) request.getSession().getAttribute("user")).getUserID();
   		IdForm form = new IdForm(request);
   		errors.addAll(form.getValidationErrors());
        if (errors.size() > 0) {
        	outputToDoList(response,errors, userId);
        	return;
        }

   		try {
	    	toDoListDAO.delete(form.getIdAsInt());
	    	outputToDoList(response,"Item Deleted", userId);
	    } catch (MyDAOException e) {
	    	errors.add(e.getMessage());
	    	outputToDoList(response,errors, userId);
	    }  
	}

    // Methods that generate & output HTML
    
    private void generateHead(PrintWriter out) {
	    out.println("  <head>");
	    out.println("    <meta http-equiv=\"cache-control\" content=\"no-cache\">");
	    out.println("    <meta http-equiv=\"pragma\" content=\"no-cache\">");
	    out.println("    <meta http-equiv=\"expires\" content=\"0\">");
	    out.println("    <title>To Do List Login</title>");
	    out.println("  </head>");
    }
    
    private void outputLoginPage(HttpServletResponse response, LoginForm form, List<String> errors) throws IOException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	
	    out.println("<html>");
	
	    generateHead(out);
	
	    out.println("<body style=\"background-color:#66FFFF\">");
	    
	    // Generate an HTML <form> to for user login 
        out.println("<br/><br/><form method=\"POST\">");
        out.println("<center><table/>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">Email address:</td>");
        out.println("            <td>");
        out.println("                <input type=\"text\" name=\"userEmail_login\"");
        if (form != null && form.getEmail_login() != null) {
        	out.println("                    value=\""+form.getEmail_login()+"\"");
        }
        out.println("                />");
        out.println("            <td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">Password:</td>");
        out.println("            <td><input type=\"password\" name=\"password_login\" /></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td colspan=\"2\" align=\"center\">");
        out.println("                <input type=\"submit\" name=\"button\" value=\"Login\" />");
        //out.println("                <input type=\"submit\" name=\"button\" value=\"Register\" />");
        out.println("            </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td colspan=\"2\" align=\"center\">");
        out.println("<h2>Or click the button to register.");
        out.println("                <input type=\"submit\" name=\"button\" value=\"Register\" />");
        out.println("            </td></h2>");
        out.println("        </tr>");
        out.println("    </table></center>");
        out.println("</form>");
        out.println("<hr/>");
        
        if (errors != null && errors.size() > 0) {
	    	for (String error : errors) {
	        	out.println("<center><p style=\"font-size: x-large; color: red\"><b>");
	        	out.println(error);
	        	out.println("</b></p></center>");
	    	}
	    }
	}
    
    private void outputRegisterPage(HttpServletResponse response, RegisterForm form, List<String> errors) throws IOException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	
	    out.println("<html>");
	
	    generateHead(out);
	
	    out.println("<body style=\"background-color:#66FFFF\">");
	    
	    out.println("<form method=\"POST\">");
        out.println("<center><table/>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">User Name:</td>");
        out.println("            <td>");
        out.println("                <input type=\"text\" name=\"userName\"");
        if (form != null && form.getUserName() != null) {
        	out.println("					value=\""+form.getUserName()+"\"");
        }
        out.println("                />");   
        out.println("             </td>");
        out.println("          </tr>");
        out.println("          <tr><td style=\"font-size: x-large\"> E-mail Address:</td>");
        out.println("              <td><input type=\"text\" name=\"userEmail_register\"");
        if (form != null && form.getEmail_register() != null) {
        	out.println("					value=\""+form.getEmail_register()+"\"");
        }
        out.println("/></td></tr>");
        out.println("          <tr><td style=\"font-size: x-large\"> First Name: </td>");
        out.println("              <td><input type=\"text\" name=\"userFirstName\"");
        if (form != null && form.getUserFirstName() != null) {
        	out.println("					value=\""+form.getUserFirstName()+"\"");
        }
        out.println("/></td></tr>");
        out.println("          <tr><td style=\"font-size: x-large\"> Last Name:</td>");
        out.println("              <td><input type=\"text\" name=\"userLastName\"");
        if (form != null && form.getUserLastName() != null) {
        	out.println("					value=\""+form.getUserLastName()+"\"");
        }
        out.println("/></td></tr>");
        out.println("          <tr><td style=\"font-size: x-large\"> Password:</td>");
        out.println("              <td><input type=\"password\" name=\"password_register\" /></td></tr>");
        out.println("          <tr><td style=\"font-size: x-large\"> Confirm Password:</td>");
        out.println("              <td><input type=\"password\" name=\"confirm_password\" /></td></tr>");
        out.println("         <tr><td colspan=\"2\" align=\"center\">");
        out.println("                 <input type=\"submit\" name=\"button_register\" value=\"Register\" />");
        out.println("             </td></tr>");
        
        out.println("</table></center>");
        out.println("</form>");
        
        out.println("</body>");
        out.println("</html>");
        
        if (errors != null && errors.size() > 0) {
	    	for (String error : errors) {
	        	out.println("<center><p style=\"font-size: x-large; color: red\"><b>");
	        	out.println(error);
	        	out.println("</b></p></center>");
	    	}
	    }
	}
    
    
    private void outputToDoList(HttpServletResponse response, int userId) throws IOException {
    	// Just call the version that takes a List passing an empty List
    	List<String> list = new ArrayList<String>();
    	outputToDoList(response,list, userId);
    }
   
    private void outputToDoList(HttpServletResponse response, String message, int userId) throws IOException {
    	// Just put the message into a List and call the version that takes a List
    	List<String> list = new ArrayList<String>();
    	list.add(message);
    	outputToDoList(response,list, userId);
    }
    
    private void outputToDoList(HttpServletResponse response, List<String> messages, int userId) throws IOException {
    	// Get the list of items to display at the end
    	ItemBean[] beans;
        try {
        	beans = toDoListDAO.getUserItems(userId);
        			//.getItems();
        	
        	
        } catch (MyDAOException e) {
        	// If there's an access error, add the message to our list of messages
        	messages.add(e.getMessage());
        	beans = new ItemBean[0];
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");

        generateHead(out);

        out.println("<body style=\"background-color:#66FFFF\">");
        out.println("<h2> Item List </h2>");

        // Generate an HTML <form> to get data from the user
        out.println("<form method=\"POST\">");
        out.println("    <table>");
        out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: large\">Item to Add:</td>");
        out.println("            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"item\"/></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: large\">Price:</td>");
        out.println("            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"price\"/></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: large\">Description:</td>");
        out.println("            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"description\"/></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td/>");
        out.println("            <td align=\"center\"><input type=\"submit\" name=\"what\" value=\"Add\"/></td>");
        out.println("            <td aligh=\"center\"><input type=\"submit\" name=\"what\" value=\"Logout\"></td>");
        out.println("        </tr>");
        out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
        out.println("    </table>");
        out.println("</form>");

        
        for (String message : messages) {
        	out.println("<p style=\"font-size: large; color: red\">");
        	out.println(message);
        	out.println("</p>");
        }
 
        out.println("<p style=\"font-size: x-large\">The list now has "+beans.length+" items.</p>");
        out.println("<table>");
        for (int i=0; i<beans.length; i++) {
        	out.println("    <tr>");
        	out.println("        <td>");
        	out.println("            <form method=\"POST\">");
        	out.println("                <input type=\"hidden\" name=\"ItemID\" value=\""+beans[i].getItemID()+"\" />");
        	out.println("                <input type=\"submit\" name=\"what\" value=\"X\" />");
        	out.println("            </form>");
        	out.println("        </td>");
			out.println("        <td><span style=\"font-size: x-large\">" + (i + 1) + ".</span></td>");
			out.println("        <td><span style=\"font-size: x-large\">" +
									beans[i].getItem() + "</span> (ItemId = " +
									beans[i].getItemID() + ", userID = " +
									beans[i].getUserID() + ", Listing Date = " +
									beans[i].getListingDate() + ", Pirce = " +
									beans[i].getPrice() + ", Description = "+
									beans[i].getDescription()+ ")</td>");
        	out.println("    </tr>");
        }
        out.println("</table>");

        out.println("</body>");
        out.println("</html>");
    }
}
