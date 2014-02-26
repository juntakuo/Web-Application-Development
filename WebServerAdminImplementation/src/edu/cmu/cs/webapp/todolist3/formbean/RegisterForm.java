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

public class RegisterForm  {
	
	// variables for register
    private String userName;
    private String userEmail_register;
    private String userFirstName;
    private String userLastName;
    private String password_register;
    private String confirm_password;
    
    private String button;
	
    public RegisterForm(HttpServletRequest request) {
    	
    	   	
    	// get variables for register
    	userName = request.getParameter("userName");
    	userEmail_register = request.getParameter("userEmail_register");
    	userFirstName = request.getParameter("userFirstName");
    	userLastName = request.getParameter("userLastName");
    	password_register = request.getParameter("password_register");
    	confirm_password = request.getParameter("confirm_password");
    	
    	// get button value
    	button   = request.getParameter("button_register");
    }
    
    // get variables for register
    public String getUserName()      { return userName; }
    public String getUserFirstName() { return userFirstName; }
    public String getUserLastName()  { return userLastName; }
    public String getEmail_register()     { return userEmail_register;}
    public String getPassword_register()  { return password_register; }
    
    // get button value
    public String getButton()    { return button;   }
    
    public boolean isPresent()   { return button != null; }

    public List<String> getRegisterValidationErrors() {
        List<String> errors = new ArrayList<String>();
        
        
        if (userName == null || userName.length() == 0) errors.add("User Name is required!");
        if (userEmail_register == null || userEmail_register.length() == 0) errors.add("Email address is required!");
        else if (!userEmail_register.matches("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$")) 
    		errors.add("Invalid email address");
                
        if (userFirstName == null || userFirstName.length() == 0) errors.add("User First Name is required!");
        if (userLastName == null || userLastName.length() == 0) errors.add("User Last Name is required!");
        if (password_register == null || password_register.length() == 0) 
        	errors.add("Password is required!");
        if (confirm_password == null || confirm_password.length() == 0) errors.add("Please confirm the password!");
        if (!password_register.equals(confirm_password)) errors.add("The passwords are not consistent!!");
        
        if (button == null) errors.add("Button is required");
        
        if (errors.size() > 0) return errors;
        
        if (userName.matches(".*[<>\"].*")) errors.add("User Name may not contain angle brackets or quotes");
        if (userFirstName.matches(".*[<>\"].*")) errors.add("User First Name may not contain angle brackets or quotes");
        if (userLastName.matches(".*[<>\"].*")) errors.add("User Last Name may not contain angle brackets or quotes");
        
        if (!button.equals("Register")) errors.add("Invalid button");
        
        return errors;
        
    }
    
}
