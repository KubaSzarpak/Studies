package utp7_3;

import javax.swing.*;
import java.awt.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Window extends JFrame {

    JList<Future<String>> list;
    DefaultListModel<Future<String>> model;

    JLabel label;
    JPanel panel;
    JPanel LabelPanel;
    JSplitPane splitPane;

    public Window() {
        list = new JList<>();
        model = new DefaultListModel<>();

        Future<String> f1 = new ExecutorOperator().getFuture("AB", 1000);
        Future<String> f2 = new ExecutorOperator().getFuture("BC", 2000);
        Future<String> f3 = new ExecutorOperator().getFuture("CD", 5000);

        model.addElement(f1);
        model.addElement(f2);
        model.addElement(f3);


        list.setModel(model);


        label = new JLabel();
        panel = new JPanel();
        LabelPanel = new JPanel();
        MyButton button  = new MyButton();
        panel.add(button);
        label.setPreferredSize(new Dimension(400, 50));
        LabelPanel.add(label);
        LabelPanel.setBackground(Color.CYAN);
        panel.add(LabelPanel);
        splitPane = new JSplitPane();

        splitPane.setLeftComponent(new JScrollPane(list));
        splitPane.setRightComponent(panel);


        list.getSelectionModel().addListSelectionListener(e -> {
            Future<String> future = list.getSelectedValue();
            String message = "Future: isDone = " + future.isDone() + " ::: " + "Is canceled: " + future.isCancelled();
            if (future.isDone()){
                try {
                    if (future.isCancelled()){
                        message += " ::: " + "Result: " + "Canceled";
                    } else
                        message += " ::: " + "Result: " + future.get(); //future.get() gives the result of call method if is not canceled
                } catch (InterruptedException | ExecutionException ex) {
                    //If thread is interrupted
                    ex.printStackTrace();
                }
            }
            label.setText(message);

            button.setFuture(future);
        });


        this.setTitle("My JList");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(splitPane);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
