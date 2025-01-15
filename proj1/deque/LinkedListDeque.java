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

    public LinkedListDeque() {
        sentinel = new Node(null);
        sentinel.previous = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node current = new Node(item);
        Node first = sentinel.next;
        first.previous = current;
        current.next = first;
        current.previous = sentinel;
        sentinel.next = current;
        size++;
    }

    public void addLast(T item) {
        Node current = new Node(item);
        Node last = sentinel.previous;
        last.next = current;
        current.previous = last;
        current.next = sentinel;
        sentinel.previous = current;
        size++;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }
    
    public void printDeque() {
        Node current = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(current.item + " ");
            current = current.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size <= 0) { return null; }
        Node current = sentinel.next;
        Node first = current.next;
        first.previous = sentinel;
        sentinel.next = first;
        size--;
        return current.item;
    }

    public T removeLast() {
        if (size <= 0) { return null; }
        Node current = sentinel.previous;
        Node last = current.previous;
        last.next = sentinel;
        sentinel.previous = last;
        size--;
        return current.item;
    }
}
