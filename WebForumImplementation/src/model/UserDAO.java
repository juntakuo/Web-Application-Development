/* Student Name: Tsu-Hao KUo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */

package model;

import java.util.Arrays;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databeans.Photo;
import databeans.User;


public class UserDAO extends GenericDAO<User> {
	
	public UserDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(User.class, tableName, pool);
	}

	public User[] getUsers() throws RollbackException {
		User[] users = match();
		Arrays.sort(users);  // We want them sorted by last and first names (as per User.compareTo());
		return users;
	}
	
	public boolean checkUsersExistence(User newUser) throws RollbackException {
		boolean flag = false;
		try {
			Transaction.begin();
			User[] users1 = match(MatchArg.equals("userEmail", newUser.getUserEmail()));
			User[] users2 = match(MatchArg.equals("userName", newUser.getUserEmail()));
			if (users1.length != 0 || users2.length != 0) 
				flag = true;
			else
				flag = false;
			
			Transaction.commit();
			return flag;
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
		
	}
	
	public void setPassword(String userName, String password) throws RollbackException {
        try {
        	Transaction.begin();
			User dbUser = read(userName);
			
			if (dbUser == null) {
				throw new RollbackException("User "+userName+" no longer exists");
			}
			
			dbUser.setPassword(password);
			
			update(dbUser);
			Transaction.commit();
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}
}
