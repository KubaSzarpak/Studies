package src.utp8_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ThreadA extends Thread {

    private final List<Towar> list;

    boolean isReady;

    public ThreadA() {
        super("A");
        list = new ArrayList<>();
        isReady = false;
    }

    @Override
    public synchronized void run() {
        Scanner reader;
        try {
            reader = new Scanner(new File(System.getProperty("user.home")+"/Towary.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String[] line;
        while (reader.hasNext()) {
            line = reader.nextLine().split(" ");
            list.add(new Towar(Integer.parseInt(line[0]), Double.parseDouble(line[1])));

            if (list.size() % 100 == 0) {
                isReady = true;
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

            if (list.size() % 200 == 0)
                System.out.println("utworzono " + list.size() + " obiektów");
        }
    }

    public  List<Towar> getList() {
        synchronized (list) {
            isReady = false;
            return list;
        }
    }

    public synchronized void notifyThis(){
        notify();
    }
}
