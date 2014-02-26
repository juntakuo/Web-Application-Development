/*
 * Student Name: Tsu-Hao Kuo
 * Andrew ID: tkuo
 * Date: Feb. 17, 2013
 * Course Number: 08764/15637
 */

package edu.cmu.cs.webapp.todolist3.databean;

public class ItemBean {
	private int    ItemID;
	private int    userID;
	private String item;
	private String listingDate;
	private String description;
	private String price;
		
	public int    getItemID()            { return ItemID;       }
	public int    getUserID()            { return userID;       }
	public String getItem()				 { return item;         }
    public String getListingDate()       { return listingDate;  }
    public String getDescription()       { return description;  }
    public String getPrice()             { return price;        }
    
    public void   setItemId(int i)          { ItemID = i;          }
	public void   setUserID(int i)          { userID = i;          }
	public void   setItem(String s)         { item = s;            }
	public void   setListingDate(String s)  { listingDate = s; ; }//System.out.println("string is " + s); listingDate = s; System.out.println("listingDate is " + listingDate);}
	public void   setDescription(String s)  { description = s;     }
	public void   setPrice(String s)        { price = s;}
	
}
