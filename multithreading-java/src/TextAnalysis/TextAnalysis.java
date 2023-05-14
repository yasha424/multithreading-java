package TextAnalysis;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

public class TextAnalysis {
    public static void main(String[] args) {

        boolean isSerial = false; // 20s vs 27s, 39.47s vs 55s
        var file = new File("/Users/yasha/second-half/Технології паралельних обчислень/data/");

        if (isSerial) {
            var serialAnalyser = new SerialAnalyser(file);
            long start = System.currentTimeMillis();
            var wordsMap = serialAnalyser.compute();
            long end = System.currentTimeMillis();

            System.out.println("Serial execution time " + (end - start) + "ms");

            System.out.println(serialAnalyser.getMean());
            System.out.println(serialAnalyser.getVariance());
            System.out.println(serialAnalyser.getDeviation());

        } else {
            var analysisTask = new AnalysisTask(file);
            var forkJoinPool = new ForkJoinPool();

            long start = System.currentTimeMillis();
            var wordsMap = forkJoinPool.invoke(analysisTask);
            long end = System.currentTimeMillis();

            System.out.println("Concurrent execution time " + (end - start) + "ms");

            System.out.println("Mean: " + analysisTask.getMean());
            System.out.println("Variance: " + analysisTask.getVariance());
            System.out.println("Deviation: " + analysisTask.getDeviation());
        }
    }
}
