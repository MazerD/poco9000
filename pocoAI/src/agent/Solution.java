package agent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import simulation.Action;

public class Solution {

	private LinkedList<Action> actions = new LinkedList<Action>();
	private HashMap<String, Object> statistics = new HashMap<String, Object>();
	
	/**
	 * Gives the list of actions that make up the solution
	 * 
	 * @return
	 */
	public Iterator<Action> actions() {
		return actions.iterator();
	}

	/**
	 * Gives a set of Strings which represent the names of all the available
	 * statistics associated with this solution
	 * 
	 * @return
	 */
	public Collection<String> statisticKeySet() {
		return statistics.keySet();
	}

	/**
	 * Gives the value of the statistic with this given name
	 * 
	 * @param key
	 *            The name of the statistic to return the value of
	 * @return
	 */
	public Object getStatistic(String key) {
		return statistics.get(key);
	}
	
	public void prependAction(Action a) {
		actions.addFirst(a);
	}
	
	public void appendAction(Action a) {
		actions.addLast(a);
	}
	
	public Action popLastAction() {
		return actions.removeLast();
	}
	
	public void addStatistic(String name, Object value) {
		statistics.put(name, value);
	}
}
