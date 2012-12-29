/**
 * 
 */
package org.cshou.zht4j.dht.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.cshou.zht4j.dht.conf.ZhtConf;
import org.cshou.zht4j.dht.entity.DataWrapper;
import org.cshou.zht4j.dht.entity.StoreStrategy;
import org.cshou.zht4j.intl.DataHandler;
import org.cshou.zht4j.intl.ZhtClient;
import org.cshou.zht4j.persistent.entity.DBEntity;

/**
 * @author cshou
 *
 */
public class ZhtClientBase implements ZhtClient {
	
	private static final int REG_PORT = 6668;
	private static final String INFO_SERVICE_NAME = "-info";
	private static final String DATA_SERVICE_NAME = "-data";
	
	protected ZhtConf conf = null;
	
	public ZhtClientBase () {
		// TODO uncomment when not testing
		// conf = ZhtConf.getZhtConf();
	}

	public int put(String key, Object object) {
		
		// test
		try {
			
			// for test only
			Registry svcReg = LocateRegistry.getRegistry("192.168.1.19", REG_PORT);
			DataHandler dataHandler = (DataHandler) svcReg.lookup("Chens-MacBook-Pro.local" + DATA_SERVICE_NAME);
			
			int res = dataHandler.receiveObject(new DataWrapper("aaa", new DBEntity()), new StoreStrategy(0));
			
			System.out.println(res);
			
			// TODO uncomment when not testing
			// TODO partition based on key - map to hostname or address
			
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		
		return 0;
	}

	public Object get(String key) {
		return null;
	}

	public int remove(String key) {
		return 0;
	}

	public void run() {
		
	}

}
