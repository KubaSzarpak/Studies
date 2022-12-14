package src.utp8_1;

import java.util.List;

public class ThreadB extends Thread {
    private final ThreadA A;
    private Double sum;

    public ThreadB(ThreadA A) {
        super("B");
        this.A = A;
        sum = 0.;
    }

    @Override
    public synchronized void run() {
        int i = 0;
        List<Towar> tmp;
        while (A.isAlive()) {
            if (A.isReady) {
                tmp = A.getList();
                while (i < tmp.size()) {
                    sum += tmp.get(i).getWeight();
                    i++;
                }
                System.out.println("policzono wage " + tmp.size() + " towarów");
                A.notifyThis();
            }
        }

        System.out.println(sum);
    }
}
