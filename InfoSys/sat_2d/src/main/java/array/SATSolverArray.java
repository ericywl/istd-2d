package array;


import java.util.List;
import java.util.Map;

public class SATSolverArray {
    private SATClassArray sat = new SATClassArray();

    public Map<Integer, Boolean> solve(int[][] clauses) {
        if (!isSolvable(clauses)) return null;

        return sat.getAssignments();
    }

    public boolean isSolvable(int[][] clauses) {
        // trivially satisfiable if clauses is empty
        if (clauses.length == 0) return true;

        int[] smallestClause = null;
        int minClauseSize = Integer.MAX_VALUE;
        for (int[] clause : clauses) {
            int clauseSize = getClauseSize(clause);

            // not satisfiable if clause is empty
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
            int[][] reducedClauses = sat.reduceLiteral(literal, clauses);
            boolean solvable = isSolvable(reducedClauses);
            if (solvable) sat.assignTrue(literal);

            return solvable;
        }

        // if not unit clause, try assigning true then false
        int[][] reducedClauses = sat.reduceLiteral(literal, clauses);
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

    private int getClauseSize(int[] clause) {
        int size = 0;
        for (int lit : clause)
            if (lit != 0)
                size++;

        return size;
    }

    private int getLiteral(int[] clause) {
        for (int lit : clause)
            if (lit != 0)
                return lit;

        // empty clause
        return 0;
    }
}
