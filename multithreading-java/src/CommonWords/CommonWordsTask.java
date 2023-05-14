package CommonWords;

import java.io.*;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class CommonWordsTask extends RecursiveTask<HashSet<String>> {
    final File file;
    final HashSet<String> commonWords = new HashSet<>();

    public CommonWordsTask(File file) {
        this.file = file;
    }

    @Override
    protected HashSet<String> compute() {
        if (file.isDirectory()) {
            List<CommonWordsTask> subTasks = new ArrayList<>();
            var subFiles = file.listFiles();

            assert subFiles != null;
            for (var subFile : subFiles) {
                var subTask = new CommonWordsTask(subFile);
                subTasks.add(subTask);
                subTask.fork();
            }

            for (var subTask : subTasks) {
                var newSet = subTask.join();
                if (this.commonWords.isEmpty()) {
                    this.commonWords.addAll(newSet);
                } else {
                    this.commonWords.retainAll(newSet);
                }
            }
        } else {
            List<String> words;
            try {
                words = this.getWords(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (var word : words) {
                commonWords.add(word.toLowerCase());
            }
        }

        return commonWords;
    }

    private List<String> getWords(File file) throws IOException {
        List<String> words = new ArrayList<>();
        String currentLine;
        var bufferedReader = new BufferedReader(new FileReader(file));
        while ((currentLine = bufferedReader.readLine()) != null) {
            var splitedLine = currentLine.trim().split("[\\s,.?!;:]+");

            for (var word : splitedLine) {
                if (word.matches("\\p{L}[\\p{L}]+")) {
                    words.add(word);
                }
            }
        }
        return words;
    }

}
