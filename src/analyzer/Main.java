package analyzer;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {

        if (args.length != 3) {
            System.out.println("Program needs 3 arguments\nex: java Main doc.pdf \"%PDF-\" \"PDF document\"");
            return;
        }

        String algo = "KMP"; //processAlgo(args[0]);
        File directory = processFile(args[0]);
        String typePattern = args[1];
        String typeResult = args[2];

        if (directory == null) {
            System.out.printf("Directory does not exits or it is a file: %s", args[1]);
            return;
        }

        // get list of files
        List<File> list = Arrays.stream(directory.listFiles()).filter(File::isFile).collect(Collectors.toList());

        // setup worker pool
        ExecutorService worker = Executors.newFixedThreadPool(10);
        AlgorithmFactory algorithmFactory = new AlgorithmFactory(algo, typePattern, typeResult);

        for (File f : list) {
            worker.submit(new RunSearch(algorithmFactory, f));
        }

        worker.shutdown();
        worker.awaitTermination(1, TimeUnit.SECONDS);
    }

    private static File processFile(String fileName) {
        File f = new File(fileName);
        return f.exists() && f.isDirectory() ? f : null;
    }

    static String processAlgo(String algoFromArguments) {
        return algoFromArguments.replace("--", "");
    }
}