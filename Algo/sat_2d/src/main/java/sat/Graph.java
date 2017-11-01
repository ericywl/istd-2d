package sat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Graph {
    private int numOfVars;
    private int numOfVertices;

    // implication graph
    private List<Integer>[] edges;
    private Stack<Integer> stack = new Stack<>();
    private Integer[] indices, lowlink;
    private int index = 0;

    // SCC graph
    private List<Integer>[] scc;
    private List<Integer>[] sccEdges;
    private int[] sccSearches;
    private int sccIndex = 0;

    private Map<Integer, Integer> assignments;

    @SuppressWarnings("unchecked")
    public Graph(int numOfVars, int[][] clauses, Map<Integer, Integer> assignments,
                 boolean[] clauseRemoved) {
        this.numOfVars = numOfVars;
        this.numOfVertices = numOfVars * 2 + 1;

        // implication graph components
        this.edges = (ArrayList<Integer>[]) new ArrayList[numOfVertices];
        this.indices = new Integer[numOfVertices];
        this.lowlink = new Integer[numOfVertices];

        // SCC graph components
        this.scc = (ArrayList<Integer>[]) new ArrayList[numOfVertices];
        this.sccSearches = new int[numOfVertices];

        // boolean assignments
        this.assignments = assignments;

        for (int i = 0; i < this.numOfVertices; i++) {
            this.edges[i] = new ArrayList<>();
        }

        for (int j = 0; j < clauses.length; j++) {
            if (!clauseRemoved[j])
                addClause(clauses[j]);
        }
    }

    private void addClause(int[] clause) {
        if (clause.length == 2) {
            addEdge(negateLiteral(clause[0]), clause[1]);
            addEdge(negateLiteral(clause[1]), clause[0]);
        }
    }

    private void addEdge(int start, int end) {
        if (!edges[start].contains(end))
            edges[start].add(end);
    }

    private int negateLiteral(int literal) {
        if (literal < 0) return -literal;
        else return literal + this.numOfVars;
    }

    private void generateSCC() {
        for (int v = 1; v < this.numOfVertices; v++) {
            if (this.indices[v] == null)
                tarjanVisit(v);
        }

        getSCCEdges();
    }

    // Tarjan's SCC algorithm
    // https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
    private void tarjanVisit(int node) {
        this.indices[node] = this.index;
        this.lowlink[node] = this.index;
        this.index++;
        this.stack.add(node);

        // depth first search
        for (int successor : this.edges[node]) {
            successor = convert(successor);
            if (this.indices[successor] == null) {
                tarjanVisit(successor);
                this.lowlink[node] = Math.min(lowlink[node], lowlink[successor]);
            } else if (this.stack.contains(successor)) {
                this.lowlink[node] = Math.min(lowlink[node], indices[successor]);
            }
        }

        // node is root
        if (this.lowlink[node].equals(this.indices[node])) {
            List<Integer> currentSCC = new ArrayList<>();
            int poppedNode;

            do {
                poppedNode = this.stack.pop();
                this.sccSearches[poppedNode] = this.sccIndex;
                currentSCC.add(convert(poppedNode));
            } while (poppedNode != node);

            if (currentSCC.size() != 0) {
                this.scc[sccIndex] = currentSCC;
                sccIndex++;
            }
        }
    }

    // evaluate satisfiability using SCC
    public boolean evalSat() {
        for (int i = 0; i < this.sccIndex; i++) {
            for (Integer component : this.scc[i]) {
                if (this.scc[i].contains(-component))
                    return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    // calculate edges in SCC graph
    private void getSCCEdges() {
        this.sccEdges = (ArrayList<Integer>[]) new ArrayList[this.sccIndex];
        for (int i = 0; i < this.sccIndex; i++) {
            this.sccEdges[i] = new ArrayList<>();
            for (int j : this.scc[i])
                for (int k : this.edges[convert(j)])
                    if (!this.sccEdges[i].contains(this.sccSearches[convert(k)])
                            && this.sccSearches[convert(k)] != i) {
                        this.sccEdges[i].add(this.sccSearches[convert(k)]);
                    }
        }
    }

    private int convert(int i) {
        if (i < 0) return this.numOfVars - i;
        else if (i > this.numOfVars) return -(i - this.numOfVars);
        else return i;
    }

}
