package deque;

import java.util.Iterator;

/** Implementation of a deque (double-ended queue) based on a resizable
 *  circular array.
 *  @author Yaohui Wu
 */
public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private int capacity; // Size of the array.
    private T[] items;
    private int size; // Size of the deque.
    //  Array index of the next first item in the deque.
    private int nextFirst;
    // Array index of the next last item in the deque.
    private int nextLast;

    /** Creates an empty array deque with an initial capacity of 8. */
    public ArrayDeque() {
        ArrayDeque(8);
    }

    /** Creates an empty array deque with a given initial capacity. */
    public ArrayDeque(int newCapacity) {
        capacity = newCapacity;
        items = (T[]) new Object[capacity];
        size = 0;
        // Initializes an empty deque in the middle of the array.
        nextFirst = capacity / 2;
        nextLast = nextFirst + 1;
    }

    private void resize(int newCapacity) {
        // Resize up or down the array with geometric expansion.
        if (newCapacity < 8) {
            return;
        }
        T[] array = (T[]) new Object[newCapacity];
        /* 
         * Copy the elements in the deque from front to back to the beginning
         * of the new array.
         */
        for (int i = 0; i < size; i += 1) {
            array[i] = get(i);
        }
        items = array;
        nextFirst = newCapacity - 1;
        nextLast = size;
        capacity = newCapacity;
    }

    private void resizeUp() {
        if (size == capacity) {
            resize(capacity * 2);
        }
    }

    private void resizeDown() {
        if (capacity > 8 && size < capacity / 2) {
            resize(capacity / 2);
        }
    }

    /** Returns a valid index in the circular array. */
    private int getArrayIndex(int index) {
        if (index < 0 || index >= capacity) {
            index = Math.floorMod(index, capacity);
        }
        return index;
    }

    @Override
    /** Adds an item of type T to the front of the deque in constant time. */
    public void addFirst(T item) {
        resizeUp();
        items[nextFirst] = item;
        size += 1;
        nextFirst = getArrayIndex(nextFirst - 1);
    }

    @Override
    /** Adds an item of type T to the back of the deque in constant time. */
    public void addLast(T item) {
        resizeUp();
        items[nextLast] = item;
        size += 1;
        nextLast = getArrayIndex(nextLast + 1);
    }

    @Override
    /** Returns the number of items in the deque in constant time. */
    public int size() {
        return size;
    }

    @Override
    /** Prints all the items in the deque from first to last, separated by a
     *  space, then prints out a new line.
     */
    public void printDeque() {
        for (int i = 0; i < size - 1; i += 1) {
            System.out.print(get(i));
        }
        System.out.println(get(size - 1));
    }

    @Override
    /** Removes and returns the item at the front of the deque. If no such
     *  item exists, returns null.
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int first = getArrayIndex(nextFirst + 1);
        T item = items[first];
        items[first] = null;
        size -= 1;
        nextFirst = getArrayIndex(nextFirst + 1);
        resizeDown();
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
        int last = getArrayIndex(nextLast - 1);
        T item = items[last];
        items[last] = null;
        size -= 1;
        nextLast = getArrayIndex(nextLast - 1);
        resizeDown();
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
        index = getArrayIndex(index + nextFirst + 1);
        T item = items[index];
        return item;
    }

    /** Returns an iterator of the deque. */
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int position;

        private ArrayDequeIterator() {
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

    @Override
    /** Returns whether or not the parameter o is equal to the deque. */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (size != other.size()) {
            return false;
        }
        for (int i = 0; i < size; i += 1) {
            if (!get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }
}
