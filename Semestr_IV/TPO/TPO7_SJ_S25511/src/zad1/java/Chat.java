import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Chat extends JFrame{
    private JTextArea chatView;
    private JPanel mainPanel;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel Title;
    private JPanel userPanel;
    private JScrollPane scrollPanel;
    private JButton loginButton;
    private JTextField topicField;
    private JButton logoutButton;

    private MessageConsumer messageConsumer;
    private MessageConsumer serverConsumer;

    private volatile boolean run;

    private final String id;
    private ArrayList<String> users;
    private String topic;

    public Chat(String login) throws HeadlessException {
        this.id = login;
        this.chatView.setPreferredSize(new Dimension(300, 200));
        this.userPanel.setPreferredSize(new Dimension(200, 300));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(mainPanel);
        this.setVisible(true);

        this.pack();
        this.userPanel.setLayout(new BoxLayout(this.userPanel, BoxLayout.Y_AXIS));

        users = new ArrayList<>();

//        for (int i = 0; i < 20; i++) {
//            JButton button = new JButton("User" + i);
//            button.setSize(new Dimension(100, 50));
//            this.userPanel.add("button", button);
//
//        }

//        sendButton.setEnabled(false);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        serverConsumer = new MessageConsumer("serverTopic", id, 0L);
        serverConsumer.kafkaConsumer.commitAsync();
        ProducerRecord<String, String> serverTopic = new ProducerRecord<>("serverTopic", 0, id,  "avatar:" + id + ":" + serverConsumer.kafkaConsumer.position(serverConsumer.partition));
        MessageProducer.send(serverTopic);


        executorService.submit(() -> {
            while (true){
                ConsumerRecords<String, String> poll = serverConsumer.kafkaConsumer.poll(Duration.of(1, ChronoUnit.SECONDS));
                poll.forEach(messageRecord -> {
                    String key = messageRecord.key();
                    if (!users.contains(key)){
                        users.add(key);
                        JButton button = new JButton(key);
                        button.addActionListener(new ButtonActionListener(executorService, button, chatView));
                        userPanel.add(button);
                        SwingUtilities.updateComponentTreeUI(this);
//                        System.out.println("Dodaje " + messageRecord.key() + " do " + id);
                    }
//                    System.out.println(id + " Server id - " + messageRecord.key());
//                    System.out.println("Server - " + messageRecord.value());
                });
            }
        });




        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topic = topicField.getText();
                run = true;
                long offset = 0;
                messageConsumer = new MessageConsumer(topic, id, 0L);



                executorService.submit(() -> {
                    while (run) {
                        messageConsumer.kafkaConsumer.poll(Duration.of(1, ChronoUnit.SECONDS)).forEach(
                                m -> {
                                    chatView.append(m.value() + System.lineSeparator());
                                    System.out.println(m.key());
                                }
                        );
                    }
                    messageConsumer.kafkaConsumer.close();
                });

                sendButton.setEnabled(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendButton.setEnabled(false);
                chatView.setText("");
                run = false;

            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MessageProducer.send(new ProducerRecord<>(topic, LocalDateTime.now()+ " - " + id + ": " + messageField.getText()));
                messageField.setText("");
            }
        });
    }


}

class ButtonActionListener implements ActionListener {
    private ExecutorService executorService;
    private JButton button;
    private JTextArea chatView;

    public ButtonActionListener(ExecutorService executorService, JButton button, JTextArea chatView) {
        this.executorService = executorService;
        this.button = button;
        this.chatView = chatView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String topic = button.getText();
        boolean run = true;
        MessageConsumer messageConsumer = new MessageConsumer(topic, topic, 0L);



        executorService.submit(() -> {
            while (run) {
                messageConsumer.kafkaConsumer.poll(Duration.of(1, ChronoUnit.SECONDS)).forEach(
                        m -> {
                            chatView.append(m.value() + System.lineSeparator());
                            System.out.println(m.key());
                        }
                );
            }
            messageConsumer.kafkaConsumer.close();
        });


    }
}

