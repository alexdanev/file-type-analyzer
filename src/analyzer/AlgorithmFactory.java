package analyzer;

import java.io.File;

public class AlgorithmFactory {
    private String searchString;
    private String successResult;
    private TYPE algo;

    enum TYPE {
        NAIVE, KMP;
    }

    public AlgorithmFactory(String algo, String searchString, String successResult) throws Exception {
        this.searchString = searchString;
        this.successResult = successResult;

        if (algo.equalsIgnoreCase(TYPE.NAIVE.name())) {
            this.algo = TYPE.NAIVE;
        } else if (algo.equalsIgnoreCase(TYPE.KMP.name())) {
            this.algo = TYPE.KMP;
        } else {
            throw new Exception(String.format("Not supported Algorithm %s", algo));
        }
    }

    Algorithm getNewAlgo() throws Exception {
        switch (algo) {
            case KMP:
                return new KmpAlgorithm().setSearchString(searchString).setSuccessResult(successResult);
            case NAIVE:
                return new NaiveAlgorithm().setSearchString(searchString).setSuccessResult(successResult);
            default:
                throw new Exception(String.format("Not supported Algorithm %s", algo.name()));
        }
    }
}
