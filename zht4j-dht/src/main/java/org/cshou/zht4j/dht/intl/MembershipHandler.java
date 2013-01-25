/**
 * 
 */
package org.cshou.zht4j.dht.intl;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author cshou
 *
 */
public interface MembershipHandler extends Remote {
	
	public int addMember (String member) throws RemoteException, NotBoundException;
	
	public int update (int index, String member) throws RemoteException, NotBoundException;
	
}
