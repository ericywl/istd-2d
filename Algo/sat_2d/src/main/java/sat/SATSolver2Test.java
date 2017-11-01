package sat;


import java.io.FileNotFoundException;
import java.util.Map;

public class SATSolver2Test {
    public static void main(String[] args) {
        try {
            System.out.println("Reading file...");
            Object[] parsed = CNFParser.readCNF("testcase.cnf");
            int[][] clauses = (int[][]) parsed[0];
            int numOfVars = (int) parsed[1];

            System.out.println("SAT solver starts!!!");
            long started = System.nanoTime();
            SATSolver2 sat2 = new SATSolver2(clauses, numOfVars);
            Map<Integer, Integer> env = sat2.solve();
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Time: " + timeTaken/1000000.0 + "ms\n");

            if (env != null) {
                System.out.println("SATISFIABLE");
                System.out.println(BooleanAssignment.convert(env));
            } else System.out.println("NOT SATISFIABLE");

        } catch (FileNotFoundException | IllegalArgumentException ex) {
            System.out.println(ex);
        }
    }
}
