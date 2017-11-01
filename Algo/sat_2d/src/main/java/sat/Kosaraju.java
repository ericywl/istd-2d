package sat;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import sat.formula.Literal;

public class Kosaraju {
    public static Map<Literal, Integer> scc(DirectedGraph graph) {
        Stack<Literal> visitOrder = dfsVisit(reverse(graph));

        Map<Literal, Integer> result = new HashMap<>();
        int i = 0;

        while (!visitOrder.isEmpty()) {
            Literal startPoint = visitOrder.pop();
            if (result.containsKey(startPoint)) continue;

            markReachable(startPoint, graph, result, i);
            ++i;
        }

        return result;
    }

    private static DirectedGraph reverse(DirectedGraph graph) {
        DirectedGraph revGraph = new DirectedGraph();

        for (sat.formula.Literal node : graph)
            revGraph.addNode(node);

        for (sat.formula.Literal node : graph) {
            for (sat.formula.Literal endPoint : graph.edgesFrom(node))
                revGraph.addEdge(endPoint, node);
        }

        return revGraph;
    }

    private static Stack<sat.formula.Literal> dfsVisit(DirectedGraph graph) {
        Stack<sat.formula.Literal> result = new Stack<>();
        Set<sat.formula.Literal> visited = new HashSet<>();

        for (sat.formula.Literal node : graph) {
            explore(node, graph, result, visited);
        }

        return result;
    }

    private static void explore(Literal node, DirectedGraph graph,
                                          Stack<Literal> result, Set<Literal> visited) {
        if (visited.contains(node)) return;

        visited.add(node);
        for (Literal endPoint : graph.edgesFrom(node)) {
            explore(endPoint, graph, result, visited);
        }

        result.push(node);
    }

    private static void markReachable(Literal node, DirectedGraph graph,
                                          Map<Literal, Integer> result, int label) {
        if (result.containsKey(node)) return;

        result.put(node, label);
        for (Literal endPoint : graph.edgesFrom(node)) {
            markReachable(endPoint, graph, result, label);
        }
    }
}
