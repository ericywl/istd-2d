package sat;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import test.env.Environment;

public class WriteENV {
    public static <T> void writeENV(Map<Integer, T> env, String fileName) throws IOException {
        // replace the following to match the required assignment format
        String envString = env.toString();
        envString = envString.substring(1, envString.length() - 1);
        envString = envString.replace("false", "FALSE");
        envString = envString.replace("true", "TRUE");
        envString = envString.replace("=", ":");
        envString = envString.replace(", ", "\n");

        // writes to the file called <fileName>Bool.txt
        String currPath = new File("").getAbsolutePath();
        FileWriter writeFile = new FileWriter(currPath + "/sat_2d/sampleCNF/" + fileName);
        writeFile.write(envString);
        writeFile.close();
    }
}
