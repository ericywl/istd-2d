package sat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Graph {
    private int numOfVars;
    private int numOfVertices;

    // implication graph
    private Set<Integer>[] adj;
    private Stack<Integer> stack = new Stack<>();
    private Integer[] indices, lowlink;
    private int index = 0;

    // SCC graph
    private int[] sccSearchSpace;
    private int sccSize = 0;

    @SuppressWarnings("unchecked")
    public Graph(int numOfVars, int[][] clauses, boolean[] clauseRemoved) {
        this.numOfVars = numOfVars;
        this.numOfVertices = 2 * numOfVars + 1;

        // implication graph components
        this.adj = (Set<Integer>[]) new HashSet[numOfVertices];
        this.indices = new Integer[numOfVertices];
        this.lowlink = new Integer[numOfVertices];

        // SCC graph components
        this.sccSearchSpace = new int[numOfVertices];

        for (int i = 0; i < this.numOfVertices; i++) {
            this.adj[i] = new HashSet<>();
        }

        for (int j = 0; j < clauses.length; j++) {
            if (!clauseRemoved[j])
                addClause(clauses[j]);
        }
    }

    // solve by assigning truth values after SCC
    public Map<Integer, Integer> solve(Map<Integer, Integer> assignments, Set<Integer>[] scc) {
        if (!evalSat(scc)) return null;

        Map<Integer, Integer> result = new HashMap<>();
        Map<Integer, Set<Integer>> sccEdges = executeTarjan(scc);

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

    private Map<Integer, Set<Integer>> executeTarjan(Set<Integer>[] scc) {
        for (int i = 1; i < this.numOfVertices; i++) {
            if (this.indices[i] == null)
                tarjanAlgo(i, scc);
        }

        return getSCCEdges(scc);
    }

    // Tarjan's SCC algorithm
    // https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
    // https://www.programming-algorithms.net/article/44220/Tarjan%27s-algorithm
    private void tarjanAlgo(int node, Set<Integer>[] scc) {
        this.indices[node] = this.index;
        this.lowlink[node] = this.index;
        this.index++;
        this.stack.push(node);

        for (int neighbor : this.adj[node]) {
            neighbor = simpleHash(neighbor);

            // if node not yet discovered
            if (this.indices[neighbor] == null) {
                tarjanAlgo(neighbor, scc);
                this.lowlink[node] = Math.min(lowlink[node], lowlink[neighbor]);
            // if component not closed
            } else if (this.stack.contains(neighbor)) {
                this.lowlink[node] = Math.min(lowlink[node], indices[neighbor]);
            }
        }

        // inside root of the component
        if (this.lowlink[node].equals(this.indices[node])) {
            Set<Integer> component = new HashSet<>();
            int poppedNode;

            do {
                // add node to component
                poppedNode = this.stack.pop();
                this.sccSearchSpace[poppedNode] = this.sccSize;
                component.add(simpleHash(poppedNode));
            } while (poppedNode != node);

            if (component.size() != 0) {
                scc[sccSize] = component;
                sccSize++;
            }
        }
    }

    private Map<Integer, Integer> assignVar(int sccCompIndex, Set<Integer> components,
                                            Map<Integer, Integer> assignments,
                                            Map<Integer, Set<Integer>> sccEdges,
                                            Set<Integer>[] scc) {
        for (int component : components) {
            int k = Math.abs(component);
            if (assignments.getOrDefault(k, 0) == 0) {
                int value = component < 0 ? 1 : -1;
                assignments.put(k, value);
            }
        }

        Set<Integer> edgeList = sccEdges.get(sccCompIndex);
        if (edgeList.size() != 0) {
            for (int edge : edgeList) {
                assignVar(edge, scc[edge], assignments, sccEdges, scc);
            }
        }

        return assignments;
    }

    // calculate adj in SCC graph
    private Map<Integer, Set<Integer>> getSCCEdges(Set<Integer>[] scc) {
        Map<Integer, Set<Integer>> output = new HashMap<>();

        for (int i = 0; i < this.sccSize; i++) {
            Set<Integer> edgeList = new HashSet<>();
            for (int j : scc[i])
                for (int k : this.adj[simpleHash(j)])
                    if (!edgeList.contains(this.sccSearchSpace[simpleHash(k)])
                            && this.sccSearchSpace[simpleHash(k)] != i) {
                        edgeList.add(this.sccSearchSpace[simpleHash(k)]);
                    }

            output.put(i, edgeList);
        }

        return output;
    }

    private void addClause(int[] clause) {
        if (clause.length == 2) {
            addEdge(negateLiteral(clause[0]), clause[1]);
            addEdge(negateLiteral(clause[1]), clause[0]);
        }
    }

    private void addEdge(int start, int end) {
        if (!this.adj[start].contains(end))
            this.adj[start].add(end);
    }

    private int negateLiteral(int literal) {
        if (literal < 0) return -literal;
        else return literal + this.numOfVars;
    }

    // simple hashing
    private int simpleHash(int i) {
        if (i < 0) return this.numOfVars - i;
        else if (i > this.numOfVars) return -(i - this.numOfVars);
        else return i;
    }
}
