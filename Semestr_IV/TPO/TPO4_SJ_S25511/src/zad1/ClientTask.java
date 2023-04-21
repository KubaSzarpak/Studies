/**
 * @author Szarpak Jakub S25511
 */

package zad1;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientTask implements Runnable {
    public String log;
    private static ClientTask clientTask;

    public static ClientTask create(Client c, List<String> reqList, boolean showSendRes) {

        return clientTask =  new ClientTask() {
            @Override
            public void run() {
                log = "";
                String response;
                c.connect();
                response = c.send("login " + c.getId());
                if (showSendRes)
                    System.out.println(response);


                for (String s : reqList) {
                    response = c.send(s);
                    if (showSendRes)
                        System.out.println(response);
                }

                log = c.send("bye and log transfer");
                if (showSendRes)
                    System.out.println(log);

            }
        };
    }


    @Override
    public void run() {

    }

    public String get() throws InterruptedException, ExecutionException {
        return clientTask.log;
    }
}
