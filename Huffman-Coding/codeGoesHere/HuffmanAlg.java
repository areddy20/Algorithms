//***************************************************************
// TODO: This is mostly done for you. See the "YOUR CODE HERE" spots below.
//***************************************************************


import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Tree;

import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.*;

/**
 *  This algorithm performs the steps to create a Huffman encoding
 *  build from a set of characters.
 *  
 *  @author Katherine (Raven) Russell and Aditi Reddy and Akshay Vuppalapati
 */
class HuffmanAlg implements FourEightyThreeForestAlg<HuffmanTreeNode,TreeEdge> {
    //*********************************************************************
    //*******                                                       *******
    //*******   You should not need to edit anything in this part.  *******
    //*******   However, you may need to read everything here.      *******
    //*******                                                       *******
    //*********************************************************************
    
    /**
     *  The forest of huffman trees.
     */
    Forest<HuffmanTreeNode, TreeEdge> forest;
    
    /**
     *  Whether or not the algorithm has been started.
     */
    private boolean started = false;
    
    /**
     *  The possible modes the algorithm can be in.
     */
    private enum Mode { COUNTING, BUILDING, ENCODING, DONE };
    
    /**
     *  The algorithm's current mode.
     */
    private Mode mode = null;
    
    /**
     *  Current character (if in counting mode);
     */
    private Character currentChar;
    
    /**
     *  {@inheritDoc}
     */
    public EdgeType TreeEdgeType() {
        return EdgeType.UNDIRECTED;
    }
    
    /**
     *  Sets the current character being examined.
     *  
     *  @param currentChar the new character
     */
    public void setCurrentChar(Character currentChar) {
        this.currentChar = currentChar;
    }
    
    /**
     *  {@inheritDoc}
     */
    public boolean isStarted() {
        return started;
    }
    
    /**
     *  {@inheritDoc}
     */
    public void start() {
        this.started = true;
    }
    
    /**
     *  {@inheritDoc}
     */
    public void finish() {
        // Unused. Required by the interface.
    }
    
    /**
     *  {@inheritDoc}
     */
    public void cleanUpLastStep() {
        // Unused. Required by the interface.
    }
    
    /**
     *  {@inheritDoc}
     */
    public boolean setupNextStep() {
        //we're counting, but out of characters
        if(this.mode == Mode.COUNTING && currentChar == null) {
            //start building
            this.mode = Mode.BUILDING;
        }
        
        //we're down to one tree, so stop building
        if(this.mode == Mode.BUILDING && this.forest.getTrees().size() <= 1) {
            //start encoding
            this.mode = Mode.ENCODING;
        }
        //you only get one step to do the encoding
        else if(this.mode == Mode.ENCODING) {
            //finished!
            this.mode = Mode.DONE;
        }
        
        return (this.mode != Mode.DONE);
    }
    
    //*********************************************************************
    //*******                                                       *******
    //*******    You have a number of things to do in this part.    *******
    //*******                                                       *******
    //*********************************************************************
    
    //YOUR CODE HERE
    //Any instance variables you'd find useful could go here!

    /**
    * Variable for a priority queue for building a tree based off the priorities of the character frequencies.
    */
    public PriorityQueue<HuffmanTreeNode> buildingTree = new PriorityQueue<>(Comparator.comparingInt(HuffmanTreeNode::getCount));

    /**
     * Variable for keeping track of characters that have already been encountered.
     */
    public Map<Character, HuffmanTreeNode> charCount = new HashMap<>();
    
    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void reset(Forest<HuffmanTreeNode, TreeEdge> forest) {
        this.forest = forest;
        this.started = false;
        this.mode = Mode.COUNTING;
        this.currentChar = null;
        
        /**
         * Resetting both variables
         */

        //Initializing HashMap
        this.charCount = new HashMap<>();
        //Initializing buildingTree
        this.buildingTree = new PriorityQueue<>(Comparator.comparingInt(HuffmanTreeNode::getCount));

    }
    
    /**
     *  {@inheritDoc}
     */
    public void doNextStep() {
        
        // This is the COUNTING step
        if (this.mode == Mode.COUNTING) {
            
            // Before reset when there is a letter
            if (currentChar != null) {
                //Creating a node
                HuffmanTreeNode node1 = charCount.get(currentChar);
                //Condition
                if (node1 != null){
                    
                    //Getting frequency of letter
                    node1.setCount(node1.getCount() + 1);
                    
                    //Displaing on simulator

                    //Removing
                    buildingTree.remove(node1);
                    
                    //Adding
                    buildingTree.add(node1);
                    
                }
                
                else {
                    
                    // When reset or when letter isn't there
                    HuffmanTreeNode nnoode = new HuffmanTreeNode(currentChar, 1);
                    
                    forest.addVertex(nnoode);
                    
                    charCount.put(currentChar, nnoode);
                    
                    buildingTree.add(nnoode);
                
                }

            }

        }
        
        // this is the BUILDING step
        else if (this.mode == Mode.BUILDING){
            
            // When queue is priority queue
            if ((buildingTree.isEmpty())){
                    //Adding all the values of Character to the priority queue.
                    buildingTree.addAll(charCount.values());
                }
                
                // Adding nodes to queue
                if (buildingTree.size() > 1){
                    
                    // leaf nodes
                    HuffmanTreeNode noder = buildingTree.poll();
                    
                    HuffmanTreeNode noder2 = buildingTree.poll();
                    
                    // leaf nodes sum for parent value
                    int nanna = noder.getCount() + noder2.getCount();
                    
                    //  parent node with sum as value
                    HuffmanTreeNode guardian = new HuffmanTreeNode(null, nanna);
                    
                    //Adding parent to thr forest of trees.
                    forest.addVertex(guardian);
                    
                    //Adding the edge with the parent and node.
                    forest.addEdge(new TreeEdge(), guardian, noder);
                    //Adding the edge with the parent and node.
                    forest.addEdge(new TreeEdge(), guardian, noder2);
                    //Adding the parent to the priority queue.  
                    buildingTree.add(guardian);

                }
            
                else {
                    // Clearing queue
                    buildingTree.clear();
                
                }
            
            }

            // this is the ENCODING step
            else if (this.mode == Mode.ENCODING) {
                //Calling the helper function made below. 
                encode(forest.getTrees().iterator().next().getRoot(), "");
                // End process so now in done mode
                this.mode = Mode.DONE;

            }

        }
            

        // Helper method for getting edges values for left and right
        public void encode(HuffmanTreeNode node, String encoding) {

        //when node has children
        if (node.getLetter() == null) {
            //Creating an Iterator
            Iterator <HuffmanTreeNode> nodeWithChildren; 
            nodeWithChildren = forest.getChildren(node).iterator();
            //Checking if the 
            if (nodeWithChildren.hasNext() == true) {
                //Left
                HuffmanTreeNode L = nodeWithChildren.next();
                //Calling encode function.
                encode(L, encoding + "0");

            }
            //Boolean Condition
            if (nodeWithChildren.hasNext() == true) {
                //Right
                HuffmanTreeNode R = nodeWithChildren.next();
                //Calling encode function.
                encode(R, encoding + "1");

            }

        }
        //Else code.
        else {

            //node is leaf/childless
            node.setEncoding(encoding);

        }

    }

}