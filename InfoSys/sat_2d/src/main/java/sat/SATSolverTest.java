package sat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import sat.array.ReadCNFArray;
import sat.array.SATSolverArray;
import sat.twoSat.BooleanAssignment;
import sat.twoSat.CNFParser;
import sat.twoSat.SATSolver2;

@SuppressWarnings("unchecked")
public class SATSolverTest {
    private static String readFile = "largeSat.cnf";
    private static String writeFile = readFile.substring(0, readFile.length() - 4) + "Bool.txt";

    public static void main(String[] args) {
        try {
            Object[] parsed = ReadCNF.readCNF(readFile);
            List<Integer>[] clauses = (List<Integer>[]) parsed[0];
            int maxClauseSize = (int) parsed[1];

            if (maxClauseSize < 3) {
                run2SAT();
                return;
            }

            if (maxClauseSize == 3) {
                run3SAT();
                return;
            }

            SATSolver sat2 = new SATSolver();

            System.out.println("SAT solver starts!!!");
            long started = System.nanoTime();
            Map<Integer, Boolean> env = sat2.solve(clauses);
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Time: " + timeTaken / 1000000.0 + "ms\n");

            if (env != null) {
                System.out.println("SATISFIABLE\n");
                System.out.println(env);
            } else System.out.println("NOT SATISFIABLE\n");

            System.out.println("DONE");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void run2SAT() throws IOException {
        int[][] clauses = CNFParser.readCNF(readFile);

        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();
        SATSolver2 sat = new SATSolver2(clauses);
        Map<Integer, Integer> env = sat.solve();
        long time = System.nanoTime();
        long timeTaken = time - started;
        System.out.println("Time: " + timeTaken / 1000000.0 + "ms\n");

        if (env != null) {
            System.out.println("SATISFIABLE\n");
            System.out.println("Writing to " + writeFile + "...");
            BooleanAssignment.writeAssignments(env, writeFile);
        } else System.out.println("NOT SATISFIABLE\n");

        System.out.println("DONE");
    }

    private static void run3SAT() throws IOException {
        int[][] clauses = ReadCNFArray.readCNF(readFile);

        SATSolverArray sat = new SATSolverArray();

        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();
        Map<Integer, Boolean> env = sat.solve(clauses);
        long time = System.nanoTime();
        long timeTaken = time - started;
        System.out.println("Time: " + timeTaken / 1000000.0 + "ms\n");

        if (env != null) {
            System.out.println("SATISFIABLE\n");
            System.out.println(env);
        } else System.out.println("NOT SATISFIABLE\n");

        System.out.println("DONE");
    }
}
