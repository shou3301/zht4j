package org.cshou.zht4j.persistent.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleDBTest {

	@Test
	public void testPut() {
		
		SimpleDB db = new SimpleDB();
		PersistTask pt = PersistTask.getPersistTask(db);
		
		FakeEntity fe1 = new FakeEntity("some data 1");
		FakeEntity fe2 = new FakeEntity("some data 2");
		FakeEntity fe3 = new FakeEntity("some data 3");
		FakeEntity fe4 = new FakeEntity("some data 4");
		FakeEntity fe5 = new FakeEntity("some data 2 again");
		
		db.put("fe1", null);
		db.put("fe2", fe2);
		db.put("fe3", fe3);
		db.put("fe4", fe4);
		
		pt.run();
		
		db.put("fe2", fe5);
		
		pt.run();
		
		FakeEntity res = (FakeEntity) db.get("fe2");
		System.out.println(res);
		
	}

	/*@Test
	public void testGet() {
	}*/

}
