//***************************************************************
// TODO: You may want/need to read everything here. This shows
// how your code will be tested. You may edit this file, but
// the TAs will use a "vanilla copy" and your edits won't affect
// what is expected of your code. It's still a good idea to add
// more tests for yourself here or in the main method tester.
//***************************************************************


import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.junit.*;
import static org.junit.Assert.*;

import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Tree;

/**
 *  This is a JUnit test class which verifies you have the correct
 *  output for your Huffman Algorithm.
 *  
 *  @author K. Raven Russell
 */
public class JUnitExample {
	HuffmanAlg alg = null;
	Forest<HuffmanTreeNode, TreeEdge> forest = null;

	/**
	 *  Anything needed before each individual test runs.
	 */
	@Before
	public void setupTest() {
		//make an instance of the Huffman algorithm
		alg = new HuffmanAlg();
		
		//make a forest for the algorithm
		forest = new OrderedDelegateForest<>();
		
		//add the forest to the algorithm and initialize all the variables
		alg.reset(forest);
	}
	
	/**
	 *  Test 1 from sample given to students in main.
	 */
	@Test(timeout = 2000)
	public void simpleTest01() {
		//get a string to build the tree out of
		String input = "aabbbc";
		
		//feed each character, one at a time to the algorithm
		feedStringToAlg(alg, input);
		
		//check that there are three nodes
		assertEquals("There are three unique characters in 'aabbbc' and so there should be three nodes.", 3, forest.getVertexCount());
	}
	
	/**
	 *  Test 2 from sample given to students in main.
	 */
	@Test(timeout = 2000)
	public void simpleTest02() {
		//get a string to build the tree out of
		String input = "aabbbc";
		
		//feed each character, one at a time to the algorithm
		feedStringToAlg(alg, input);
		
		//check node counts
		checkForRootInTreeWithCount(forest,'a',2);
		checkForRootInTreeWithCount(forest,'b',3);
		checkForRootInTreeWithCount(forest,'c',1);
	}
	
	/**
	 *  Test 3 from sample given to students in main.
	 */
	@Test(timeout = 2000)
	public void simpleTest03() {
		//get a string to build the tree out of
		String input = "aabbbc";
		
		//feed each character, one at a time to the algorithm
		feedStringToAlg(alg, input);
		
		//check node order (should be 'a', 'b', 'c')
		checkTreeRootsInOrder(forest, new Character[] {'a', 'b', 'c'});
	}
	
	/**
	 *  Test 4 from sample given to students in main.
	 */
	@Test(timeout = 2000)
	public void simpleTest04() {
		//get a string to build the tree out of
		String input = "aabbbc";
		
		//feed each character, one at a time to the algorithm
		feedStringToAlg(alg, input);
		
		//progress the algorithm...
		alg.setCurrentChar(null);
		
		//this next step should join the 'a' and 'c' nodes
		alg.step();
		
		//make sure we have two trees 
		assertEquals("After merging two of the trees in a three-tree forest, there should be two trees.", 2, forest.getTrees().size());
	}
	
	/**
	 *  Test 5 from sample given to students in main.
	 */
	@Test(timeout = 2000)
	public void simpleTest05() {
		//get a string to build the tree out of
		String input = "aabbbc";
		
		//feed each character, one at a time to the algorithm
		feedStringToAlg(alg, input);
		
		//progress the algorithm...
		alg.setCurrentChar(null);
		
		//this next step should join the 'a' and 'c' nodes
		alg.step();
		
		//make sure the two trees have the right root counts
		checkForRootInTreeWithCount(forest, null, 3);
		checkForRootInTreeWithCount(forest, 'b', 3);
	}
	
	/**
	 *  Test 6 from sample given to students in main.
	 */
	@Test(timeout = 2000)
	public void simpleTest06() {
		//get a string to build the tree out of
		String input = "aabbbc";
		
		//feed each character, one at a time to the algorithm
		feedStringToAlg(alg, input);
		
		//progress the algorithm...
		alg.setCurrentChar(null);
		
		//this next step should join the 'a' and 'c' nodes
		alg.step();
		
		//find the a/c node and check it's children
		for(Tree<HuffmanTreeNode,TreeEdge> tree : forest.getTrees()) {
			HuffmanTreeNode root = tree.getRoot();
			
			//is this the a/c node?
			if(root.getLetter() == null) {
				//should have two children
				checkForChildWithCount(root, forest.getChildren(root), 'a', 2);
				checkForChildWithCount(root, forest.getChildren(root), 'c', 1);
				
				//avoid fail()
				return;
			}
		}
		
		fail("Could not find appropriate root node to check children");
	}
	
	/**
	 *  Test 7 from sample given to students in main.
	 */
	@Test(timeout = 2000)
	public void simpleTest07() {
		//get a string to build the tree out of
		String input = "aabbbc";
		
		//feed each character, one at a time to the algorithm
		feedStringToAlg(alg, input);
		
		//progress the algorithm...
		alg.setCurrentChar(null);
		
		//this next step should join the 'a' and 'c' nodes
		alg.step();
		
		//find the a/c node and check it's children
		for(Tree<HuffmanTreeNode,TreeEdge> tree : forest.getTrees()) {
			HuffmanTreeNode root = tree.getRoot();
			
			//is this the a/c node?
			if(root.getLetter() == null) {
				//should have two children in order
				checkChildrenInOrder(forest.getChildren(root), new Character[] {'a', 'c'});
				
				//avoid fail()
				return;
			}
		}
		
		fail("Could not find appropriate root node to check children");
	}
	
	/**
	 *  Test 8 from sample given to students in main.
	 */
	@Test(timeout = 2000)
	public void simpleTest08() {
		//get a string to build the tree out of
		String input = "aabbbc";
		
		//feed each character, one at a time to the algorithm
		feedStringToAlg(alg, input);
		
		//progress the algorithm...
		alg.setCurrentChar(null);
		
		//this next step should join the 'a' and 'c' nodes
		alg.step();
		
		//this next step should join the 'a/c' and 'b' nodes
		alg.step();
		
		//make sure we have one tree
		assertEquals("After merging two trees in a two-tree forest, there should be only one tree.", 1, forest.getTrees().size());
	}
	
	/**
	 *  Test 9 from sample given to students in main.
	 */
	@Test(timeout = 2000)
	public void simpleTest09() {
		//get a string to build the tree out of
		String input = "aabbbc";
		
		//feed each character, one at a time to the algorithm
		feedStringToAlg(alg, input);
		
		//progress the algorithm...
		alg.setCurrentChar(null);
		
		//this next step should join the 'a' and 'c' nodes
		alg.step();
		
		//this next step should join the 'a/c' and 'b' nodes
		alg.step();
		
		//make sure the root of the tree has the correct count
		checkForRootInTreeWithCount(forest, null, 6);
	}
	
	/**
	 *  Test 10 from sample given to students in main.
	 */
	@Test(timeout = 2000)
	public void simpleTest10() {
		//get a string to build the tree out of
		String input = "aabbbc";
		
		//feed each character, one at a time to the algorithm
		feedStringToAlg(alg, input);
		
		//progress the algorithm...
		alg.setCurrentChar(null);
		
		//this next step should join the 'a' and 'c' nodes
		alg.step();
		
		//this next step should join the 'a/c' and 'b' nodes
		alg.step();
		
		//check that the tree has the children correct
		//note: there is only one tree, but "Collection" doesn't have a get() method,
		//so this loop will only run once getting the single root
		assertEquals("Trying to check children of single root, but could not check because of incorrect number of trees.", 1, forest.getTrees().size());
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
	}
	
	/**
	 *  Test 11 from sample given to students in main.
	 */
	@Test(timeout = 2000)
	public void simpleTest11() {
		//get a string to build the tree out of
		String input = "aabbbc";
		
		//feed each character, one at a time to the algorithm
		feedStringToAlg(alg, input);
		
		//progress the algorithm...
		alg.setCurrentChar(null);
		
		//this next step should join the 'a' and 'c' nodes
		alg.step();
		
		//this next step should join the 'a/c' and 'b' nodes
		alg.step();
		
		//this next step should write the encodings onto each node
		alg.step();
		
		//get the encodings from the nodes
		HashMap<Character,String> encodings = getEncodings(this.forest);
		
		//check the encodings
		Character[] chars = {'a', 'b', 'c'};
		String[] encs = {"10", "0", "11"};
		for(int i = 0; i < chars.length; i++) {
			assertEquals("Letter '"+chars[i]+"' should have encoding '"+encs[i]+"' for tree created from '"+input+"'.", encs[i], encodings.getOrDefault(chars[i],""));
		}
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
				fail("Algorithm shouldn't end before it is finished counting the characters.");
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
				assertEquals("Root node with character '"+c+"' should have count of "+count, count, root.getCount());
				
				//stop looking
				break;
			}
		}
		
		//make sure we found the character!
		assertTrue("There should be a root node in the forest for '"+c+"'.", found);
	}
	
	public static void checkTreeRootsInOrder(Forest<HuffmanTreeNode, TreeEdge> forest, Character[] rootChars) {
		int i = 0;
		assertEquals("Nodes should be in order of their id. Could not check because missing roots.", rootChars.length, forest.getTrees().size());
		for(Tree<HuffmanTreeNode,TreeEdge> tree : forest.getTrees()) {
			assertEquals("Nodes should be in order of their id. This error is most likely caused by creating new nodes rather than using existing nodes.", rootChars[i++], tree.getRoot().getLetter());
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
				assertEquals("Child node with character '"+c+"' should have count of "+count, count, child.getCount());
				
				//stop looking
				break;
			}
		}
		
		//make sure we found the character!
		assertTrue("There should be a root node in the forest for '"+c+"'.", found);
	}
	
	public static void checkChildrenInOrder(Collection<HuffmanTreeNode> children, Character[] chars) {
		int i = 0;
		for(HuffmanTreeNode child : children) {
			assertEquals("Child nodes should be in order of their id. This error is most likely caused by creating new nodes rather than using existing nodes.", chars[i++], child.getLetter());
		}
	}
	
	public static HashMap<Character,String> getEncodings(Forest<HuffmanTreeNode,TreeEdge> forest) {
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
		
		return encoding;
	}
	
	//run the unit tests
	public static void main(String args[]) {
		org.junit.runner.JUnitCore.main("JUnitExample");
	}
}