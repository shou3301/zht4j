/**
 * 
 */
package org.cshou.zht4j.dht.core;

import org.cshou.zht4j.dht.conf.ZhtConf;
import org.cshou.zht4j.dht.intl.Locator;

/**
 * @author cshou
 *
 */
public class ZhtLocator implements Locator {

	protected final int capacity;
	
	protected MembershipManager memberManager;
	protected ZhtConf conf;
	
	public ZhtLocator () throws Exception {
		conf = ZhtConf.getZhtConf();
		memberManager = MembershipManager.getMemberManager();
		capacity = memberManager.getCapacity();
	}
	
	public String getCoordinator(String key) throws Exception {
		return hash(key);
	}
	
	// zero-hop ???
	protected String hash (String key) {
		
		// a simple rough way for starting the job
		
		int position = Math.abs(key.hashCode()) % capacity;
		
		String[] members = memberManager.getMembers();
		
		while (members[position] == null) {
			position = (++position) % capacity;
		}
		
		return members[position];
	}

}
