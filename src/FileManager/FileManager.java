package FileManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Graph.Edge;
import Graph.GraphManager;
import Graph.Vertex;

public class FileManager {
	
	private static int numberOfEdges = 0;
	private static int maxID = 0;

	public static int getMaxID(String filename) {
		int id = 0;
		String line = "";
		BufferedReader br = null;
		StringTokenizer st;
		File f = null;
		int i = 0;

		try {
			f = new File(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
		} catch (FileNotFoundException e) {
			System.err.println("Error opening file here!");
		}

		while (line != null) {
			try {
				line = br.readLine();
				if (line != null) {
					numberOfEdges++;
					st = new StringTokenizer(line);
					i = Integer.parseInt(st.nextToken());
					if (i > id)
						id = i;
					i = Integer.parseInt(st.nextToken());
					if (i > id)
						id = i;

				}
			} catch (IOException e) {
				System.err.println("Error reading line .");
			}
		}

		try {
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		maxID = id;
		return maxID;
	}
	
	/*This method loads the graph by reading a file that has the form: 
	 *  VertexID1 VertexID2 weight
	 *  ....
	 *  ....
	 * 
	 * Thus, this file is consisted of several lines, where each of them is
	 * associated with a specific edge from the graph. Therefore the edge
	 * (a,b) with weight equal to 5, would be represented in the file as
	 * a b 5
	 * 
	 * Mainly, we store the graph in a matrix with all its vertices. Each vertex
	 * has a list with all its neighbours, along with the corresponding weights
	 * of all the edges with each neighbours. 
	 * 
	 * Initially we parse the whole file in order to find the vertex with the 
	 * maximum ID and then we initialize an empty matrix with the number of cells
	 * being equal to this maximum ID. Then we parse the file once again and we store
	 * the vertices accordingly.
	 * 
	 * In the case where the file does not contain a specific ID from 0 up to maxID
	 * we consider that that this vertex exists and is totally disconnected from the graph.
	 * 
	 * */
	public static void loadGraph(String filename){
		System.out.println("\nLoading Graph . . .");
		String line = "";
		BufferedReader br = null;
		StringTokenizer st;
		File f = null;
		int u = 0, v = 0, weight = 0;
		Edge newEdge = null ;
		int length = getMaxID(filename) + 1;
		Vertex[] vertices = new Vertex[length];
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = new Vertex(i);
		}
		
		try {
			f = new File(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
		} catch (FileNotFoundException e) {
			System.err.println("Error opening file for writing!");
		}

		ArrayList<Edge> edges = new ArrayList<Edge>();
		while (line != null) {
			try {
				line = br.readLine();
				if (line != null) {
					st = new StringTokenizer(line);
					u = Integer.parseInt(st.nextToken());
					v = Integer.parseInt(st.nextToken());
					
					weight = Integer.parseInt(st.nextToken());
					vertices[u].addNeighbour(vertices[v], weight);
					vertices[v].addNeighbour(vertices[u], weight);
					
					newEdge = new Edge(vertices[u],vertices[v], weight);
					edges.add(newEdge);
					vertices[u].addEdge(newEdge);
					vertices[v].addEdge(newEdge);
					
				}
			} catch (IOException e) {
				System.err.println("Error reading line .");
			}
		}

		try {
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Graph " + filename);
		System.out.println("Vertices : " + length);
		System.out.println("Edges : " + numberOfEdges);
		GraphManager.setEdges(edges);
		GraphManager.setVertices( vertices);
		System.out.println("Finished loading file " + filename);
	}
	
	
}
