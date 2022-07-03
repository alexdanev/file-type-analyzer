package analyzer;

import java.io.*;
import java.util.Arrays;

public class Main {
    private static final String FILE_NOT_RECOGNIZED = "Unknown file type";

    public static void main(String[] args) throws IOException {

        if (args.length != 4) {
            System.out.println("Program needs 4 arguments\nex: java Main doc.pdf \"%PDF-\" \"PDF document\"");
            return;
        }

        String algo = processAlgo(args[0]);
        File file = processFile(args[1]);
        String typePattern = args[2];
        String typeResult = args[3];
        String result = FILE_NOT_RECOGNIZED;
        TrackTime tt = new TrackTime();

        if (file == null) {
            System.out.printf("File does not exits: %s", args[1]);
            return;
        }

        Algorithm algorithm;

        try {
            algorithm = new AlgorithmFactory()
                    .getAlgo(algo)
                    .setSource(file)
                    .setSearchString(typePattern)
                    .setSuccessResult(typeResult);

            tt.start();
            String algoResult = algorithm.process();
            tt.end();

            result = algoResult != null ? algoResult : FILE_NOT_RECOGNIZED;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }



        System.out.println(result);
        System.out.printf("It took %s seconds", tt.getResultInSeconds());
    }

    private static File processFile(String fileName) {
        File f = new File(fileName);
        return f.exists() ? f : null;
    }

    static String processAlgo(String algoFromArguments) {
        return algoFromArguments.replace("--", "");
    }
}