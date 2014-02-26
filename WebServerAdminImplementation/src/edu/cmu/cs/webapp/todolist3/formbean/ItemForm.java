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

public class ItemForm {
	private String item;
	private String price;
	private String description;
	
	public ItemForm(HttpServletRequest request) {
		item = sanitize(request.getParameter("item"));
		price = sanitize(request.getParameter("price"));
		description = sanitize(request.getParameter("description"));
	}
	
	public String getItem()           { return item; }
	public String getPrice()          { return price; }
	public String getDescription()    { return description; }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (item == null || item.length() == 0) { errors.add("Item name is required"); }
		if (price == null || price.length() == 0) { 
			errors.add("Price is required"); 
		} else {
			// check whether price is numeric
			try {  
			    double d = Double.parseDouble(price);  
			  }  
			  catch(NumberFormatException nfe) {  
			    errors.add("Price is not numeric!!!"); 
			  }  
		} 
			
		if (description == null || description.length() == 0) { errors.add("Description cannot be blank!"); }

		return errors;
	}
	
	private String sanitize(String s) {
    	return s.replace("&", "&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;");
	}
}
