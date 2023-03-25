import java.io.*;
import java.net.*;
import javax.management.*;
import javax.management.remote.*;
import java.lang.management.*;

import static java.lang.management.ManagementFactory.*;


public class MXClient {

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

      JMXServiceURL jmxurl = null;
      JMXConnector jmxcon = null;
      MBeanServerConnection srvcon = null;
      MemoryMXBean memxbean = null;

      try {
         jmxurl = new JMXServiceURL(url);
      }
      catch(MalformedURLException e){
         System.err.println(e);
      }

      try {
         // Nawi±zujemy po³±czenie z m-serwerem
         jmxcon = JMXConnectorFactory.connect(jmxurl);
         srvcon = jmxcon.getMBeanServerConnection();

         // Pobieramy mx-ziarno reprezentuj±ce pamiêæ
         memxbean = 
            newPlatformMXBeanProxy(srvcon, 
                                   MEMORY_MXBEAN_NAME,
                                   MemoryMXBean.class);
      }
      catch(IOException e){
         System.err.println(e);
      }

      // Pobieramy rozmiar sterty i uruchamiamy od¶miecanie.
      long used = memxbean.getHeapMemoryUsage().getUsed();
      System.out.println("Rozmiar sterty: " + used);

      memxbean.gc();
      System.out.println("Od¶miecanie...");

      used = memxbean.getHeapMemoryUsage().getUsed();
      System.out.println("Rozmiar sterty: " + used);
   }
}
