package deque;

import java.util.Iterator;

/** Implementation of a deque (double-ended queue) based on a circular doubly
 *  linked list with a sentinel node.
 *  @author Yaohui Wu
 */
public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private class Node {
        private T item;
        private Node previous;
        private Node next;

        public Node(T item, Node previous, Node next) {
            this.item = item;
            this.previous = previous;
            this.next = next;
        }
    }

    private Node sentinel;
    private int size;

    /** Creates an empty linked list deque. */
    public LinkedListDeque() {
        /*
         * Initializes an empty sentinel node with its previous and next
         * pointers pointing to itself.
         */
        sentinel = new Node(null, null, null);
        sentinel.previous = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    @Override
    /** Adds an item of type T to the front of the deque in constant time. */
    public void addFirst(T item) {
        Node first = sentinel.next; // Old first node.
        Node current = new Node(item, sentinel, first); // New first Node.
        first.previous = current;
        sentinel.next = current;
        size += 1;
    }

    @Override
    /** Adds an item of type T to the back of the deque in constant time. */
    public void addLast(T item) {
        Node last = sentinel.previous; // Old last node.
        Node current = new Node(item, last, sentinel); // New last node.
        last.next = current;
        sentinel.previous = current;
        size += 1;
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
        Node current = sentinel.next;
        for (int i = 0; i < size - 1; i += 1) {
            System.out.print(current.item + " ");
            current = current.next;
        }
        System.out.println(current.item);
    }

    @Override
    /** Removes and returns the item at the front of the deque. If no such
     *  item exists, returns null.
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node current = sentinel.next; // Old first node.
        Node first = current.next; // New first node.
        first.previous = sentinel;
        sentinel.next = first;
        size -= 1;
        return current.item;
    }

    @Override
    /** Removes and returns the item at the back of the deque. If no such item
     *  exists, returns null.
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node current = sentinel.previous; // Old last node.
        Node last = current.previous; // New last node.
        last.next = sentinel;
        sentinel.previous = last;
        size -= 1;
        return current.item;
    }

    @Override
    /**
     * Gets the item at the given index using iteration, where 0 is the front,
     * 1 is the next item, and so forth. If no such item exists, returns null.
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node current = sentinel.next;
        for (int i = 0; i < index; i += 1) {
            current = current.next;
        }
        return current.item;
    }

    /**
     * Gets the item at the given index using recursion, where 0 is the front,
     * 1 is the next item, and so forth. If no such item exists, returns null.
     */
    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node current = sentinel.next;
        return getItemRecursive(current, index);
    }

    private T getItemRecursive(Node current, int index) {
        if (index == 0) {
            return current.item;
        } else {
            return getItemRecursive(current.next, index - 1);
        }
    }

    /** Returns an iterator of the deque. */
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node current;

        public LinkedListDequeIterator() {
            current = sentinel.next;
        }

        public boolean hasNext() {
            if (isEmpty()) {
                return false;
            }
            return current != null;
        }

        public T next() {
            T item = current.item;
            current = current.next;
            return item;
        }
    }

    @Override
    /** Returns whether or not the parameter o is equal to the deque. */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Deque) {
            LinkedListDeque<T> other = (LinkedListDeque<T>) o;
            if (size != other.size) {
                return false;
            }
            for (int i = 0; i < size; i += 1) {
                if (get(i) != other.get(i)) {
                    return false;
                }
            }
        }
        return false;
    }
}
