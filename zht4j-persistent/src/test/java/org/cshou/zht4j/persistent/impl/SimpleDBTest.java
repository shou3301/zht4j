package org.cshou.zht4j.persistent.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleDBTest {
	
	@Test
	public void testClean() throws Exception {
		
		SimpleDB db = new SimpleDB(10);
		PersistTask pt = PersistTask.getPersistTask(db);
		
		FakeEntity fe1 = new FakeEntity("some data 1");
		FakeEntity fe2 = new FakeEntity("some data 2");
		FakeEntity fe3 = new FakeEntity("some data 3");
		FakeEntity fe4 = new FakeEntity("some data 4");
		FakeEntity fe5 = new FakeEntity("some data 5");
		FakeEntity fe6 = new FakeEntity("some data 6");
		FakeEntity fe7 = new FakeEntity("some data 7");
		FakeEntity fe8 = new FakeEntity("some data 8");
		FakeEntity fe9 = new FakeEntity("some data 9");
		FakeEntity fe10 = new FakeEntity("some data 10");
		FakeEntity fe11 = new FakeEntity("some data 11");
		FakeEntity fe12 = new FakeEntity("some data 12");
		FakeEntity fe13 = new FakeEntity("some data 13");
		FakeEntity fe14 = new FakeEntity("some data 14");
		FakeEntity fe15 = new FakeEntity("some data 15");
		FakeEntity fe16 = new FakeEntity("some data 16");
		FakeEntity fe17 = new FakeEntity("some data 17");
		FakeEntity fe18 = new FakeEntity("some data 18");
		FakeEntity fe19 = new FakeEntity("some data 19");
		FakeEntity fe20 = new FakeEntity("some data 20");
		
		db.put("fe1", fe1);
		Thread.sleep(2000);	
		db.put("fe2", fe2);
		Thread.sleep(2000);
		db.put("fe3", fe3);
		Thread.sleep(2000);
		db.put("fe4", fe4);
		Thread.sleep(2000);
		db.put("fe5", fe5);
		Thread.sleep(2000);	
		db.put("fe6", fe6);
		Thread.sleep(2000);
		db.put("fe7", fe7);
		Thread.sleep(2000);
		db.put("fe8", fe8);
		Thread.sleep(2000);
		db.put("fe9", fe9);
		Thread.sleep(2000);	
		db.put("fe10", fe10);
		Thread.sleep(2000);
		
		db.put("fe11", fe11);
		db.put("fe1", fe12);
		db.put("fe2", fe13);
		db.put("fe3", fe14);
		db.put("fe15", fe15);
		/*db.put("fe16", fe16);
		db.put("fe17", fe17);
		db.put("fe18", fe18);
		db.put("fe19", fe19);
		db.put("fe20", fe20);*/
		
		
		FakeEntity res1 = (FakeEntity) db.get("fe1");
		FakeEntity res2 = (FakeEntity) db.get("fe15");
		System.out.println(res1);
		System.out.println(res2);
		
		/*db.put("fe1", null);
		db.put("fe2", fe5);
		db.put("fe5", fe6);
		
		pt.run();
		
		res1 = (FakeEntity) db.get("fe1");
		res2 = (FakeEntity) db.get("fe2");
		FakeEntity res3 = (FakeEntity) db.get("fe5");
		System.out.println(res1);
		System.out.println(res2);
		System.out.println(res3);*/
		
	}

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
