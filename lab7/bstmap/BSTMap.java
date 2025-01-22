package bstmap;

import java.util.Set;
import java.util.Iterator;

/** Map based on a binary search tree (BST).
 *  @author Yaohui Wu
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node root; // Root of the BST.
    private int size; // Numbers of nodes in the BST.

    /** BST node with a key value pair. */
    private class Node {
        private K key; // Sorts by key.
        private V value; // Associated data.
        private Node left; // Root of left subtree.
        private Node right; // Root of right subtree.
        
        public Node(K newKey, V newValue) {
            key = newKey;
            value = newValue;
            left = null;
            right = null;
        }
    }

    /** Initializes an empty BST map. */
    public BSTMap() {
        root = null;
        size = 0;
    }
    
    /** Removes all of the mappings from this map. */
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        return search(root, key) != null;
    }

    /** Returns the value to which the specified key is mapped, or null if
     *  this map contains no mapping for the key.
     */
    public V get(K key) {
        if (root == null) {
            return null;
        }
        return search(root, key).value;
    }

    /** Searches the node that has the given key using recursion. Returns the
     *  node if it exists, otherwise returns null.
     */
    private Node search(Node root, K key) {
        if (root == null) {
            return null;
        }
        int order = key.compareTo(root.key);
        if (order < 0) {
            // If the key is smaller, search the left subtree.
            return search(root.left, key);
        } else if (order > 0) {
            // If the key is larger, search the right subtree.
            return search(root.right, key);
        } else {
            // If the key is equal, return the current node.
            return root;
        }
    }

    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /** Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        root = insert(root, key, value);
    }

    /** Inserts a node with the given key value pair into the BSTMap using
     *  recursion and returns the root of the BST.
     */
    private Node insert(Node root, K key, V value) {
        if (root == null) {
            root = new Node(key, value);
            size += 1;
            return root;
        }
        int order = key.compareTo(root.key);
        if (order < 0) {
            // If the key is smaller, insert it into the left subtree.
            root.left = insert(root.left, key, value);
        } else if (order > 0) {
            // If the key is larger, insert it into the right subtree.
            root.right = insert(root.right, key, value);
        } else {
            // If the key already exists, update the value associated with it.
            root.value = value;
        }
        // Return the root of the BST after the insertion.
        return root;
    }

    /** Returns a Set view of the keys contained in this map. Not required for
     *  Lab 7.
     *  If you don't implement this, throw an UnsupportedOperationException.
     */
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    /** Removes the mapping for the specified key from this map if present.
     *  Not required for Lab 7. If you don't implement this, throw an
     *  UnsupportedOperationException.
     */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /** Removes the entry for the specified key only if it is currently mapped to
     *  the specified value. Not required for Lab 7. If you don't implement this,
     *  throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    /** Prints the BSTMap in order of increasing key. */
    public void printInOrder() {
        throw new UnsupportedOperationException();
    }
}
