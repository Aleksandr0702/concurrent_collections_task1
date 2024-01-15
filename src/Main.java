import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    static List<Thread> threads = new ArrayList<>();
    static BlockingQueue<String> textsA = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> textsB = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> textsC = new ArrayBlockingQueue<>(100);
    static int[] maxSizeA = {0}, maxSizeB = {0}, maxSizeC = {0};  // Используем массивы
    static final int size = 10_000;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            for (int i = 0; i < size; i++) {
                String str = generateText("abc", 100_000);
                try {
                    textsA.put(str);
                    textsB.put(str);
                    textsC.put(str);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        runLogic('a', textsA, maxSizeA);
        runLogic('b', textsB, maxSizeB);
        runLogic('c', textsC, maxSizeC);

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Mаксимально символов - a: " + maxSizeA[0]);
        System.out.println("Mаксимально символов - b: " + maxSizeB[0]);
        System.out.println("Mаксимально символов - c: " + maxSizeC[0]);
    }

    private static void runLogic(char letter, BlockingQueue<String> texts, int[] maxSize) {
        Runnable logic = () -> {
            for (int i = 0; i < size; i++) {
                int count = 0;
                String str;
                try {
                    str = texts.take();
                    count = countOccurrences(str, letter);
                    if (count > maxSize[0]) {
                        maxSize[0] = count;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(logic);
        threads.add(thread);
        thread.start();
    }

    private static int countOccurrences(String str, char letter) {
        int count = 0;
        for (int j = 0; j < str.length(); j++) {
            if (str.charAt(j) == letter) {
                count++;
            }
        }
        return count;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}