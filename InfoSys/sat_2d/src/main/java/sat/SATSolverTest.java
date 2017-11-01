package sat;


import static org.junit.Assert.*;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import sat.env.*;
import sat.formula.*;

public class SATSolverTest {
    public static void main(String[] args) {
        // String readFile = "testcase.cnf";
        // String readFile = "randomKSat.cnf";
        String readFile = "testcase.cnf";
        // String readFile = "s8Sat.cnf";
        String writeFile = readFile.substring(0, readFile.length() - 4) + "Bool.txt";

        try {
            System.out.println("Reading " + readFile + "...\n");
            Formula formula = ReadCNF.readCNF(readFile);

            System.out.println("SAT solver starts!!!");
            long started = System.nanoTime();
            Environment e = SATSolver.solve(formula);
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Time: " + timeTaken/1000000.0 + "ms");

            if (e != null) {
                System.out.println("SATISFIABLE\n");
                System.out.println("Writing to " + writeFile + "...");
                WriteEnv.writeEnv(e, writeFile);
            } else {
                System.out.println("NOT SATISFIABLE\n");
            }

            System.out.println("DONE");

        } catch (FileNotFoundException ex) {
            System.out.println(readFile + " not found!");
        } catch (IllegalArgumentException ex) {
            System.out.println(readFile + " is not CNF format!");
        } catch (IOException ex) {
            System.out.println("Write Error!");
        }
    }


    private Literal a = PosLiteral.make("a");
    private Literal b = PosLiteral.make("b");
    private Literal c = PosLiteral.make("c");
    private Literal na = a.getNegation();
    private Literal nb = b.getNegation();
    private Literal nc = c.getNegation();

    @Test
    public void testSATSolver1() {
        // (a v b v c)
        Environment e = SATSolver.solve(makeFm(makeCl(a, nb, c)));
        assertTrue("WRONG! A or C has to be true, or B has to be false.",
                Bool.TRUE == e.get(a.getVariable())
                        || Bool.FALSE == e.get(nb.getVariable())
                        || Bool.TRUE == e.get(c.getVariable()));
    }

    @Test
    public void testSATSolver2() {
        // (~a)
        Environment e = SATSolver.solve(makeFm(makeCl(na)));
        System.out.println("A = " + e.get(na.getVariable()));

        assertEquals(Bool.FALSE, e.get(na.getVariable()));
    }

    @Test
    public void testSATSolver3() {
        // (~a v b)
        Clause c1 = makeCl(na, b);
        Environment e = SATSolver.solve(makeFm(c1));
        System.out.println("A = " + e.get(na.getVariable()) + ", B = " + e.get(b.getVariable()));

        assertTrue("WRONG! A has to be false or B has to be true.",
                Bool.FALSE == e.get(na.getVariable())
                        || Bool.TRUE == e.get(b.getVariable()));
    }

    @Test
    public void testSATSolver4() {
        // (a) ^ (~c)
        Clause c1 = makeCl(a);
        Clause c2 = makeCl(nc);
        Environment e = SATSolver.solve(makeFm(c1, c2));
        System.out.println("A = " + e.get(na.getVariable()) + ", C = " + e.get(c.getVariable()));

        assertTrue("WRONG! A has to be false and C has to be true.",
                Bool.TRUE == e.get(a.getVariable())
                        && Bool.FALSE == e.get(nc.getVariable()));
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