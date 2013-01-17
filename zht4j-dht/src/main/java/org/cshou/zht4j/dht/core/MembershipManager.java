/**
 * 
 */
package org.cshou.zht4j.dht.core;

import java.util.Collections;
import java.util.List;

import org.cshou.zht4j.dht.conf.MemberLoader;
import org.cshou.zht4j.dht.conf.ZhtConf;

/**
 * @author cshou
 *
 */
public class MembershipManager {

	protected static MembershipManager memberManager = null;
	
	protected final int capacity;
	protected int followerNum;
	
	// TODO need to add a lock to protect member list
	
	protected String[] members;
	
	protected MembershipManager () throws Exception {
		
		ZhtConf conf = ZhtConf.getZhtConf();
		
		// init zht capacity
		int tmp = conf.getZhtCapacity();
		if (tmp == 0)
			capacity = 100;
		else
			capacity = tmp;
		
		// init membership
		initMemberRing(new MemberLoader().loadMember());
	}
	
	public static MembershipManager getMemberManager () throws Exception {
		
		if (memberManager == null)
			memberManager = new MembershipManager ();
		
		return memberManager;
	}
	
	public int getCapacity () {
		return this.capacity;
	}
	
	public int getFollowerNum () {
		return this.followerNum;
	}
	
	public String[] getMembers () {
		return members;
	}
	
	private void initMemberRing (List<String> raw) throws Exception {
		
		if (raw.size() > capacity)
			throw new Exception("Capacity overflow!");
		
		Collections.sort(raw);
		members = new String[capacity];
		
		int gap = capacity / raw.size();
		followerNum = capacity % (gap * (raw.size() - 1) + 1);
		
		int begin = 0;
		for (int i = 0; i < raw.size(); i++) {
			members[begin] = raw.get(i);
			begin += gap;
		}
		
	}
 
}
