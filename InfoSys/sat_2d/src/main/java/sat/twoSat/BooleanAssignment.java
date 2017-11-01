package sat.twoSat;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class BooleanAssignment {
    private static String convert(Map<Integer, Integer> env) {
        StringBuilder output = new StringBuilder();
        String current;

        for (int literal : env.keySet()) {
            if (literal < 0) {
                 current = "" + (-literal) + "=";
            } else {
                current = "" + literal + "=";
            }

            current += env.get(literal) < 0 ? "FALSE" : "TRUE";
            output.append(current);
            output.append("\n");
        }

        return output.toString();
    }

    public static void writeAssignments(Map<Integer, Integer> env, String fileName)
            throws IOException {
        String currPath = new File("").getAbsolutePath();
        FileWriter writeFile = new FileWriter(currPath + "/sat_2d/sampleCNF/" + fileName);

        String assigString = convert(env);
        writeFile.write(assigString);
        writeFile.close();
    }
}
