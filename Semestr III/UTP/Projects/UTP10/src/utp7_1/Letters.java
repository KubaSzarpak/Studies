package utp7_1;

import java.util.ArrayList;
import java.util.List;

public class Letters {
    List<Thread> threads;
    String name;

    public Letters(String name) {
        this.name = name;
        threads = new ArrayList<>();

        for (int i = 0; i < name.length(); i++) {
            createThreads(name.charAt(i));
        }
    }

    private void createThreads(char c) {
        threads.add(new Thread(() -> {
            for (int i = 0; i < name.length(); i++) {

                try {
                    System.out.println(name.charAt(i));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }, "Thread " + c));
    }

    public void runThreads() {
        for (Thread t : threads) {
            t.start();
        }
    }

    public void stopThreads() {
        for (Thread t : threads) {
            t.interrupt();
        }
    }

    public List<Thread> getThreads() {
        return threads;
    }
}
