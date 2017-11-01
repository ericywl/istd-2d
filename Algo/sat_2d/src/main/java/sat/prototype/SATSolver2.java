package sat.prototype;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SATSolver2 {
    private int numOfVars;

    private int[] clauseSize;
    private int[][] origClauses;
    private int[][] tempClauses;

    private boolean[] clauseRemoved;
    private Map<Integer, Integer> assignments = new HashMap<>();
    private Map<Integer, Integer> literalOccurrences = new HashMap<>();
    private Map<Integer, List<Integer>> literalClauses;

    public SATSolver2(int[][] clauses, int numOfVars) {
        this.origClauses = clauses;
        this.tempClauses = clauses;
        this.numOfVars = numOfVars;
        this.literalClauses = findLiteralClauses(this.tempClauses, this.numOfVars);
        preProcess();
    }

    public Map<Integer, Integer> solve() {
        if (this.hasEmptyClause(tempClauses)) return null;
        else if (this.noClauses(clauseRemoved)) return assignments;
        else {
            Graph graph = new Graph(numOfVars, tempClauses, clauseRemoved);
            return graph.solve(assignments);
        }
    }

    private void preProcess() {
        while (eliminateUnitClauses() || eliminatePureLiterals()) {

        }
    }

    private boolean eliminateUnitClauses() {
        boolean unitClauseFound = true;
        while (unitClauseFound)
        {
            unitClauseFound = false;
            for (int i = 0; i < tempClauses.length; i++) {
                if (clauseSize[i] == 1) {
                    int var = getLiteral(tempClauses[i]);
                    if (var != 0 && !clauseRemoved[i]) {
                        reduceLiteral(var);
                        unitClauseFound = true;
                    }
                }
            }
        }

        return unitClauseFound;
    }

    private boolean eliminatePureLiterals() {
        for (int lit = 1; lit <= numOfVars; lit++) {
            int positiveOccurrences = getOccurrenceOfLiteral(lit);
            int negativeOccurrences = getOccurrenceOfLiteral(-lit);
            if (positiveOccurrences == 0 && negativeOccurrences != 0) {
                reduceLiteral(-lit);
                return true;
            } else if (positiveOccurrences != 0 && negativeOccurrences == 0) {
                reduceLiteral(lit);
                return true;
            }
        }

        return false;
    }

    private int getLiteral(int[] clause) {
        for (int literal : clause) {
            if (literal != 0) {
                return literal;
            }
        }

        return 0;
    }

    private int getOccurrenceOfLiteral(int literal) {
        int index = literal < 0 ? Math.abs(literal) + numOfVars - 1 : literal - 1;
        return this.literalOccurrences.getOrDefault(index, 0);
    }

    private int getClauseSize(int[] clause) {
        int size = 0;
        for (int lit : clause) {
            if (lit != 0) size++;
        }

        return size;
    }

    private boolean hasEmptyClause(int[][] clauses) {
        for (int i = 0; i < clauses.length; i++) {
            boolean emptyClause = true;
            if (!clauseRemoved[i]) {
                for (int literal : clauses[i]) {
                    if (literal != 0) {
                        emptyClause = false;
                    }
                }

                if (emptyClause) return true;
            }

        }

        return false;
    }

    private boolean noClauses(boolean[] cRem) {
        for (boolean removed : cRem) {
            if (!removed) {
                return false;
            }
        }

        return true;
    }

    private void reduceLiteral(int literal) {
        int index;
        int assignment;
        int literalOccIndex;
        int posMapPosition;
        int negMapPosition;

        if (literal < 0) {
            index = -literal - 1;
            assignment = -1;
            literalOccIndex = Math.abs(literal) + numOfVars - 1;
            posMapPosition = Math.abs(literal) + numOfVars - 1;
            negMapPosition = index;
        } else {
            index = literal - 1;
            assignment = 1;
            literalOccIndex = index;
            posMapPosition = index;
            negMapPosition = Math.abs(literal) + numOfVars - 1;
        }

        this.assignments.put(index, assignment);
        this.literalOccurrences.put(literalOccIndex, 0);

        List<Integer> posLiteralClauses = literalClauses.get(posMapPosition);
        List<Integer> negLiteralClauses = literalClauses.get(negMapPosition);

        if (posLiteralClauses != null) {
            for (int clause : posLiteralClauses) {
                this.clauseRemoved[clause] = true;
                for (int currLit : tempClauses[clause]) {
                    int currLitIndex;
                    if (currLit == 0) continue;

                    if (currLit < 0) {
                        currLitIndex = Math.abs(currLit) + numOfVars - 1;
                    } else {
                        currLitIndex = currLit - 1;
                    }

                    int currLitOccur = this.literalOccurrences.get(currLitIndex);
                    if (currLitOccur != 0) {
                        this.literalOccurrences.put(currLitIndex, currLitOccur - 1);
                    }
                }
            }
        }

        if (negLiteralClauses != null) {
            for (int clause : negLiteralClauses) {
                for (int j = 0, len = tempClauses[clause].length; j < len; j++) {
                    if (tempClauses[clause][j] == -literal) {
                        tempClauses[clause][j] = 0;
                        clauseSize[clause]--;
                        int negLitIndex = -literal < 0 ? literal - 1 : -literal - 1;
                        int negLitOccur = this.literalOccurrences.get(negLitIndex);
                        this.literalOccurrences.put(negLitIndex, negLitOccur - 1);
                    }
                }
            }
        }
    }

    private Map<Integer, List<Integer>> findLiteralClauses(int[][] clauses, int num) {
        int len = clauses.length;
        this.clauseRemoved = new boolean[len];
        this.clauseSize = new int[len];
        Map<Integer, List<Integer>> output = new HashMap<>();

        for (int i = 0; i < len; i++) {
            for (int literal : clauses[i]) {
                int position = literal > 0 ? literal - 1 : Math.abs(literal) + num - 1;

                if (!output.containsKey(position)) {
                    output.put(position, new ArrayList<>());
                }

                output.get(position).add(i);
                int occurrence = this.literalOccurrences.getOrDefault(position, 0);
                this.literalOccurrences.put(position, occurrence + 1);
            }

            clauseSize[i] = getClauseSize(clauses[i]);
        }

        return output;
    }
}
