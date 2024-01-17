//***************************************************************
// TODO: Nothing, all done. You may read this if you'd like,
// but you may not need to.
//***************************************************************


import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 *  Interface for forest algorithms in the simulator.
 *  
 *  @author Katherine (Raven) Russell
 */
interface FourEightyThreeForestAlg<V extends TreeNode,E extends TreeEdge> {
	/**
	 *  Indicates the edge type required by the algotithm.
	 *  
	 *  @return the edge type expected by the algorithm
	 */
	public EdgeType TreeEdgeType();
	
	/**
	 *  Indicates if the algorithm has been started.
	 *  
	 *  @return true if the algorithm has been started
	 */
	public boolean isStarted();
	
	/**
	 *  What to do before the simulator begins.
	 */
	public void start();
	
	/**
	 *  What to do after a step to "clean up".
	 */
	public void cleanUpLastStep();
	
	/**
	 *  What to do before a step to "get started".
	 *  
	 *  @return true if there was another step setup
	 */
	public boolean setupNextStep();
	
	/**
	 *  What to actually do during a step.
	 */
	public void doNextStep();
	
	/**
	 *  What to do when the simulator is stepped.
	 *  
	 *  @return whether or not there are more steps.
	 */
	public default boolean step() {
		if(!isStarted()) {
			start();
			//return true;
		}
		
		cleanUpLastStep();
		if(!setupNextStep()) {
			finish();
			return false;
		}
		doNextStep();
		
		return true;
	}
	
	/**
	 *  What to do after the simulator finishes all steps.
	 */
	public void finish();
	
	/**
	 *  Resets the algorithm for a new forest.
	 *  
	 *  @param forest the new forest
	 */
	public void reset(Forest<V,E> forest);
}