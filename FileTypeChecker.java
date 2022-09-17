package analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;

public class FileTypeChecker {

    private final HashMap<String, String[]> patternResultMap;
    private SubstringFindingMethod method;

    public FileTypeChecker(HashMap<String, String[]> patternResultMap, String method) {
        this.patternResultMap = patternResultMap;
        setMethod(method);
    }

    private void setMethod(String method) {
        switch(method) {
            case "--KMP":
                this.method = new KmpSubstringFindingMethod();
                break;
            case "--RK":
                this.method = new RkSubstringFindingMethod();
                break;
        }
    }

    public HashMap<String, String> checkType(String path) {
        File folder = new File(path);
        File[] arrayOfFiles = folder.listFiles();
        String[] arrayOfNames = folder.list();
        int length = arrayOfNames.length;
        String[] arrayOfPaths = new String[length];
        for (int i = 0; i < length; ++i) {
            arrayOfPaths[i] = arrayOfFiles[i].getAbsolutePath();
        }
        List<String> listOfStringFiles = new ArrayList<>();
        HashMap<String, String> result = new HashMap<>();
        for (String pathToFile : arrayOfPaths) {
            String stringFile = "";
            try {
                stringFile = new String(Files.readAllBytes(Paths.get(pathToFile)));
            } catch (IOException e) {
                System.out.println("Such file doesn't exist!");
            }
            listOfStringFiles.add(stringFile);
        }

        ExecutorService executor = Executors.newFixedThreadPool(8);
        List<Callable<String>> callables = new ArrayList<>();
        for (String stringFile : listOfStringFiles) {
            Callable<String> callable = () -> {
                return method.findSubstring(stringFile, patternResultMap);
            };
            callables.add(callable);
        }
        List<Future<String>> futures = null;
        try {
            futures = executor.invokeAll(callables);
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
        }
        List<String> listOfResults = new ArrayList<>();
        for (Future<String> future : futures) {
            try {
                listOfResults.add(future.get());
            } catch (ExecutionException e) {
                System.out.println("Execution Excepton!");
            } catch (InterruptedException e) {
                System.out.println("Interrupted!");
            }
        }
        for (int i = 0; i < length; ++i) {
            result.put(arrayOfNames[i], listOfResults.get(i));
        }

        return result;
    }

}
