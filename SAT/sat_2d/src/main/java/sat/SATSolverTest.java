package sat;


import static org.junit.Assert.*;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import sat.env.*;
import sat.formula.*;

public class SATSolverTest {
    public static void main(String[] args) {
        String readFile = "/sat_2d/sampleCNF/largeSat.cnf";
        String writeFile = "/sat_2d/sampleCNF/largeSatBooleanAssignment.txt";
        // String readFile = "/sat_2d/sampleCNF/s8Sat.cnf";
        // String writeFile = "/sat_2d/sampleCNF/s8SatBooleanAssignment.txt";

        try {
            System.out.println("Reading " + readFile + "...\n");
            Formula formula = ReadCNF.readCNF(readFile);

            System.out.println("SAT solver starts!!!");
            long started = System.nanoTime();
            Environment e = SATSolver.solve(formula);
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Time: " + timeTaken/1000000.0 + "ms\n");

            if (e != null) {
                System.out.println("Writing to " + writeFile + "...");
                WriteEnv.writeEnv(e, writeFile);
            }

            System.out.println("DONE");

        } catch (FileNotFoundException ex) {
            System.out.println(readFile + " not found!");
        } catch (IllegalArgumentException ex) {
            System.out.println(readFile + " is not CNF format!");
        } catch (IOException ex) {
            System.out.println("Writing error!");
        }
    }


    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    @Test
    public void testSATSolver1() {
        // (a v b)
        Environment e = SATSolver.solve(makeFm(makeCl(a, b)));
        assertTrue("one of the literals should be set to true",
                Bool.TRUE == e.get(a.getVariable()) || Bool.TRUE == e.get(b.getVariable()));
    }

    @Test
    public void testSATSolver2() {
        // (~a)
        Environment e = SATSolver.solve(makeFm(makeCl(na)));
        assertEquals(Bool.FALSE, e.get(na.getVariable()));
    }

    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }

        return f;
    }

    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }

        return c;
    }
}