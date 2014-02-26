/* Student Name: Tsu-Hao Kuo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */

package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class LoginForm extends FormBean {
	private String userName;
	private String password;
	private String searchBar;
	
	public String getUserName()  { return userName; }
	public String getPassword()  { return password; }
	public String getSearchBar() { return searchBar;}
	
	public void setUserName(String s) { userName = trimAndConvert(s,"<>\"");  }
	public void setPassword(String s) {	password = s.trim();                  }
	public void setSearchBar(String s){ searchBar = s.trim();                 }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (userName == null || userName.trim().length() == 0) {
			errors.add("User Name is required");
		}
		
		if (password == null || password.trim().length() == 0) {
			errors.add("Password is required");
		}
		
		return errors;
	}
}