/**
 * 
 */
package org.cshou.zht4j.dht.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.cshou.zht4j.dht.client.ZhtClientBase;
import org.cshou.zht4j.dht.intl.Locator;
import org.cshou.zht4j.dht.intl.ZhtClient;
import org.cshou.zht4j.dht.intl.ZhtServer;
import org.cshou.zht4j.dht.membership.MembershipManager;
import org.cshou.zht4j.dht.membership.ZhtLocator;
import org.cshou.zht4j.dht.server.ZhtServerBase;

/**
 * @author cshou
 *
 */
public class ZhtService {

	public static volatile AtomicBoolean shutdown;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		shutdown = new AtomicBoolean();
		shutdown.set(false);
		
		try {
			
			MembershipManager memberManagerRunnable = new MembershipManager();
			Thread memberManagerThread = new Thread(memberManagerRunnable, "memberManagerSocket-thread");
			memberManagerThread.start();
			
			Locator locator = ZhtLocator.getZhtLocator(memberManagerRunnable);
			
			ZhtServer serverRunnable = new ZhtServerBase(locator);
			Thread serverThread = new Thread(serverRunnable, "server-thread");
			serverThread.start();
			
			ZhtClient clientRunnable = new ZhtClientBase(locator);
			Thread clientThread = new Thread(clientRunnable, "client-thread");
			clientThread.start();
		
			String input = null;
			BufferedReader stdin = null;
			System.out.println("Please input your action: \n");
		
			stdin = new BufferedReader(new InputStreamReader(
					System.in));
			
			while (null != (input = stdin.readLine())) {
				if (input.equalsIgnoreCase("exit")) {
					System.exit(0);
				}
				else if (input.startsWith("put")) {
					String[] params = input.split(" ");
					if (params.length != 3) {
						System.out.println("Wrong input format!");
						continue;
					}
					String key = params[1];
					String value = params[2];
					clientRunnable.put(key, value);
				} 
				else if (input.startsWith("get")) {
					String[] params = input.split(" ");
					if (params.length != 2) {
						System.out.println("Wrong input format!");
						continue;
					}
					String key = params[1];
					System.out.println(clientRunnable.get(key));
				}
				else if (input.startsWith("join")) {
					String[] params = input.split(" ");
					if (params.length != 2) {
						System.out.println("Wrong input format!");
						continue;
					}
					String member = params[1];
					memberManagerRunnable.addMember(member);
				}
				else {
					System.out.println("Please input correct command! \n");
				}
			}

		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

}
