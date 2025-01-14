package deque;

/** Implementation of a deque (double-ended queue) using a doubly linked list
 *  with a sentinel node.
 *  @author Yaohui Wu
 */
public class LinkedListDeque<T> {

    public static class Node<T> {
        private T item;
        private Node<T> next;
        private Node<T> previous;

        public Node(T item) {
            this.item = item;
            this.next = null;
            this.previous = null;
        }

        public T getitem() { return item; }
    }


    private Node<T> sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = null;
        size = 0;
    }

    public void addFirst(T item) {
        Node<T> first = new Node<>(item);
        first.next = sentinel;
        sentinel = first;
        size++;
    }

    public void addLast(T item) {}

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }
    
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            Node<T> current = sentinel;
            System.out.print(current.item);
            current = current.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size <= 0) { return null; }
        T firstItem = sentinel.item;
        sentinel = sentinel.next;
        return firstItem;
    }

    public T removeLast() {
        if (size <= 0) { return null; }
    }
}
