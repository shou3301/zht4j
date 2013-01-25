/**
 * 
 */
package org.cshou.zht4j.dht.util;

/**
 * @author cshou
 *
 */
public class TrafficLock {

	private int count1;
	private int count2;
	
	public TrafficLock () {
		count1 = 0;
		count2 = 0;
	}
	
	public void lock (int index) throws InterruptedException {
		synchronized (this) {
			
			if (index == 1) {
				while (this.count2 > 0) {
					this.wait();
				}
				this.count1++;
			}
			else {
				while (this.count1 > 0) {
					this.wait();
				}
				this.count2++;
			}
			
        }
	}
	
	public void lock (int index1, int index2) throws InterruptedException {
		synchronized (this) {
			while (this.count1 > 0 || this.count2 > 0) {
				this.wait();
			}
			this.count2++;
		}
	}
	
	public void unlock (int index) throws InterruptedException {
		synchronized (this) {
			
			if (index == 1) {
				this.count1--;
				this.notifyAll();
			}
			else {
				this.count2--;
				this.notifyAll();
			}
			
        }
	}
	
}
