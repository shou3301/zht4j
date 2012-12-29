/**
 * 
 */
package org.cshou.zht4j.intl;

/**
 * @author cshou
 *
 */
public interface ZhtServer extends Runnable {

	public int put (String key, Object object);
	public Object get (String key);
	public int remove (String key);
	
}
