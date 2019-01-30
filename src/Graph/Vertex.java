package Graph;
import java.util.ArrayList;

import MyMethods.Sort;


public class Vertex implements Comparable<Vertex> {
	// The id of the vertex
	private int ID;
	// The number of neighbours of the vertex. It is equivalent with adjacentVertices.list()
	private int degree;
	private ArrayList<Vertex> adjacentVertices;
	private ArrayList<Double> edgeWeights;
	// This variable is used just to sort the edges of the whole graph to apply BnB Technique.
	// Also We use it to give wcd order on the edges.
	private ArrayList<Edge> edges;
	
	// This is a variable used by wcd, so that we give a proper ordering on the edges
	private static int orderingIndex = 0;
	
	/*used for weighted core decomposition*/
	/* Whenever we want to find disjoint subgraphs using wcd, we need initialContribution to
	 * reinitialize the contribution of the remaining vertices. Suppose you did a wcd then every
	 * vertex contribution is 0. The contribution of the vertices you don't want to consider in 
	 * the new wcd stays the same. And for the rest you reinitialize with the initial contribution.
	 * (Which is not entirely correct since you might consider edges between vertices outside the graph
	 * and inside, therefore you should carefully set contribution.) */
	private double initialContribution;
	private double contribution;
	/*the sum of best k edges. To initialize it we need to run method sortEdges()*/
	private double bestEdges;
	
	public Vertex(int ID){
		this.ID = ID;
		bestEdges = 0;
		degree = 0;
		initialContribution = 0;
		adjacentVertices = new ArrayList<Vertex>();
		edgeWeights = new ArrayList<Double>();
		edges = new ArrayList<Edge>();
		contribution = 0;
	}
	public int getID(){
		return ID;
	}
	public void setID(int ID){
		this.ID = ID;
	}
	
	public void addEdge(Edge e){
		edges.add(e);
	}
	
	public void addNeighbour(Vertex v, double weight){
		adjacentVertices.add(v);
		
		edgeWeights.add(weight);
		degree++;
		contribution += weight;
		initialContribution = contribution;
	}
	public int getDegree(){
		return degree;
	}
	
	/* Remove edge tries to remove an edge(u,v) from the graph. Suppose the method 
	 * was called from vertex u that is deleted from the graph. Then we should remove
	 * - Vertex v from its adjacency list as long as its weight.
	 * - Edge(u,v) from its edge list
	 * - Himself from the vertex's s adjacency list
	 * - Edge(u,v) from v's edge list*/
	public Edge removeEdge(Vertex v){
		// Remove vertex from vertex list
				
		// First remove THIS from the vertex list of v.
		int i = v.adjacentVertices.indexOf(this);
		v.degree--;
		v.contribution -= v.edgeWeights.get(i);
		v.edgeWeights.remove(i);
		v.adjacentVertices.remove(i);
		
		i = adjacentVertices.indexOf(v);
		degree--;
		contribution -= edgeWeights.get(i);
		initialContribution -=edgeWeights.get(i);
		edgeWeights.remove(i);
		adjacentVertices.remove(i);
		
		// Remove edge from edge list
		Edge rem = null;
		for(Edge e : edges){
			if(e.isIncident(v) && e.isIncident(this)){
				rem = e;
				break;
			}
		}
		v.edges.remove(rem);
		edges.remove(rem);
		
		return rem;
	}
	
	public void resetContribution(){
		contribution = initialContribution;
	}
	
	public void reduceContribution(Vertex v){
		int i = adjacentVertices.indexOf(v);
		contribution = contribution - edgeWeights.get(i);
	}
	public void updateContribution(){
		for(Edge e: edges){
			if(e.getOrder() == 0){
				orderingIndex++;
				e.setOrder(orderingIndex);
				e.getVertex1().reduceContribution(e.getVertex2());
				e.getVertex2().reduceContribution(e.getVertex1());
			}
		}
	}
	
	public boolean isNeighbour(Vertex v){
		boolean flag = false;
		for(Vertex u : adjacentVertices){
			if(u.ID == v.ID){
				flag = true;
				break;
			}
		}
		return flag;
	}
	public void printNeighbours(){
		System.out.println("Vertex " + this.ID);
		for(Vertex v:adjacentVertices){
			System.out.print(v.ID + " ");
		}
		System.out.println();
	}
	public ArrayList<Vertex> getNeighbourVertices(){
		return adjacentVertices;
	}
	public ArrayList<Double> getEdgeWeights(){
		return edgeWeights;
	}
	
	public ArrayList<Edge> getEdgeList(){
		return edges;
	}
	public double getWeight(int i){
		return edgeWeights.get(i);
	}
	public double getInitialContribution(){
		return initialContribution;
	}
	public double getContribution(){
		return contribution;
	}
	public void setContribution(double contribution){
		this.contribution = contribution;
	}
	public int compareTo(Vertex v) {
		if( this.contribution < v.contribution ) 
			return 1;
		else if ( this.contribution == v.contribution )
			return 0;
		else
			return -1;
	}
	public int getOrder(int index){
		return edges.get(index).getOrder();
	}
	public String toString(){
		return ""+ID;
	}
	public double getBestEdgesWeight(){
		return bestEdges;
	}
	/* k : How many edges you want counted in the bestEdges*/
	public void sortEdges(int k){
		Sort.method(adjacentVertices, edgeWeights);
		for(int i = 0; i < k; i++){
			if(adjacentVertices.size() > i){
				bestEdges += edgeWeights.get(i);
			}
		}
	}
	public void printEdges(int M){
		System.out.println("Vertex : " + ID + " sum of best edges : " + bestEdges);
		for(int i = 0; i < M && i < adjacentVertices.size(); i++){
			System.out.println(ID + " " + adjacentVertices.get(i) + " : " + edgeWeights.get(i));
		}
	}
	public void print(){
		System.out.println("Vertex " + ID );
		if(adjacentVertices.size() > 0){
			for(int i = 0; i < adjacentVertices.size(); i++){
				System.out.println(adjacentVertices.get(i) + " " + edgeWeights.get(i));
			}
		}
		else{
			System.out.println("No neighbours");
		}
	}

	public int indexOfNeighbour(Vertex v){
		return adjacentVertices.indexOf(v);
	}
	public void setWeight(int index, double w){
		edgeWeights.set(index, w);
	}
}
