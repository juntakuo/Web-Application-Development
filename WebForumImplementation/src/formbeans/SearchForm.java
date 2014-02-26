/* Student Name: Tsu-Hao Kuo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */

package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class SearchForm extends FormBean {
	private String keywords;
	private String button;
	
	public String getKeywords()  { return keywords;  }
    public String getButton()    { return button;    }
    
	public void setKeyword(String s)  { keywords  = trimAndConvert(s, "<>\""); }
	public void setButton(String s)   { button = trimAndConvert(s, "<>\"");}
	public boolean isPresent()   { return button != null; }
	
	/*(public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
			
		
		return errors;
	}*/

}

