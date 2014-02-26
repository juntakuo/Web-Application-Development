/* Student Name: Tsu-Hao KUo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */

package controller;

import java.awt.image.BufferedImage;
import java.io.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.genericdao.RollbackException;

import model.Model;
import databeans.User;
import databeans.Photo;

@SuppressWarnings("serial")
public class Controller extends HttpServlet {
	
	private User[] prepopulateUsers = new User[3];
	private Photo[] prepopulatePhotos = new Photo[9];
	private String[] FirstNameList = {"Barack", "Geroge", "Benjamin"};
	private String[] LastNameList = {"Obama", "Washington", "Franklin"};
	private String[] UserNameList = {"president44", "president1", "president6"};
	private String[] UserEmailList = {"bobama@whitehouse.org", "gw@usa.us", "bf@philadelphia.pa"};
	private String[] PasswordList = {"YesWeCan", "ididit", "lightning"};
	private String[] pricesList = {"121", "345", "4345", "456", "1230", "976", "3456.67", "8463", "2364"};
	private String[] imageList = {"bible.jpg", "ship.jpg", "ak47.jpg", "axe.jpg", "cherry.jpg", "pan.jpg", "kite.jpg","money.jpg", "independence.jpg"};
	private String[] descriptionList = {"bible", "ship", "ak47", "axe", "cherry", "pan", "kite", "money","Declaration of independence" };
	private Model model;
	
    public void init() throws ServletException {
        model = new Model(getServletConfig());
        
        // check is there any user in the table
        try {
			if (model.getUserDAO().getCount() == 0) {
				prepopulate();
				for (int i = 0 ; i < 3; i++) {
					model.getUserDAO().create(prepopulateUsers[i]);
				}
				for (int i = 0 ; i < 9; ++i) {
					model.getPhotoDAO().create(prepopulatePhotos[i]);
				}
				
				
			}
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Action.add(new ChangePwdAction(model));
        Action.add(new ImageAction(model));
        Action.add(new ListAction(model));
        Action.add(new LoginAction(model));
        Action.add(new LogoutAction(model));
        Action.add(new ManageAction(model));
        Action.add(new RegisterAction(model));
        Action.add(new RemoveAction(model));
        Action.add(new UploadAction(model));
        Action.add(new ViewAction(model));
        Action.add(new SearchAction(model));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nextPage = performTheAction(request);
        sendToNextPage(nextPage,request,response);
    }
    
    /*
     * Extracts the requested action and (depending on whether the user is logged in)
     * perform it (or make the user login).
     * @param request
     * @return the next page (the view)
     */
    private String performTheAction(HttpServletRequest request) {
        HttpSession session     = request.getSession(true);
        String      servletPath = request.getServletPath();
        User        user = (User) session.getAttribute("user");
        String      action = getActionName(servletPath);

        // System.out.println("servletPath="+servletPath+" requestURI="+request.getRequestURI()+"  user="+user);

        if (action.equals("register.do") || action.equals("login.do") || action.equals("search.do") || action.equals("view.do") || action.equals("image.do")) {
        	// Allow these actions without logging in
			return Action.perform(action,request);
        }
        
        if (user == null) {
        	// If the user hasn't logged in, direct him to the login page
        	return Action.perform("search.do",request);
        }

      	// Let the logged in user run his chosen action
		return Action.perform(action,request);
    }

    /*
     * If nextPage is null, send back 404
     * If nextPage ends with ".do", redirect to this page.
     * If nextPage ends with ".jsp", dispatch (forward) to the page (the view)
     *    This is the common case
     */
    private void sendToNextPage(String nextPage, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	if (nextPage == null) {
    		response.sendError(HttpServletResponse.SC_NOT_FOUND,request.getServletPath());
    		return;
    	}
    	
    	if (nextPage.endsWith(".do")) {
			response.sendRedirect(nextPage);
			return;
    	}
    	
    	if (nextPage.endsWith(".jsp")) {
	   		RequestDispatcher d = request.getRequestDispatcher("WEB-INF/" + nextPage);
	   		d.forward(request,response);
	   		return;
    	}
    	
    	if (nextPage.equals("image")) {
	   		RequestDispatcher d = request.getRequestDispatcher(nextPage);
	   		d.forward(request,response);
	   		return;
    	}
    	
    	throw new ServletException(Controller.class.getName()+".sendToNextPage(\"" + nextPage + "\"): invalid extension.");
    }

	/*
	 * Returns the path component after the last slash removing any "extension"
	 * if present.
	 */
    private String getActionName(String path) {
    	// We're guaranteed that the path will start with a slash
        int slash = path.lastIndexOf('/');
        return path.substring(slash+1);
    }
    
    private void prepopulate() {
    	for (int i = 0 ; i < 3; ++i) {
    		
    		// user Info
    		User user = new User();
    		user.setFirstName(FirstNameList[i]);
    		user.setLastName(LastNameList[i]);
    		user.setUserEmail(UserEmailList[i]);
    		user.setPassword(PasswordList[i]);
    		user.setUserName(UserNameList[i]);
    		prepopulateUsers[i] = user;
    		
    		// user's item
    		for (int j = 0 ; j < 3; ++j) {
    			try{
    				InputStream logoStream = getServletContext().getResourceAsStream("WEB-INF/images/" + imageList[3*i+j]);
    				if(logoStream != null) {
    					
    					BufferedImage image = ImageIO.read(logoStream);
		    	
    					ByteArrayOutputStream baos = new ByteArrayOutputStream();
    					ImageIO.write(image, "jpg", baos);
    					baos.flush();
    					byte[] imageInByte = baos.toByteArray();
    					baos.close();
		    
    					Photo photo = new Photo();
    					photo.setBytes(imageInByte);
    					photo.setContentType("image/jpg");
    					photo.setDescription(descriptionList[3*i+j]);
    					photo.setPrices(pricesList[3*i+j]);
    					photo.setOwner(UserNameList[i]);
    					photo.setOwnerEmail(UserEmailList[i]);
    					
    					// set listing date
    					Date d = new Date();
    					SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
    					photo.setListingDate(ft.format(d).toString());
    					prepopulatePhotos[3*i+j] = photo;
		    		}
    			} catch (IOException e) {
    				System.out.println(e.getMessage());
    			}		
    		}
    	}
    }
}


