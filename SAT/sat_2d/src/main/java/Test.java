import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import sat.formula.Clause;
import sat.formula.Formula;

public class Test {
    public static void main(String[] args) {
        String filePath = new File("").getAbsolutePath() + "\\sat_2d\\sampleCNF\\largeSat.cnf";
        File file = new File(filePath);

        try {
            Scanner reader = new Scanner(file);
            Character[] preamble = {'c', 'p'};

            String s;
            Formula formula = new Formula();

            while (reader.hasNextLine()) {
                s = reader.nextLine();

                if (!Arrays.asList(preamble).contains(s.charAt(0))) {
                    formula = problem(s, formula);
                }
            }

            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Formula problem(String s, Formula formula) {
        Clause clause = new Clause();

        for (String splitLine : s.split(" ")) {

        }
    }
}
