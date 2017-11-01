package sat.prototype;


import java.io.FileNotFoundException;

public class SATSolver2Test {
    public static void main(String[] args) {
        try {
            System.out.println("Reading file...");
            Object[] parsed = CNFParser.readCNF("shit.cnf");
            int[][] clauses = (int[][]) parsed[0];
            int numOfVars = (int) parsed[1];

            SATSolver2 sat2 = new SATSolver2(clauses, numOfVars);

            System.out.println("SAT solver starts!!!");
            long started = System.nanoTime();
            System.out.println(sat2.solve());
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Time: " + timeTaken/1000000.0 + "ms");

        } catch (FileNotFoundException | IllegalArgumentException ex) {
            System.out.println(ex);
        }
    }
}
