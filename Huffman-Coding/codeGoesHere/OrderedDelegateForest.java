//***************************************************************
// TODO: Nothing, all done. You may read this if you'd like,
// but you may not need to.
//***************************************************************


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

import java.awt.Color;

import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.graph.DelegateForest;

import edu.uci.ics.jung.graph.util.EdgeType;

/**
 *  Overrides how DelegateForests return their children. Also labels
 *  edges by this ordering.
 */
class OrderedDelegateForest<V extends TreeNode,E extends TreeEdge> extends DelegateForest<V,E> {
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Collection<E> getChildEdges(V vertex) {
		List<E> c = new ArrayList<>(super.getChildEdges(vertex));
		Collections.sort(c, new Comparator<E>() {
			public int compare(E thing1, E thing2) {
				//edges ordered by the child node's ID
				return getDest(thing1).getId() - getDest(thing2).getId();
			}
		});
		
		return c;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Collection<V> getChildren(V vertex) {
		List<V> c = new ArrayList<>(super.getChildren(vertex));
		Collections.sort(c, new Comparator<V>() {
			public int compare(V thing1, V thing2) {
				//node's ordered by ID
				return thing1.getId() - thing2.getId();
			}
		});
		
		return c;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Collection<Tree<V,E>> getTrees() {
		List<Tree<V,E>> c = new ArrayList<>(super.getTrees());
		Collections.sort(c, new Comparator<Tree<V,E>>() {
			public int compare(Tree<V,E> thing1, Tree<V,E> thing2) {
				//trees are ordered by the root node's ID
				return (thing1.getRoot().getId() - thing2.getRoot().getId());
			}
		});
		return c;
	}

	/**
	 *  {@inheritDoc}
	 */
	public boolean addEdge(E e, V v1, V v2) {
		if(super.addEdge(e, v1, v2)) {
			labelEdges(v1);
			return true;
		}
		return false;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean addEdge(E e, V v1, V v2, EdgeType edgeType) {
		if(super.addEdge(e, v1, v2, edgeType)) {
			labelEdges(v1);
			return true;
		}
		return false;
	}
	
	/**
	 *  Labels all edges from the given parent node base on the
	 *  order of the edges currently on the vertex. This may
	 *  re-label existing edges.
	 *  
	 *  @param parent the partent (source) node of the edges
	 */
	private void labelEdges(V parent) {
		int i = 0;
		for(E e : getChildEdges(parent)) {
			if(i == 0) e.setColor(Color.MAGENTA);
			if(i == 1) e.setColor(Color.BLACK);
			
			e.setTextLabel(""+(i++));
		}
	}
}