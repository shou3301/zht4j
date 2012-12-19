/**
 * 
 */
package org.cshou.zht4j.persistent.entity;

/**
 * @author cshou
 *
 */
public class KeyPointer {

	private int offset;
	private int length;
	
	public KeyPointer () {
		super();
	}

	public KeyPointer (int offset, int length) {
		super();
		this.offset = offset;
		this.length = length;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
}
