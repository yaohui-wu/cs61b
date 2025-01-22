package bstmap;

import java.util.Set;
import java.util.HashSet;
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

    @Override
    /** Removes all of the mappings from this map. */
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        return search(root, key) != null;
    }

    @Override
    /** Returns the value to which the specified key is mapped, or null if
     *  this map contains no mapping for the key.
     */
    public V get(K key) {
        if (root == null) {
            return null;
        }
        Node node = search(root, key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    /** Searches the node that has the given key using recursion. Returns the
     *  node if it exists, otherwise returns null.
     */
    private Node search(Node root, K key) {
        if (root == null) {
            return null;
        }
        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            // Key is smaller, searches the left subtree.
            return search(root.left, key);
        } else if (cmp > 0) {
            // Key is larger, searches the right subtree.
            return search(root.right, key);
        } else {
            // Key is equal, returns the current node.
            return root;
        }
    }

    @Override
    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    @Override
    /** Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        if (!containsKey(key)) {
            size += 1;
        }
        root = insert(root, key, value);
    }

    /** Inserts a node with the given key value pair into the BSTMap using
     *  recursion and returns the root of the BST after the insertion.
     */
    private Node insert(Node root, K key, V value) {
        if (root == null) {
            root = new Node(key, value);
            return root;
        }
        /*
         * Compares the key to the current node's key to determine the
         * direction.
         */
        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            // Key is smaller, inserts it into the left subtree.
            root.left = insert(root.left, key, value);
        } else if (cmp > 0) {
            // Key is larger, inserts it into the right subtree.
            root.right = insert(root.right, key, value);
        } else {
            // Key already exists, updates the value associated with it.
            root.value = value;
        }
        // Returns the root of the BST after the insertion.
        return root;
    }

    @Override
    /** Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        HashSet<K> keySet = new HashSet<>();
        addKeys(root, keySet);
        return keySet;
    }

    private void addKeys(Node root, Set<K> keySet) {
        if (root == null) {
            return;
        }
        K key = root.key;
        if (!keySet.contains(key)) {
            keySet.add(key);
        }
        addKeys(root.left, keySet);
        addKeys(root.right, keySet);
    }

    @Override
    /** Removes the mapping for the specified key from this map if present. */
    public V remove(K key) {
        if (containsKey(key)) {
            V value = get(key);
            root = delete(root, key);
            size -= 1;
            return value;
        }
        return null;
    }

    @Override
    /** Removes the entry for the specified key only if it is currently mapped to
     *  the specified value.
     */
    public V remove(K key, V value) {
        if (containsKey(key)) {
            V keyValue = get(key);
            if (value.equals(keyValue)) {
                return remove(key);
            }
        }
        return null;
    }

    /** Deletes a node with the given key using recursion if it exists and
     *  returns the root of the BST after the deletion.
     */
    private Node delete(Node root, K key) {
        if (root == null) {
            return null;
        }
        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            root.left = delete(root.left, key);
        } else if (cmp > 0) {
            root.right = delete(root.right, key);
        } else {
            // Node has either zero or one child.
            /*
             * Replaces the node with its child if it exists, otherwise return
             * null if it has no children.
             */
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            // Node has two children.
            /* 
             * Finds the largest node in the left subtree (in-order
             * predecessor).
             */
            Node temp = root;
            root = max(root.left);
            // Deletes the in-order predecessor from the left subtree.
            root.left = deleteMax(temp.left);
            // Reattaches the right subtree to the new root.
            root.right = temp.right;
        }
        // Returns the updated root.
        return root;
    }

    /** Returns the largest node in the BST. */
    private Node max(Node root) {
        if (root == null) {
            return null;
        }
        // Traverses to the rightmost node in the subtree.
        if (root.right == null) {
            // The rightmost node is the largest.
            return root;
        }
        return max(root.right);
    }

    /** Deletes the largest node in the BST and returns the root of the BST
     *  after the deletion.
     */
    private Node deleteMax(Node root) {
        if (root.right == null) {
            return root.left;
        }
        root.right = deleteMax(root.right);
        return root;
    }

    /** Returns an iterator over the keys of the BSTMap. */
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
    
    /** Prints the BSTMap in order of increasing key. */
    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node root) {
        if (root == null) {
            return;
        }
        printInOrder(root.left);
        System.out.println(root.key + ": " + root.value);
        printInOrder(root.right);
    }
}
