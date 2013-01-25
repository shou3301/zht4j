/**
 * 
 */
package org.cshou.zht4j.dht.membership;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cshou.zht4j.dht.conf.MemberLoader;
import org.cshou.zht4j.dht.conf.ZhtConf;
import org.cshou.zht4j.dht.service.ZhtService;

/**
 * @author cshou
 *
 */
public class MembershipManager implements Runnable {
	
	protected static final int MIN_FOLLOWER = 5;

	protected static MembershipManager memberManager = null;
	
	protected final int capacity;
	protected int followerNum;
	
	// TODO need to add a lock to protect member list
	
	protected String[] members;
	
	public MembershipManager () throws Exception {
		
		ZhtConf conf = ZhtConf.getZhtConf();
		
		// init zht capacity
		int tmp = conf.getZhtCapacity();
		if (tmp == 0)
			capacity = 100;
		else
			capacity = tmp;
		
		// init membership
		initMemberRing(new MemberLoader().loadMember());
		
		// TODO register service 
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
		
		double initgap = (double) capacity / (double) raw.size();
		
		int gap = (int) Math.ceil(initgap);
		
		followerNum = gap + 1;
		
		// single node test
		/*if (followerNum < MIN_FOLLOWER)
			followerNum = MIN_FOLLOWER;*/
		
		int start = 0;
		int i = 0;
		while (start < gap) {
			int begin = start;
			while (begin < capacity) {
				if (i < raw.size()) {
					members[begin] = raw.get(i);
					begin += gap;
					i++;
				}
				else
					break;
			}
			start++;
		}
		
		// single node test
		// System.out.println("Memberlist: " + Arrays.asList(members));
		
	}

	public void run() {
		while (!ZhtService.shutdown.get()) {
			// socket accept
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
 
}
