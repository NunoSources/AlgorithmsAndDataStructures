import java.util.Iterator;
import java.util.List;

public class BinZZTree<Key extends Comparable<Key>, Value> implements ZZTree <Key, Value> {

    
	private Node root;
	
	
    //************************************************************************

    // node data type
    private class Node {
        private Key key;            // key
        private Value value;        // associated data
        private Node left;          // left and right subtrees
        private Node right;

        public Node(Key key, Value value) {
            this.key   = key;
            this.value = value;
        }
        
        // @ref https://stackoverflow.com/questions/4965335
        // @requires source code saved in UTF-8 encoding
        @Override
        public String toString() {
            return this.toString(new StringBuilder(), true, new StringBuilder()).toString();
        }

        private StringBuilder toString(StringBuilder prefix, boolean isTail, StringBuilder sb) {
            if(right!=null) {
                right.toString(new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
            }
            sb.append(prefix).append(isTail ? "└── " : "┌── ").append(value.toString()).append("\n");
            if(left!=null) {
                left.toString(new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
            }
            return sb;
        }

    }

    //************************************************************************
   
    // default constructor builds an empty tree
    public BinZZTree() {}  
    
    // @requires keys.length == values.length
    public BinZZTree(List<Key> keys, List<Value> values) {
    	
    	Iterator<Key>   ks = keys  .iterator();
    	Iterator<Value> vs = values.iterator();
    	
    	while (ks.hasNext() && vs.hasNext())
    		
    		put(ks.next(), vs.next());
    }
    
    //************************************************************************

    /**
     * @worst-case O(1)
     * 
     * Method that checks if tree is empty
     * 
     * @return true iff the tree is empty
     */
	public boolean isEmpty(){
		return this.root == null;
	}
	
    //************************************************************************
	
	/**
	 * @worst-case O(n)
	 * 
	 * Method that checks if the key exists
	 * 
	 * @param key The key to search
	 * @return true iff the given key exists
 
	 */
	public boolean contains(Key key) {
		if( root == null ){
			return false;
		}
		if(key.compareTo(root.key) == 0){
			return true;
		}
		else{
			return contains(key, this.root);
		}
	}
	
	/**
	 * @worst-case O(n)
	 * 
	 * Auxiliary method that looks for the key
	 * 
	 * @param key true iff the given key exists
	 * @param rootAUX BinZZTree
	 * @return True iff the given key exists in the BinZZTree
	 */
	private  boolean contains (Key key, BinZZTree<Key, Value>.Node rootAUX){
		if(key.compareTo(rootAUX.key) < 0 ){
			if(rootAUX.left != key && rootAUX.left!=null){
				contains(key,rootAUX.left);
			}
			else{
				return true;
			}
		}
		else {
			if( rootAUX.right != key && rootAUX.right != null ){
				contains(key,rootAUX.right);
			}
			else{
				return true;
			}
		}
		return false;
	}

    //************************************************************************

	/**
	 * @worst-case O(1)
	 * 
	 * Method that returns the key at tree's root
	 * 
	 * @requires !isEmpty()
     * @return The key at tree's root
	 */
	public Key rootKey(){
		return root.key;
	}
	
    //************************************************************************

	/**
	 * @worst-case O(n)
	 * Method that returns the height of the tree
	 * 
	 * @return The height of the tree
	 */
	public int height(){ 
		return height (root);
	}

	/**
	 * Auxiliary method that returns the height of the tree
	 * 
	 * @param rootAUX BinZZTree
	 * @return The height of the tree
	 */
	private int height(BinZZTree<Key, Value>.Node rootAUX){
		return rootAUX == null? 0:1 + Math.max(height(( rootAUX.left )),height( rootAUX.right ));
	}
    
	
	/**
	 * @worst-caseO(n)
	 * 
	 * Method that returns the size of the tree
	 * 
	 * @return The size of the tree
	 */
	public int size(){
		return size(root);
	}

	/**
	 * Auxiliary method that returns the height of the tree
	 * 
	 * @param rootAUX BinZZTree
	 * @return The size of the tree
	 */
	private int size(BinZZTree<Key, Value>.Node rootAUX){
		if( rootAUX == null ) {
			return 0;
		}
		else {
			return size( rootAUX.left ) + 1 +  size( rootAUX.right );
		}
	}
    
    //************************************************************************

	/**
	 * 
	 * @worst-case O(n)
	 * 
	 * Method that returns the value associated with the given key
	 * 
	 * @param key The key to search
	 * @return The value associated with the given key, 
	 *         otherwise (if key does not exist) null
	 */
    public Value get(Key key){
        return get(root, key);
    }

    /**
     * Auxiliary method that returns the value associated with the given key
     * 
     * @param nodeAUX node of the tree
     * @param key key to search
     * @return The value associated with the given key, 
	 *         otherwise (if key does not exist) null
     */
    private Value get(Node nodeAUX, Key key){
        if (nodeAUX == null){
        	return null;
        }
        int keyCompare = key.compareTo(nodeAUX.key);
        if (keyCompare < 0){
        	return get( nodeAUX.left, key);
        }
        else if (keyCompare > 0){
        	return get(nodeAUX.right, key);
        }
        else {
        	return nodeAUX.value;
        }
    }   

    //************************************************************************

    /**
     * @worst-case O(n)
     * 
     * Insert (or replace) a pair <key,value>
     * 
     * @param key The key to add/replace
     * @param value The new value associated with the key
     */
    public void put(Key key, Value val) {
        if (key == null){
        	return ;
        }
        if (val == null){
            remove(key);
            return ;
        }
        root = put(root, key, val);
    }

    /**
     * Insert (or replace) a pair <key,value>
     * 
     * @param nodeB
     * @param key The key to add/replace
     * @param val The new value associated with the key
     * @return returns the node where the changes were made
     */
    private Node put(Node nodeB, Key key, Value val){
        Node noAUX = new Node (key,val);
    	this.root = bubble(noAUX);
        if (nodeB == null) {
        	return new Node(key, val);
        }
        this.root=bubble(root);
        int cmp = key.compareTo(nodeB.key);
        if(cmp < 0){
        	nodeB.left  = put(nodeB.left,  key, val);
        }
        else if (cmp > 0){
        	nodeB.right = put(nodeB.right, key, val);
        }
        else{
        	nodeB.value = val;
		}
        return nodeB;
    }
    
    //************************************************************************

    /**
     * @worst-case O(n)
     * 
     * Method that makes the bubble up
     * 
     * @param nodeB
     * @return
     * Metodo não acabado, decidimos deixar em comentário o que tentámos fazer
     */
    private BinZZTree<Key, Value>.Node bubble(Node nodeB) {
		return nodeB;

//    	this.pilha.add(x);
//    	BinZZTree arvoreAUX = new BinZZTree();
//    	arvoreAUX.root =  (Node) pilha.pop();
//    	
//    	while(!pilha.isEmpty()) {
//    		arvoreAUX.put(null,pilha.pop());
//    	}
//		return arvoreAUX.root;
	}

    /**
     * @worst-case O(n)
     * 
     * Method that removes a given key from tree (if key does not exist, do nothing)
     * @param key The key to remove
     */
	public void remove(Key key) {
        if (key == null){
        	return;
        }
        root = remove(root, key);
    }

	/**
	 * Auxiliary methos that removes a given key from a node (if node does not exist, do nothing)
	 * 
	 * @param nodeToDo Node were the changes are made
	 * @param key The key to remove
	 * @return node with the changes 
	 */
    private Node remove(Node nodeToDo, Key key){
        if ( nodeToDo == null ){
        	return null;
        }
        int keyCompare = key.compareTo(nodeToDo.key);
        if (keyCompare < 0) {
        	nodeToDo.left  = remove(nodeToDo.left,  key);
        }
        else if (keyCompare > 0){
        	nodeToDo.right = remove(nodeToDo.right, key);
        }
        else { 
            if (nodeToDo.right == null){
            	return nodeToDo.left;
            }
            if (nodeToDo.left  == null){
            	return nodeToDo.right;
            }
            Node aux = nodeToDo;
            nodeToDo = min(aux.right);
            nodeToDo.right = deleteMin(aux.right);
            nodeToDo.left = aux.left;
        } 
        return nodeToDo;
    } 
    
    /**
     * @worst-case O(n)
     * 
     * Method that returns the minimum key
     * 
     * @return Returns the mininum key 
     */
    public Key min(){
        if (isEmpty()){
        	return null;
        }
        return min(root).key;
    } 

    /**
     * Auxiliary method that returns the node which contains the minimum key
     * @param nodeAUX Node to check
     * @return Node which contains the minimum key
     */
    private Node min(Node nodeAUX){ 
        if ( nodeAUX.left == null ){
        	return nodeAUX; 
        }
        else{            
        	return min(nodeAUX.left); 
        }
    } 
    
    /**
     * @worst-case O(n)
     * 
     * Method that removes the node with less value
     */
    public void deleteMin(){
        if ( isEmpty() ){ 
        	return ;
        }
        root = deleteMin(root);
    }

    /**
     * Auxiliary method that removes the node with less value ( key and value ) in the tree
     * 
     * @param nodeAUX Node with less value in the tree
     * @return Node with the smallest values in the tree
     */
    private Node deleteMin(Node nodeAUX){
        if ( nodeAUX.left == null ) {
        	return nodeAUX.right;
        }
        nodeAUX.left = deleteMin(nodeAUX.left);
        return nodeAUX;
    }

    /**
     * @worst-case O(n)
     * 
     * Removes the node with the largest key and associated value in the tree
     */
    public void deleteMax(){
        if (isEmpty()) {
        	return ;
        }
        root = deleteMax(root);
    }

    /**
     * Auxiliary method that removes the node with the largest key and associated value in the tree
     * 
     * @param nodeAUX 
     * @return Node with the highest values in the tree
     */
    private Node deleteMax(Node nodeAUX){
        if(nodeAUX.right == null){
        	return nodeAUX.left;
        }
        nodeAUX.right = deleteMax(nodeAUX.right);
        return nodeAUX;
    }


    //************************************************************************

    /**
     * Method that prints the node
     * 
     * O(1)
     */
    public String toString(){
    	return root.toString();
    }
    
    //************************************************************************
    
    
    
    /**
     * @worst-case O(n)
     * 
     * A ZZTree must be a binary search tree
     * @return true iff invariant is uphold
     */
    public boolean keepsInvariant() {
        if( !isBST() ){     
        	return false;
        }
        if(!isSize()){
        	return false;
        }
		return false;
    }

    /**
     * Auxiliary method that ensures that data structure is a binary tree
     * 
     * @return True if is a binary tree, false if is not
     */
    private boolean isBST() {
        return isBST(root, null, null);
    }

    /**
     * Auxiliary method that ensures that data structure is a binary tree 
     * 	with all keys strictly between min and max
 
     * @param node Root node
     * @param min lower key value
     * @param max higher key value
     * 
     * @return
     */
    private boolean isBST(Node node, Key min, Key max) {
        if(node == null){
        	return true;
        }
        if (max != null && node.key.compareTo(max) >= 0){
        	return false;
        }
        if (min != null && node.key.compareTo(min) <= 0){
        	return false;
        }
        return isBST(node.left, min, node.key) && isBST(node.right, node.key, max);
    } 
    
    private boolean isSize(){
    	return isSizeAUX(root); 
    }
    
    private boolean isSizeAUX(Node x){
        if (x == null){
        	return true;
        }
		return false;
    } 
    
}