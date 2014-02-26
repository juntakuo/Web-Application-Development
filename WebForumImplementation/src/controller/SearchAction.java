/* Student Name: Tsu-Hao KUo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */

package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.*;
import formbeans.*;
import model.*;

public class SearchAction extends Action {
	private FormBeanFactory<SearchForm> formBeanFactory = FormBeanFactory.getInstance(SearchForm.class);
	
	private PhotoDAO photoDAO;
	private UserDAO  userDAO;
	
    public SearchAction(Model model) {
		photoDAO = model.getPhotoDAO();
		userDAO = model.getUserDAO();
	}

    public String getName() { return "search.do"; }

    public String perform(HttpServletRequest request) {
    	try {
        	int numEntries = photoDAO.getCount();
			request.setAttribute("numEntries",numEntries);
			
			SearchForm searchForm = formBeanFactory.create(request);
			request.setAttribute("searchForm",searchForm);
			
			// first time direct to "search" page
	    	if (!searchForm.isPresent()) {
	    		// list all items
	    		User[] users = userDAO.getUsers();
	    		ArrayList<Photo> photoList = new ArrayList<Photo>();
	    		for (int i = 0 ; i < users.length; ++i) {
	    			Photo[] photos = photoDAO.getPhotos(users[i].getUserName());
	    			for (int j = 0 ; j < photos.length; ++j) {
	    				photoList.add(photos[j]);
	    			}
	    			
	    		}
	    		
	    		request.setAttribute("photoList", photoList);
	    		return "search.jsp";
	    	}

        	List<String> errors = searchForm.getValidationErrors();
			request.setAttribute("errors",errors);
	    	if (errors.size() > 0) return "search.jsp";

	    	
	    	searchForm.setKeyword(request.getParameter("keywords"));
	        String[] tokens = searchForm.getKeywords().split("[ .,?!]");
	        ArrayList<Photo> PhotoList = new ArrayList<Photo>();
	        for (int i = 0 ; i < tokens.length; ++i) {
	        	Photo[] list = photoDAO.lookupStartsWith(tokens[i]);
	        	for (int j = 0; j < list.length; ++j) {
	        		PhotoList.add(list[j]);
	        	}
	    	}
	        
	    	
	    	if (PhotoList.size() == 0) {
				errors.add("No matches for your search");
				return "search.jsp";
			}
			
			if (PhotoList.size() >= 1) {
				request.setAttribute("photoList",PhotoList);
				return "search.jsp";
			}
			
			return "search.jsp";
			
        } catch (RollbackException e) {
        	e.printStackTrace();
        	//request.setAttribute("dbError",e.toString());
        	return "error.jsp";
		} catch (FormBeanException e) {
        	e.printStackTrace();
        	//request.setAttribute("formError",e.getMessage());
        	return "error.jsp";
		}
    }
}
