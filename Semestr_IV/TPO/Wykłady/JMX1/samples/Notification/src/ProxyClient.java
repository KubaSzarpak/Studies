import javax.management.*;
import javax.management.remote.*;
import java.io.*;

public class ProxyClient {

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
         // Nawi�zujemy po��czenie z m-serwerem
         JMXServiceURL jmxurl = new JMXServiceURL(url);
         jmxcon = JMXConnectorFactory.connect(jmxurl);
         srvcon = jmxcon.getMBeanServerConnection();

         // Tworzymy nazw� m-ziarna
         name = 
            new ObjectName("software.JMX:example=standard");

         // Tworzymy nas�uch
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

      // Tworzymy po�rednika
      SomeValueMBean someValueMBeanProxy = (SomeValueMBean)
         MBeanServerInvocationHandler.newProxyInstance(
            srvcon, name, SomeValueMBean.class, false
            );
      // Wywo�ujemy metody:
      someValueMBeanProxy.setValue(2);        
      someValueMBeanProxy.getValue();        
      someValueMBeanProxy.reset();        

        
      // Musimy poczeka� na nadej�cie sygna�u.
      try {
         Thread.sleep(500);
      }
      catch(InterruptedException e){
      }
   }

}
