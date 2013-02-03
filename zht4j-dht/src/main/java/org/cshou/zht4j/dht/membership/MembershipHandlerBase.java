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
		
		int res = -1;
		
		try {
			res = manager.addMember(member);
		} catch (Exception e) {
			e.printStackTrace();
			return res;
		}
		
		return res;
	}

	public int update(int index, String member) throws RemoteException,
			NotBoundException {
		
		int res = 1;
		
		try {
			res = manager.updateMember(index, member);
		} catch (Exception e) {
			e.printStackTrace();
			return res;
		}
		
		return 0;
	}

	public int setMemberList(String[] members) throws RemoteException,
			NotBoundException {

		try {
			manager.updateMember(members);
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		
		return 0;
	}

}
