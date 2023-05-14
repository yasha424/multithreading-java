package KeywordsSearch;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        var file = new File("/Users/yasha/second-half/Технології паралельних обчислень/data/NONFICTION");
        var forkJoinPool = new ForkJoinPool(4);
        ArrayList<String> keywords = new ArrayList<>(Arrays.asList(
                "java"
        ));

        var task = new KeywordsSearchTask(file, keywords);
        var files = forkJoinPool.invoke(task);
        for (var currentFile : files) {
            System.out.println(currentFile.getName());
        }
    }
}

