package analyzer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NaiveAlgorithm implements Algorithm {
    private File source;
    private String search;
    private String successResult;

    @Override
    public Algorithm setSource(File file) {
        source = file;
        return this;
    }

    @Override
    public Algorithm setSearchString(String typePattern) {
        search = typePattern;
        return this;
    }

    @Override
    public Algorithm setSuccessResult(String typeResult) {
        successResult = typeResult;
        return this;
    }

    @Override
    public String process() throws Exception{
        checkNecessaryOptions();

        FileInputStream fis = new FileInputStream(source);
        try (BufferedInputStream bis = new BufferedInputStream(fis)) {
            long totalBytes = source.length();
            byte[] bytes = new byte[(int) totalBytes];
            bis.read(bytes, 0, (int) totalBytes);

            byte[] pattern = search.getBytes(StandardCharsets.UTF_8);

            for (int i = 0; i <= totalBytes; i++) {
                // check if we have enough bytes for the check
                int neededLength = i + pattern.length;
                if (neededLength > totalBytes) {
                    break;
                }

                // get a copy of bytes to match against the pattern
                byte[] check = Arrays.copyOfRange(bytes, i, neededLength);

                if (Arrays.compare(check, pattern) == 0) {
                    return successResult;
                }
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }


        return null;
    }

    private void checkNecessaryOptions() throws Exception {

        if (source == null || search == null || successResult == null) {
            throw new Exception("Necessary options not set");
        }

        if (!source.exists()) {
            throw new Exception("Source file does not exists");
        }

        if (!source.isFile()) {
            throw new Exception("Source supplied is not a file");
        }

    }
}
