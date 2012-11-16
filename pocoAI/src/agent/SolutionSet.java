package agent;

import java.util.HashMap;
import java.util.Set;

public class SolutionSet {

	private HashMap<String, Solution> solutions = new HashMap<String, Solution>();
	
	public SolutionSet() {
		
	}
	
	public void addSolution(String algorithmName, Solution sol) {
		solutions.put(algorithmName,  sol);
	}
	
	public Set<String> algorithmSet() {
		return solutions.keySet();
	}
	
	public Solution getSolution(String algorithmName) {
		return solutions.get(algorithmName);
	}
}
