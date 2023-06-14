import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame{
    private JTextField Login;
    private JTextField Password;
    private JButton LoginButton;
    private JPanel mainPanel;

    public Login() {
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(mainPanel);
        this.setVisible(true);

        this.pack();

        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Login.getText().equals("")) {
                    SwingUtilities.invokeLater(() -> new Chat(Login.getText()));
                }
            }
        });
    }
}

