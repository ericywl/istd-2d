package sat.prototype;


import java.util.Map;

public class BooleanAssignment {
    public static String convert(Map<Integer, Integer> env) {
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
}
