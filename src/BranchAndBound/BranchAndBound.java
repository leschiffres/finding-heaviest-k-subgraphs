package BranchAndBound;


import Graph.*;
import PriorityQueue.BnBNodesPriorityQueue;

import java.util.LinkedList;

public class BranchAndBound {
	
	// bestLowerBound contains the highest weight among the subgraphs found
	public static double bestLowerBound;
	private static NodeBnB bestSolution;
	static int maxVertices;
	static int maxEdges;
	
	public static long queueTime = 0;
	
	public static double timeLasted = 0;
	
	public static NodeBnB bbHeap(int k, int l, int iterations, double lowerBound){
		long time = System.currentTimeMillis();
		maxVertices = k;
		maxEdges = l;
		/*Checkpoint 1 : Initializing list*/
		NodeBnB n = new NodeBnB("");
		NodeBnB largestString = n;
		BnBNodesPriorityQueue nodes = new BnBNodesPriorityQueue(GraphManager.getTotalEdges());
		
		/*Checkpoint 2 : adding the first node*/
		nodes.insert(n);

		bestLowerBound = lowerBound;
		double smallerUpperBound = n.getUpperBound();
		bestSolution = n;
		String s = "";
		int iter = 0;
		int feasibleNodes = 0;
		/*int notfeasible = 0, prunedDueToUpperBound = 0, doubleComputed = 0;
		int prunedDueToVertices = 0;*/
		long qt = 0;//queue time
		/* The algorithm lasts for 6 hours*/
		while(!nodes.empty() && (iter < iterations || iterations == 0) && timeLasted < 3600*6){
			iter++;
			if(iter% 10000000 == 0){
				System.out.println("Time this far : " + timeLasted);
				System.out.println("Reached " + iter + " iteration");
				bestSolution.print();
				System.out.println("Lower Upper Bound : " + smallerUpperBound);
			}
			
			if(largestString.getStringLength() < n.getStringLength()){
				largestString  = n;
			}
			
			/*Checkpoint 3 : Removing a node from the list*/
			qt = System.currentTimeMillis();
			n = nodes.remove();
			qt = System.currentTimeMillis() - qt;
			queueTime +=qt;
			/*If the solution is valid and this subgraph was not previously examined in the tree*/
			if(!n.isDouble() && n.getInducedVertices() <= k && n.getInducedEdges() <= l){
				/*Updating upper bound in order to see which is the smaller lower bound*/
				if(n.getUpperBound() < smallerUpperBound && n.getUpperBound() > -1){
					smallerUpperBound = n.getUpperBound();
				}
				
				/*It prunes the branch if the bestLowerBound is greater or equal than the upperBound*/
				if(n.getUpperBound() >   bestLowerBound){
					
					feasibleNodes++;
					if(n.getWeight() > bestLowerBound){
						bestLowerBound = n.getWeight();
						bestSolution = n;
					}
					if(n.getInducedVertices() == k){
						break;
					}
					/* The purpose of the first part of the if statement is that If we have reached the max 
					 * number of permitted of nodes and edges there is no point continuing adding its children
					 * the second one ensures that we have not reached the final edge of the table*/
					if( !( n.getInducedEdges() == maxEdges && n.getInducedVertices() == maxVertices) && s.length() <= GraphManager.getTotalEdges() - 1){
						NodeBnB n1 = new NodeBnB(n, "1");
						NodeBnB n0 = new NodeBnB(n, "0");
						/*Checkpoint 4 : Adding a node in the queue*/
						qt = System.currentTimeMillis();
						nodes.insert(n1);
						nodes.insert(n0);
						qt = System.currentTimeMillis() - qt;
						queueTime +=qt;
					}
				}
				else{
					//prunedDueToUpperBound++;
				}
			}
			else{
				if(n.getInducedEdges() > l){
					//notfeasible++;
					
				}
				else if(n.getInducedVertices() > k){
					//prunedDueToVertices++;
				}
				else {
					//doubleComputed++;
				}
			}
			timeLasted +=  (double) (System.currentTimeMillis() - time)/1000;
			time = System.currentTimeMillis();
		}
		
		/*System.out.println("Largest Node length : " + largestString.getStringLength());
		System.out.println("Largest Node Zeros : " + largestString.getZeros());
		System.out.println("Largest Node ones : " + largestString.getInducedEdges());
		*/
		System.out.println("Best Solution Weight : " + bestSolution.getWeight()+ "\n");
		System.out.println("Actual Weight : " + CalculateWeightEdges.inducedWeight(bestSolution.getVertices()));
		
		System.out.println("Lower Upper Bound : " + smallerUpperBound);
		
		/*System.out.println("Double computed nodes : " + doubleComputed);
		System.out.println("Branches Pruned Due To Weight: " + prunedDueToUpperBound);
		System.out.println("Branched Pruned Due to Vertices: " + prunedDueToVertices);
		System.out.println("Not feasible nodes parsed : " + notfeasible);*/
		System.out.println("Feasible Nodes : " + feasibleNodes + "\n");
		
		System.out.println("Total Number of Iterations : " + iter );
		return bestSolution;
	}
	
	
}
