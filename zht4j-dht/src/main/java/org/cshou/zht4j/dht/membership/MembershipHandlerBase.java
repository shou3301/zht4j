/**
 * 
 */
package org.cshou.zht4j.dht.membership;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.cshou.zht4j.dht.intl.MembershipHandler;

/**
 * @author cshou
 *
 */
public class MembershipHandlerBase extends UnicastRemoteObject implements MembershipHandler {

	private static final long serialVersionUID = -6211489215972184709L;
	
	protected MembershipManager manager = null;
	
	public MembershipHandlerBase (int port, MembershipManager manager) throws RemoteException {
		super(port);
		this.manager = manager;
	}
	
	public int addMember(String member) throws RemoteException,
			NotBoundException {
		return 0;
	}

	public int update(int index, String member) throws RemoteException,
			NotBoundException {
		return 0;
	}

}
