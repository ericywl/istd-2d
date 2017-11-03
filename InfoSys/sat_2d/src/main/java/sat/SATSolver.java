package sat;


import java.util.List;
import java.util.Map;

public class SATSolver {
    private SATClass sat = new SATClass();
    private int numOfVars;

    public SATSolver(int numOfVars) {
        this.numOfVars = numOfVars;
    }

    // solve the SAT problem and return assignments
    public Map<Integer, Boolean> solve(List<Integer>[] clauses) {
        if (!isSolvable(clauses))
            return null;

        for (int i = 1; i <= numOfVars; i++) {
            if (!sat.getAssignments().containsKey(i)) {
                sat.assignTrue(i);
            }
        }

        return sat.getAssignments();
    }

    // check if the SAT problem is solvable before assigning truth values
    private boolean isSolvable(List<Integer>[] clauses) {
        // empty list of clauses -> trivially satisfiable
        if (clauses.length == 0) return true;

        List<Integer> smallestClause = null;
        int minClauseSize = Integer.MAX_VALUE;
        for (List<Integer> clause : clauses) {
            int clauseSize = clause.size();

            // empty clause -> not satisfiable
            if (clauseSize == 0) return false;

            // get the smaller clause of the two
            if (clauseSize < minClauseSize) {
                minClauseSize = clauseSize;
                smallestClause = clause;
            }
        }

        int literal = getLiteral(smallestClause);

        // unit clause
        if (minClauseSize == 1) {
            List<Integer>[] reducedClauses = sat.reduceLiteral(literal, clauses);
            boolean solvable = isSolvable(reducedClauses);
            if (solvable) sat.assignTrue(literal);

            return solvable;
        }

        // if not unit clause, try assigning true then false
        List<Integer>[] reducedClauses = sat.reduceLiteral(literal, clauses);
        boolean solvable = isSolvable(reducedClauses);
        if (solvable) {
            sat.assignTrue(literal);
            return true;
        } else {
            reducedClauses = sat.reduceLiteral(-literal, clauses);
            solvable = isSolvable(reducedClauses);
            if (solvable) {
                sat.assignFalse(literal);
                return true;
            }

            return false;
        }
    }

    // get first literal in clause
    private int getLiteral(List<Integer> clause) {
        for (int lit : clause)
            if (lit != 0)
                return lit;

        // empty clause
        return 0;
    }
}
