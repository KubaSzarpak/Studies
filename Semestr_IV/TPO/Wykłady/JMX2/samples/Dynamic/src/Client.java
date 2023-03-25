import java.io.*;
import java.net.*;
import java.util.*;
import javax.management.*;
import javax.management.remote.*;

/**
 * Klient tworzy na zdalnym m-serwerze dwa m-ziarna: standardowe i dynamiczne, 
 * i demonstruje ich dzia³anie, w tym równie¿ odbiór sygna³ów. 
 * Nastêpnie analizuje zawarto¶æ tego m-serwera (domeny i m-ziarna).
 * Na koñcu wyrejestrowujemy m-ziarna i zamykamy po³±czenie.
 *
 * Klasa Client dziedziczy z klasy MBeanManager kilka metod 
 * wspólnych dla agenta i klienta oraz pole "server" przechowuj±ce serwer m-ziaren.
 */

public class Client 
    extends MBeanManager {     // Dziedziczymy kilka u¿ytecznych metod


    public static void main(String[] args) {

        int port = args.length > 0 ? Integer.parseInt(args[0]) : 12345;
        String host = args.length > 1 ? args[1] : "localhost";
        Client client = new Client(host, port);

        // M-ziarno standardowe klasy AnotherStandard
        ObjectName stdMBeanName = client.createMBean("AnotherStandard", "Client");
        client.manageMBean(stdMBeanName);

        // M-ziarno dynamiczne klasy Dynamic
        ObjectName dynMBeanName = client.createMBean("Dynamic", "Client");
        client.manageMBean(dynMBeanName);

        // Analiza zawarto¶ci m-serwera
        client.inspectServer();

        // Wyrejestrowujemy m-ziarna i zamykamy po³±czenie z serwerem.
        try {
            client.server.unregisterMBean(stdMBeanName);
            client.server.unregisterMBean(dynMBeanName);
            client.jmxcon.close();
        }
        catch(IOException e){
            System.err.println(e);
        }
        catch(JMException e){
            System.err.println(e);
        }
    }

    /**
     * Strona kliencka ³±cznika
     */
    private JMXConnector jmxcon;

    /**
     * S³uchacz sygna³ów
     */
    private NotificationListener clientListener = new NotificationListener(){
            public void handleNotification(Notification notification, Object handback) {
                System.out.println("\tOdebrano sygna³:");
                System.out.println("\t\tKomunikat: " + notification.getMessage());
                System.out.println("\t\tNumer:     " + notification.getSequenceNumber());
                System.out.println("\t\tWys³ano:   " + new Date(notification.getTimeStamp()));
                System.out.println("\t\tTyp:       " + notification.getType());
                System.out.println("\t\tDane u¿.:  " + notification.getUserData());
            }
        };

    /**
     * Konstruktor.
     * Przekazujemy nazwê hosta i numer portu serwera
     */
    public Client(String host, int port) {
        super(null);   // na razie nie znamy m-serwera
        try {
            JMXServiceURL jmxurl = new JMXServiceURL(
                "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/dynamic");
            jmxcon = JMXConnectorFactory.connect(jmxurl);
            this.server = jmxcon.getMBeanServerConnection();
        }
        catch(MalformedURLException e){
            System.err.println(e);
        }
        catch(IOException e){
            System.err.println(e);
        }
    }

    /**
     * Dodaje nas³uch sygna³ów emitowanych przez podane m-ziarno.
     * Nastêpnie wykonuje na nim operacje i odbiera sygna³.
     */
    public void manageMBean(ObjectName mbName) {
        try {
            server.addNotificationListener(mbName, clientListener, null, null);

            super.manageMBean(mbName);
            // Musimy poczekaæ na nadej¶cie sygna³u zanim zakoñczymy program
            System.out.println("\tCzekam na nadej¶cie sygna³u...");

            Thread.sleep(1000);

            server.removeNotificationListener(mbName, clientListener);
        }
        catch(IOException e){
            System.err.println(e);
        }
        catch(JMException e){
            System.err.println(e);
        }
        catch(InterruptedException e){
            System.err.println(e);
        }
    }

    /**
     * Wypisuje informacje o zawarto¶ci m-serwera
     */
    public void inspectServer() {
        try {
            System.out.println("\nAnaliza serwera m-ziaren:");
            // Domeny
            System.out.println("\tDomeny:");
            String domains[] = server.getDomains();
            for (int i = 0; i < domains.length; i++)
                System.out.println("\t\tDomena[" + i + "] = " + domains[i]);
            String defaultDomain = server.getDefaultDomain();
            System.out.println("\tDomy¶lna domena = \"" + defaultDomain + "\"");
            // M-ziarna
            System.out.println("\tM-ziarna (liczba zarejestrowanych = " + server.getMBeanCount() + "):");
            Set names = server.queryNames(null, null);
            int num = 0;
            for (Iterator i = names.iterator(); i.hasNext(); )
                System.out.println("\t\tM-ziarno[" + num++ + "] = " + (ObjectName) i.next());
            
            // M-ziarna stworzone przez agenta
            String domain = "pl.edu.pjwstk.JMX";
            String agentQuery = domain + ":*,creator=Agent";
            Set agentNames = server.queryNames(new ObjectName(agentQuery), null);
            System.out.println("\tM-ziarna Agenta (liczba zarejestrowanych = " + agentNames.size() + "):");
            num = 0;
            for (Iterator a = agentNames.iterator(); a.hasNext(); )
                System.out.println("\t\tM-ziarno[" + num++ + "] = " + (ObjectName) a.next());

            // M-ziarna dynamiczne
            String dynamicQuery = domain + ":type=Dynamic,*";
            Set dynamicNames = server.queryNames(new ObjectName(dynamicQuery), null);
            System.out.println("\tM-ziarna typu Dynamic (liczba zarejestrowanych = " + dynamicNames.size() + "):");
            num = 0;
            for (Iterator d = dynamicNames.iterator(); d.hasNext(); )
                System.out.println("\t\tM-ziarno[" + num++ + "] = " + (ObjectName) d.next());

        }
        catch(IOException e){
            System.err.println(e);
        }
        catch(MalformedObjectNameException e){
            System.err.println(e);
        }
        System.out.println();
    }

}
