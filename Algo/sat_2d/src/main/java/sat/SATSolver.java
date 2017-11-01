package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

public class SATSolver {
    private static boolean solved = false;
    private static ImList<Clause> tempClauses;
    private static Environment tempEnv;
    private static Literal lit;
    private static Variable var;

    public static Environment solve(Formula formula) {
        ImList<Clause> clauses = formula.getClauses();
        Environment newEnv = new Environment();
        Environment assignment = preSolve(clauses, newEnv);

        if (assignment == null) return null;
        if (solved) return assignment;

        return SCC.solve2Sat(clauses, assignment);
    }

    private static Environment preSolve(ImList<Clause> clauses, Environment env) {
        if (clauses.isEmpty()) {
            solved = true;
            return env;
        }

        for (Clause clause : clauses) {
            if (clause.size() == 0) return null;

            if (clause.isUnit()) {
                lit = clause.chooseLiteral();
                var = lit.getVariable();
                tempClauses = substitute(clauses, lit);
                tempEnv = (lit instanceof PosLiteral) ?
                        env.putTrue(var) : env.putFalse(var);
                return preSolve(tempClauses, tempEnv);
            }
        }

        return env;
    }

    static ImList<Clause> substitute(ImList<Clause> clauses, Literal literal) {
        if (clauses.isEmpty()) return clauses;

        ImList<Clause> subClauses = new EmptyImList<>();
        for (Clause clause : clauses) {
            Clause reducedClause = clause.reduce(literal);
            if (reducedClause != null) subClauses = subClauses.add(reducedClause);
        }

        return subClauses;
    }

}
