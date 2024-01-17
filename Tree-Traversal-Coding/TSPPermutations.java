//***************************************************************
// TODO: This is mostly done for you. See the "YOUR CODE HERE" spots below.
//***************************************************************

import edu.uci.ics.jung.graph.Graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 *  This algorithm attempts to solve the TSP problem by
 *  checking all possible travel plans and choosing the
 *  smallest one found.
 *  
 *  @author Katherine (Raven) Russell and Aditi Reddy and Akshay Vuppalapati
 */
class TSPPermutations extends TSPAlg {
	//*********************************************************************
	//*******                                                       *******
	//*******   You should not need to edit anything in this part.  *******
	//*******   However, you may need to read everything here.      *******
	//*******                                                       *******
	//*********************************************************************
	
	/**
	 *  A iterator over all permutations of cities (except
	 *  the first city which is "locked in").
	 */
	Iterator<List<City>> permutationItr;
	
	/**
	 *  The current permutation being examined by the algorithm.
	 */
	List<City> currentPermutation = null;
	
	/**
	 *  {@inheritDoc}
	 */
	public void reset(Graph<City, Flight> graph) {
		super.reset(graph);
		permutationItr = null;
		currentPermutation = null;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void start() {
		super.start();
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void finish() {
		System.out.println("TSP Via Permutation:");
		if(visitOrder != null) {
			this.visitOrder.add(0, this.startingCity);
		}
		super.finish();
	}
	
	//*********************************************************************
	//*******                                                       *******
	//*******    You have a number of things to do in this part.    *******
	//*******                                                       *******
	//*********************************************************************
	
	/**
	 *  Get the next permutation. Note that a permutation does
	 *  NOT include the starting city since this should be "locked
	 *  in place". If you include the starting city in the permuations
	 *  you end up examining "rotations" of trips you have already
	 *  looked at.
	 *  
	 *  @return the next permutation of cities (other than the starting city)
	 */
	public List<City> permutate() {
		//Checking if it is null
		if(permutationItr == null){
			//ArrayList to go thorugh the cities
            List<City> wrapAround = new ArrayList<>(graph.getVertices());
			//Removing the starting city
            wrapAround.remove(startingCity);
			//Creating Iterator
            permutationItr = new PermutationIterator<>(wrapAround);
        }
		//Checking if there is a next city to iterate through
        if(permutationItr.hasNext()){
			//Changing to next
			currentPermutation = permutationItr.next();
			//Returning the updated currentPermutation
        	return currentPermutation;
        }
		//Return null
		return null;
		
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public boolean setupNextStep() {
		//Calling permutate method made earlier
		List<City> iterNexting = permutate();
		//Null checker
        if(iterNexting != null){
            return true;
        }
		//Return false
        return false;
	}
	//Helper to go through and highlight the edges
	private void goingEdges() {
		//Null checker
        if (currentPermutation == null || currentPermutation.isEmpty()) {
            return;
        }
		//Going through the current permutation
        for (int i = 1; i < currentPermutation.size() - 1; i++) {
			//Creating variable for starting and next cities
            City now = currentPermutation.get(i);
            City corresponding = currentPermutation.get(i + 1);
			//Getting the edge
            Flight edge = graph.findEdge(now, corresponding);
			//Edge null checker
            if (edge != null) {
                edge.setColor(TSPAlg.COLOR_ACTIVE_EDGE);
            }
        }
		//Creating variable for final city.
        City finalC = currentPermutation.get(currentPermutation.size() - 1);
		//Finding corresponding edge.
        Flight alterEdge = graph.findEdge(finalC, startingCity);
        if (alterEdge != null) {
			//Setting the color
            alterEdge.setColor(TSPAlg.COLOR_ACTIVE_EDGE);
        }
    }
	/**
	 *  {@inheritDoc}
	 */
	public void doNextStep() {
		//variable for total amount of cost
		int total_Money = 0;
		//Boolean check variable
		boolean valid = true;
		//First meeting of the city
		Flight firstMeet = graph.findEdge(this.startingCity, currentPermutation.get(0));
		//Null Checker
		if (firstMeet == null) {
			valid = false;
		} 
		//Adding cost to total money.
		else {
			total_Money += firstMeet.getCost();
		}
		//Previous city
		City prev = currentPermutation.get(0);
		//Iterating through the current permutation
		for (int i = 1; i < currentPermutation.size() && valid; i++) {
			//Current City
			City curr = currentPermutation.get(i);
			//Finding the edge we are currently in.
			Flight loop = graph.findEdge(prev, curr);
			//Loop null checker
			if (loop != null) {
				//Adding to the total money
				total_Money += loop.getCost();
			} else {
				valid = false;
			}
			//Setting the prevous city to the current one
			prev = curr;
		}
		if (valid) {
			//Finding the edge we are currently in.
			Flight looping = graph.findEdge(prev, this.startingCity);
			//Loop null checker
			if (looping != null) {
				//Adding to the total money
				total_Money += looping.getCost();
			} else {
				//Setting boolean check to false
				valid = false;
			}
		}
		//Condition if the boolean check and total money is less than visitOrderCost
		if (valid && total_Money < visitOrderCost) {
			//Setting it equal to total_Money
			visitOrderCost = total_Money;
			//Setting it to currentPermutation.
			visitOrder = currentPermutation;
			
		}
		//Calling the helper
		goingEdges();
	}
	

}