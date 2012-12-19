/**
 * 
 */
package org.cshou.zht4j.persistent.intl;

/**
 * @author cshou
 * 
 */
public interface PersistentStorage {
	
	public int put (String key, Object value);
	public Object get (String key);
	public int remove (String key);
	public int getSize ();
	
}
