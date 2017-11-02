package sat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Graph {
    private int numOfVars;
    private int numOfVertices;

    // for Tarjan's algorithm
    private Map<Integer, Set<Integer>> adj;
    private Map<Integer, Integer> indices, lowlink;
    private int index = 0;

    // SCC graph related
    private Map<Integer, Integer> sccSearchSpace;
    private int sccSize = 0;

    public Graph(int numOfVars, int[][] clauses, boolean[] clauseRemoved) {
        this.numOfVars = numOfVars;
        this.numOfVertices = 2 * numOfVars + 1;

        this.adj = new HashMap<>();
        this.sccSearchSpace = new HashMap<>();
        this.indices = new HashMap<>();
        this.lowlink = new HashMap<>();

        for (int j = 0; j < clauses.length; j++) {
            if (!clauseRemoved[j])
                addClause(clauses[j]);
        }

        System.out.println(adj);
    }

    // solve by assigning truth values after SCC
    public Map<Integer, Integer> solve(Map<Integer, Integer> assignments, Set<Integer>[] scc) {
        Stack<Integer> stack = new Stack<>();
        Map<Integer, Set<Integer>> sccEdges = executeTarjan(scc, stack);

        if (!evalSat(scc)) return null;

        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < this.sccSize; i++) {
            Set<Integer> components = scc[i];
            result = assignVar(i, components, assignments, sccEdges, scc);
        }

        return result;
    }

    // evaluate satisfiability using SCC
    private boolean evalSat(Set<Integer>[] scc) {
        for (int i = 0; i < this.sccSize; i++) {
            for (Integer component : scc[i]) {
                if (scc[i].contains(-component))
                    return false;
            }
        }

        return true;
    }

    private Map<Integer, Set<Integer>> executeTarjan(Set<Integer>[] scc, Stack<Integer> stack) {
        for (int v = 1; v < this.numOfVertices; v++) {
            if (this.indices.get(v) == null)
                tarjanAlgo(v, scc, stack);
        }

        return getSCCEdges(scc);
    }

    // Tarjan's SCC algorithm
    // https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
    // https://www.programming-algorithms.net/article/44220/Tarjan%27s-algorithm
    private void tarjanAlgo(int node, Set<Integer>[] scc, Stack<Integer> stack) {
        this.indices.put(node, this.index);
        this.lowlink.put(node, this.index);
        this.index++;
        stack.push(node);

        for (int neighbor : this.adj.getOrDefault(node, new HashSet<>())) {
            // if node not yet discovered
            if (this.indices.get(neighbor) == null) {
                tarjanAlgo(neighbor, scc, stack);
                this.lowlink.put(node, Math.min(lowlink.get(node), lowlink.get(neighbor)));
            // if component not closed
            } else if (stack.contains(neighbor)) {
                this.lowlink.put(node, Math.min(lowlink.get(node), indices.get(neighbor)));
            }
        }

        // inside root of the component
        if (this.lowlink.get(node).equals(this.indices.get(node))) {
            Set<Integer> component = new HashSet<>();
            int poppedNode;

            do {
                // add node to component
                poppedNode = stack.pop();
                this.sccSearchSpace.put(poppedNode, this.sccSize);
                component.add(poppedNode);
            } while (poppedNode != node);

            if (component.size() != 0) {
                scc[sccSize] = component;
                sccSize++;
            }
        }
    }

    // assign variables in each strongly-connected component
    private Map<Integer, Integer> assignVar(int sccCompIndex, Set<Integer> components,
                                            Map<Integer, Integer> assignments,
                                            Map<Integer, Set<Integer>> sccEdges,
                                            Set<Integer>[] scc) {
        for (int component : components) {
            int k = Math.abs(component);
            if (assignments.get(k) == null) {
                int assignment = component < 0 ? 1 : -1;
                assignments.put(k, assignment);
            }
        }

        Set<Integer> edgeList = sccEdges.get(sccCompIndex);
        if (edgeList.size() != 0) {
            for (int edge : edgeList) {
                assignments = assignVar(edge, scc[edge],assignments, sccEdges, scc);
            }
        }

        return assignments;
    }

    // calculate map of edges in SCC graph
    private Map<Integer, Set<Integer>> getSCCEdges(Set<Integer>[] scc) {
        Map<Integer, Set<Integer>> output = new HashMap<>();

        for (int i = 0; i < this.sccSize; i++) {
            Set<Integer> edgeList = new HashSet<>();
            for (int node : scc[i]) {
                for (int neighbor : this.adj.getOrDefault(node, new HashSet<>())) {
                    int edge = this.sccSearchSpace.get(neighbor);

                    if (!edgeList.contains(edge) && edge != i) {
                        edgeList.add(edge);
                    }
                }
            }

            output.put(i, edgeList);
        }

        return output;
    }

    private void addClause(int[] clause) {
        if (clause.length == 2) {
            addEdge(-clause[0], clause[1]);
            addEdge(-clause[1], clause[0]);
        }
    }

    private void addEdge(int start, int end) {
        this.adj.putIfAbsent(start, new HashSet<>());

        if (!this.adj.get(start).contains(end))
            this.adj.get(start).add(end);
    }
}
