package KeywordsSearch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class KeywordsSearchTask extends RecursiveTask<ArrayList<File>> {

    File file;
    ArrayList<File> files = new ArrayList<>();
    ArrayList<String> keywords;

    public KeywordsSearchTask(File file, ArrayList<String> keywords) {
        this.file = file;
        this.keywords = keywords;
    }

    @Override
    protected ArrayList<File> compute() {
        if (file.isDirectory()) {
            var subTasks = new ArrayList<KeywordsSearchTask>();
            var subFiles = file.listFiles();

            assert subFiles != null;
            for (var subFile : subFiles) {
                var subTask = new KeywordsSearchTask(subFile, this.keywords);
                subTask.fork();
                subTasks.add(subTask);
            }

            for (var subTask : subTasks) {
                var newFiles = subTask.join();
                this.files.addAll(newFiles);
            }
        } else {
            List<String> words;
            try {
                words = getWords(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (var keyword : keywords) {
                if (!words.contains(keyword)) {
                    return new ArrayList<>();
                }
            }
            this.files.add(file);
        }
        return this.files;
    }

    private List<String> getWords(File file) throws IOException {
        List<String> words = new ArrayList<>();
        String currentLine;
        var bufferedReader = new BufferedReader(new FileReader(file));
        while ((currentLine = bufferedReader.readLine()) != null) {
            var splitLine = currentLine.trim().split("[\\s,.?!;:]+");

            for (var word : splitLine) {
                if (word.matches("\\p{L}[\\p{L}]+")) {
                    words.add(word.toLowerCase());
                }
            }
        }
        return words;
    }

}
