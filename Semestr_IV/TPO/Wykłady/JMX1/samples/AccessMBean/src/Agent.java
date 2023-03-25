import java.lang.management.*;
import javax.management.*;

import javax.management.remote.*;

public class Agent {

   public static void main(String[] args) 
      // dla uproszczenia przyk³adu nie obs³ugujemy wyj±tków
      throws Exception { 

      // Pobieramy odniesienie do m-serwera systemowego
      MBeanServer mbs = 
         ManagementFactory.getPlatformMBeanServer();

      // Tworzymy nazwê dla m-ziarna, 
      // pod któr± zostanie on zarejestrowany
      ObjectName name = 
         new ObjectName("software.JMX:example=standard"); 

      // Tworzymy m-ziarno SomeValue 
      SomeValue mbean = new SomeValue();

      // Rejestrujemy m-ziarno w m-serwerze
      mbs.registerMBean(mbean, name);


      // Czekamy na zg³oszenia klientów
      System.out.println("Czekam na zg³oszenia...");
      Thread.sleep(Long.MAX_VALUE);
   }
}
