package sat;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class SATSolver2Test {
    public static void main(String[] args) {
        String readFile = "2Sat.cnf";
        String writeFile = readFile.substring(0, readFile.length() - 4) + "Bool.txt";

        try {
            System.out.println("Reading " + readFile + "...\n");
            Object[] parsed = CNFParser.readCNF(readFile);
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
                System.out.println("Writing to " + writeFile + "...");
                BooleanAssignment.writeAssignments(env, writeFile);
            } else System.out.println("NOT SATISFIABLE");

            System.out.println("DONE");

        } catch (FileNotFoundException ex) {
            System.out.println(readFile + " not found!");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println("IO Error!");
        }
    }
}
