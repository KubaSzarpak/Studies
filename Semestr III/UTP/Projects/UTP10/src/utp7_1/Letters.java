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
        threads.add(new Thread("Thread " + c) {

            @Override
            public void run() {
                for (int i = 0; i < name.length(); i++) {

                    try {
                        System.out.println(name.charAt(i));
                        sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                super.run();

            }
        });
    }

    public void runThreads() {
        for (Thread t : threads) {
            t.start();
        }
    }

    public void stopThreads() {
        for (Thread t : threads) {
            t.stop();
            t.suspend();
        }
    }

    public List<Thread> getThreads() {
        return threads;
    }
}
