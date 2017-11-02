package test;


import static org.junit.Assert.*;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import sat.ReadCNF;
import sat.env.*;
import sat.formula.*;
import sat.twoSat.BooleanAssignment;
import sat.twoSat.CNFParser;
import sat.twoSat.SATSolver2;
import test.env.Bool;
import test.env.Environment;
import test.formula.Clause;
import test.formula.Formula;
import test.formula.Literal;
import test.formula.PosLiteral;

public class SATSolverTestOld {
    private static String readFile = "testcase.cnf";
    private static String writeFile = readFile.substring(0, readFile.length() - 4) + "Bool.txt";

    public static void main(String[] args) {
        try {
            System.out.println("Reading " + readFile + "...\n");
            Object[] parsed = ReadCNF.readCNF(readFile);
            Formula formula = (Formula) parsed[0];
            int maxClauseSize = (int) parsed[1];
            if (maxClauseSize < 2) {
                run2SAT();
                return;
            }

            System.out.println("SAT solver starts!!!");
            long started = System.nanoTime();
            Environment e = SatSolverOld.solve(formula);
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Time: " + timeTaken / 1000000.0 + "ms");

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
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println("IO Error!");
        }
    }

    private static void run2SAT() throws IOException {
        int[][] clauses = CNFParser.readCNF(readFile);

        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();
        SATSolver2 sat2 = new SATSolver2(clauses);
        Map<Integer, Integer> env = sat2.solve();
        long time = System.nanoTime();
        long timeTaken = time - started;
        System.out.println("Time: " + timeTaken/1000000.0 + "ms\n");

        if (env != null) {
            System.out.println("SATISFIABLE\n");
            System.out.println("Writing to " + writeFile + "...");
            BooleanAssignment.writeAssignments(env, writeFile);
        } else System.out.println("NOT SATISFIABLE\n");

        System.out.println("DONE");
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
        Environment e = SatSolverOld.solve(makeFm(makeCl(a, nb, c)));
        assertTrue("WRONG! A or C has to be true, or B has to be false.",
                Bool.TRUE == e.get(a.getVariable())
                        || Bool.FALSE == e.get(nb.getVariable())
                        || Bool.TRUE == e.get(c.getVariable()));
    }

    @Test
    public void testSATSolver2() {
        // (~a)
        Environment e = SatSolverOld.solve(makeFm(makeCl(na)));
        System.out.println("A = " + e.get(na.getVariable()));

        assertEquals(Bool.FALSE, e.get(na.getVariable()));
    }

    @Test
    public void testSATSolver3() {
        // (~a v b)
        Clause c1 = makeCl(na, b);
        Environment e = SatSolverOld.solve(makeFm(c1));
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
        Environment e = SatSolverOld.solve(makeFm(c1, c2));
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