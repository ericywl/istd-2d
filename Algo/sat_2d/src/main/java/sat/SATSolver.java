package sat;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     *
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     * null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        ImList<Clause> clauses = formula.getClauses();
        Environment newEnv = new Environment();
        return solve(clauses, newEnv);
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     *
     * @param clauses formula in conjunctive normal form
     * @param env     assignment of some or all variables in clauses to true or
     *                false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     * or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        return env;
    }

    public static boolean isSatisfiable(ImList<Clause> clauses) {
        Set<Variable> variables = new HashSet<>();
        for (Clause clause : clauses) {
            if (!clause.isUnit()) {
                variables.add(clause.chooseLiteral().getVariable());
                variables.add(clause.chooseLiteral2().getVariable());
            }
        }

        DirectedGraph implications = new DirectedGraph();
        for (Variable var : variables) {
            implications.addNode(PosLiteral.make(var));
            implications.addNode(NegLiteral.make(var));
        }

        for (Clause clause : clauses) {
            if (!clause.isUnit()) {
                implications.addEdge(clause.chooseLiteral().getNegation(),
                    clause.chooseLiteral2());
                implications.addEdge(clause.chooseLiteral2().getNegation(),
                        clause.chooseLiteral());
            }
        }

        Map<Literal, Integer> scc = Kosaraju.scc(implications);

        for (Variable var : variables) {
            Literal posLiteral = PosLiteral.make(var);
            Literal negLiteral = NegLiteral.make(var);
            if (scc.get(posLiteral).equals(scc.get(negLiteral)))
                return false;
        }

        return true;
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses , a list of clauses
     * @param literal       , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses, Literal literal) {
        if (clauses.isEmpty()) return clauses;

        ImList<Clause> subClauses = new EmptyImList<>();
        for (Clause clause : clauses) {
            Clause reducedClause = clause.reduce(literal);
            if (reducedClause != null) subClauses = subClauses.add(reducedClause);
        }

        return subClauses;
    }

}
