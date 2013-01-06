package org.cshou.zht4j.dht.client;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.cshou.zht4j.dht.conf.ZhtConf;
import org.junit.Test;

public class MembershipManagerTest {

	@Test
	public void testGetMemberManager() throws Exception {
		MembershipManager memberManager = MembershipManager.getMemberManager();
		System.out.println("Capacity: " + memberManager.getCapacity());
		System.out.println("Follower Num: " + memberManager.getFollowerNum());
		System.out.println("Ring: " + Arrays.asList(memberManager.getMembers()));
	}

}
