package sat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class GraphTest {
    private Map<Integer, Integer> indices, lowlinks;
    private Map<Integer, Set<Integer>> graph;
    private Map<Integer, Integer> assignments;
    private Stack<Integer> stack = new Stack<>();
    private List<Set<Integer>> scComponents = new ArrayList<>();
    private int index = 0;
    private int[][] clauses;

    public GraphTest(int[][] clauses, Map<Integer, Integer> assignments) {
        indices = new HashMap<>();
        lowlinks = new HashMap<>();
        graph = new HashMap<>();

        this.assignments = assignments;
        this.clauses = clauses;

        for (int[] clause : clauses) {
            System.out.println(Arrays.toString(clause));
        }

        for (int[] clause : this.clauses) {
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
        graph.putIfAbsent(start, new HashSet<>());
        graph.get(start).add(end);
    }

    private void tarjanAlgorithm(int node) {
        this.indices.put(node, this.index);
        this.lowlinks.put(node, this.index);
        this.index++;
        this.stack.add(node);

        // depth first search
        Set<Integer> successors = graph.get(node);
        if (successors != null) {
            for (int successor : successors) {
                if (this.lowlinks.get(successor) == null) {
                    tarjanAlgorithm(successor);
                    this.lowlinks.put(node, Math.min(lowlinks.get(node), lowlinks.get(successor)));
                } else if (this.stack.contains(successor)) {
                    this.lowlinks.put(node, Math.min(lowlinks.get(node), indices.get(successor)));
                }
            }
        }

        // node is root
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

    public boolean solve() {
        System.out.println(scComponents);
        for (Set<Integer> component : scComponents) {
            System.out.println(component);
            for (int literal : component) {
                System.out.println(literal);
                if (component.contains(-literal))
                    return false;

                int ass = literal < 0 ? -1 : 1;
                if (!assignments.containsKey(Math.abs(literal)))
                    assignments.put(Math.abs(literal), ass);
            }
        }

        return true;
    }

    public Map<Integer, Integer> getAssignments() {
        return this.assignments;
    }
}

