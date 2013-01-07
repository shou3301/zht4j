package org.cshou.zht4j.dht.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cshou.zht4j.dht.client.ZhtClientBase;
import org.cshou.zht4j.dht.intl.ZhtClient;
import org.junit.Test;

public class LocatorTest {

	@Test
	public void testGetCoordinator() {
		ZhtClient client = new ZhtClientBase();
		
		String key = "shouasdasdasfsafasf";
		List<String> feature = new ArrayList<String>();
		feature.add("Good person!");
		feature.add("Good developer!");
		feature.add("Good friend!");
		
		int r = client.put(key, feature);
	
	}

}
