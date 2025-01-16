package deque;

import java.lang.Math;

/** Implementation of a deque (double-ended queue) using a resizable circular
 *  array.
 *  @author Yaohui Wu
 */
public class ArrayDeque<T> implements Deque<T> {
    private int capacity; // Size of the array.
    private T[] items;
    private int size; // Size of the deque.
    private int nextFirst; // Position of the next first item in the deque.
    private int nextLast; // Position of the next last item in the deque.

    /** Creates an empty array deque with an initial capacity of 8. */
    public ArrayDeque() {
        capacity = 8;
        items = (T[]) new Object[capacity];
        size = 0;
        // Initializes the deque in the middle of the array.
        nextFirst = capacity / 2;
        nextLast = nextFirst + 1;
    }

    public void resize() {}

    public int arrayIndex(int index) {
        if (index < 0 || index >= size) {
            if (size > 0) {
                index = Math.floorMod(index, size);
            }
        }
        return index;
    }

    @Override
    /** Adds an item of type T to the front of the deque in constant time. */
    public void addFirst(T item) {
        items[nextFirst] = item;
        nextFirst = arrayIndex(nextFirst - 1);
        size += 1;
    }

    @Override
    /** Adds an item of type T to the back of the deque in constant time. */
    public void addLast(T item) {
        items[nextLast] = item;
        nextLast = arrayIndex(nextLast + 1);
        size += 1;
    }

    @Override
    /** Returns the number of items in the deque in constant time. */
    public int size() { return size; }

    @Override
    /** Prints all the items in the deque from first to last, separated by a
     *  space, then prints out a new line.
     */
    public void printDeque() {
        // Position of the first element of the deque.
        int index = nextFirst + 1;
        for (int i = 0; i < size - 1; i += 1) {
            index = arrayIndex(index);
            System.out.print(items[index] + " ");
            index += 1;
        }
        System.out.println(items[index]);
    }

    @Override
    /** Removes and returns the item at the front of the deque. If no such
     *  item exists, returns null.
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int first = arrayIndex(nextFirst + 1);
        T item = items[first];
        items[first] = null;
        nextFirst = first;
        size -= 1;
        return item;
    }

    @Override
    /** Removes and returns the item at the back of the deque. If no such item
     *  exists, returns null.
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int last = arrayIndex(nextLast - 1);
        T item = items[last];
        items[last] = null;
        nextLast = last;
        size -= 1;
        return item;
    }

    @Override
    /** Gets the item at the given index in constant time, where 0 is the
     *  front, 1 is the next item, and so forth. If no such item exists,
     *  returns null.
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int first = arrayIndex(nextFirst + 1);
        index += first;
        index = arrayIndex(index);
        T item = items[index];
        return item;
    }

    /** Returns an iterator of the deque. */
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private int position;

        public LinkedListDequeIterator() {
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
        ArrayDeque deque = (ArrayDeque) o;
        if (this.size != deque.size) {
            return false;
        }
        for (int i = 0; i < size; i += 1) {
            if (this.get(i) != deque.get(i)) {
                return false;
            }
        }
        return true;
    }
}
