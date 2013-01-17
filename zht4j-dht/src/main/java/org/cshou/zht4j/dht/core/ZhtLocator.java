/**
 * 
 */
package org.cshou.zht4j.dht.core;

import java.util.ArrayList;
import java.util.List;

import org.cshou.zht4j.dht.conf.ZhtConf;
import org.cshou.zht4j.dht.intl.Locator;

/**
 * @author cshou
 *
 */
public class ZhtLocator implements Locator {

	protected static ZhtLocator locator = null;
	
	protected final int capacity;
	
	protected MembershipManager memberManager;
	protected ZhtConf conf;
	
	private ZhtLocator () throws Exception {
		conf = ZhtConf.getZhtConf();
		memberManager = MembershipManager.getMemberManager();
		capacity = memberManager.getCapacity();
	}
	
	public synchronized static ZhtLocator getZhtLocator () throws Exception {
		if (locator == null)
			return new ZhtLocator();
		return locator;
	}
	
	public String getCoordinator(String key) throws Exception {
		
		int position = hash(key);
		
		String[] members = memberManager.getMembers();
		
		while (members[position] == null) {
			position = (++position) % capacity;
		}
		
		return members[position];
	}
	
	// zero-hop ???
	protected int hash (String key) {
		
		// a simple rough way for starting the job
		
		int position = Math.abs(key.hashCode()) % capacity;
		
		return position;
	}

	public String getOriginPos(String key) {
		
		int position = hash(key);
		
		String[] members = memberManager.getMembers();
		
		return members[position];
	}

	public List<String> getFollowers(String current) throws Exception {
		
		List<String> res = new ArrayList<String>();
		
		String[] members = memberManager.getMembers();
		int num = memberManager.getFollowerNum();
		
		// search for current position
		for (int i = 0; i < members.length; i++) {
			if (members[i].equals(current)) {
				int j = (i + 1) % capacity;
				int count = 0;
				while (count < num) {
					
					if (j == i)
						throw new Exception("Not enough nodes available");
					
					if (members[j] != null) {
						res.add(members[j]);
						count++;
						j = (j + 1) % capacity;
					}
				}
				break;
			}
		}
		
		return res;
	}

}
