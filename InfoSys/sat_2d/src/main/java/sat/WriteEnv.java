package sat;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sat.env.Environment;

public class WriteEnv {
    public static void writeEnv(Environment env, String fileName) throws IOException {
        String envString = env.toString();
        envString = envString.substring(13, envString.length() - 1);
        envString = envString.replace("->", ":");
        envString = envString.replace(", ", "\n");

        String currPath = new File("").getAbsolutePath();
        FileWriter writeFile = new FileWriter(currPath + "/sat_2d/sampleCNF/" + fileName);
        writeFile.write(envString);
        writeFile.close();
    }
}
