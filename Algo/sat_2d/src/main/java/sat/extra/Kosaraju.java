package sat.extra;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import sat.formula.Literal;

public class Kosaraju {
    public static Map<Literal, Integer> scc(DirectedGraph graph) {
        Stack<Literal> visitOrder = dfsVisit(reverse(graph));
        Map<Literal, Integer> output = new HashMap<>();
        int i = 0;

        while (!visitOrder.isEmpty()) {
            Literal startPoint = visitOrder.pop();
            if (output.containsKey(startPoint)) continue;

            markReachable(startPoint, graph, output,  i);
            ++i;
        }

        System.out.println(i);
        return output;
    }

    private static DirectedGraph reverse(DirectedGraph graph) {
        DirectedGraph revGraph = new DirectedGraph();

        for (Literal node : graph)
            revGraph.addNode(node);

        for (Literal node : graph) {
            for (Literal endPoint : graph.edgesFrom(node))
                revGraph.addEdge(endPoint, node);
        }

        return revGraph;
    }

    private static Stack<Literal> dfsVisit(DirectedGraph graph) {
        Stack<Literal> result = new Stack<>();
        Set<Literal> visited = new HashSet<>();

        for (Literal node : graph) {
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
                                      Map<Literal, Integer> sccMap, int label) {
        if (sccMap.containsKey(node)) return;

        sccMap.put(node, label);
        for (Literal endPoint : graph.edgesFrom(node)) {
            markReachable(endPoint, graph, sccMap, label);
        }
    }
}
