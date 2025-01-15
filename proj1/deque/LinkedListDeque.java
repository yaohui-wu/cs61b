package deque;

/** Implementation of a deque (double-ended queue) using a circular doubly
 *  linked list with a sentinel node.
 *  @author Yaohui Wu
 */
public class LinkedListDeque<T> {

    public class Node {
        private T item;
        private Node previous;
        private Node next;

        public Node(T item) {
            this.item = item;
            previous = null;
            next = null;
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
        sentinel = new Node(null);
        sentinel.previous = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /** Adds an item of type T to the front of the deque in constant time. */
    public void addFirst(T item) {
        Node current = new Node(item); // New first node.
        Node first = sentinel.next; // Old first node.
        first.previous = current;
        current.next = first;
        current.previous = sentinel;
        sentinel.next = current;
        size++;
    }

    /** Adds an item of type T to the back of the deque in constant time. */
    public void addLast(T item) {
        Node current = new Node(item); // New last node.
        Node last = sentinel.previous; // Old last node.
        last.next = current;
        current.previous = last;
        current.next = sentinel;
        sentinel.previous = current;
        size++;
    }

    /** Returns true if the deque is empty, false otherwise. */
    public boolean isEmpty() { return size == 0; }

    /** Returns the number of items in the deque in constant time. */
    public int size() { return size; }
    
    /** Prints all the items in the deque from first to last, separated by a
     *  space, then prints out a new line.
     */
    public void printDeque() {
        Node current = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(current.item + " ");
            current = current.next;
        }
        System.out.println();
    }

    /** Removes and returns the item at the front of the deque. If no such
     *  item exists, returns null. */
    public T removeFirst() {
        if (size <= 0) { return null; }
        Node current = sentinel.next; // Old first node.
        Node first = current.next; // New first node.
        first.previous = sentinel;
        sentinel.next = first;
        size--;
        return current.item;
    }

    /** Removes and returns the item at the back of the deque. If no such item
     *  exists, returns null. */
    public T removeLast() {
        if (size <= 0) { return null; }
        Node current = sentinel.previous; // Old last node.
        Node last = current.previous; // New last node.
        last.next = sentinel;
        sentinel.previous = last;
        size--;
        return current.item;
    }

    /**
     * Gets the item at the given index using iteration, where 0 is the front,
     * 1 is the next item, and so forth. If no such item exists, returns null.
     */
    public T get(int index) {
        if (index >= size) { return null; }
        Node current = sentinel.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.item;
    }

    /**
     * Gets the item at the given index using recursion, where 0 is the front,
     * 1 is the next item, and so forth. If no such item exists, returns null.
     */
    public T getRecursive(int index) {
        if (index >= size) { return null; }
        Node current = sentinel.next;
        if (index == 0) {
            return current.item;
        } else {
            current = current.next;
            getRecursive(index - 1);
        }
    }
}
