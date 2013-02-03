/**
 * 
 */
package org.cshou.zht4j.dht.intl;

import org.cshou.zht4j.dht.entity.StorePolicy;
import org.cshou.zht4j.dht.entity.ZhtEntity;

/**
 * @author cshou
 * 
 */
public interface ZhtServer extends Runnable {

	public int put(String key, Object object, ObjectContext context, StorePolicy strategy);
	
	public int put (String key, Object object, ObjectContext context);
	
	public int migrate (String key, Object object, ObjectContext context, StorePolicy strategy, String target);

	public ZhtEntity get(String key);
	
	public ZhtEntity getReplica(String key);

	public int remove(String key);

}
