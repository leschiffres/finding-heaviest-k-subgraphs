package PriorityQueue;

import BranchAndBound.BranchAndBound;
import BranchAndBound.MainBranchAndBound;
import BranchAndBound.NodeBnB;

public class BnBNodesPriorityQueue {

	// A heap containing vertices
    private NodeBnB[] heap;
    
    // Size of heap
    private int size = 0;
    
    public BnBNodesPriorityQueue(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException();
        this.heap = new NodeBnB[capacity+1];
        this.size = 0;
    }
    
    
    public NodeBnB getFirst(){
    	return heap[1];
    }
    
    /**Inserts a new vertex in the heap. If the heap is full then it doubles the space
     * of the array by calling the method insert.*/
    public void insert(NodeBnB n) {
        // If heap is full double space.
    	if ( isFull() ){
    		doubleHeap();
    	}
    	// Ensure object is not null
        if (n == null) throw new IllegalArgumentException();
        // Check available space
        if (size == heap.length-1) throw new IllegalStateException();
        // Place object at the next available position
        heap[++size] = n;
        // Let the newly added object swim
        swim(size);
    }
    /**
     * Removes the object at the root of this heap.
     * throws IllegalStateException if heap is empty.
     * return The Vertex removed.
     */
  /*  public NodeBnB remove() {
        // Ensure not empty
        if (size == 0) throw new IllegalStateException();
        // Keep a reference to the root object
        NodeBnB n = heap[1];
        // Replace root object with the one at rightmost leaf
        if (size > 1) heap[1] = heap[size];
        // Dispose the rightmost leaf
        heap[size--] = null;
        // Sink the new root element
        sink(1);
        // Return the object removed
        return n;
    }*/
    
    public NodeBnB remove() {
        // Ensure not empty
        if (size == 0) throw new IllegalStateException();
        // Keep a reference to the root object
        NodeBnB n = heap[1];
        // Replace root object with the one at rightmost leaf
        NodeBnB bad = null;
        while (size > 1) {
        	bad = heap[size];
        	if(bad.getUpperBound() <= BranchAndBound.bestLowerBound){
        		heap[size] = null;
        		size--;
        	}
        	else{
        		break;
        	}
        }
        if(size > 0 ){
        	heap[1] = heap[size];
        }
        else{
        	System.out.println("Why are you making my life miserable");
        }
        // Dispose the rightmost leaf
        heap[size--] = null;
        // Sink the new root element
        sink(1);
        // Return the object removed
        return n;
    }
    
    /* When inserting a new element in the priority queue you, place it in the last
     * position, and then you update the array with  swim. We want that every node
     * has lower value than its children. So by starting on a certain position we 
     * compare the values of the current node and its father and we update accordingly
     * until we find the right spot of the element.*/
    private void swim(int i) {
        while (i > 1) {  //if i root (i==1) return
            int p = i/2;  //find parent
            int result = heap[i].compareTo(heap[p]);
            if (result <= 0) return;    //if child <= parent return
            swap(i, p);                 //else swap and i=p
            i = p;
        }
    }
    
    
    /* Similar as swim whereas now we start from the root and we look the child with 
     * lower value and we update accordingly (if one of its children has lower
     * value then the node goes down children goes up in the tree).*/
    private void sink(int i){
        int left = 2*i, right = left+1, min = left;
        // If 2*i >= size, node i is a leaf
        while (left <= size) {
            // Determine the largest children of node i
            if (right <= size) {
            	min = heap[left].compareTo(heap[right]) < 0 ? right : left;
            }
            if (heap[i].compareTo(heap[min]) >= 0) return;
            swap(i, min);
            i = min; left = 2*i; right = left+1; min = left;
        }
    }
    
    /**
     * Interchanges two array elements.
     */
    private void swap(int i, int j) {
        NodeBnB tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }
    
    public boolean empty(){
        return size == 0;
    }
    
    public boolean isFull(){
    	return size == heap.length-1;
    }
    public int getSize(){
    	return size;
    }
    /*Doubles the size of the array in case we insert a new element but
     * the heap is full.*/
    public void doubleHeap(){
    	NodeBnB [] temp = new NodeBnB [size*2];
    	for (int i = 1; i <= size; i++){
    		temp[i] = heap[i];
    	}
    	heap = temp;
    }
}
