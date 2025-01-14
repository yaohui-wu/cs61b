package deque;

/**
 * @author Yaohui Wu
 */
public class LinkedListDeque<T> {

    public static class Node<T> {
        private T item;
        private Node<T> next;

        public Node(T item) {
            this.item = item;
            this.next = null;
        }

        public Node(T item, Node<T> next) {
            this.item = item;
            this.next = next;
        }

        public T getitem() { return item; }
    }


    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedListDeque() {
        head = null;
        tail = null;
        size = 0;
    }

    public LinkedListDeque(T item) {
        head = new Node<>(item);
        tail = head;
        size = 1;
    }

    public void addFirst(T item) {
        Node<T> first = new Node<>(item, head);
        head = first;
        size++;
    }

    public void addLast(T item) {
        Node<T> last = new Node<>(item);
        tail.next = last;
        tail = last;
        size++;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }
    
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            Node<T> current = head;
            System.out.print(current.item);
            current = current.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size <= 0) { return null; }
        T firstItem = head.item;
        head = head.next;
        return firstItem;
    }

    public T removeLast() {
        if (size <= 0) { return null; }
        T lastItem = tail.item;
        return lastItem;
    }
}
