import java.lang.management.*;
import javax.management.*;

import javax.management.remote.*;

public class Agent {

   public static void main(String[] args) 
      // dla uproszczenia przyk�adu nie obs�ugujemy wyj�tk�w
      throws Exception { 

      // Pobieramy odniesienie do m-serwera systemowego
      MBeanServer mbs = 
         ManagementFactory.getPlatformMBeanServer();

      // Tworzymy nazw� dla m-ziarna, 
      // pod kt�r� zostanie on zarejestrowany
      ObjectName name = 
         new ObjectName("software.JMX:example=standard"); 

      // Tworzymy m-ziarno SomeValue 
      SomeValue mbean = new SomeValue();

      // Rejestrujemy m-ziarno w m-serwerze
      mbs.registerMBean(mbean, name);


      // Czekamy na zg�oszenia klient�w
      System.out.println("Czekam na zg�oszenia...");
      Thread.sleep(Long.MAX_VALUE);
   }
}
