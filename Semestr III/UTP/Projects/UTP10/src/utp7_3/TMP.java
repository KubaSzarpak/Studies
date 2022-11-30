package utp7_3;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class TMP {
    private String result;
    private String name;

    public TMP(String name) {
        this.name = name;
    }



    private final ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, name){
                @Override
                public String toString() {
                    return getName();
                }
            };
        }
    });
    public Future<String> run(String word, int multiplications) {
        return executor.submit(() -> {
            result = "";
            for (int i = 0; i < multiplications; i++) {
                Thread.sleep(3);
                result = result + word;
            }


            return result;
        }); //run
    }
}
