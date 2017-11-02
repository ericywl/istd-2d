package sat;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SATClass {
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
    public List<Integer>[] reduceLiteral(int literal, List<Integer>[] clauses) {
        Set<Integer> literalClauses = findLiteralClauses(clauses, literal);
        int len = clauses.length;

        List<Integer> helper = new ArrayList<>();
        List<Integer>[] firstPass = (List<Integer>[]) new ArrayList[len];
        for (int i = 0; i < len; i++) {
            List<Integer> clause = clauses[i];
            for (int lit : clause) {
                if (lit != -literal) helper.add(lit);
            }

            firstPass[i] = helper;
            helper = new ArrayList<>();
        }

        List<Integer>[] secondPass = (List<Integer>[]) new ArrayList[len - literalClauses.size()];
        for (int i = 0, x = 0; i < len; i++) {
            if (!literalClauses.contains(i)) {
                secondPass[x] = firstPass[i];

                x++;
            }
        }

        return secondPass;
    }

    // find index of clauses that the literal is in
    private Set<Integer> findLiteralClauses(List<Integer>[] clauses, int target) {
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
