package sd.lab.concurrency.exercise;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.time.Duration;

import static sd.lab.concurrency.ResourcesUtils.openResource;

public class MultiReadingExecutorExample {

    public static void main(String... args) throws InterruptedException, IOException {
        var readingThread = new MultiReadingExecutor(
                openResource("file1.txt"),
//                System.in,
                openResource("file2.txt"),
                openResource("file3.txt")
        );
        readingThread.start();
        readingThread.join();
    }
}
