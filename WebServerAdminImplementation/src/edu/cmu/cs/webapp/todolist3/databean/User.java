/*
 * Student Name: Tsu-Hao Kuo
 * Andrew ID: tkuo
 * Date: Feb. 17, 2013
 * Course Number: 08764/15637
 */


package edu.cmu.cs.webapp.todolist3.databean;

public class User {
	private int userID;
	private String userName;
	private String userEmail;
	private String userFirstName;
	private String userLastName;
	private String password;

	public int    getUserID()          { return userID;        }
    public String getUserName()        { return userName;      }
    public String getUserEmail()       { return userEmail;     }
    public String getUserFirstName()   { return userFirstName; }
    public String getUserLastName()    { return userLastName;  }
    public String getPassword()        { return password;      }

    public void setUserID(int Id)           { userID = Id;          }                      
    public void setUserName(String s)       { userName = s;         }
    public void setUserEmail(String s)      { userEmail = s;        }
    public void setUserFirstName(String s)  { userFirstName = s;    }
    public void setUserLastName(String s)   { userLastName = s;     }
    public void setPassword(String s)       { password = s;         }
}
