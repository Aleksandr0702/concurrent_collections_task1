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
    static int maxSizeA = 0, maxSizeB = 0, maxSizeC = 0;
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

        Runnable logicA = () -> {
            for (int i = 0; i < size; i++) {
                int count = 0;
                String str;
                try {
                    str = textsA.take();
                    for (int j = 0; j < str.length(); j++) {
                        if (str.charAt(j) == 'a') {
                            count++;
                        }
                    }

                    if (count > maxSizeA) {
                        maxSizeA = count;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread threadA = new Thread(logicA);
        threads.add(threadA);
        threadA.start();

        Runnable logicB = () -> {
            for (int i = 0; i < size; i++) {
                int count = 0;
                String str;
                try {
                    str = textsB.take();
                    for (int j = 0; j < str.length(); j++) {
                        if (str.charAt(j) == 'b') {
                            count++;
                        }
                    }
                    if (count > maxSizeB) {
                        maxSizeB = count;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread threadB = new Thread(logicB);
        threads.add(threadB);
        threadB.start();


        Runnable logicC = () -> {
            for (int i = 0; i < size; i++) {
                int count = 0;
                String str;
                try {
                    str = textsC.take();
                    for (int j = 0; j < str.length(); j++) {
                        if (str.charAt(j) == 'c') {
                            count++;
                        }
                    }
                    if (count > maxSizeC) {
                        maxSizeC = count;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread threadC = new Thread(logicC);
        threads.add(threadC);
        threadC.start();

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Mаксимально сивмолов - а: " + maxSizeA);
        System.out.println("Mаксимально сивмолов - b: " + maxSizeB);
        System.out.println("Mаксимально сивмолов - c: " + maxSizeC);

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