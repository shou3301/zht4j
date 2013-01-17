/**
 * 
 */
package org.cshou.zht4j.dht.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.cshou.zht4j.dht.entity.DataWrapper;
import org.cshou.zht4j.dht.intl.DataHandler;
import org.cshou.zht4j.dht.intl.ObjectContext;

/**
 * @author cshou
 *
 */
public class ReplicateTask extends Thread {

	private static final int REG_PORT = 6668;
	private static final String INFO_SERVICE_NAME = "-info";
	private static final String DATA_SERVICE_NAME = "-data";
	
	protected String target = null;
	protected DataWrapper object = null;
	
	public ReplicateTask(String target, String key, Object object,
			ObjectContext context) {
		super();
		this.target = target;
		this.object = new DataWrapper (key, object, context);
	}

	@Override
	public void run () {
		
		try {
			
			Registry svcReg = LocateRegistry.getRegistry(target, REG_PORT);
			DataHandler dataHandler = (DataHandler) svcReg.lookup(target + DATA_SERVICE_NAME);
			
			dataHandler.receiveReplica(object);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}
	
}
