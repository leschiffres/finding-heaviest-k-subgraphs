package BranchAndBound;

import java.util.ArrayList;

import Graph.CalculateWeightEdges;
import Graph.Edge;
import Graph.GraphManager;
import Graph.Vertex;

public class NodeBnB implements Comparable<NodeBnB> {

	/* The weight of the solution. */
	private double weight;

	/*
	 * The upperBound is computed directly in the corresponding method. It might
	 * be -1 in case we have pruned a node for the case we might delete an edge
	 * whose both endpoints are already in the list.
	 * 
	 * Suppose you have the list of vertices (u,v,w) (because you added firstly
	 * (u,v), (v,w)) if you delete (u,w) then there is no point considering this
	 * node since it is exactly the same as the one where you add the same edge.
	 * Therefore you can skip it
	 */
	private double upperBound;

	/* This list contains the vertices of the subgraph */
	private ArrayList<Vertex> vertices;

	/*
	 * When applying the BnB method for every possible edge we made two
	 * branches. Either we remove, or add the edge into the subgraph. The
	 * following variables contains the number of edges we removed (numberZeros)
	 * and the number of edges we added (numberOnes) to reach this code
	 */
	private int numberZeros;
	private int numberOnes;

	/*
	 * A boolean variable that indicates whether this node does the same
	 * computation with another branch in the tree, and therefore it should be
	 * pruned
	 */
	private boolean doubleFlag;

	/*
	 * How many only zero nodes we have created : the number of BnB nodes that
	 * were created, without having any edge.
	 */

	public static int zeroNodes = 0;

	/* These variables serve only for timing purposes. */

	public static long initialTime;
	public static long inducedVerticesTime;
	public static long oneTime;
	public static long zeroTime;
	public static long upperBoundTime;

	/*
	 * Initial Constructor:
	 * 
	 * It is used only for the creation of the first node
	 */
	public NodeBnB(String s) {
		vertices = new ArrayList<Vertex>();
		weight = 0;
		upperBound = 0;
		numberZeros = 0;
		numberOnes = 0;
		doubleFlag = false;
		for (int i = 0; i < BranchAndBound.maxEdges; i++) {
			upperBound += GraphManager.getEdge(i).getWeight();
		}
		System.out.println("Upper Bound for initial Node : " + upperBound);
	}

	/*
	 * Every time we create a new node in the tree we need its parent, and the
	 * decision "1" or "0" for the corresponding edge.
	 */
	public NodeBnB(NodeBnB n, String newChar) {
		long startOneTime = 0, startZeroTime = 0, startInitialTime = 0, startInducedVerticesTime = 0;
		long endOneTime = 0, endZeroTime = 0, endInitialTime = 0, endInducedVerticesTime = 0;

		startInitialTime = System.currentTimeMillis();
		this.numberZeros = n.numberZeros;
		this.numberOnes = n.numberOnes;
		this.weight = n.weight;
		this.upperBound = 0;
		doubleFlag = false;
		endInitialTime = System.currentTimeMillis();
		initialTime += endInitialTime - startInitialTime;

		startInducedVerticesTime = System.currentTimeMillis();

		/*
		 * We need to copy the following lists because if we just use a pointer
		 * like this.vertices = n.vertices then both nodes will point in the
		 * list and therefore there will be synchronization problems. Thus we
		 * need to create a whole new list with the same elements.
		 */
		vertices = new ArrayList<Vertex>();
		for (Vertex u : n.vertices) {
			vertices.add(u);
		}

		endInducedVerticesTime = System.currentTimeMillis();
		inducedVerticesTime += endInducedVerticesTime
				- startInducedVerticesTime;

		if (newChar.equals("1")) {
			startOneTime = System.currentTimeMillis();
			numberOnes++;
			Edge edge = GraphManager.getEdge(numberZeros + numberOnes - 1);

			/*
			 * Whenever we add a new edge in the solution we need to check if
			 * its endpoints are already part of the list. If they are not then
			 * we need to add this vertex in the list and every edge that is
			 * part of the subgraph induced by adding this vertex.
			 */

			Vertex u = edge.getVertex1();
			int index = -1;
			if (!vertices.contains(u)) {

				for (Vertex in : vertices) {
					// Find vertex "in", inside the neighbourhood of node u
					index = u.indexOfNeighbour(in);
					/*
					 * If u is linked with in then their edge has an order. If
					 * the order of the new edge added is greater than the edge
					 * between u and "in", then we should prune this solution.
					 * 
					 * The order is initially set to 0 by the constructor, and
					 * then we use the GraphManager to give value. For example
					 * if we use the decreasing order according to the weight of
					 * these edges then, the order of an edge it will be the
					 * time it was deleted. Here the order is set by the
					 * GraphManager.sortEdges() function.
					 * 
					 * Where do we set the order of the edges in the Vertex
					 * class? We use a method addEdge() which is added when
					 * parsing the file.
					 */
					if (index > -1) {
						if (edge.getOrder() > u.getOrder(index)) {
							doubleFlag = true;
							break;
						}
						weight += u.getWeight(index);
					}
				}
				vertices.add(u);

			}

			u = edge.getVertex2();
			index = -1;

			if (!doubleFlag && !vertices.contains(u)) {
				for (Vertex in : vertices) {
					index = u.indexOfNeighbour(in);
					if (index > -1) {

						if (edge.getOrder() > u.getOrder(index)) {
							doubleFlag = true;
							break;
						}
						weight += u.getWeight(index);
					}
				}
				vertices.add(u);
			}

			endOneTime = System.currentTimeMillis();
			oneTime += endOneTime - startOneTime;
		}

		else if (newChar.equals("0")) {

			startZeroTime = System.currentTimeMillis();
			numberZeros++;

			Edge edge = GraphManager.getEdge(numberZeros + numberOnes - 1);

			Vertex u = edge.getVertex1();
			Vertex v = edge.getVertex2();

			/*
			 * In case we delete an edge that was already computed in the weight
			 * of this solution, there is no point considering this node,
			 * therefore it should be pruned. Actually there is entirely the
			 * same vertex somewhere else.
			 */

			if (vertices.contains(u) && vertices.contains(v)) {
				weight = -1;
				upperBound = -1;
			}
			endZeroTime = System.currentTimeMillis();
			zeroTime += endZeroTime - startZeroTime;
		}

		// Calculation of upper Bound

		long upperBoundStartTime = System.currentTimeMillis();
		if (upperBound > -1) {
			upperBound = weight;
			int maxEdges = binom(BranchAndBound.maxVertices) - binom(vertices.size());
			for (int i = numberOnes + numberZeros; i < numberOnes + numberZeros + maxEdges; i++) {
				upperBound += GraphManager.getEdge(i).getWeight();
			}
		}
		long upperBoundEndTime = System.currentTimeMillis();
		upperBoundTime += upperBoundEndTime - upperBoundStartTime;
	}

	public int getInducedEdges() {
		return numberOnes;
	}

	public int getZeros() {
		return numberZeros;
	}

	public int getStringLength() {
		return numberOnes + numberZeros;
	}

	public double getWeight() {
		return weight;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public boolean isDouble() {
		return doubleFlag;
	}

	private int binom(int i) {
		return i * (i - 1) / 2;
	}

	public int getInducedVertices() {
		return vertices.size();
	}

	public ArrayList<Vertex> getVertices() {
		return vertices;
	}

	public String toString() {
		String s = "Vertices : ";
		for (Vertex v : vertices) {
			s += " " + v;
		}
		return s;
	}

	public void print() {
		/*System.out.println("Number of zeros : " + numberZeros);
		System.out.println("Number of ones : " + numberOnes);*/
		System.out.print("Vertices : ");
		for (Vertex v : vertices) {
			System.out.print("\t" + v);
		}
		System.out.println("\n Node Weight : " + weight);
		System.out.println("Total Number of edges : " + CalculateWeightEdges.inducedEdges(vertices));
	}

	@Override
	public int compareTo(NodeBnB n) {
		if (this.upperBound > n.upperBound)
			return 1;
		else if (this.upperBound == n.upperBound)
			return 0;
		else
			return -1;
	}

}
