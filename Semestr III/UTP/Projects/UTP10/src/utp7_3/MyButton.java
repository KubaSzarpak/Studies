package utp7_3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Future;

public class MyButton extends JButton {
    private Future<String> future;
    public MyButton() {
        this.setText("Abort");
        this.setPreferredSize(new Dimension(50, 50));
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                future.cancel(true);
            }
        });
    }

    public void setFuture(Future<String> future) {
        this.future = future;
    }
}
