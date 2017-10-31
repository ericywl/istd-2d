package sat;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DirectedGraph<E> implements Iterable<E> {
    private final Map<E, Set<E>> mGraph = new HashMap<>();
    private int V = 0;

    public void addNode(E node) {
        if (mGraph.containsKey(node)) return;

        mGraph.put(node, new HashSet<>());
        V++;
    }

    public void addEdge(E start, E dest) {
        mGraph.get(start).add(dest);
    }

    public void removeEdge(E start, E dest) {
        mGraph.get(start).remove(dest);
    }

    public boolean edgeExists(E start, E end) {
        return mGraph.get(start).contains(end);
    }

    public Set<E> edgesFrom(E node) {
        Set<E> arcs = mGraph.get(node);

        return Collections.unmodifiableSet(arcs);
    }

    public int getV() {
        return this.V;
    }

    @Override
    public Iterator<E> iterator() {
        return mGraph.keySet().iterator();
    }

    static class NoSuchElement extends Exception {
        NoSuchElement(String message) {
            super(message);
        }
    }
}
