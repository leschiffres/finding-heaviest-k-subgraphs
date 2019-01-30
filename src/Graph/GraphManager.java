package Graph;

import java.util.ArrayList;

import PriorityQueue.EdgePriorityQueue;
/*In this class we store the whole graph so that different classes
 that want to have access to the graph, look up to this class.
 */
public class GraphManager {
	// Contains the vertices of the graph
	private static Vertex[] vertices;
	// The edges of the graph
	private static ArrayList<Edge> edges;

	public static void sortEdges() {
		EdgePriorityQueue eq = new EdgePriorityQueue(edges.size());
		while (edges.size() > 0) {
			eq.insert(edges.remove(edges.size()-1));
		}
		System.out.println("PQ Finished");
		Edge e = null;

		System.out.println("Sorting list started");
		int i = 0;
		while (!eq.empty()) {
			e = eq.remove();
			edges.add(e);
			e.setOrder(i);
			i++;
		}
	}

	public static void printEdges(int M) {
		for (int i = 0; i < M; i++) {
			System.out.println(edges.get(i));
		}
	}

	public static void setVertices(Vertex[] v) {
		vertices = v;
	}

	// Returns a vertex with a specific index
	public static Vertex getVertex(int index) {
		return vertices[index];
	}

	// Returns the total number of vertices
	public static int getNumberOfVertices() {
		return vertices.length;
	}

	public static Edge getEdge(int i) {
		return edges.get(i);
	}

	public static void setEdges(ArrayList<Edge> newEdges) {
		edges = newEdges;
	}

	public static int getTotalEdges() {
		return edges.size();
	}
}
