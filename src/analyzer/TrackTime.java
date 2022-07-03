package analyzer;

public class TrackTime {
    private long start;
    private long end;

    void start() {
        start = System.nanoTime();
    }

    void end() {
        end = System.nanoTime();
    }

    double getResultInSeconds() {
        return (double) (end - start) / 1_000_000_000;
    }
}