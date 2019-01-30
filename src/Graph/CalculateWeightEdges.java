package Graph;

import java.util.ArrayList;

import FileManager.FileManager;

/*This class contains to methods that given the list of vertices, they return the
 * number of total induced edges and total induced weight.*/

public class CalculateWeightEdges {
	public static double inducedWeight(ArrayList<Vertex> myList){
		ArrayList<Vertex> neigh;
		double totalWeight = 0;
		for (int i = 0; i < myList.size(); i++) {
			neigh = myList.get(i).getNeighbourVertices();
			for (int j = 0; j < neigh.size(); j++) {
				if (myList.contains(neigh.get(j))) {
					totalWeight += myList.get(i).getWeight(j);
				}
			}
		}
		return totalWeight/2;
	}
	
	public static int inducedEdges(ArrayList<Vertex> myList) {
		ArrayList<Vertex> neigh;
		int total = 0;
		for (int i = 0; i < myList.size(); i++) {
			neigh = myList.get(i).getNeighbourVertices();
			for (int j = 0; j < neigh.size(); j++) {
				if (myList.contains(neigh.get(j))) {
					total++;
				}
			}
		}
		return total/2;
	}
	
}
