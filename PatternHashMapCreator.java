package analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;

public class PatternHashMapCreator {
    public static void createPatternHashMap(HashMap<String, String[]> patternTypeMap,
                                           String pathToPatternsDataBase) {
        String stringFile = "";
        try {
            stringFile = new String(Files.readAllBytes(Paths.get(pathToPatternsDataBase)));
        } catch (IOException e) {
            System.out.println("Such file doesn't exist!");
        }
        String[] lines = stringFile.split("\n");
        ExecutorService executor = Executors.newFixedThreadPool(8);
        List<Callable<String[]>> callables = new ArrayList<>();
        for (String line : lines) {
            Callable<String[]> callable = () -> {
                return line.split(";");
            };
            callables.add(callable);
        }
        List<Future<String[]>> futures = null;
        try {
            futures = executor.invokeAll(callables);
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
        }
        List<String[]> listOfResults = new ArrayList<>();
        for (Future<String[]> future : futures) {
            try {
                listOfResults.add(future.get());
            } catch (ExecutionException e) {
                System.out.println("Execution Excepton!");
            } catch (InterruptedException e) {
                System.out.println("Interrupted!");
            }
        }
        int length = listOfResults.size();
        for (int i = 0; i < length; ++i) {
            String[] arr = listOfResults.get(i);
            String pattern = removeQuotes(arr[1]);
            String[] priorityAndType = new String[2];
            priorityAndType[0] = arr[0];
            priorityAndType[1] = removeQuotes(arr[2]);
            patternTypeMap.put(pattern, priorityAndType);
        }
    }

    private static String removeQuotes(String string) {
        int length = string.length();
        if (string.charAt(0) == '\"' && string.charAt(length - 1) == '\"') {
            return string.substring(1, length - 1);
        } else {
            return string;
        }
    }
}
