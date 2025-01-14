package deque;

/**
 * @author Yaohui Wu
 */
public class LinkedListDeque<T> {

    public static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }

        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }

        public T getData() { return data; }
    }


    private Node<T> head;
    private int size;

    public LinkedListDeque() {
        head = null;
        size = 0;
    }

    public LinkedListDeque(T data) {
        head = new Node<>(data);
        size = 1;
    }
}
