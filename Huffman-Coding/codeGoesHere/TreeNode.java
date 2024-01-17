//***************************************************************
// TODO: You should not need to edit anything in this part.
// However, you may need to read everything here.
//***************************************************************


import org.apache.commons.collections15.Factory;
import java.awt.Color;

/**
 *  A generic tree node to be extended.
 *  
 *  @author Katherine (Raven) Russell
 */
public class TreeNode implements Comparable<TreeNode> {
	/**
	 *  The last id given to a node.
	 */
	public static int LAST_ID = -1;
	
	/**
	 *  The unique id of this node.
	 */
	protected final int id;
	
	/**
	 *  The color of this node in the visualization.
	 */
	private Color c = Color.WHITE;
	
	/**
	 *  Sets the id of the node.
	 */
	protected TreeNode() {
		id = ++LAST_ID;
	}
	
	/**
	 *  Creates a tree node with a given ID, but
	 *  only if that tree node has previously been
	 *  generated with the standard constructor.
	 *  (This is to allow making tree node objects
	 *  that are equal to each other, but not tree nodes
	 *  with id numbers out of sequence.)
	 *  @param id the unique identifier of the node
	 */
	public TreeNode(int id) {
		if(id < 0 || id > LAST_ID) {
			throw new IllegalArgumentException("Cannot create a node with an arbitrary id.");
		}
		this.id = id;
	}
	
	/**
	 *  Returns the id of the tree node.
	 *  @return the tree node's unique identifier
	 */
	public int getId() {
		return id;
	}
	
	/**
	 *  Returns the color of the tree node in the simulation.
	 *  @return the tree node's current color
	 */
	public Color getColor() {
		return c;
	}
	
	/**
	 *  Sets the color of the tree node in the simulation.
	 *  @param c the new color to use
	 */
	public void setColor(Color c) {
		this.c = c;
	}
	
	/**
	 *  Sets the hashcode of the tree node
	 *  to be a hash of the string value
	 *  which contains the id.
	 *  @return the hash code of the tree node
	 */
	@Override
	public int hashCode() {
		return id;
	}
	
	/**
	 *  The string representation of a tree node.
	 *  @return the string representation of the tree node
	 */
	public String toString() {
		return ""+id;
	}
	
	/**
	 *  Two hosts are equal if they have the same id.
	 *  @return whether two hosts are equal
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof TreeNode) {
			return this.id == ((TreeNode)o).id;
		}
		return false;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public int compareTo(TreeNode o) {
		return this.id - o.id;
	}
}