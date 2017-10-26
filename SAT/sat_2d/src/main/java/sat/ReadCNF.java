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
    public static Formula readCNF(String fileName) throws FileNotFoundException {
        Formula formula = new Formula();
        Character[] preamble = {'c', 'p'};
        String s;

        String currPath = new File("").getAbsolutePath();
        FileReader readFile = new FileReader(currPath + fileName);
        Scanner reader = new Scanner(readFile);

        while (reader.hasNextLine()) {
            s = reader.nextLine();

            if (!Arrays.asList(preamble).contains(s.charAt(0))) {
                formula = addToFormula(s, formula);
            }
        }

        reader.close();
        return formula;
    }

    private static Formula addToFormula(String s, Formula formula) {
        Clause clause = new Clause();
        Literal literal;

        for (String num : s.split(" ")) {
            if (num.isEmpty()) {
                // do nothing
            } else if (!num.equals("0")) {
                literal = num.charAt(0) == '-'
                        ? NegLiteral.make(num.substring(1)) : PosLiteral.make(num);

                clause = clause.add(literal);
            } else {
                formula = formula.addClause(clause);
                clause = new Clause();
            }
        }

        return formula;
    }
}
