package org.cshou.zht4j.dht.intl;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InfoHandler extends Remote {

	public boolean isAlive () throws RemoteException, NotBoundException;
	
}
