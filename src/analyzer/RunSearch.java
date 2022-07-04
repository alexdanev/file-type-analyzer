package analyzer;

import java.io.File;

public class RunSearch extends Thread {
    private AlgorithmFactory af;
    private File f;

    public RunSearch(AlgorithmFactory af, File f) {
        super();
        this.af = af;
        this.f = f;
    }

    @Override
    public void run() {
        try {
            Algorithm algorithm = af.getNewAlgo().setSource(f);
            String algoResult = algorithm.process();
            String result = algoResult != null ? algoResult : "Unknown file type";
            System.out.printf("%s: %s\n",f.getName(), result);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
