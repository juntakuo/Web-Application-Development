/* Student Name: Tsu-Hao KUo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */

package databeans;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.genericdao.PrimaryKey;

@PrimaryKey("id")
public class Photo implements Comparable<Photo> {
	public static final List<String> EXTENSIONS = Collections.unmodifiableList(Arrays.asList( new String[] {
			".jpg", ".gif", ".JPG"
	} ));

	private int    id          = -1;
	
	private byte[] bytes       = null;
	private String caption     = null;
	private String contentType = null;
	private String owner       = null;
	private String ownerEmail  = null;
	private int    position    = 0;
	private String description = null;
	private String prices      = null;
	private String listingDate = null;
	
	public int compareTo(Photo other) {
		// Order first by owner, then by position
		if (owner == null && other.owner != null) return -1;
		if (owner != null && other.owner == null) return 1;
		int c = owner.compareTo(other.owner);
		if (c != 0) return c;
		return position - other.position;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Photo) {
			Photo other = (Photo) obj;
			return compareTo(other) == 0;
		}
		return false;
	}
	
    public byte[] getBytes()       { return bytes;       }
    public String getCaption()     { return caption;     }
    public String getContentType() { return contentType; }
    public int    getId()          { return id;          }
    public String getOwner()       { return owner;       }
    public String getOwnerEmail()  { return ownerEmail;  }
    public int    getPosition()    { return position;    }
    public String getDescription() { return description; }
    public String getPrices()      { return prices;      }
    public String getListingDate() { return listingDate; }
    
    public void setBytes(byte[] a)        { bytes = a;        }
    public void setCaption(String s)      { caption = s;      }
    public void setContentType(String s)  { contentType = s;  }
    public void setId(int x)              { id = x;           }
    public void setOwner(String s)        { owner = s; }
    public void setOwnerEmail(String s)   { ownerEmail = s; }
    public void setPosition(int p)        { position = p;     }
    public void setDescription(String s)  { description = s;  }
    public void setPrices(String s)       { prices = s;       }
    public void setListingDate(String s)  { listingDate = s;  }
    
    public String toString() {
    	return "Photo("+id+")";
    }
}
