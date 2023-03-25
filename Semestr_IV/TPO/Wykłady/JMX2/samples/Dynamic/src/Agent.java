import java.io.*;
import java.net.*;
import javax.management.*;
import javax.management.remote.*;

/**
 * Agent tworzy i demonstruje dwa m-ziarna: standardowe i dynamiczne.
 * Nastêpnie uruchamia po³±czenie, przez które klient bêdzie 
 * mia³ dostêp do m-serwera stworzonego w konstruktorze.
 *
 * Klasa Agent dziedziczy z klasy MBeanManager kilka metod 
 * wspólnych dla agenta i klienta oraz pole "server" przechowuj±ce serwer m-ziaren.
 */

public class Agent 
    extends MBeanManager {     // Dziedziczymy kilka u¿ytecznych metod

    public static void main(String[] args) {

        int port = args.length > 0 ? Integer.parseInt(args[0]) : 12345;

        // Uruchamiamy rejestr RMI
        try {
            java.rmi.registry.LocateRegistry.createRegistry(port);
        }
        catch(java.rmi.RemoteException e){
            System.err.println(e);
        }

        Agent agent = new Agent();

        // M-ziarno standardowe klasy AnotherStandard
        ObjectName stdMBeanName = agent.createMBean("AnotherStandard", "Agent");
        agent.manageMBean(stdMBeanName);

        // M-ziarno dynamiczne klasy Dynamic
        ObjectName dynMBeanName = agent.createMBean("Dynamic", "Agent");
        agent.manageMBean(dynMBeanName);

        // Test ³±cznika
        agent.testConnector(port);
    }

    /**
     * Konstruktor tworzy w³asny (nie domy¶lny) serwer m-ziaren
     */
    public Agent() {
        super(MBeanServerFactory.createMBeanServer());
    }


    /**
     * Tworzy serwer po³±czeñ (RMI) na podanym porcie.
     * Po uruchomieniu oczekuje na zg³oszenia klientów do serwera.
     * Koñczy dzia³anie wraz z naci¶niêciem <Enter> na konsoli.
     */

    public void testConnector(int port) {
        JMXServiceURL jmxurl = null;
        try {
            jmxurl = new JMXServiceURL(
                "service:jmx:rmi:///jndi/rmi://localhost:" + port + "/dynamic");
        }
        catch(MalformedURLException e){
            System.err.println(e);
        }
        try {
            // Musimy rzutowaæ pole server na jego rzeczywisty typ MBeanServer, 
            // bo nastêpna metoda wymaga takiego argumentu
            MBeanServer mServer = (MBeanServer)server;
            // Tworzymy serwer po³±czeñ
            JMXConnectorServer cs =
                JMXConnectorServerFactory.newJMXConnectorServer(jmxurl, null, mServer);
            // Rozpoczynamy nas³uchiwanie po³±czeñ od klientów
            cs.start();
            System.out.println("Naci¶nij <Enter> aby zakoñczyæ serwer");
            System.in.read();
            // Koniec nas³uchu
            cs.stop();
        }
        catch(IOException e){
            System.err.println(e);
        }
    }

}
