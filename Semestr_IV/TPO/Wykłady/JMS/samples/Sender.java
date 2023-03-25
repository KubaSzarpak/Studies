import javax.naming.*;
import javax.jms.*;

public class Sender {

  public static void main(String[] args) {

    Connection con = null;
    try {
      Context ctx = new InitialContext();
      ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
      String admDestName = args[0];
      Destination dest = (Destination) ctx.lookup(admDestName);
      con = factory.createConnection();
      Session ses = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageProducer sender = ses.createProducer(dest);
      con.start();
      TextMessage msg = ses.createTextMessage();
      msg.setText(args[1]);
      sender.send(msg);
      System.out.println("Sender sent msg: " + args[1]);
    } catch (Exception exc) {
        exc.printStackTrace();
        System.exit(1);
    } finally {
        if (con != null) {
          try {
            con.close();
          } catch (JMSException exc) {
              System.err.println(exc);
          }
        }
    }
    System.exit(0);
  }
}