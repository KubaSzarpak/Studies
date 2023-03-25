import java.lang.management.*;
import javax.management.*;

public class Agent {

   public static void main(String[] args) {

      // Pobieramy odniesienie do domy¶lnego m-serwera
      MBeanServer mbs = 
         ManagementFactory.getPlatformMBeanServer();

      // Tworzymy nazwê dla m-ziarna, 
      // pod któr± zostanie on zarejestrowany
      ObjectName name = null;
      try {
         name = new ObjectName("software.JMX:example=standard");
      }
      catch(MalformedObjectNameException e){
         System.err.println(e);
      }

      // Tworzymy m-ziarno SomeValue 
      SomeValue mbean = new SomeValue();

      // Rejestrujemy m-ziarno w m-serwerze
      try {
         mbs.registerMBean(mbean, name);
      }
      catch(JMException e){
         System.err.println(e);
      }

      // Czekamy na zg³oszenia
      System.out.println("Czekam na zg³oszenia...");
      try {
         Thread.sleep(Long.MAX_VALUE);
      }
      catch(InterruptedException e){
      }
   }
}

