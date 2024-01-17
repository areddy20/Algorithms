//***************************************************************
// TODO: You should not need to edit anything in this part.
// However, you may need to read everything here.
//***************************************************************


import org.apache.commons.collections15.Factory;
import java.awt.Color;
import java.util.Random;

/**
 *  Class to represent an edge in a tree.
 *  
 *  @author Katherine (Raven) Russell
 */
public class TreeEdge {
	/**
	 *  The last id given to a edge.
	 */
	public static int LAST_ID = -1;
	
	/**
	 *  The unique id of this edge.
	 */
	protected final int id;
	
	/**
	 *  The color of this edge in the visualization.
	 */
	protected Color c = Color.BLACK;
	
	/**
	 *  The text label to display next to the edge.
	 */
	protected String text = "";
	
	/**
	 *  Makes a new edges with a random cost between
	 *  1 and 6.
	 */
	protected TreeEdge() {
		id = ++LAST_ID;
	}
	
	/**
	 *  Returns the id of the edge.
	 *  @return the edge's unique identifier
	 */
	public int getId() {
		return id;
	}
	
	/**
	 *  Returns the color of the edge in the simulation.
	 *  
	 *  @return the edge's current color
	 */
	public Color getColor() {
		return c;
	}
	
	/**
	 *  Sets the color of the edge in the simulation.
	 *  @param c the new color to use
	 */
	public void setColor(Color c) {
		this.c = c;
	}
	
	/**
	 *  Sets the text label to display for this edge.
	 *  @param text the new next to display
	 */
	public void setTextLabel(String text) {
		this.text = text;
	}
	
	/**
	 *  Gets the text label displaying for ths edge.
	 *  @return the text on the label
	 */
	public String getTextLabel() {
		return this.text;
	}
	
	/**
	 *  The string representation of a edge
	 *  is just it's cost.
	 *  
	 *  @return the string representation of the edge
	 */
	@Override
	public String toString() {
		return ""+text;
	}
	
	/**
	 *  Sets the hash code of the edge (cost * id).
	 *  
	 *  @return the hash code of the edge
	 */
	@Override
	public int hashCode() {
		return id;
	}
	
	/**
	 *  Two edges are equal if they have the same id.
	 *  
	 *  @return whether two edges are equal
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof TreeEdge) {
			return this.id == ((TreeEdge)o).id;
		}
		return false;
	}
}