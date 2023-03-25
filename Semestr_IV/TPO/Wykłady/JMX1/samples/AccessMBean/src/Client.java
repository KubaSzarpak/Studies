import javax.management.*;
import javax.management.remote.*;

public class Client {

   public static void main(String[] args) 
      // dla uproszczenia przyk³adu nie obs³ugujemy wyj±tków
      throws Exception { 

      int port = 2006;
      String host = "localhost";

      if(args.length > 0)
         host = args[0];
      if(args.length > 1)
         port = Integer.parseInt(args[1]);

      String url = "service:jmx:rmi:///jndi/rmi://" + 
         host + ":" + port + "/jmxrmi";

      // Nawi±zujemy po³±czenie z m-serwerem w kilku krokach
      JMXConnector jmxcon = 
         JMXConnectorFactory.connect(new JMXServiceURL(url));
      MBeanServerConnection server = 
         jmxcon.getMBeanServerConnection();

      // Tworzymy nazwê m-ziarna, z którego chcemy skorzystaæ
      ObjectName name = 
         new ObjectName("software.JMX:example=standard");


      // Pobieramy warto¶æ atrybutu Value z naszego m-ziarna
      int value = (Integer)server.getAttribute(name, "Value");    
      System.out.println("Warto¶æ atrybutu Value: " + value);

      // Zmieniamy warto¶æ atrybutu
      server.setAttribute(name, 
                          new Attribute("Value", value+1));

      // Ponownie pobieramy warto¶æ atrybutu Value
      value = (Integer)server.getAttribute(name, "Value");    
      System.out.println("Nowa warto¶æ atrybutu: " + value);
   }
}
