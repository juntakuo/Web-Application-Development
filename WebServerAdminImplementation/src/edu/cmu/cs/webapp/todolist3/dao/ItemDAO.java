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
import java.util.*;

import edu.cmu.cs.webapp.todolist3.databean.ItemBean;

public class ItemDAO {
	private List<Connection> connectionPool = new ArrayList<Connection>();

	private String jdbcDriver;
	private String jdbcURL;
	private String tableName;
	
	public ItemDAO(String jdbcDriver, String jdbcURL, String tableName) throws MyDAOException {
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

		
	public void create(ItemBean item) throws MyDAOException {
		Connection con = null;
    	try {
        	con = getConnection();
        	con.setAutoCommit(false);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " FOR UPDATE");
            rs.next();
            
            rs.close();
            stmt.close();
            
            PreparedStatement pstmt = con.prepareStatement(
            		"INSERT INTO " + tableName + " (item,userID,description, price, listingDate) VALUES (?,?,?,?,?)");
            pstmt.setString(1, item.getItem());
            pstmt.setInt(2, item.getUserID());
            pstmt.setString(3, item.getDescription());
            pstmt.setString(4, item.getPrice());
            pstmt.setString(5, item.getListingDate());
            pstmt.executeUpdate();
            pstmt.close();
            
            // set Item ID
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            item.setItemId(rs.getInt("LAST_INSERT_ID()"));
            rs.close();
            stmt.close();
            
            con.commit();
            con.setAutoCommit(true);
            
            releaseConnection(con);
            
    	} catch (SQLException e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
		}
	}
	
	public void delete(int id) throws MyDAOException {
		Connection con = null;
    	try {
        	con = getConnection();

            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM " + tableName + " WHERE ItemID= " + id);
            stmt.close();
            releaseConnection(con);
    	} catch (SQLException e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
		}
	}

	public ItemBean[] getUserItems(int id) throws MyDAOException {
		Connection con = null;
    	try {
        	con = getConnection();

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " where userID = " + id);
            
            List<ItemBean> list = new ArrayList<ItemBean>();
            while (rs.next()) {
            	ItemBean bean = new ItemBean();
            	bean.setItemId(rs.getInt("ItemID"));
            	bean.setItem(rs.getString("item"));
            	bean.setListingDate(rs.getString("listingDate"));
            	bean.setDescription(rs.getString("description"));
            	bean.setPrice(rs.getString("price"));
            	bean.setUserID(rs.getInt("userID"));
            	list.add(bean);
            }
            stmt.close();
            releaseConnection(con);
            
            return list.toArray(new ItemBean[list.size()]);
    	} catch (SQLException e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
		}
	}
	
	public int size() throws MyDAOException {
		Connection con = null;
    	try {
        	con = getConnection();

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id) FROM " + tableName);
            
            rs.next();
            int count = rs.getInt("COUNT(id)");

            stmt.close();
            releaseConnection(con);
            
            return count;

    	} catch (SQLException e) {
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
    	Connection con = getConnection();
    	try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(
            		"CREATE TABLE " + tableName + 
            		" (ItemID INT AUTO_INCREMENT," +
            		"userID INT, " + 
            		"item VARCHAR(255)," +
            		"listingDate VARCHAR(255)," +
            		"description VARCHAR(255)," +
            		"price VARCHAR(255)," +
            		"PRIMARY KEY(itemID))");
            stmt.close();
            releaseConnection(con);
        } catch (SQLException e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
        }
    }
}
