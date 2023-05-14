package TextAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SerialAnalyser {
    private final ArrayDeque<File> fileArray = new ArrayDeque<>();
    private final HashMap<Integer, Integer> wordsMap = new HashMap<>();
    private final Analyser analyserProcessor = new Analyser();

    public SerialAnalyser(File file) {
        fileArray.add(file);
    }

    public HashMap<Integer, Integer> compute() {
        File file;
        while ((file = fileArray.poll()) != null) {
            if (file.isDirectory()) {
                var subFiles = file.listFiles();

                assert subFiles != null;
                Collections.addAll(fileArray, subFiles);
            } else {
                try {
                    List<String> words = analyserProcessor.getWords(file);
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
