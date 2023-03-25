import javax.naming.*;
import javax.jms.*;
import javax.swing.*;
import java.awt.event.*;

public class AsynchrReceiver extends JFrame implements MessageListener {

  private Connection con;
  private JTextArea ta = new JTextArea(10, 20);

  public AsynchrReceiver(String destName) {

    try {
      Context ctx = new InitialContext();
      ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
      Destination dest = (Destination) ctx.lookup(destName);
      con = factory.createConnection();
      Session ses = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageConsumer receiver = ses.createConsumer(dest);
      receiver.setMessageListener(this);
      con.start();
    } catch (Exception exc) {
         exc.printStackTrace();
         System.exit(1);
    }

    add(new JScrollPane(ta));

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        try { con.close(); } catch(Exception exc) {}
        dispose();
        System.exit(0);
      }
    });
    setTitle("Czekam");
    pack();
    setLocationRelativeTo(null);
    show();
  }

  int i=0;
  public void onMessage(Message msg) {
    setTitle("Received msg " + ++i);
    try {
      ta.append(((TextMessage) msg).getText() + "\n");
    } catch(JMSException exc) { System.err.println(exc); }
  }

  public static void main(String[] args) {
    new AsynchrReceiver("queue1");
  }

}