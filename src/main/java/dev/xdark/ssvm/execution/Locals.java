package dev.xdark.ssvm.execution;

import dev.xdark.ssvm.thread.ThreadRegion;
import dev.xdark.ssvm.thread.SimpleThreadStorage;
import dev.xdark.ssvm.value.Value;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * Storage for local variables.
 *
 * @author xDark
 */
@RequiredArgsConstructor
public final class Locals implements AutoCloseable {

	private final ThreadRegion table;

	/**
	 * @param maxSize
	 * 		The maximum amount of local variables.
	 */
	public Locals(int maxSize) {
		table = SimpleThreadStorage.threadPush(maxSize);
	}

	/**
	 * Sets value at specific index.
	 *
	 * @param index
	 * 		Index of local variable.
	 * @param value
	 * 		Value to set.
	 */
	public void set(int index, Value value) {
		table.set(index, Objects.requireNonNull(value, "value"));
	}

	/**
	 * Loads value from local variable.
	 *
	 * @param index
	 * 		Index of local variable.
	 * @param <V>
	 * 		Type of the value to load.
	 *
	 * @return value at {@code index}.
	 */
	public <V extends Value> V load(int index) {
		return (V) table.get(index);
	}

	/**
	 * @return underlying content of the LVT.
	 */
	public Value[] getTable() {
		return table.unwrap();
	}

	/**
	 * @return the maximum amount of slots of this LVT.
	 */
	public int maxSlots() {
		return table.length();
	}

	/**
	 * Deallocates internal table.
	 */
	public void deallocate() {
		table.close();
	}

	@Override
	public void close() {
		deallocate();
	}

	@Override
	public String toString() {
		return "Locals{" +
				"table=" + Arrays.toString(table.unwrap()) +
				'}';
	}
}
