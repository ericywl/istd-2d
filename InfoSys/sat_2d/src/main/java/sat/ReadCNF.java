package sat;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ReadCNF {
    @SuppressWarnings("unchecked")
    public static Object[] readCNF(String fileName)
            throws IllegalArgumentException, IOException {
        if (!fileName.substring(fileName.length() - 4).equals(".cnf")) {
            throw new IllegalArgumentException("Invalid file format.");
        }

        String currPath = new File("").getAbsolutePath();
        FileReader readFile = new FileReader(currPath + "/sat_2d/sampleCNF/" + fileName);
        Scanner reader = new Scanner(readFile);
        List<Integer> helper = new ArrayList<>();
        String[] headers;
        String[] params;

        // skip all comment lines in the front
        String line = reader.nextLine().trim();
        while (line.startsWith("c") || line.matches("\\s+") || line.isEmpty())
            line = reader.nextLine().trim();

        // parse the line that starts with p to get number of variables and clauses
        headers = line.split("\\s+");
        if (!headers[0].equals("p")) throw new IllegalArgumentException("Missing problem line.");
        if (!headers[1].equals("cnf"))
            throw new IllegalArgumentException("Missing file format declaration.");

        int numOfVars = Integer.parseInt(headers[2]);
        int numOfClauses = Integer.parseInt(headers[3]);
        List<Integer>[] clauses = (List<Integer>[]) new ArrayList[numOfClauses];
        int counter = 0;
        int maxClauseSize = 0;

        // proceed to CNF block
        outerloop:
        while (reader.hasNextLine()) {
            line = reader.nextLine().trim();

            // check for comment or empty lines inside the CNF block
            while (line.startsWith("c") || line.matches("\\s+") || line.isEmpty()) {
                try {
                    line = reader.nextLine();
                } catch (NoSuchElementException ex) {
                    break outerloop;
                }
            }

            // parse the split string to get the literals
            // the parser will write to the same clause until 0 is reached
            params = line.split("\\s+");
            for (String param : params) {
                if (param.equals("")) {
                    // do nothing
                } else if (!param.equals("0")) {
                    int literal = Integer.parseInt(param);
                    helper.add(literal);
                } else {
                    clauses[counter] = helper;
                    maxClauseSize = Math.max(maxClauseSize, helper.size());
                    counter++;
                    helper = new ArrayList<>();
                }
            }
        }

        if (counter != numOfClauses) {
            throw new IllegalArgumentException("Wrong number of clauses.");
        }

        reader.close();
        readFile.close();
        return new Object[]{clauses, maxClauseSize, numOfVars};
    }
}

