package analyzer;

import java.io.File;

interface Algorithm {

    Algorithm setSource(File file);

    Algorithm setSearchString(String typePattern);

    Algorithm setSuccessResult(String typeResult);

    String process() throws Exception;
}
