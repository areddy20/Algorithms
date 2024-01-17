//***************************************************************
// TODO: You should not need to edit anything in this part.
// However, you may need to read everything here.
//***************************************************************


/**
 *  Class to represent a node in a Huffman tree.
 *  
 *  @author Katherine (Raven) Russell
 */
class HuffmanTreeNode extends TreeNode {
	/**
	 *  The character count of the node (used for comparison).
	 *  For leaf nodes, this should be the character count for
	 *  the given letter. For internal nodes, this should be
	 *  the sum of all character counts lower in the tree.
	 */
	protected int count;
	
	/**
	 *  The letter for this node (if any). Null if
	 *  this is an internal node;
	 */
	protected Character letter = null;
	
	/**
	 *  The letter for this node (if any). Null if
	 *  this is an internal node;
	 */
	protected String encoding = null;
	
	/**
	 *  Creates a new Huffman tree internal node.
	 *  
	 *  @param count the character count for this node
	 *  @param letter the letter (for leaves)
	 */
	public HuffmanTreeNode(int count) {
		this.count = count;
	}
	
	/**
	 *  Creates a new Huffman tree leaf node.
	 *  
	 *  @param letter the letter (for leaves)
	 *  @param count the character count for this node
	 */
	public HuffmanTreeNode(Character letter, int count) {
		this.count = count;
		this.letter = letter;
	}
	
	/**
	 *  Accessor method for the count.
	 *  
	 *  @return the count
	 */
	public int getCount() {
		return this.count;
	}
	
	/**
	 *  Mutator method for the count
	 *  
	 *  @param count the new count
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 *  Accessor method for the letter.
	 *  
	 *  @return the letter
	 */
	public Character getLetter() {
		return this.letter;
	}
	
	/**
	 *  Mutator method for encoding.
	 *  
	 *  @param encoding the new encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	/**
	 *  Accessor method for encoding.
	 *  
	 *  @return the new encoding for this character
	 */
	public String getEncoding() {
		return this.encoding;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public String toString() {
		if(this.encoding != null && this.letter != null) {
			return ""+(letter.equals(' ')?'_':letter)+":"+this.encoding;
		}
		else {
			return ""+count+((letter == null) ? "" : " "+(letter.equals(' ')?'_':letter));
		}
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public int compareTo(TreeNode o) {
		int ret = 0;
		
		//if this is a huffman node, we can compare by count
		if(o instanceof HuffmanTreeNode) {
			ret = this.count - ((HuffmanTreeNode) o).count;
		}
		
		//if this wasn't a huffman node or if the counts were equal
		//then just go with the default (compare by id)
		if(ret == 0) {
			ret = super.compareTo(o);
		}
		
		return ret;
	}

    
}