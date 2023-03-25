import javax.management.*;
import javax.management.remote.*;
import java.io.*;

public class Client {

   public static void main(String[] args) {

      int port = 2006;
      String host = "localhost";

      if(args.length > 0)
         host = args[0];
      if(args.length > 1)
         port = Integer.parseInt(args[1]);

      String url = "service:jmx:rmi:///jndi/rmi://" 
         + host + ":" 
         + port + "/jmxrmi";
      ObjectName name = null;
      JMXConnector jmxcon = null;
      MBeanServerConnection srvcon = null;

      try {
         // Nawi±zujemy po³±czenie z m-serwerem
         JMXServiceURL jmxurl = new JMXServiceURL(url);
         jmxcon = JMXConnectorFactory.connect(jmxurl);
         srvcon = jmxcon.getMBeanServerConnection();

         // Tworzymy nazwê m-ziarna, z którego chcemy skorzystaæ
         name = new ObjectName("software.JMX:example=standard");

         // Tworzymy nas³uch
         ClientListener listener = new ClientListener();
         srvcon.addNotificationListener(name,
                                        listener,
                                        null,
                                        null);
      }
      catch(JMException e){
         System.err.println(e);
      }
      catch(IOException e){
         System.err.println(e);
      }


      try {
         // Zmieniamy warto¶æ atrybutu tak by by³a != od 0
         srvcon.setAttribute(name, 
                             new Attribute("Value", 1));
      
         // I wywo³ujemy metodê reset:
         Object[] params = new Object[0];
         String[] signature = new String[0];
         int retval = 
            (Integer)srvcon.invoke(name, 
                                   "reset", 
                                   params, 
                                   signature);
      }
      catch(JMException e){
         System.err.println(e);
      }
      catch(IOException e){
         System.err.println(e);
      }

      // Musimy poczekaæ na nadej¶cie sygna³u.
      try {
         Thread.sleep(1000);
      }
      catch(InterruptedException e){
      }
   }

}
