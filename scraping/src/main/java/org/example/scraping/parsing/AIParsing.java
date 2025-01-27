package org.example.scraping.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;

public class AIParsing {

    public static void initParsing(Path rawTextPath) {
        String python = "python";
        URL resource = AIParsing.class.getClassLoader().getResource("main.py");
        String scriptPath = new File(resource.getFile()).getAbsolutePath();

        String rawTextPathAbsolute = rawTextPath.toFile().getAbsolutePath();
        ProcessBuilder processBuilder = new ProcessBuilder(python, scriptPath, rawTextPathAbsolute, rawTextPathAbsolute);
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Parsing script exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
