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
    public static Formula readCNF(String fileName)
            throws FileNotFoundException, IllegalArgumentException {
        if (!fileName.substring(fileName.length() - 4).equals(".cnf")) {
            throw new IllegalArgumentException();
        }

        Formula formula = new Formula();
        Character[] preamble = {'c', 'p'};
        String problemLine;

        String currPath = new File("").getAbsolutePath();
        FileReader readFile = new FileReader(currPath + "/sat_2d/sampleCNF/" + fileName);
        Scanner reader = new Scanner(readFile);

        while (reader.hasNextLine()) {
            problemLine = reader.nextLine();

            if (problemLine.isEmpty()) {
                reader.nextLine();
            } else if (!Arrays.asList(preamble).contains(problemLine.charAt(0))) {
                formula = addToFormula(problemLine, formula);
            } else if (problemLine.charAt(0) == 'p') {
                if (!problemLine.substring(2, 5).equals("cnf")) throw new IllegalArgumentException();
            }
        }

        reader.close();
        return formula;
    }

    private static Formula addToFormula(String problemLine, Formula formula) {
        Clause clause = new Clause();
        Literal literal;

        for (String param : problemLine.split(" ")) {
            if (param.isEmpty()) {
                continue;
            }

            if (!param.equals("0")) {
                literal = param.charAt(0) == '-'
                        ? NegLiteral.make(param.substring(1)) : PosLiteral.make(param);

                clause = clause.add(literal);
            } else {
                formula = formula.addClause(clause);
                clause = new Clause();
            }
        }

        return formula;
    }
}
