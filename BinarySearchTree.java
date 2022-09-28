import java.util.*;

public class BinarySearchTree<E extends Comparable<E>> {

	public E largest(){

	}

	/** The root of the binary tree */
	public Node<E> root;

	public BinarySearchTree(Node<E> root) {
		this.root = root;
	}
	
	public BinarySearchTree() {
		this.root = null;
	}

	public BinarySearchTree(E[] elems) {
		this.root = null;
		for(E elem : elems)
			add(elem);
	}
	
	public boolean isEmpty() {
		return root == null;
	}

	// @ requires target != null;
	private boolean find(E target) {
		return find(root, target);
	}

	//@ requires target != null;
	private boolean find(Node<E> localRoot, E target) {
		if (localRoot == null)
			return false;
		// Compare the target with the data field at the root.
		int compResult = target.compareTo(localRoot.data);
		if (compResult == 0)
			return true;
		else if (compResult < 0)
			return find(localRoot.left, target);
		else
			return find(localRoot.right, target);
	}

	// @ requires target != null;
	public void add(E target) {
		root = add(root, target);
	}

	// @ requires target != null;
	private Node<E> add(Node<E> localRoot, E target) {
		if (localRoot == null) {
			// item is not in the tree ? insert it.
			return new Node<E>(target);
		} else if (target.compareTo(localRoot.data) == 0) {
			// item is equal to localRoot.data
			return localRoot;
		} else if (target.compareTo(localRoot.data) < 0) {
			// item is less than localRoot.data
			localRoot.left = add(localRoot.left, target);
			return localRoot;
		} else {
			// item is greater than localRoot.data
			localRoot.right = add(localRoot.right, target);
			return localRoot;
		}
	}

	// @ requires target != null;
	public void delete(E target) {
		root = delete(root, target);
	}

	// @ requires target != null;
	private Node<E> delete(Node<E> localRoot, E target) {
		if (localRoot == null) {
			// item is not in the tree.
			return localRoot;
		}
		// Search for item to delete.
		int compResult = target.compareTo(localRoot.data);
		if (compResult < 0) {
			// item is smaller than localRoot.data.
			localRoot.left = delete(localRoot.left, target);
			return localRoot;
		} else if (compResult > 0) {
			// item is larger than localRoot.data.
			localRoot.right = delete(localRoot.right, target);
			return localRoot;
		} else {
			// item is at local root.
			if (localRoot.left == null) {
				// If there is no left child, return right child
				// which can also be null.
				return localRoot.right;
			} else if (localRoot.right == null) {
				// If there is no right child, return left child.
				return localRoot.left;
			} else {
				// Node being deleted has 2 children, replace the data
				// with inorder predecessor.
				if (localRoot.left.right == null) {
					// The left child has no right child.
					// Replace the data with the data in the
					// left child.
					localRoot.data = localRoot.left.data;
					// Replace the left child with its left child.
					localRoot.left = localRoot.left.left;
					return localRoot;
				} else {
					// Search for the inorder predecessor (ip) and
					// replace deleted node?s data with ip.
					localRoot.data = findLargestChild(localRoot.left);
					return localRoot;
				}
			}
		}
	}
	
	/**
	 * Find the node that is the largest child and replace it with its left
	 * child (if any).
	 * 
	 * @param node
	 *            The parent of possible inorder predecessor (ip)
	 * @return The data in the node containing the largest child
	 */
	//@ requires (* The tree has a right subtree *);
	//@ ensures (* The inorder predecessor is removed from the tree *);
	private E findLargestChild(Node<E> node) {
		// If the right child has no right child, it is
		// the inorder predecessor.
		if (node.right.right == null) {
			E result = node.right.data;
			node.right = node.right.left;
			return result;
		} else {
			return findLargestChild(node.right);
		}
	}
	
	public String toString() {
		LinkedList<E> list = new LinkedList<>();

		if (!isEmpty()) {
			append(list, root);
			Collections.sort(list);
		}
		return list.toString();
	}

	private void append(LinkedList<E> list, Node<E> node) {
		if (node==null)
			return;
		
		list.addFirst(node.data);
		append(list, node.left);
		append(list, node.right);	
	}
	
	/**
	 * A class describing a tree node
	 */
	public static class Node<E extends Comparable<E>> {

		/** The information stored in this node. */
		public E data;

		/** Reference to the left child. */
		public Node<E> left;

		/** Reference to the right child. */
		public Node<E> right;

		/**
		 * Construct a node with given data and no children.
		 * @param data The data to store in this node
		 */
		public Node(E data) {
			this(data, null, null);
		}

		/**
		 * Construct a node with given data and two children.
		 * @param data  The data to store in this node
		 * @param left  The left child
		 * @param right The right child
		 */
		public Node(E data, Node<E> left, Node<E> right) {
			this.data = data;
			this.left = left;
			this.right = right;
		}
	}
}	