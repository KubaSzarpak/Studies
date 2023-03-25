import javax.naming.*;
import javax.jms.*;

public class DurableSubscriber {

  static void printMsg(Message message) throws Exception {
    if (message instanceof TextMessage) {
        TextMessage text = (TextMessage) message;
        System.out.println("Received: " + text.getText());
    } else if (message != null) {
        System.out.println("Received non text message");
    }
  }

  public static void main(String[] args) {
    Connection con = null;
    try {
      Context ctx = new InitialContext();
      ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
      String admTopicName = args[0];
      String subscriptionName = args[1];
      Topic topic = (Topic) ctx.lookup(admTopicName);
      con = factory.createConnection();
      Session ses = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageConsumer subs = ses.createDurableSubscriber(topic, subscriptionName);

      con.start();
      while (true) {
        Message message = subs.receiveNoWait();
        if (message == null) {    // nie ma dla mnie wiadomosci
          System.out.println("Waiting... Press ctrl-c to stop");
          try { Thread.sleep(10000); } catch (Exception exc) { break; }
        }
        else printMsg(message); // sa!
      }
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