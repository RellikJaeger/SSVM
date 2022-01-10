package dev.xdark.ssvm.thread;

import dev.xdark.ssvm.value.InstanceValue;

/**
 * VM thread manager.
 *
 * @author xDark
 */
public interface ThreadManager {

	/**
	 * Returns VM thread from Java thread.
	 *
	 * @param thread
	 * 		Thread to get VM thread from.
	 *
	 * @return VM thread.
	 */
	VMThread getVmThread(Thread thread);

	/**
	 * Returns VMThread by an instance.
	 *
	 * @param thread
	 * 		VM thread oop.
	 */
	VMThread getVmThread(InstanceValue thread);

	/**
	 * Assigns VM thread.
	 *
	 * @param thread
	 * 		Thread to assign.
	 */
	void setVmThread(VMThread thread);
}
