package dev.xdark.ssvm.thread;

import dev.xdark.ssvm.execution.Locals;
import dev.xdark.ssvm.execution.Stack;
import dev.xdark.ssvm.value.Value;
import lombok.val;

import java.util.Arrays;
import java.util.List;

/**
 * Thread cache for VM
 *
 * @author xDark
 */
public class SimpleThreadStorage implements ThreadStorage {

	private static final ThreadLocal<SimpleThreadStorage> THREAD_LOCAL = ThreadLocal.withInitial(SimpleThreadStorage::create);
	private static final int DEFAULT_STORAGE_SIZE = 65536;
	private final Thread thread;
	private final List<Value> storage;
	private int currentIndex;

	private SimpleThreadStorage(int maxSize) {
		thread = Thread.currentThread();
		storage = Arrays.asList(new Value[maxSize]);
	}

	@Override
	public ThreadRegion push(int size) {
		validateAccess();
		int currentIndex = this.currentIndex;
		int toIndex = currentIndex + size;
		val storage = this.storage;
		if (toIndex >= storage.size()) {
			throw new IndexOutOfBoundsException();
		}
		val region = new ThreadRegion(storage.subList(currentIndex, toIndex), this);
		this.currentIndex = toIndex;
		return region;
	}

	@Override
	public void pop(int size) {
		validateAccess();
		if ((currentIndex -= size) < 0) {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public Stack newStack(int size) {
		return new Stack(push(size));
	}

	@Override
	public Locals newLocals(int size) {
		return new Locals(push(size));
	}

	private void validateAccess() {
		if (Thread.currentThread() != thread) {
			throw new IllegalStateException();
		}
	}

	/**
	 * Creates new thread storage.
	 *
	 * @param maxSize
	 *      Storage size.
	 * @return new storage.
	 */
	public static SimpleThreadStorage create(int maxSize) {
		return new SimpleThreadStorage(maxSize);
	}

	/**
	 * Creates new thread storage
	 * of default size.
	 *
	 * @return new storage.
	 */
	public static SimpleThreadStorage create() {
		return new SimpleThreadStorage(DEFAULT_STORAGE_SIZE);
	}

	/**
	 * @return thread-local storage.
	 */
	public static SimpleThreadStorage get() {
		return THREAD_LOCAL.get();
	}

	/**
	 * @see SimpleThreadStorage#get()
	 * @see SimpleThreadStorage#push(int)
	 */
	public static ThreadRegion threadPush(int size) {
		return THREAD_LOCAL.get().push(size);
	}
}
