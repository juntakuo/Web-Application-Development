/*
 * Student Name: Tsu-Hao Kuo
 * Andrew ID: tkuo
 * Date: Feb. 17, 2013
 * Course Number: 08764/15637
 */

package edu.cmu.cs.webapp.todolist3.formbean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class LoginForm  {
	
	// variables for login
	private String userEmail_login;
	private String password_login;
	
	// button for login
    private String button;
	
    public LoginForm(HttpServletRequest request) {
    	
    	// get variables for login 
    	userEmail_login = request.getParameter("userEmail_login");
    	password_login = request.getParameter("password_login");
    	
    	
    	// get button value
    	button   = request.getParameter("button");
    }
    
    // get variables for login
    public String getEmail_login()     { return userEmail_login;}
    public String getPassword_login()  { return password_login; }
    
    
    // get button value
    public String getButton()    { return button;   }
    
    public boolean isPresent()   { return button != null; }

    
    public List<String> getLoginValidationErrors() {
        List<String> errors = new ArrayList<String>();

        if (userEmail_login == null || userEmail_login.length() == 0) errors.add("Email address is required for login.");
        else if (!userEmail_login.matches("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$")) 
    		errors.add("Invalid email address");
        if (password_login == null || password_login.length() == 0) errors.add("Password is required");
        
        
        if (button == null) errors.add("Button is required");

        if (errors.size() > 0) return errors;

        if (!button.equals("Login") && !button.equals("Register")) errors.add("Invalid button");
        if (!userEmail_login.matches("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$")) 
    		errors.add("Invalid email address");
		
        return errors;
    }
}
