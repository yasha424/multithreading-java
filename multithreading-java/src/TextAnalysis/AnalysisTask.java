package TextAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class AnalysisTask extends RecursiveTask<HashMap<Integer, Integer>> {

    private File file;
    private HashMap<Integer, Integer> wordsMap = new HashMap<>();

    public AnalysisTask(File file) {
        this.file = file;
    }

    @Override
    protected HashMap<Integer, Integer> compute() {
        if (file.isDirectory()) {
            var files = file.listFiles();
            var subTasks = new ArrayList<AnalysisTask>();
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
                List<String> words = getWords();
                for (var word : words) {
                    int length = word.length(); // key
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

    private List<String> getWords() throws IOException {

        List<String> words = new ArrayList<>();
        String currentLine;

        var bufferedReader = new BufferedReader(new FileReader(file));

        while ((currentLine = bufferedReader.readLine()) != null) {
            var tokens = currentLine.trim().split("[\\s,:;.?!]+");
            for (var token : tokens) {
                if (token.matches("\\p{L}[\\p{L}-']*")) {
                    words.add(token);
                }
            }
        }
        return words;
    }

    public HashMap<Integer, Double> getDistributionLaw() {
        var law = new HashMap<Integer, Double>();

        long sum = 0;
        for (var entry : wordsMap.entrySet()) {
            sum += entry.getValue();
        }

        for (var entry: wordsMap.entrySet()) {
            law.put(entry.getKey(), ((double) entry.getValue()) / sum);
        }

        return law;
    }


    public double getMean() {
        double mean = 0;
        var law = getDistributionLaw();

        for (var entry : law.entrySet()) {
            mean += entry.getKey() * entry.getValue();
        }

        return mean;
    }

    public double getDeviation() {
        return Math.sqrt(getVariance());
    }

    public double getVariance() {
        double variance = 0;

        var law = getDistributionLaw();
        for (var entry : law.entrySet()) {
            variance += Math.pow(entry.getKey(), 2) * entry.getValue();
        }

        variance -= Math.pow(getMean(), 2);
        return variance;
    }
}
