package utp7_3;


import java.util.concurrent.*;

public class ExecutorOperator {
    private String result;

    public ExecutorOperator() {
        this.result = "";
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Test");
        }
    });

    public Future<String> getFuture(String word, int multiplications) {
        return executor.submit(() -> {
            for (int i = 0; i < multiplications; i++) {
                Thread.sleep(3);
                result += word;
            }
            return result;
        }); //call from Callable interface
    }
}
