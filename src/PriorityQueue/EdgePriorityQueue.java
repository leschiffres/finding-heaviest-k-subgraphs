package PriorityQueue;

import Graph.Edge;

/*It is used by the FileManager to sort the edges*/

public class EdgePriorityQueue {
    
    private Edge[] heap;
    
    // Size of heap
    private int size = 0;
    
    public EdgePriorityQueue(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException();
        this.heap = new Edge[capacity+1];
        this.size = 0;
    }
    
    
    public void insert(Edge e) {
    	if ( isFull() ){
    		doubleHeap();
    	}
        if (e == null) throw new IllegalArgumentException();
        if (size == heap.length-1) throw new IllegalStateException();
        heap[++size] = e;
        swim(size);
    }
    public Edge remove() {
        if (size == 0) throw new IllegalStateException();
        Edge e = heap[1];
        if (size > 1) heap[1] = heap[size];
        heap[size--] = null;
        sink(1);
        return e;
    }
    
    private void swim(int i) {
        while (i > 1) {
            int p = i/2;
            int result = heap[i].compareTo(heap[p]); 
            if (result <= 0) return;
            swap(i, p);                 
            i = p;
        }
    }
    
    
    private void sink(int i){
        int left = 2*i, right = left+1, min = left;
        while (left <= size) {
            if (right <= size) {
                min = heap[left].compareTo(heap[right]) < 0 ? right : left;
            }
            if (heap[i].compareTo(heap[min]) >= 0) return;
            swap(i, min);
            i = min; left = 2*i; right = left+1; min = left;
        }
    }
    
    private void swap(int i, int j) {
        Edge tmp = heap[i];
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
    
    public void doubleHeap(){
    	Edge [] temp = new Edge [size*2];
    	for (int i = 1; i <= size; i++){
    		temp[i] = heap[i];
    	}
    	heap = temp;
    }
}




















