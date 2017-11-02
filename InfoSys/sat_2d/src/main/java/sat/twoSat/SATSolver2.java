package sat.twoSat;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SATSolver2 {
    private int[][] tempClauses;

    private int[] clauseSize;
    private boolean[] trueClause;
    private Map<Integer, Integer> assignments = new HashMap<>();
    private Map<Integer, Integer> literalOccurrences = new HashMap<>();

    public SATSolver2(int[][] clauses, int numOfVars) {
        this.tempClauses = clauses;

        Map<Integer, Set<Integer>> literalClausesMap
                = findLiteralClauses(clauses);

        unitPropagation(literalClausesMap);
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, Integer> solve() {
        // empty clause -> not satisfiable
        if (this.hasEmptyClause(tempClauses)) return null;

            // empty list of clauses -> trivially satisfiable
        else if (this.noClauses(trueClause))
            return this.assignments;

            // proceed to use SCC to solve
        else {
            Graph g = new Graph(tempClauses, this.assignments);
            if (g.solve()) return g.getAssignments();

            return null;
        }
    }

    // apply unit propagation
    private void unitPropagation(Map<Integer, Set<Integer>> literalClausesMap) {
        boolean unitClauseFound = true;
        while (unitClauseFound) {
            unitClauseFound = false;
            for (int i = 0; i < tempClauses.length; i++) {
                if (clauseSize[i] == 1) {
                    int literal = getLiteral(tempClauses[i]);
                    if (literal != 0 && !trueClause[i]) {
                        reduceLiteral(literal, literalClausesMap);
                        unitClauseFound = true;
                    }
                }
            }
        }
    }

    // get non-zero literal
    private int getLiteral(int[] clause) {
        if (clause[0] != 0) return clause[0];

        return clause[1];
    }

    // check if there's any empty clause
    private boolean hasEmptyClause(int[][] clauses) {
        for (int i = 0; i < clauses.length; i++) {
            boolean emptyClause = true;
            if (!trueClause[i]) {
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

    // check if formula is empty
    private boolean noClauses(boolean[] cRem) {
        for (boolean removed : cRem) {
            if (!removed) return false;
        }

        return true;
    }

    // reduce literal by binding it to TRUE
    private void reduceLiteral(int literal, Map<Integer, Set<Integer>> literalClauses) {
        int index = Math.abs(literal);
        int assignment = literal < 0 ? -1 : 1;
        int trueMapPosition = literal < 0 ? -index : index;
        int falseMapPosition = literal < 0 ? index : -index;

        // put the assignment to the bindings map
        this.assignments.put(index, assignment);

        Set<Integer> trueClauses
                = literalClauses.getOrDefault(trueMapPosition, new HashSet<Integer>());
        Set<Integer> falseClauses
                = literalClauses.getOrDefault(falseMapPosition, new HashSet<Integer>());

        // remove clause with literal from formula because its TRUE
        for (int clauseIndex : trueClauses) {
            this.trueClause[clauseIndex] = true;
            for (int currLit : tempClauses[clauseIndex]) {
                if (currLit == 0) continue;

                int currLitOccur = this.literalOccurrences.getOrDefault(currLit, 0);
                if (currLitOccur != 0) {
                    this.literalOccurrences.put(currLit, currLitOccur - 1);
                }
            }
        }

        // remove literal from clauses that contains it because its FALSE
        for (int clauseIndex : falseClauses) {
            for (int j = 0; j < 2; j++) {
                if (tempClauses[clauseIndex][j] == -literal) {
                    tempClauses[clauseIndex][j] = 0;
                    clauseSize[clauseIndex]--;

                    int negLitOccur = this.literalOccurrences.getOrDefault(-literal, 0);
                    if (negLitOccur != 0)
                        this.literalOccurrences.put(-literal, negLitOccur - 1);
                }
            }
        }
    }

    // map literals to the clauses that they are in
    private Map<Integer, Set<Integer>> findLiteralClauses(int[][] clauses) {
        int len = clauses.length;
        this.trueClause = new boolean[len];
        this.clauseSize = new int[len];
        Map<Integer, Set<Integer>> output = new HashMap<>();

        for (int i = 0; i < len; i++) {
            for (int literal : clauses[i]) {
                if (literal == 0) continue;

                if (!output.containsKey(literal)) {
                    output.put(literal, new HashSet<Integer>());
                }

                output.get(literal).add(i);
                int occurrence = this.literalOccurrences.getOrDefault(literal, 0);
                this.literalOccurrences.put(literal, occurrence + 1);
            }

            clauseSize[i] = getClauseSize(clauses[i]);
        }

        return output;
    }

    // get size of clause
    private int getClauseSize(int[] clause) {
        int size = 0;
        for (int lit : clause) {
            if (lit != 0) size++;
        }

        return size;
    }
}
