/* Student Name: Tsu-Hao KUo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */

package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databeans.Photo;
import formbeans.*;

public class PhotoDAO extends GenericDAO<Photo> {
	//private MyComparator comparator = new MyComparator(false);
	
	
	public PhotoDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(Photo.class, tableName, pool);
	}

	public void create(Photo newPhoto) throws RollbackException {
		try {
			Transaction.begin();
			Photo[] oldList = match(MatchArg.equals("owner",newPhoto.getOwner()));
			int maxPos = 0;
			for (Photo p : oldList) {
				if (p.getPosition() > maxPos) maxPos = p.getPosition();
			}
			
			newPhoto.setPosition(maxPos + 1);
			createAutoIncrement(newPhoto);
			Transaction.commit();
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}

	public void delete(int id, String owner) throws RollbackException {
		try {
			Transaction.begin();
    		Photo p = read(id);

    		if (p == null) {
				throw new RollbackException("Photo does not exist: id="+id);
    		}

    		if (!owner.equals(p.getOwner())) {
				throw new RollbackException("Photo not owned by "+owner);
    		}

			delete(id);
			Transaction.commit();
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}
	
	public Photo[] getPhotos(String owner) throws RollbackException {
		Photo[] list = match(MatchArg.equals("owner",owner));
		Arrays.sort(list);
		return list;
	}
	
	public Photo[] lookupStartsWith(String keywords) throws RollbackException {
		//System.out.println("KEYWORD =" + keywords);
		Photo[] list = match(MatchArg.containsIgnoreCase("description", keywords));
		return list;
	}
}
