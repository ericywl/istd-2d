package sat.array;

import java.util.Map;

public class SATSolverArrayTest {
    public static void main(String[] args) {
        try {
            String readFile = "randomKSat.cnf";
            String writeFile = readFile.substring(0, readFile.length() - 4) + "Bool.txt";
            int[][] clauses = ReadCNFArray.readCNF(readFile);
            SATSolverArray sat2 = new SATSolverArray();

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
}
