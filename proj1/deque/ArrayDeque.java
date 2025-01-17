package deque;

import java.lang.Math;
import java.util.Iterator;

/** Implementation of a deque (double-ended queue) using a resizable circular
 *  array.
 *  @author Yaohui Wu
 */
public class ArrayDeque<T> implements Iterable<T> {
    private int capacity; // Size of the array.
    private T[] items;
    private int size; // Size of the deque.
    //  Array index of the next first item in the deque.
    private int nextFirst;
    // Array index of the next last item in the deque.
    private int nextLast;

    /** Creates an empty array deque with an initial capacity of 8. */
    public ArrayDeque() {
        capacity = 8;
        items = (T[]) new Object[capacity];
        size = 0;
        // Initializes the deque in the middle of the array.
        nextFirst = capacity / 2;
        nextLast = nextFirst + 1;
    }

    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        System.arraycopy(newItems, 0, items, 0, size);
        items = newItems;
    }

    /** Adds an item of type T to the front of the deque in constant time. */
    public void addFirst(T item) {
        items[nextFirst] = item;
        size += 1;
        nextFirst = getArrayIndex(nextFirst - 1);
    }

    /** Adds an item of type T to the back of the deque in constant time. */
    public void addLast(T item) {
        items[nextLast] = item;
        size += 1;
        nextLast = getArrayIndex(nextLast + 1);
    }

    /** Returns true if the deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size() == 0;
    }

    /** Returns the number of items in the deque in constant time. */
    public int size() {
        return size;
    }

    /** Prints all the items in the deque from first to last, separated by a
     *  space, then prints out a new line.
     */
    public void printDeque() {
        for (int i = 0; i < size - 1; i += 1) {
            System.out.print(get(i));
        }
        System.out.println(get(size - 1));
    }

    /** Removes and returns the item at the front of the deque. If no such
     *  item exists, returns null.
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T item = get(0);
        size -= 1;
        nextFirst = getArrayIndex(nextFirst + 1);
        return item;
    }

    /** Removes and returns the item at the back of the deque. If no such item
     *  exists, returns null.
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T item = get(size - 1);
        size -= 1;
        nextLast = getArrayIndex(nextLast - 1);
        return item;
    }

    /** Gets the item at the given index in constant time, where 0 is the
     *  front, 1 is the next item, and so forth. If no such item exists,
     *  returns null.
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        index = getArrayIndex(index + nextFirst + 1);
        T item = items[index];
        return item;
    }

    /** Returns the index in the array given the index in the deque. */
    private int getArrayIndex(int index) {
        if (index < 0 || index >= capacity) {
            index = Math.floorMod(index, capacity);
        }
        return index;
    }

    /** Returns an iterator of the deque. */
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int position;

        public ArrayDequeIterator() {
            position = 0;
        }

        public boolean hasNext() {
            return position < size;
        }

        public T next() {
            T item = get(position);
            position += 1;
            return item;
        }
    }

    /** Returns whether or not the parameter o is equal to the deque. */
    public boolean equals(Object o) {
        ArrayDeque other = (ArrayDeque) o;
        if (size != other.size) {
            return false;
        }
        for (int i = 0; i < size; i += 1) {
            if (get(i) != other.get(i)) {
                return false;
            }
        }
        return true;
    }
}
