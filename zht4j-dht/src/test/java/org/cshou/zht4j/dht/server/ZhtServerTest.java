package org.cshou.zht4j.dht.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cshou.zht4j.dht.client.ZhtClientBase;
import org.cshou.zht4j.dht.intl.ZhtClient;
import org.cshou.zht4j.dht.intl.ZhtServer;
import org.cshou.zht4j.persistent.impl.SimpleDB;
import org.cshou.zht4j.persistent.intl.PersistentStorage;
import org.junit.Test;

public class ZhtServerTest {

	@Test
	public void testZhtServerBase() throws Exception {
		
		ZhtServer server = new ZhtServerBase();
		
		ZhtClient client = new ZhtClientBase();
		
		String key = "shou";
		List<String> feature = new ArrayList<String>();
		feature.add("Good person!");
		feature.add("Good developer!");
		feature.add("Good friend!");
		
		int r = client.put(key, feature);
		
		if (r == 0)
			System.out.println("Successfully put the object!");
		
		List<String> res = (List<String>) client.get(key);
	
		Thread.sleep(31000);
		
		System.out.println(res);
		
	}

	@Test
	public void testZhtServer() throws Exception {
		
		// create db
		PersistentStorage ps1 = new SimpleDB("simpledb/storage1.db", 30000L, 12800);
		PersistentStorage ps2 = new SimpleDB("simpledb/storage2.db", 30000L, 12800);
		PersistentStorage ps3 = new SimpleDB("simpledb/storage3.db", 30000L, 12800);
		PersistentStorage ps4 = new SimpleDB("simpledb/storage4.db", 30000L, 12800);
		PersistentStorage ps5 = new SimpleDB("simpledb/storage5.db", 30000L, 12800);
		
		// create zht server
		ZhtServer server1 = new ZhtServerBase(ps1, "service1", 4000);
		ZhtServer server2 = new ZhtServerBase(ps2, "service2", 4010);
		ZhtServer server3 = new ZhtServerBase(ps3, "service3", 4020);
		ZhtServer server4 = new ZhtServerBase(ps4, "service4", 4030);
		ZhtServer server5 = new ZhtServerBase(ps5, "service5", 4040);
		
		// create object
		String key = "test key";
		String value = "test value";
		
		// create client
		ZhtClient client = new ZhtClientBase();
		
		// insert object
		client.put(key, value);
		
		// wait for some time
		Thread.sleep(31000);
		
		// get result
		String res = (String) client.get(key);
		
		System.out.println("Got result = " + res);
	}
}
