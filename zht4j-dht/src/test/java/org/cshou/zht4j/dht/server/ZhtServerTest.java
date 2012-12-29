package org.cshou.zht4j.dht.server;

import static org.junit.Assert.*;

import org.cshou.zht4j.dht.client.ZhtClientBase;
import org.cshou.zht4j.intl.ZhtClient;
import org.cshou.zht4j.intl.ZhtServer;
import org.junit.Test;

public class ZhtServerTest {

	@Test
	public void testZhtServerBase() throws Exception {
		
		ZhtServer server = new ZhtServerBase();
		
		ZhtClient client = new ZhtClientBase();
		client.put("aaa", "ddd");
	
	}

}
