//***************************************************************
// TODO: You may want/need to read everything here. This shows
// how your code will be tested. You may edit this file, but
// the TAs will use a "vanilla copy" and your edits won't affect
// what is expected of your code. It's still a good idea to add
// more tests for yourself here or in the JUnit files.
//***************************************************************


import java.util.*;

import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Tree;

class MainMethodExampleTest {
	public static void main(String[] args) {
		//make an instance of the Huffman algorithm
		HuffmanAlg alg = new HuffmanAlg();
		
		//make a forest for the algorithm
		OrderedDelegateForest<HuffmanTreeNode, TreeEdge> forest = new OrderedDelegateForest<>();
		
		//add the forest to the algorithm and initialize all the variables
		alg.reset(forest);
		
		//get a string to build the tree out of
		String input = "aabbbc";
		
		//feed each character, one at a time to the algorithm
		feedStringToAlg(alg, input);
		
		//check that there are three nodes
		if(forest.getVertexCount() != 3)
			exitWithErrorMessage("There are three unique characters in 'aabbbc' and so there should be three nodes.");
		
		//check that there are three trees too
		if(forest.getTrees().size() != 3)
			exitWithErrorMessage("There are three unique characters in 'aabbbc' and so there should be three trees before any merging.");
		
		//check node counts
		checkForRootInTreeWithCount(forest,'a',2);
		checkForRootInTreeWithCount(forest,'b',3);
		checkForRootInTreeWithCount(forest,'c',1);
		
		//check node order (should be 'a', 'b', 'c')
		checkTreeRootsInOrder(forest, new Character[] {'a', 'b', 'c'});
		
		System.out.println("Character Counting Yay!");
		
		//progress the algorithm...
		alg.setCurrentChar(null);
		
		//this next step should join the 'a' and 'c' nodes
		alg.step();
		
		//make sure we have two trees 
		if(forest.getTrees().size() != 2)
			exitWithErrorMessage("After merging two of the trees in a three-tree forest, there should be two trees.");
		
		//make sure the two trees have the right root counts
		checkForRootInTreeWithCount(forest,null, 3);
		checkForRootInTreeWithCount(forest,'b', 3);
		
		//find the a/c node and check it's children
		for(Tree<HuffmanTreeNode,TreeEdge> tree : forest.getTrees()) {
			HuffmanTreeNode root = tree.getRoot();
			
			//is this the a/c node?
			if(root.getLetter() == null) {
				//should have two children
				checkForChildWithCount(root, forest.getChildren(root), 'a', 2);
				checkForChildWithCount(root, forest.getChildren(root), 'c', 1);
				
				//should have those children in order
				checkChildrenInOrder(forest.getChildren(root), new Character[] {'a', 'c'});
			}
		}
		
		//this next step should join the 'a/c' and 'b' nodes
		alg.step();
		
		//make sure we have one tree
		if(forest.getTrees().size() != 1)
			exitWithErrorMessage("After merging two trees in a two-tree forest, there should be only one tree.");
		
		//make sure the root of the tree has the correct count
		checkForRootInTreeWithCount(forest, null, 6);
		
		//check that the tree has the children correct
		//note: there is only one tree, but "Collection" doesn't have a get() method,
		//so this loop will only run once getting the single root
		for(Tree<HuffmanTreeNode,TreeEdge> tree : forest.getTrees()) {
			HuffmanTreeNode root = tree.getRoot();
			
			//is this the root node?
			if(root.getLetter() == null) {
				//should have two children
				checkForChildWithCount(root, forest.getChildren(root), null, 3);
				checkForChildWithCount(root, forest.getChildren(root), 'b', 3);
				
				//should have those children in order
				checkChildrenInOrder(forest.getChildren(root), new Character[] {'b', null});
			}
		}
		
		System.out.println("Tree Building Yay!");
		
		//this next step should write the encodings onto each node
		alg.step();
		
		//get the encodings from the nodes in the tree and put them in this map
		HashMap<Character,String> encoding = new HashMap<>();
		
		//for each node...
		for(HuffmanTreeNode n : forest.getVertices()) {
			//if the node represents a character and the character
			//has an encoding
			if(n.getLetter() != null && n.getEncoding() != null) {
				//put the encoding in the map
				encoding.put(n.getLetter(), n.getEncoding());
			}
		}
		
		//make sure the encodings are right...
		checkCharEncoding(encoding, 'b',"0");
		checkCharEncoding(encoding, 'a',"10");
		checkCharEncoding(encoding, 'c',"11");
		
		System.out.println("Encodings Yay!");
	}
	
	public static void feedStringToAlg(HuffmanAlg alg, String input) {
		for(int i = 0; i < input.length(); i++) {
			//get the character from the strong
			Character c = input.charAt(i);
			
			//put it into the algorithm
			alg.setCurrentChar(c);
			
			//try to "step" the algorithm
			boolean retVal = alg.step();
			
			//make sure the algorithm doesn't think it should stop (it shouldn't)
			if(!retVal) {
				exitWithErrorMessage("Algorithm shouldn't end before it is finished counting the characters.");
			}
		}
	}
	
	public static void checkForRootInTreeWithCount(Forest<HuffmanTreeNode, TreeEdge> forest, Character c, int count) {
		boolean found = false;
		
		//look through all the vertices in the tree
		for(Tree<HuffmanTreeNode,TreeEdge> tree : forest.getTrees()) {
			HuffmanTreeNode root = tree.getRoot();
			//if one of them comtains the letter we are looking for...
			if(root.getLetter() == c) {
				//indicate we found it
				found = true;
				
				//and make sure the count is correct
				if(root.getCount() != count) {
					exitWithErrorMessage("Root node with character '"+c+"' should have count of "+count);
				}
				
				//stop looking
				break;
			}
		}
		
		//somehow we never found that character at all!
		if(!found) {
			exitWithErrorMessage("There should be a root node in the forest for '"+c+"'.");
		}
	}
	
	public static void checkTreeRootsInOrder(Forest<HuffmanTreeNode, TreeEdge> forest, Character[] rootChars) {
		int i = 0;
		for(Tree<HuffmanTreeNode,TreeEdge> tree : forest.getTrees()) {
			if(tree.getRoot().getLetter() != rootChars[i++])
				exitWithErrorMessage("Nodes should be in order of their id. This error is most likely caused by creating new nodes rather than using existing nodes.");
		}
	}
	
	public static void checkForChildWithCount(HuffmanTreeNode parent, Collection<HuffmanTreeNode> children, Character c, int count) {
		boolean found = false;
		
		//look through all the vertices in the tree
		for(HuffmanTreeNode child : children) {
			//if one of them comtains the letter we are looking for...
			if(child.getLetter() == c) {
				//indicate we found it
				found = true;
				
				//and make sure the count is correct
				if(child.getCount() != count) {
					exitWithErrorMessage("Child node with character '"+c+"' should have count of "+count);
				}
				
				//stop looking
				break;
			}
		}
		
		//somehow we never found that character at all!
		if(!found) {
			exitWithErrorMessage("There should be a child node '"+c+"' for node '"+parent.getLetter()+"'.");
		}
	}
	
	public static void checkChildrenInOrder(Collection<HuffmanTreeNode> children, Character[] chars) {
		int i = 0;
		for(HuffmanTreeNode child : children) {
			if(child.getLetter() != chars[i++])
				exitWithErrorMessage("Child nodes should be in order of their id. This error is most likely caused by creating new nodes rather than using existing nodes.");
		}
	}
	
	public static void checkCharEncoding(HashMap<Character,String> encodings, Character c, String enc) {
		if(!encodings.getOrDefault(c,"").equals(enc)) {
			exitWithErrorMessage("Letter '"+c+"' should have encoding '"+enc+"'.");
		}
	}
	
	public static void exitWithErrorMessage(String message) {
		System.out.println(message);
		System.exit(-1);
	}
}