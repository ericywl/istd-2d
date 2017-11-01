package sat.twoSat;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SATSolver2 {
    private int numOfVars;

    private int[] clauseSize;
    private int[][] tempClauses;

    private boolean[] clauseRemoved;
    private Map<Integer, Integer> assignments = new HashMap<>();
    private Map<Integer, Integer> literalOccurrences = new HashMap<>();
    private Map<Integer, List<Integer>> literalClauses;

    public SATSolver2(int[][] clauses, int numOfVars) {
        this.tempClauses = clauses;
        this.numOfVars = numOfVars;
        this.literalClauses = findLiteralClauses(this.tempClauses, this.numOfVars);
        preProcess();
    }

    public Map<Integer, Integer> solve() {
        if (this.hasEmptyClause(tempClauses)) {
            return null;
        } else if (this.noClauses(clauseRemoved)) {
            return assignments;
        } else {
            Graph graph = new Graph(numOfVars, tempClauses, clauseRemoved);
            return graph.solve(assignments);
        }
    }

    private void preProcess() {
        boolean unitClauseFound = true;
        while (unitClauseFound) {
            unitClauseFound = false;
            for (int i = 0; i < tempClauses.length; i++) {
                if (clauseSize[i] == 1) {
                    int literal = getLiteral(tempClauses[i]);
                    if (literal != 0 && !clauseRemoved[i]) {
                        reduceLiteral(literal);
                        unitClauseFound = true;
                    }
                }
            }
        }
    }

    private int getLiteral(int[] clause) {
        for (int literal : clause) {
            if (literal != 0) {
                return literal;
            }
        }

        return 0;
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
            if (!removed) return false;
        }

        return true;
    }

    private void reduceLiteral(int literal) {
        int index = Math.abs(literal);
        int assignment = literal < 0 ? -1 : 1;
        int trueMapPosition = literal < 0 ? -index : index;
        int falseMapPosition = literal < 0 ? index : -index;

        this.assignments.put(index, assignment);
        this.literalOccurrences.put(literal, 0);

        List<Integer> trueClauses =
                literalClauses.getOrDefault(trueMapPosition, new ArrayList<Integer>());
        List<Integer> falseClauses =
                literalClauses.getOrDefault(falseMapPosition, new ArrayList<Integer>());

        for (int clause : trueClauses) {
            this.clauseRemoved[clause] = true;
            for (int currLit : tempClauses[clause]) {
                if (currLit == 0) continue;

                int currLitOccur = this.literalOccurrences.get(currLit);
                if (currLitOccur != 0) {
                    this.literalOccurrences.put(currLit, currLitOccur - 1);
                }
            }
        }

        for (int clause : falseClauses) {
            for (int j = 0, len = tempClauses[clause].length; j < len; j++) {
                if (tempClauses[clause][j] == -index) {
                    tempClauses[clause][j] = 0;
                    clauseSize[clause]--;
                    int negLitIndex = -index;
                    int negLitOccur = this.literalOccurrences.get(negLitIndex);
                    if (negLitOccur != 0)
                        this.literalOccurrences.put(negLitIndex, negLitOccur - 1);
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
                if (literal == 0) continue;

                if (!output.containsKey(literal)) {
                    output.put(literal, new ArrayList<Integer>());
                }

                output.get(literal).add(i);
                int occurrence = this.literalOccurrences.getOrDefault(literal, 0);
                this.literalOccurrences.put(literal, occurrence + 1);
            }

            clauseSize[i] = getClauseSize(clauses[i]);
        }

        return output;
    }
}
