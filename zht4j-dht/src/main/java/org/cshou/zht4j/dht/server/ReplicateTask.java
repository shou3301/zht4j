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
import org.cshou.zht4j.dht.util.Naming;

/**
 * @author cshou
 *
 */
public class ReplicateTask extends Thread {
	
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
			Registry svcReg = LocateRegistry.getRegistry(target, Naming.getRegPort());
			DataHandler dataHandler = (DataHandler) svcReg.lookup(Naming.getDataService(target));
			
			dataHandler.receiveReplica(object);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}
	
}
