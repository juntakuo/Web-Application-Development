/* Student Name: Tsu-Hao KUo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */

package formbeans;

import java.util.ArrayList;

import org.mybeans.form.FileProperty;
import org.mybeans.form.FormBean;

public class UploadPhotoForm extends FormBean {
	private String button     = "";
	private String caption    = "";
	private String prices     = "";
	private String description = "";
		
	private FileProperty file = null;
	
	public static int FILE_MAX_LENGTH = 1024 * 1024;
	
	// 'get' functions
	public String       getButton()         { return button;         }
	public FileProperty getFile()           { return file;           }
	public String       getCaption()        { return caption;        }
	public String       getPrices()         { return prices;         }
	public String       getDescription()    { return description;    }
		
	// 'set' functions
	public void setButton(String s)         { button      = s;        }
	public void setCaption(String s)        { caption     = trimAndConvert(s,"<>\""); }
	public void setFile(FileProperty file)  { this.file   = file;     }
	public void setPrices(String s)         { prices      = trimAndConvert(s, "<>\""); }
	public void setDescription(String s)    { description = trimAndConvert(s, "<>\""); }
	

	
	public ArrayList<String> getValidationErrors() {
		ArrayList<String> errors = new ArrayList<String>();
		
		if (prices == null || prices.trim().length() == 0) { 
			errors.add("Price is required!!"); 
		} else {
			// check whether price is numeric
			try {  
			    double d = Double.parseDouble(prices);  
			  }  
			  catch(NumberFormatException nfe) {  
			    errors.add("Price is not numeric!!!"); 
			  }  
		}
		
		if (description == null || description.trim().length() == 0) errors.add("The description is required!!");
		
		if (errors.size() != 0) return errors;
		
		if (file == null || file.getFileName().length() == 0) { 
			errors.add("You must provide a file"); 
			return errors;
		}
		
		

		if (file.getBytes().length == 0) {
			errors.add("Zero length file");
		}
		
		return errors;
	}
}
