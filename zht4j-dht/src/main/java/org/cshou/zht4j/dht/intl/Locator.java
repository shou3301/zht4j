/**
 * 
 */
package org.cshou.zht4j.dht.intl;

import java.util.List;

/**
 * @author cshou
 *
 */
public interface Locator {
	
	public String getCoordinator (String key) throws Exception;
	
	public String getOriginPos (String key);
	
	public List<String> getFollowers (String current) throws Exception;
	
}
