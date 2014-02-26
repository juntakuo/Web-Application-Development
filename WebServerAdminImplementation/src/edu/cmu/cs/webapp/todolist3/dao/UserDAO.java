/*
 * Student Name: Tsu-Hao Kuo
 * Andrew ID: tkuo
 * Date: Feb. 17, 2013
 * Course Number: 08764/15637
 */

package edu.cmu.cs.webapp.todolist3.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.cs.webapp.todolist3.databean.User;

public class UserDAO {
	private List<Connection> connectionPool = new ArrayList<Connection>();

	private String jdbcDriver;
	private String jdbcURL;
	private String tableName;
	
	public UserDAO(String jdbcDriver, String jdbcURL, String tableName) throws MyDAOException {
		this.jdbcDriver = jdbcDriver;
		this.jdbcURL    = jdbcURL;
		this.tableName  = tableName;
		
		if (!tableExists()) createTable();
	}
	
	private synchronized Connection getConnection() throws MyDAOException {
		if (connectionPool.size() > 0) {
			return connectionPool.remove(connectionPool.size()-1);
		}
		
        try {
            Class.forName(jdbcDriver);
        } catch (ClassNotFoundException e) {
            throw new MyDAOException(e);
        }

        try {
            return DriverManager.getConnection(jdbcURL);
        } catch (SQLException e) {
            throw new MyDAOException(e);
        }
	}
	
	private synchronized void releaseConnection(Connection con) {
		connectionPool.add(con);
	}


	public User create(User user) throws MyDAOException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
        try {
        	con = getConnection();
        	
        	// check user's existence
        	stmt = con.createStatement();
        	rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE userEmail=\'" + user.getUserEmail() + "\'");
        	if (rs.next()) {
        		user = null;
        		return user;
        	}
        	
        	rs.close();
        	stmt.close();
        	
        	PreparedStatement pstmt = con.prepareStatement("INSERT INTO " + tableName + " (userName,userEmail,userFirstName,userLastName,password) VALUES (?,?,?,?,?)");
        	
        	pstmt.setString(1,user.getUserName());
        	pstmt.setString(2,user.getUserEmail());
        	pstmt.setString(3,user.getUserFirstName());
        	pstmt.setString(4,user.getUserLastName());
        	pstmt.setString(5,user.getPassword());
        	
        	stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            user.setUserID(rs.getInt("LAST_INSERT_ID()"));
      	        	
        	int count = pstmt.executeUpdate();
        	if (count != 1) throw new SQLException("Insert updated "+count+" rows");
        	
        	pstmt.close();
        	stmt.close();
        	releaseConnection(con);
        	return user;
        	
        } catch (Exception e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
        }
	}

	public User read(String userEmail) throws MyDAOException {
		Connection con = null;
        try {
        	con = getConnection();

        	PreparedStatement pstmt = con.prepareStatement("SELECT * FROM " + tableName + " WHERE userEmail=?");
        	pstmt.setString(1,userEmail);
        	ResultSet rs = pstmt.executeQuery();
        	
        	User user;
        	if (!rs.next()) {
        		user = null;
        	} else {
        		user = new User();
        		user.setUserName(rs.getString("userName"));
        		user.setPassword(rs.getString("password"));
        		user.setUserID(rs.getInt("userID"));                       
        	    user.setUserEmail(rs.getString("userEmail"));
        	    user.setUserFirstName(rs.getString("userFirstName"));
        	    user.setUserLastName(rs.getString("userLastName"));
        	    
        	}
        	
        	rs.close();
        	pstmt.close();
        	releaseConnection(con);
            return user;
            
        } catch (Exception e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
        }
	}

	private boolean tableExists() throws MyDAOException {
		Connection con = null;
        try {
        	con = getConnection();
        	DatabaseMetaData metaData = con.getMetaData();
        	ResultSet rs = metaData.getTables(null, null, tableName, null);
        	
        	boolean answer = rs.next();
        	
        	rs.close();
        	releaseConnection(con);
        	
        	return answer;

        } catch (SQLException e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
        }
    }

	private void createTable() throws MyDAOException {
		Connection con = null;
        try {
        	con = getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE " + tableName +
            		" (userID INT NOT NULL AUTO_INCREMENT," +
            		" userName VARCHAR(255) NOT NULL," +
            		" userEmail VARCHAR(255)," +
            		" userFirstName VARCHAR(255)," +
            		" userLastName VARCHAR(255), " +
            		" password VARCHAR(255)," +
            		" PRIMARY KEY(userID))");
            		//"CREATE TABLE " + tableName + " (" + "id INT NOT NULL AUTO_INCREMENT, userName VARCHAR(255) NOT NULL, userEmail VARCHAR(255),userFirstName VARCHAR(255),userLastName VARCHAR(255),password VARCHAR(255), PRIMARY KEY(userID))");
             		//"userName VARCHAR(255) NOT NULL, password VARCHAR(255), PRIMARY KEY(userName))");
            stmt.close();
        	
        	releaseConnection(con);

        } catch (SQLException e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
        }
    }
}
