package MyMethods;

import java.util.ArrayList;

import Graph.Vertex;

public class Sort {
	private static ArrayList<Vertex> adjacentVertices;
	private static ArrayList<Double> edgeWeights;
	/*Sorts the array from position a[p] till position a[r]
	 * for p = 0 and r = a.length - 1 it sorts the whole array*/
	public static void method(ArrayList<Vertex> adjVertices,ArrayList<Double> edWeights){
		adjacentVertices = adjVertices;
		edgeWeights = edWeights;
		quicksort(0, edgeWeights.size()-1);
	}
	public static void quicksort( int p, int r){ 
		
		if (r <= p) return;
		int i = partition(p, r);
		quicksort(p, i-1);
		quicksort(i+1, r);
	}
	private static int partition(int p, int r){ 
		int i = p-1, j = r; 
		double v = edgeWeights.get(r);
		for (;;){ 
			while (great(edgeWeights.get(++i), v)) ;
			while (great(v, edgeWeights.get(--j)))
				if (j == p) break;
			if (i >= j) break;
			exch(i, j);
		}
		exch(i, r);
		return i; 
	}
	private static  boolean great(double a, double b){
		if(a > b) return true;
		else return false;
	}
	private static void exch(int i, int j){
		double temp = edgeWeights.get(i);
		edgeWeights.set(i, edgeWeights.get(j));
		edgeWeights.set(j, temp);
		Vertex v = adjacentVertices.get(i);
		adjacentVertices.set(i, adjacentVertices.get(j));
		adjacentVertices.set(j, v);
	}	
	
}
