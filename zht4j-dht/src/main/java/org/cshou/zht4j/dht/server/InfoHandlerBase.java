/**
 * 
 */
package org.cshou.zht4j.dht.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.cshou.zht4j.dht.intl.InfoHandler;

/**
 * @author cshou
 *
 */
public class InfoHandlerBase implements InfoHandler {

	public boolean isAlive() throws RemoteException, NotBoundException {
		
		return false;
	}

}
