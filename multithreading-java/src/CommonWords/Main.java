package CommonWords;

import java.io.File;
import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        var file = new File("/Users/yasha/second-half/Технології паралельних обчислень/data/AUTHORS/AUTHORS/WILDE");
        var task = new CommonWordsTask(file);
        var forkJoinPool = new ForkJoinPool();
        HashSet<String> commonWords = forkJoinPool.invoke(task);

        var count = 0;
        for (var entry : commonWords) {
            System.out.print(entry + " ");
            count++;
            if (count % 20 == 0) {
                System.out.println();
            }
        }
        System.out.println("\n" + "Count of common words: " + commonWords.size());
    }
}
