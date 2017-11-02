package sat.twoSat;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CNFParser {
    public static int[][] readCNF(String fileName)
            throws IllegalArgumentException, IOException {
        if (!fileName.substring(fileName.length() - 4).equals(".cnf")) {
            throw new IllegalArgumentException("Invalid file format.");
        }

        String currPath = new File("").getAbsolutePath();
        FileReader readFile = new FileReader(currPath + "/sat_2d/sampleCNF/" + fileName);
        Scanner reader = new Scanner(readFile);
        String[] headers;
        String[] params;

        String line = reader.nextLine().trim();
        while (line.startsWith("c") || line.matches("\\s+") || line.isEmpty())
            line = reader.nextLine().trim();

        headers = line.split(" ");
        if (!headers[0].equals("p")) throw new IllegalArgumentException("Missing problem line.");
        if (!headers[1].equals("cnf"))
            throw new IllegalArgumentException("Missing file format declaration.");

        int numOfVars = Integer.parseInt(headers[2]);
        int numOfClauses = Integer.parseInt(headers[3]);
        int[][] clauses = new int[numOfClauses][2];
        int counter = 0;

        outerloop:
        while (reader.hasNextLine()) {
            line = reader.nextLine().trim();

            while (line.startsWith("c") || line.matches("\\s+") || line.isEmpty()) {
                try {
                    line = reader.nextLine();
                } catch (NoSuchElementException ex) {
                    break outerloop;
                }
            }

            params = line.split(" ");
            int innerCounter = 0;
            for (String param : params) {
                if (!param.equals("0")) {
                    int literal = Integer.parseInt(param);
                    clauses[counter][innerCounter] = literal;
                    innerCounter++;
                    if (innerCounter > 2)
                        throw new IllegalArgumentException("File contains more than 2 literals per clause.");
                } else {
                    innerCounter = 0;
                    counter++;
                }
            }
        }

        if (counter != numOfClauses)
            throw new IllegalArgumentException("Wrong number of clauses.");

        reader.close();
        readFile.close();
        return clauses;
    }
}
