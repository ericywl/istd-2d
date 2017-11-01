package sat.extra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import immutable.ImList;
import sat.env.Bool;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;

public class SCC {
    public static Environment solve2Sat(ImList<Clause> clauses, Environment assignment) {
        Set<Variable> variables = new HashSet<>();
        HashMap<Integer, Literal> literals = new HashMap<>();
        int numOfLiterals = 0;
        for (Clause clause : clauses) {
            Literal firstLit = clause.chooseLiteral();
            literals.put(numOfLiterals, firstLit);
            variables.add(firstLit.getVariable());

            numOfLiterals++;
            if (!clause.isUnit()) {
                Literal secondLit = clause.chooseLiteral2();
                literals.put(numOfLiterals, secondLit);
                variables.add(secondLit.getVariable());

                numOfLiterals++;
            }
        }

        DirectedGraph implicationGraph = new DirectedGraph();
        for (Variable var : variables) {
            implicationGraph.addNode(PosLiteral.make(var));
            implicationGraph.addNode(NegLiteral.make(var));
        }

        for (Clause clause : clauses) {
            if (!clause.isUnit()) {
                implicationGraph.addEdge(clause.chooseLiteral().getNegation(),
                        clause.chooseLiteral2());
                implicationGraph.addEdge(clause.chooseLiteral2().getNegation(),
                        clause.chooseLiteral());
            } else {
                implicationGraph.addEdge(clause.chooseLiteral().getNegation(),
                        clause.chooseLiteral());
            }
        }

        Map<Literal, Integer> scc = Kosaraju.scc(implicationGraph);
        System.out.println(scc);

        for (Variable var : variables) {
            Literal posLiteral = PosLiteral.make(var);

            if (scc.get(posLiteral).equals(scc.get(posLiteral.getNegation())))
                return null;
        }

        long start = System.nanoTime();
        for (int i = 0; i < numOfLiterals; i++) {
            Literal lit = literals.get(i);
            Variable var = lit.getVariable();
            if (assignment.get(var) == Bool.UNDEFINED) {
                assignment = (lit instanceof PosLiteral) ?
                        assignment.putTrue(var) : assignment.putFalse(var);
            }
        }
        long timeTaken = System.nanoTime() - start;
        System.out.println(timeTaken/1000000.0 + "ms");

        return assignment;
    }
}
