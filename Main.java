package analyzer;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        String method = "--RK";
        String pathToFiles = args[0];
        String patoToPatternsDataBase = args[1];
        HashMap<String, String[]> patternResultMap = new HashMap<>();
        PatternHashMapCreator.createPatternHashMap(patternResultMap, patoToPatternsDataBase);

        FileTypeChecker fileTypeChecker = new FileTypeChecker(patternResultMap, method);

        Map<String, String> typesOfFile = fileTypeChecker.checkType(pathToFiles);

        for (Map.Entry<String, String> entry : typesOfFile.entrySet()) {
            System.out.printf(entry.getKey() + ": " + entry.getValue() + "\n");
        }
    }
}
