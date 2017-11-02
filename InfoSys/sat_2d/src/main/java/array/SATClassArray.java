package array;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SATClassArray {
    private Map<Integer, Boolean> assignments = new HashMap<>();

    public void assignTrue(int literal) {
        boolean assignment = literal > 0;
        this.assignments.put(Math.abs(literal), assignment);
    }

    public void assignFalse(int literal) {
        boolean assignment = literal < 0;
        this.assignments.put(literal, assignment);
    }

    public Map<Integer, Boolean> getAssignments() {
        return assignments;
    }

    @SuppressWarnings("unchecked")
    public int[][] reduceLiteral(int literal, int[][] clauses) {
        Set<Integer> literalClauses = findLiteralClauses(clauses, literal);
        int len = clauses.length;

        int[][] firstPass = new int[len][3];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 3; j++) {
                if (clauses[i][j] == -literal) {
                    firstPass[i][j] = 0;
                } else {
                    firstPass[i][j] = clauses[i][j];
                }
            }
        }

        int[][] secondPass = new int[len - literalClauses.size()][3];
        for (int i = 0, x = 0; i < len; i++) {
            if (!literalClauses.contains(i)) {
                for (int j = 0; j < 3; j++) {
                    secondPass[x][j] = firstPass[i][j];
                }

                x++;
            }
        }

        return secondPass;
    }

    // find index of clauses that the literal is in
    private Set<Integer> findLiteralClauses(int[][] clauses, int target) {
        int len = clauses.length;
        Set<Integer> output = new HashSet<>();

        for (int i = 0; i < len; i++) {
            for (int literal : clauses[i]) {
                if (literal == 0) continue;
                if (literal == target)
                    output.add(i);
            }
        }

        return output;
    }
}