package utp7_3;

import javax.swing.*;
import java.awt.*;
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

        Future<String> f1 = new TMP("A").run("AB", 1000);
        Future<String> f2 = new TMP("B").run("BC", 2000);
        Future<String> f3 = new TMP("C").run("CD", 5000);

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

            label.setText("Future: isDone = " + future.isDone() + "| Is canceled: " + future.isCancelled());
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
