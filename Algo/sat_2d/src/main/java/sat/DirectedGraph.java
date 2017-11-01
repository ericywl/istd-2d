package sat;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import sat.formula.Literal;

public class DirectedGraph implements Iterable<Literal> {
    private final Map<Literal, Set<Literal>> mGraph = new HashMap<>();
    private int V = 0;

    public void addNode(Literal node) {
        if (mGraph.containsKey(node)) return;

        mGraph.put(node, new HashSet<>());
        V++;
    }

    public void addEdge(Literal start, Literal dest) {
        if (!mGraph.containsKey(start) || !mGraph.containsKey(dest))
            throw new NoSuchElementException("Both nodes must be in the graph.");

        mGraph.get(start).add(dest);
    }

    public void removeEdge(Literal start, Literal dest) {
        if (!mGraph.containsKey(start) || !mGraph.containsKey(dest))
            throw new NoSuchElementException("Both nodes must be in the graph.");

        mGraph.get(start).remove(dest);
    }

    public boolean edgeExists(Literal start, Literal dest) {
        if (!mGraph.containsKey(start) || !mGraph.containsKey(dest))
            throw new NoSuchElementException("Both nodes must be in the graph.");

        return mGraph.get(start).contains(dest);
    }

    public Set<Literal> edgesFrom(Literal node) {
        Set<Literal> arcs = mGraph.get(node);
        if (arcs == null) throw new NoSuchElementException("Source node does not exist.");

        return Collections.unmodifiableSet(arcs);
    }

    public int getV() {
        return this.V;
    }

    @Override
    public Iterator<Literal> iterator() {
        return mGraph.keySet().iterator();
    }
}
