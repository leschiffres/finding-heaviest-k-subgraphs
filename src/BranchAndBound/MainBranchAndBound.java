package BranchAndBound;

import FileManager.FileManager;
import Graph.*;

public class MainBranchAndBound {
	
	public static void main(String[] args) {
		
		if(args.length == 0 ){
			System.out.println("No arguments were given. Proper command :");
			System.out.println("java MainPackage/MainBranchAndBound graphFile.txt kappa iterations lowerBound");
		}
		else{
			System.out.println("Branch And Bound");
			
			for(int i = 0; i < args.length; i++){
				System.out.print(args[i] + " ");
			}
			String file = args[0];
			int kapa = Integer.parseInt(args[1]);
			int lambda = kapa*(kapa-1)/2;
			int iterations = Integer.parseInt(args[2]);
			int lowerBound = Integer.parseInt(args[3]);
			
			
			long startTime = System.currentTimeMillis();
			FileManager.loadGraph(file);
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			double fileTime = (double) elapsedTime / 1000;
			System.out.println("Time for storing graph from file : " + fileTime + " seconds\n");

			
			System.out.println("Started ordering the edges");
			GraphManager.sortEdges();
			System.out.println("Finished ordering the edges.");
			
			System.out.println("Branch and Bound started");
			long startAlgorithm = System.currentTimeMillis();
			

			NodeBnB n = BranchAndBound.bbHeap(kapa, lambda, iterations, lowerBound);
			n.print();
			startAlgorithm = System.currentTimeMillis() - startAlgorithm;
			
			System.out.println("Initial time : " + (double) NodeBnB.initialTime/1000 + " seconds");
			System.out.println("One time : " + (double) NodeBnB.oneTime/1000 + " seconds");
			System.out.println("Zero time : " + (double) NodeBnB.zeroTime/1000 + " seconds");
			System.out.println("Upper Bound : " + (double) NodeBnB.upperBoundTime/1000 + " seconds");
			System.out.println("Updating lists time : " + (double)NodeBnB.inducedVerticesTime/1000 + " seconds");
			System.out.println("Queue Time : " + (double)BranchAndBound.queueTime/1000 + " seconds");
			System.out.println("\nAlgorithm Total time : " + (double) startAlgorithm / 1000 + " seconds");

		}
	}
}