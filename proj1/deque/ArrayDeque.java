package deque;

/** Implementation of a deque (double-ended queue) using a resizable array.
 *  @author Yaohui Wu
 */
public class ArrayDeque<T> {
    private int capacity; // Size of the array.
    private T[] items;
    private int size; // Size of the deque.

    /** Creates an empty array deque with an initial capacity of 8. */
    public ArrayDeque() {
        capacity = 8;
        items = new T[capacity];
        size = 0;
    }

    /** Adds an item of type T to the front of the deque in constant time. */
    public void addFirst(T item) {}

    /** Adds an item of type T to the back of the deque in constant time. */
    public void addLast(T item) {
        // Last item in the deque is always in position size - 1.
        items[size] = item;
        size += 1;
    }

    /** Returns true if the deque is empty, false otherwise. */
    public boolean isEmpty() { return size == 0; }

    /** Returns the number of items in the deque in constant time. */
    public int size() { return size; }

    /** Prints all the items in the deque from first to last, separated by a
     *  space, then prints out a new line.
     */
    public void printDeque() {
        T item;
        for (int i = 0; i < size - 1; i += 1) {
            item = items[i];
            System.out.print(item + " ");
        }
        System.out.println(items[size - 1]);
    }

    /** Removes and returns the item at the front of the deque. If no such
     *  item exists, returns null.
     */
    public T removeFirst() {}

    /** Removes and returns the item at the back of the deque. If no such item
     *  exists, returns null.
     */
    public T removeLast() {
        T last = items[size - 1];
        size -= 1;
        return last;
    }

    /**
     * Gets the item at the given index using iteration, where 0 is the front,
     * 1 is the next item, and so forth. If no such item exists, returns null.
     */
    public T get(int index) {
        if (index >= size) { return null; }
        return items[index];
    }
}