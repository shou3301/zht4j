/**
 * 
 */
package org.cshou.zht4j.dht.membership;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

import org.cshou.zht4j.dht.conf.ZhtConf;
import org.cshou.zht4j.dht.intl.Locator;
import org.cshou.zht4j.dht.util.TrafficLock;

/**
 * @author cshou
 *
 */
public class ZhtLocator implements Locator {

	protected static ZhtLocator locator = null;
	
	protected final int capacity;
	
	protected MembershipManager memberManager;
	protected TrafficLock memberLock;
	protected ZhtConf conf;
	
	private ZhtLocator (MembershipManager memberManager) throws Exception {
		conf = ZhtConf.getZhtConf();
		this.memberManager = memberManager;
		this.memberLock = memberManager.getMemberLock();
		capacity = memberManager.getCapacity();
	}
	
	public synchronized static ZhtLocator getZhtLocator (MembershipManager memberManager) throws Exception {
		if (locator == null)
			return new ZhtLocator(memberManager);
		return locator;
	}
	
	public String getCoordinator(String key) throws Exception {
		
		int position = hash(key);
		String res = null;
		
		memberLock.lock(1);
		
		try {
			String[] members = memberManager.getMembers();
			
			while (members[position] == null) {
				position = (++position) % capacity;
			}
		
			res = members[position];
		}
		finally {
			memberLock.unlock(1);
		}
		
		return res;
	}
	
	// zero-hop ???
	protected int hash (String key) {
		
		// a simple rough way for starting the job
		
		int position = Math.abs(key.hashCode()) % capacity;
		
		return position;
	}

	public String getOriginPos(String key) throws Exception {
		
		int position = hash(key);
		String res = null;

		memberLock.lock(1);
		try {
			String[] members = memberManager.getMembers();
			res = members[position];
		}
		finally {
			memberLock.unlock(1);
		}
		
		return res;
	}

	public List<String> getFollowers(String current) throws Exception {
		
		// single node test
		// System.out.println("Current pos: " + current);
		
		List<String> res = new ArrayList<String>();
		
		memberLock.lock(1);
		
		try {
		
			String[] members = memberManager.getMembers();
			int num = memberManager.getFollowerNum();
			
			// single node test
			// System.out.println("Follower num: " + num);
			
			// search for current position
			for (int i = 0; i < members.length; i++) {
				
				// single node test
				//if (members[i] != null)
					// System.out.println("Debug info: member i = " + members[i]);
				
				if (members[i] != null && members[i].equals(current)) {
					int j = (i + 1) % capacity;
					int count = 0;
					while (count < num) {
						
						// single node test
						// System.out.println("Debug info: member j = " + members[j]);
						
						if (j == i)
							throw new Exception("Not enough nodes available");
						
						if (members[j] != null) {
							
							// single node test
							// System.out.println("Debug info: " + members[j]);
							
							res.add(members[j]);
							count++;
						}
						
						j = (j + 1) % capacity;
						
					}
					break;
				}
			}
		}
		finally {
			memberLock.unlock(1);
		}
		
		return res;
	}

}
