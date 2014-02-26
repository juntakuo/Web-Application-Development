/*
 * Student Name: Tsu-Hao Kuo
 * Andrew ID: tkuo
 * Date: Feb. 17, 2013
 * Course Number: 08764/15637
 */

package edu.cmu.cs.webapp.todolist3.dao;

public class MyDAOException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public MyDAOException(Exception e) { super(e); }
	public MyDAOException(String s)    { super(s); }
}
