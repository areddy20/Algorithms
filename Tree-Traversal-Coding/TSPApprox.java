//***************************************************************
// TODO: This is mostly done for you. See the "YOUR CODE HERE" spots below.
//***************************************************************

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.algorithms.shortestpath.MinimumSpanningForest;

import java.awt.Color;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 *  This algorithm attempts to solve the TSP problem by
 *  forming a MST and then doing a DFT to choose a flight
 *  order.
 *  
 *  @author Katherine (Raven) Russell and Aditi Reddy and Akshay Vuppalapati
 */
class TSPApprox extends TSPAlg {
	//*********************************************************************
	//*******                                                       *******
	//*******   You should not need to edit anything in this part.  *******
	//*******   However, you may need to read everything here.      *******
	//*******                                                       *******
	//*********************************************************************
	
	/**
	 *  The step number for doing a MST.
	 */
	private static final int STEP_MST = 1;
	
	/**
	 *  The step number for starting the DFT.
	 */
	private static final int STEP_DFT = 2;
	
	/**
	 *  The step number for finishing.
	 *  NOTE: this one gets changed!
	 */
	private int stepDone = 3;
	
	/**
	 *  The current step.
	 */
	private int step = 0;
	
	/**
	 *  The MST found with Prim's algorithm.
	 */
	Forest<City,Flight> tree = null;
	
	/**
	 *  {@inheritDoc}
	 */
	public void reset(Graph<City, Flight> graph) {
		super.reset(graph);
		
		//reset steps
		step = 0;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void start() {
		super.start();
		
		//calculate how many steps of the DFT
		//will be displayed.
		stepDone = STEP_DFT + graph.getVertexCount();
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void finish() {
		System.out.println("TSP Via Approximation:");
		super.finish();
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public boolean setupNextStep() {
		step++;
		return (step != stepDone);
	}
	
	/**
	 *  Helper method to color all edges of a tree.
	 *  
	 *  @param tree the tree to highlight on the graph
	 *  @param c the color to use
	 */
	protected void colorAllEdges(Forest<City,Flight> tree, Color c) {
		for(City c1: tree.getVertices()) {
			for(City c2: tree.getVertices()) {
				if(tree.isNeighbor(c1, c2)) {
					Flight f = graph.findEdge(c1, c2);
					f.setColor(c);
				}
			}
		}
	}
	
	/**
	 *  Helper method to print out one step of the DFT at a time
	 *  to make the simulation more interesting.
	 */
	protected void stepDisplayDFT() {
		colorAllEdges(COLOR_DONE_EDGE_2);
		colorAllEdges(tree, COLOR_ACTIVE_EDGE);
		
		for(int i = 0; i < step-STEP_DFT; i++) {
			Flight f = null;
			
			if(i == stepDone-1) {
				f = graph.findEdge(visitOrder.get(i), this.startingCity);
			}
			else {
				f = graph.findEdge(visitOrder.get(i), visitOrder.get(i+1));
			}
			
			f.setColor(COLOR_DONE_EDGE_1);
		}
	}
	
	//*********************************************************************
	//*******                                                       *******
	//*******    You have a number of things to do in this part.    *******
	//*******                                                       *******
	//*********************************************************************
	
	/**
	 *  {@inheritDoc}`
	 */
	public void doNextStep() {

		// Checking if the current step is in MST
		if (step == STEP_MST) {

			// Making new min forest for storing the weights for edges
			Map<Flight, Double> ivan = new HashMap<>();
			
			// looping through all edges
			for (Flight f : graph.getEdges()) {

				// typecasting
				ivan.put(f, (double) f.getCost());
			
			}
			
			// Made for prim's alg
			MinimumSpanningForest<City, Flight> minSpan = new MinimumSpanningForest<>(graph, new DelegateForest<>(), startingCity, ivan);
			
			// Storing MST in tree variable
			tree = minSpan.getForest();
			
			// Setting edge colors
			colorAllEdges(tree, COLOR_ACTIVE_EDGE);
		
		// Checking if current step is in DFT
		} else if (step == STEP_DFT) {
			
			// New arraylist for storing cities in order
			List<City> depth = new ArrayList<>();
			
			// DFT alg is performing now
			depthFirstTraversal(tree, startingCity, depth);
			// Setting visitOrder to depth.
			visitOrder = depth;
			
			// Calculate the visit order cost
			visitOrderCost = Cost(visitOrder, graph);
		}

		// Checking if current step is at or more for DFS
		if (step >= STEP_DFT) {
			//Calling method.
			stepDisplayDFT();
		
		}

	}

	// Helper method for doNextStep
	private int Cost(List<City> visitOrder, Graph<City, Flight> graph) {
		// Creating count variable
		int count = 0;
		// Creating the start variable
		int start = 0;
		
		for (int i = start; i < visitOrder.size() - 1; i++) {
			// Finding the endges for the graph.
			Flight f1 = graph.findEdge(visitOrder.get(i), visitOrder.get(i + 1));
			// Null checker
			if (f1 != null) {
				// Adding the cost
				count += f1.getCost();
		
			}
		
		}
		// Add cost for returning to the starting city
		Flight f2 = graph.findEdge(visitOrder.get(visitOrder.size() - 1), startingCity);
		// Null checker
		if (f2 != null) {
			// Adding the cost
			count += f2.getCost();
		}
		// Returning the count
		return count;
	}
	
	// Method for Depth First Traversal
	static void depthFirstTraversal(Forest<City,Flight> treeToWalk, City currNode, List<City> dftVisitOrder) {
		// Null checker
		if (currNode == null) {
			//Empty return
			return;
		}
	
		// For pre order
		dftVisitOrder.add(currNode);
	
		// Iterating through all children
		for (City child : treeToWalk.getChildren(currNode)) {
			//Recursive
			depthFirstTraversal(treeToWalk, child, dftVisitOrder);
		
		}


		
	}
}