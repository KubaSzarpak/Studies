import javax.management.*;
import javax.management.remote.*;

public class Client {

   public static void main(String[] args) 
      // dla uproszczenia przyk�adu nie obs�ugujemy wyj�tk�w
      throws Exception { 

      int port = 2006;
      String host = "localhost";

      if(args.length > 0)
         host = args[0];
      if(args.length > 1)
         port = Integer.parseInt(args[1]);

      String url = "service:jmx:rmi:///jndi/rmi://" + 
         host + ":" + port + "/jmxrmi";

      // Nawi�zujemy po��czenie z m-serwerem w kilku krokach
      JMXConnector jmxcon = 
         JMXConnectorFactory.connect(new JMXServiceURL(url));
      MBeanServerConnection server = 
         jmxcon.getMBeanServerConnection();

      // Tworzymy nazw� m-ziarna, z kt�rego chcemy skorzysta�
      ObjectName name = 
         new ObjectName("software.JMX:example=standard");


      // Pobieramy warto�� atrybutu Value z naszego m-ziarna
      int value = (Integer)server.getAttribute(name, "Value");    
      System.out.println("Warto�� atrybutu Value: " + value);

      // Zmieniamy warto�� atrybutu
      server.setAttribute(name, 
                          new Attribute("Value", value+1));

      // Ponownie pobieramy warto�� atrybutu Value
      value = (Integer)server.getAttribute(name, "Value");    
      System.out.println("Nowa warto�� atrybutu: " + value);
   }
}
