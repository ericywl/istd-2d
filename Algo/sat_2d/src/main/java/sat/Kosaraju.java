package sat;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Kosaraju {
    public static <E> Map<E, Integer> scc(DirectedGraph<E> graph) {
        Stack<E> visitOrder = dfsVisit(reverse(graph));

        Map<E, Integer> result = new HashMap<>();
        int i = 0;

        while (!visitOrder.isEmpty()) {
            E startPoint = visitOrder.pop();
            if (result.containsKey(startPoint)) continue;

            markReachable(startPoint, graph, result, i);
            ++i;
        }

        return result;
    }

    private static <E> DirectedGraph<E> reverse(DirectedGraph<E> graph) {
        DirectedGraph<E> revGraph = new DirectedGraph<>();

        for (E node : graph)
            revGraph.addNode(node);

        for (E node : graph) {
            for (E endPoint : graph.edgesFrom(node))
                revGraph.addEdge(endPoint, node);
        }

        return revGraph;
    }

    private static <E> Stack<E> dfsVisit(DirectedGraph<E> graph) {
        Stack<E> result = new Stack<>();
        Set<E> visited = new HashSet<>();

        for (E node : graph) {
            explore(node, graph, result, visited);
        }

        return result;
    }

    private static <E> void explore(E node, DirectedGraph<E> graph,
                                    Stack<E> result, Set<E> visited) {
        if (visited.contains(node)) return;

        visited.add(node);
        for (E endPoint : graph.edgesFrom(node)) {
            explore(endPoint, graph, result, visited);
        }

        result.push(node);
    }

    private static <E> void markReachable(E node, DirectedGraph<E> graph,
                                          Map<E, Integer> result, int label) {
        if (result.containsKey(node)) return;

        result.put(node, label);
        for (E endPoint : graph.edgesFrom(node)) {
            markReachable(endPoint, graph, result, label);
        }
    }
}
