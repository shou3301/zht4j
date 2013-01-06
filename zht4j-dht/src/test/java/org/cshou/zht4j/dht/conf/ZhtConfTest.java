package org.cshou.zht4j.dht.conf;

import static org.junit.Assert.*;

import org.junit.Test;

public class ZhtConfTest {

	@Test
	public void testGetZhtConf() throws Exception {
		ZhtConf zc = ZhtConf.getZhtConf();
		System.out.println(zc.getParams());
	}

}
