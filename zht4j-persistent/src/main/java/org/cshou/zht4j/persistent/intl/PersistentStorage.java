/**
 * 
 */
package org.cshou.zht4j.persistent.intl;

import org.cshou.zht4j.persistent.entity.DBEntity;

/**
 * @author cshou
 * provide standard persistent storage interface
 */
public interface PersistentStorage {
	
	public int put (String key, DBEntity value);
	
	public DBEntity get (String key);
	
	public int remove (String key);
	
	public int getSize ();
	
	public int getCapacity ();
	
}
