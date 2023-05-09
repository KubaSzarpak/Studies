/**
 * @author Szarpak Jakub S25511
 */

package zad1;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatClientTask implements Runnable {

    public String log;
    public ChatClient client;

    public static ChatClientTask create(ChatClient c, List<String> msgs, int wait) {
        return new ChatClientTask() {
            @Override
            public synchronized void run() {
                try {
                    client = c;
                    log = "";
                    String response;

                    c.login();
                    if (wait > 0)
                        Thread.sleep(wait);

                    for (String s : msgs){
                        c.send(s);
                        if (wait > 0)
                            Thread.sleep(wait);
                    }

                    c.logout();
                    if (wait > 0)
                        Thread.sleep(wait);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public synchronized void get() throws InterruptedException, ExecutionException {

    }

    public synchronized ChatClient getClient() {
        return client;
    }

    @Override
    public void run() {

    }
}
