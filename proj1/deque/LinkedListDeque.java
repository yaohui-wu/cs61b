package deque;

/** Implementation of a deque (double-ended queue) using a circular doubly
 *  linked list with a sentinel node.
 *  @author Yaohui Wu
 */
public class LinkedListDeque<T> {

    public class Node {
        private T item;
        private Node next;
        private Node previous;

        public Node(T item) {
            this.item = item;
            next = null;
            previous = null;
        }
    }


    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null);
        sentinel.next = sentinel;
        sentinel.previous = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node first = new Node(item);
        Node current = sentinel.next;
        current.previous = first;
        first.next = current;
        first.previous = sentinel;
        sentinel.next = first;
        size++;
    }

    public void addLast(T item) {
        Node last = new Node(item);
        Node current = sentinel.previous;
        current.next = last;
        last.previous = current;
        last.next = sentinel;
        sentinel.previous = last;
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
        size--;
        return null;
    }

    public T removeLast() {
        if (size <= 0) { return null; }
        size--;
        return null;
    }
}
