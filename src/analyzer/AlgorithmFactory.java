package analyzer;

public class AlgorithmFactory {
    enum TYPE {
        NAIVE, KMP;
    }

    Algorithm getAlgo(String algo) throws Exception {

        if (algo.equalsIgnoreCase(TYPE.NAIVE.name())) {
            return new NaiveAlgorithm();
        }

        if (algo.equalsIgnoreCase(TYPE.KMP.name())) {
            return new KmpAlgorithm();
        }

        throw new Exception(String.format("Not supported Algorithm %s", algo));
    }
}
