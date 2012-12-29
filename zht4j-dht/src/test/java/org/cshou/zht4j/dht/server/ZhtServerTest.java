package org.cshou.zht4j.dht.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cshou.zht4j.dht.client.ZhtClientBase;
import org.cshou.zht4j.dht.intl.ZhtClient;
import org.cshou.zht4j.dht.intl.ZhtServer;
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
		
		client.put(key, feature);
		
		List<String> res = (List<String>) client.get(key);
	
		Thread.sleep(31000);
		
		System.out.println(res);
		
	}

}
