package sat;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;

public class ReadCNF {
    private static Clause clause = new Clause();

    public static Formula readCNF(String fileName)
            throws FileNotFoundException, IllegalArgumentException {
        if (!fileName.substring(fileName.length() - 4).equals(".cnf")) {
            throw new IllegalArgumentException();
        }

        Formula formula = new Formula();
        Character[] preamble = {'c', 'p'};
        String s;

        String currPath = new File("").getAbsolutePath();
        FileReader readFile = new FileReader(currPath + "/sat_2d/sampleCNF/" + fileName);
        Scanner reader = new Scanner(readFile);

        while (reader.hasNextLine()) {
            s = reader.nextLine();

            if (s.isEmpty()) {
                reader.nextLine();
            } else if (!Arrays.asList(preamble).contains(s.charAt(0))) {
                formula = addToFormula(s, formula);
            } else if (s.charAt(0) == 'p') {
                if (!s.substring(2, 5).equals("cnf")) throw new IllegalArgumentException();
            }
        }

        reader.close();
        return formula;
    }

    private static Formula addToFormula(String s, Formula formula) {
        Literal literal;

        for (String var : s.split(" ")) {
            if (var.isEmpty()) {
                // do nothing
            } else if (!var.equals("0")) {
                literal = var.charAt(0) == '-'
                        ? NegLiteral.make(var.substring(1)) : PosLiteral.make(var);

                clause = clause.add(literal);
            } else {
                formula = formula.addClause(clause);
                clause = new Clause();
            }
        }

        return formula;
    }
}
