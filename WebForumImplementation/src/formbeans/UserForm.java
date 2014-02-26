/* Student Name: Tsu-Hao Kuo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */

package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class UserForm extends FormBean {
	private String userName = "";
	
	public String getUserName()  { return userName; }
	
	public void setUserName(String s)  { userName = trimAndConvert(s,"<>>\"]"); }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (userName == null || userName.trim().length() == 0) {
			errors.add("User Name is required");
		}
		
		return errors;
	}
}
