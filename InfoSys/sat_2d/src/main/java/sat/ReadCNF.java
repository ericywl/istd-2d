package sat;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;

public class ReadCNF {
    private static Clause clause = new Clause();
    private static int maxClauseSize = 0;

    public static Object[] readCNF(String fileName)
            throws IllegalArgumentException, IOException {
        if (!fileName.substring(fileName.length() - 4).equals(".cnf")) {
            throw new IllegalArgumentException();
        }

        Formula formula = new Formula();
        String[] headers;

        String currPath = new File("").getAbsolutePath();
        FileReader readFile = new FileReader(currPath + "/sat_2d/sampleCNF/" + fileName);
        Scanner reader = new Scanner(readFile);

        String line = reader.nextLine().trim();
        while (line.startsWith("c") || line.matches("\\s+") || line.isEmpty())
            line = reader.nextLine().trim();

        headers = line.split(" ");
        if (!headers[0].equals("p")) throw new IllegalArgumentException("Missing problem line.");
        if (!headers[1].equals("cnf"))
            throw new IllegalArgumentException("Missing file format declaration.");

        while (reader.hasNextLine()) {
            line = reader.nextLine().trim();

            if (line.startsWith("c") || line.matches("\\s+") || line.isEmpty()) {
                line = reader.nextLine();
            }

            formula = addToFormula(line, formula);
        }

        reader.close();
        readFile.close();
        return new Object[]{formula, maxClauseSize};
    }

    private static Formula addToFormula(String s, Formula formula) {
        Literal literal;
        int counter = 0;

        for (String var : s.split(" ")) {
            if (var.isEmpty()) {
                // do nothing
            } else if (!var.equals("0")) {
                literal = var.charAt(0) == '-'
                        ? NegLiteral.make(var.substring(1)) : PosLiteral.make(var);

                clause = clause.add(literal);
                counter++;
            } else {
                formula = formula.addClause(clause);
                clause = new Clause();
                if (counter > maxClauseSize) maxClauseSize = counter;
                counter = 0;
            }
        }

        return formula;
    }
}
