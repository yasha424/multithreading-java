package TextAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Analyser {

    public List<String> getWords(File file) throws IOException {

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

    public HashMap<Integer, Double> getDistributionLaw(HashMap<Integer, Integer> wordsMap) {
        long sum = 0;
        HashMap<Integer, Double> law = new HashMap<>();
        for (var entry : wordsMap.entrySet()) {
            sum += entry.getValue();
        }

        for (var entry: wordsMap.entrySet()) {
            law.put(entry.getKey(), ((double) entry.getValue()) / sum);
        }

        return law;
    }


    public double getMean(HashMap<Integer, Double> law) {
        double mean = 0;

        for (var entry : law.entrySet()) {
            mean += entry.getKey() * entry.getValue();
        }

        return mean;
    }

    public double getDeviation(HashMap<Integer, Double> law) {
        return Math.sqrt(getVariance(law));
    }

    public double getVariance(HashMap<Integer, Double> law) {
        double variance = 0;

        for (var entry : law.entrySet()) {
            variance += Math.pow(entry.getKey(), 2) * entry.getValue();
        }

        variance -= Math.pow(getMean(law), 2);
        return variance;
    }
}
