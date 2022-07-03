package analyzer;

import java.io.*;
import java.util.Arrays;

public class KmpAlgorithm implements Algorithm {
    private File source;
    private byte[] searchBytes;
    private String searchString;
    private String successResult;

    @Override
    public Algorithm setSource(File file) {
        source = file;
        return this;
    }

    @Override
    public Algorithm setSearchString(String typePattern) {
        searchString = typePattern;
        searchBytes = typePattern.getBytes();
        return this;
    }

    @Override
    public Algorithm setSuccessResult(String typeResult) {
        successResult = typeResult;
        return this;
    }

    @Override
    public String process() throws Exception {
        checkNecessaryOptions();

        // 1. get the prefix function array of the searchBytes pattern
        int[] prefixFunction = getPrefixFunctionForString(searchString);

        try (InputStream stream = new BufferedInputStream(new FileInputStream(source))) {

            // get the length of the searched string
            int patternLength = searchBytes.length;

            // start from the beginning of the source
            int searchTextOffset = 0;

            // initiate the byte array with the length of the searched pattern
            byte[] searchText = new byte[(int) source.length()];


            // read as much as we can from the stream
            int bytesRead = stream.read(searchText);

            while (bytesRead != -1) {

                for (; ; ) {
                    // if we don't have enough bytes available, bail out of the for loop
                    if ((searchTextOffset + patternLength) > bytesRead) {
                        break;
                    }

                    // 3. match the bytes from the source with the bytes of the searchBytes pattern
                    int longestMatchIndex = findLongestMatch(
                            Arrays.copyOfRange(searchText, searchTextOffset, patternLength + searchTextOffset),
                            searchBytes);

                    // 4. the algo needs to find only one occurrence then we stop as soon as we find one
                    // if the longest match is the whole searchBytes pattern we have found our match
                    if (longestMatchIndex == searchBytes.length - 1) {
                        // 5. if it is a match then return
                        return successResult;
                    }

                    // we don't have a match, so we continue...

                    // if we have no match then shift the searchBytes text with the whole length of searchBytes pattern
                    if (longestMatchIndex == -1) {
                        searchTextOffset += searchBytes.length;
                        continue;
                    }

                    // 6. if it is not, we check the part that matches and get the next bytes from the text as we shift the index forward
                    int substringMatchLength = longestMatchIndex + 1;
                    int prefixFunctionValueAtMatchedIndex = prefixFunction[longestMatchIndex];

                    // shift the next text searchBytes offset based on KMP spec
                    searchTextOffset += substringMatchLength - prefixFunctionValueAtMatchedIndex;
                }

                // get more bytes if there are suck available from the source
                bytesRead = stream.read(searchText);
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        return null;
    }

    /**
     * Compares two byte arrays and returned the index of the longest matched byte
     *
     * @param text
     * @param search
     * @return -1 if match not found or length of arrays miss match
     */
    private int findLongestMatch(byte[] text, byte[] search) {
        // two arrays must be with the same length
        if (text.length != search.length) {
            return -1;
        }

        for (int i = 0; i < text.length; i++) {
            // check each byte

            if (text[i] != search[i]) {
                // if we find non-matched position we return previous index
                return i - 1;
            }
        }

        // if we reach this point all bytes are matching
        return text.length - 1;
    }

    private int[] getPrefixFunctionForString(String string) {
        // create an array with the length of the string
        int[] prefixFunc = new int[string.length()];

        // set the first element to 0
        prefixFunc[0] = 0;

        // go through each index of the string and fill in the prefix function array
        for (int i = 1; i < prefixFunc.length; i++) {
            // initial step. set the current prefix func index to the previous one
            prefixFunc[i] = prefixFunc[i - 1];

            // take value of П[i-1] and use it as the index from the string
            Character leftChar = string.charAt(prefixFunc[i]);
            Character rightChar = string.charAt(i);

            // set the array at index if we have a match
            if (leftChar.equals(rightChar)) {
                // meaning the border is longer than the previous one
                prefixFunc[i]++;
                continue;
            }

            // we don't have a match still
            while (true) {
                if (prefixFunc[i] == 0) {
                    // we don't have to check anything, the value will be 0
                    break;
                }

                prefixFunc[i]--;

                // move the String index one position back
                leftChar = string.charAt(prefixFunc[i]);

                // set the array at index if we have a match
                if (leftChar.equals(rightChar)) {
                    break;
                }
            }
        }

        return prefixFunc;
    }

    private void checkNecessaryOptions() throws Exception {

        if (source == null || searchBytes == null || successResult == null) {
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
