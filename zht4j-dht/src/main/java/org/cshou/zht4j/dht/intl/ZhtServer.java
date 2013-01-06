/**
 * 
 */
package org.cshou.zht4j.dht.intl;

import org.cshou.zht4j.dht.entity.StoreStrategy;
import org.cshou.zht4j.dht.entity.ZhtEntity;

/**
 * @author cshou
 * 
 */
public interface ZhtServer extends Runnable {

	public int put(String key, Object object, StoreStrategy strategy);

	public int put(String key, Object object, ObjectContext context,
			StoreStrategy strategy);

	public ZhtEntity get(String key);

	public int remove(String key);

}
