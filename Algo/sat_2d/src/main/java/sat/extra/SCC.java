package sat.extra;

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
        Stack<Literal> literals = new Stack<>();
        for (Clause clause : clauses) {
            if (!clause.isUnit()) {
                literals.add(clause.chooseLiteral());
                variables.add(clause.chooseLiteral().getVariable());
                literals.add(clause.chooseLiteral2());
                variables.add(clause.chooseLiteral2().getVariable());
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
            }
        }

        Map<Literal, Integer> scc = Kosaraju.scc(implicationGraph);
        System.out.println(scc);

        for (Variable var : variables) {
            Literal posLiteral = PosLiteral.make(var);
            Literal negLiteral = NegLiteral.make(var);

            if (scc.get(posLiteral).equals(scc.get(negLiteral)))
                return null;
        }

        while (!literals.isEmpty()) {
            Literal lit = literals.pop();
            Variable var = lit.getVariable();
            if (assignment.get(var) == Bool.UNDEFINED) {
                assignment = (lit instanceof PosLiteral) ?
                        assignment.putTrue(var) : assignment.putFalse(var);
            }
        }

        return assignment;
    }
}
