package utp7_4;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class MyCloseThread extends Thread{
    private boolean close;
    private ExecutorService executor;

    public MyCloseThread(ExecutorService executor) {
        this.executor = executor;
        close = false;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();

        while (!msg.equals("quit")){
            msg = scanner.nextLine();
        }

        executor.shutdownNow();
        System.exit(-1);

    }

    public boolean getClose () {
        return close;
    }
}
