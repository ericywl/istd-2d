package sat.twoSat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Graph {
    private Map<Integer, Integer> indices, lowlinks;
    private Map<Integer, Set<Integer>> graph;
    private Map<Integer, Integer> assignments;
    private Stack<Integer> stack = new Stack<>();
    private List<Set<Integer>> scComponents = new ArrayList<>();
    private int index = 0;

    public Graph(int[][] clauses, Map<Integer, Integer> assignments) {
        indices = new HashMap<>();
        lowlinks = new HashMap<>();
        graph = new HashMap<>();

        this.assignments = assignments;

        //generate digraph
        for (int[] clause : clauses) {
            addClause(clause);
        }

        for (int node : graph.keySet()) {
            if (!lowlinks.containsKey(node)) {
                tarjanAlgorithm(node);
            }
        }
    }

    private void addClause(int[] clause) {
        if (clause.length == 2) {
            addEdge(-clause[0], clause[1]);
            addEdge(-clause[1], clause[0]);
        }
    }

    private void addEdge(int start, int end) {
        graph.putIfAbsent(start, new HashSet<Integer>());
        graph.get(start).add(end);
    }

    // Tarjan's SCC algorithm
    // https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
    private void tarjanAlgorithm(int node) {
        this.indices.put(node, this.index);
        this.lowlinks.put(node, this.index);
        this.index++;
        this.stack.add(node);

        // depth first search
        Set<Integer> successors = graph.get(node);
        if (successors != null) {
            for (int successor : successors) {
                // unvisited successor
                if (this.lowlinks.get(successor) == null) {
                    tarjanAlgorithm(successor);
                    this.lowlinks.put(node, Math.min(lowlinks.get(node), lowlinks.get(successor)));

                    // successor in current SCC
                } else if (this.stack.contains(successor)) {
                    this.lowlinks.put(node, Math.min(lowlinks.get(node), indices.get(successor)));
                }
            }
        }

        // node is root, generate SCC
        if (lowlinks.get(node).equals(indices.get(node))) {
            Set<Integer> currentSCC = new HashSet<>();
            int successor;

            while (true) {
                successor = this.stack.pop();
                currentSCC.add(successor);
                if (successor == node) break;
            }

            if (!scComponents.contains(currentSCC)) scComponents.add(currentSCC);
        }
    }

    // solve by simultaneously assigning and checking satisfiability
    public boolean solve() {
        for (Set<Integer> component : scComponents) {
            for (int literal : component) {
                if (component.contains(-literal))
                    return false;

                int ass = literal < 0 ? -1 : 1;
                if (!assignments.containsKey(Math.abs(literal)))
                    assignments.put(Math.abs(literal), ass);
            }
        }

        return true;
    }

    // used to get the truth values
    public Map<Integer, Integer> getAssignments() {
        return this.assignments;
    }
}

