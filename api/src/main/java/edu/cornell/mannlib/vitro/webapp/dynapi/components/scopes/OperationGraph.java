package edu.cornell.mannlib.vitro.webapp.dynapi.components.scopes;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OperationGraph {
	private Map<OperationNode, List<OperationNode>> forwardMap;
	private Map<OperationNode, List<OperationNode>> backwardMap;
	
	public OperationGraph() {
		forwardMap = new HashMap<OperationNode, List<OperationNode>>();
		backwardMap = new HashMap<OperationNode, List<OperationNode>>();
	}
	
	public OperationGraph(OperationNode rootNode) {
		this();
		initialize(rootNode);
	}

	private void initialize(OperationNode tail) {
		if (tail == null) {
			return;
		}
		Set<OperationNode> heads = tail.getNextNodes();
		for (OperationNode head : heads) {
			if (!containsTail(head)) {
				addArc(tail, head);
				initialize(head);
			}
		}
	}

	public List<OperationNode>getTailsOf(OperationNode head){
		List<OperationNode> tails = backwardMap.get(head);
		if (tails == null) {
			return Collections.emptyList();
		}
		return tails;
	}
	
	public List<OperationNode>getHeadsOf(OperationNode head){
		List<OperationNode> heads = forwardMap.get(head);
		if (heads == null) {
			return Collections.emptyList();
		}
		return heads;
	}
	
	private void addArc(OperationNode tail, OperationNode head) {
		addToForwardMap(tail, head);
		addToBackwardMap(tail, head);
	}
	
	private boolean containsHead(OperationNode node) {
		if (backwardMap.containsKey(node)) {
			return true;
		}
		return false;
	}
	
	private boolean containsTail(OperationNode node) {
		if (forwardMap.containsKey(node)) {
			return true;
		}
		return false;
	}

	private void addToBackwardMap(OperationNode tail, OperationNode head) {
		if (backwardMap.containsKey(head)){
			backwardMap.get(head).add(tail);
		} else {
			LinkedList<OperationNode> tails = new LinkedList<OperationNode>();
			tails.add(tail);
			backwardMap.put(head, tails);
		}
	}
	
	private void addToForwardMap(OperationNode tail, OperationNode head) {
		if (forwardMap.containsKey(tail)){
			forwardMap.get(tail).add(head);
		} else {
			LinkedList<OperationNode> heads = new LinkedList<OperationNode>();
			heads.add(head);
			forwardMap.put(tail, heads);
		}
	}

} 
