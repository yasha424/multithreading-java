package TextAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class AnalysisTask extends RecursiveTask<HashMap<Integer, Integer>> {

    private final File file;
    private final HashMap<Integer, Integer> wordsMap = new HashMap<>();
    private final Analyser analyserProcessor = new Analyser();

    public AnalysisTask(File file) {
        this.file = file;
    }

    @Override
    protected HashMap<Integer, Integer> compute() {
        if (file.isDirectory()) {
            var files = file.listFiles();
            var subTasks = new ArrayList<AnalysisTask>();

            assert files != null;
            if (files.length != 0) {
                for (var file : files) {
                    var subTask = new AnalysisTask(file);
                    subTasks.add(subTask);
                    subTask.fork();
                }

                for (var subTask : subTasks) {
                    HashMap<Integer, Integer> words = subTask.join();
                    for (var entry : words.entrySet()) {
                        var key = entry.getKey();
                        var value = entry.getValue();
                        if (this.wordsMap.containsKey(key)) {
                            this.wordsMap.put(key, this.wordsMap.get(key) + value);
                        } else {
                            this.wordsMap.put(key, value);
                        }
                    }
                }
            }
        } else {
            try {
                List<String> words = analyserProcessor.getWords(file);
                for (var word : words) {
                    int length = word.length();
                    if (wordsMap.containsKey(length)) {
                        wordsMap.put(length, wordsMap.get(length) + 1);
                    } else {
                        wordsMap.put(length, 1);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return wordsMap;
    }

    public HashMap<Integer, Double> getDistributionLaw() {
        return analyserProcessor.getDistributionLaw(wordsMap);
    }

    public double getMean() {
        return analyserProcessor.getMean(getDistributionLaw());
    }

    public double getDeviation() {
        return Math.sqrt(getVariance());
    }

    public double getVariance() {
        return analyserProcessor.getVariance(getDistributionLaw());
    }
}
