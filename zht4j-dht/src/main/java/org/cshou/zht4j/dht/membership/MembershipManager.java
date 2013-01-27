/**
 * 
 */
package org.cshou.zht4j.dht.membership;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.cshou.zht4j.dht.conf.MemberLoader;
import org.cshou.zht4j.dht.conf.ZhtConf;
import org.cshou.zht4j.dht.intl.MembershipHandler;
import org.cshou.zht4j.dht.service.ZhtService;
import org.cshou.zht4j.dht.util.Naming;
import org.cshou.zht4j.dht.util.TrafficLock;

/**
 * @author cshou
 *
 */
public class MembershipManager implements Runnable {
	
	protected static final int MIN_FOLLOWER = 5;
	protected static final int DEFULAT_CAP = 100;

	protected static MembershipManager memberManager = null;
	
	protected String serviceName;
	protected static int currentIndex; 
	
	protected final int capacity;
	protected int followerNum;
	
	// TODO need to add a lock to protect member list
	
	protected String[] members;
	
	protected TrafficLock memberLock;
	
	public MembershipManager () throws Exception {
		this(InetAddress.getLocalHost().getHostName());
	}
	
	public MembershipManager (String serviceName) throws Exception {
		
		ZhtConf conf = ZhtConf.getZhtConf();
		
		// init zht capacity
		int tmp = conf.getZhtCapacity();
		if (tmp == 0)
			capacity = DEFULAT_CAP;
		else
			capacity = tmp;
		
		this.serviceName = serviceName;
		
		// init membership
		initMemberRing(new MemberLoader().loadMember());
		
		memberLock = new TrafficLock();
		
		// TODO register service 
		Registry svcReg = LocateRegistry.createRegistry(Naming.getMemberRegPort());
		MembershipHandler memberService = new MembershipHandlerBase(Naming.getMemberSvcPort(), this);
		svcReg.rebind(Naming.getMemberService(this.serviceName), memberService);
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
					
					if (members[begin].equals(serviceName)) {
						currentIndex = begin;
					}
					
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
	
	public TrafficLock getMemberLock () {
		return this.memberLock;
	}
	
	public int addMember (String member) throws Exception {
		
		memberLock.lockBoth();
		int res = -1;
		
		try {
			
			int candidate = (capacity + currentIndex - 1) % capacity;
			if (members[candidate] == null) {
				
				res = candidate;
				members[candidate] = member;
				
				// TODO new thread to inform previous neighbor
				int neighbor = (candidate - 1) % capacity;
				while (members[neighbor] == null) {
					neighbor = (neighbor - 1) % capacity;
				}
				new UpdateMemberTask((MembershipHandler) getHandler(members[neighbor], Naming.getMemberService(members[neighbor])),
						candidate, member).run();
			}
		}
		finally {
			memberLock.unlockBoth();
		}
		
		return res;
	}
	
	private Remote getHandler (String hostaddress, String service) throws Exception {
		Registry svcReg = LocateRegistry.getRegistry(hostaddress, Naming.getRegPort());
		return svcReg.lookup(service);
	}
 
}
