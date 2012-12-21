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
		FakeEntity fe6 = new FakeEntity("overwrite for data 1 later");
		
		db.put("fe1", fe1);
		db.put("fe2", fe2);
		db.put("fe3", fe3);
		db.put("fe4", fe4);
		
		pt.run();
		
		FakeEntity res1 = (FakeEntity) db.get("fe1");
		FakeEntity res2 = (FakeEntity) db.get("fe2");
		System.out.println(res1);
		System.out.println(res2);
		
		db.put("fe1", null);
		db.put("fe2", fe5);
		db.put("fe5", fe6);
		
		pt.run();
		
		res1 = (FakeEntity) db.get("fe1");
		res2 = (FakeEntity) db.get("fe2");
		FakeEntity res3 = (FakeEntity) db.get("fe5");
		System.out.println(res1);
		System.out.println(res2);
		System.out.println(res3);
		
	}

	/*@Test
	public void testGet() {
	}*/

}
