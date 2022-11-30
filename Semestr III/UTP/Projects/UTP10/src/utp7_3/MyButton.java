package utp7_3;

import javax.swing.*;
import java.util.concurrent.Future;

public class MyButton extends JButton {
    private Future<String> future;
    public MyButton() {
        this.setText("Abort");
        this.addActionListener(e -> future.cancel(true));
    }

    public void setFuture(Future<String> future) {
        this.future = future;
    }
}
