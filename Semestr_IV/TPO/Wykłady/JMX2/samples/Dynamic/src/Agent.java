import java.io.*;
import java.net.*;
import javax.management.*;
import javax.management.remote.*;

/**
 * Agent tworzy i demonstruje dwa m-ziarna: standardowe i dynamiczne.
 * Nast�pnie uruchamia po��czenie, przez kt�re klient b�dzie 
 * mia� dost�p do m-serwera stworzonego w konstruktorze.
 *
 * Klasa Agent dziedziczy z klasy MBeanManager kilka metod 
 * wsp�lnych dla agenta i klienta oraz pole "server" przechowuj�ce serwer m-ziaren.
 */

public class Agent 
    extends MBeanManager {     // Dziedziczymy kilka u�ytecznych metod

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

        // Test ��cznika
        agent.testConnector(port);
    }

    /**
     * Konstruktor tworzy w�asny (nie domy�lny) serwer m-ziaren
     */
    public Agent() {
        super(MBeanServerFactory.createMBeanServer());
    }


    /**
     * Tworzy serwer po��cze� (RMI) na podanym porcie.
     * Po uruchomieniu oczekuje na zg�oszenia klient�w do serwera.
     * Ko�czy dzia�anie wraz z naci�ni�ciem <Enter> na konsoli.
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
            // Musimy rzutowa� pole server na jego rzeczywisty typ MBeanServer, 
            // bo nast�pna metoda wymaga takiego argumentu
            MBeanServer mServer = (MBeanServer)server;
            // Tworzymy serwer po��cze�
            JMXConnectorServer cs =
                JMXConnectorServerFactory.newJMXConnectorServer(jmxurl, null, mServer);
            // Rozpoczynamy nas�uchiwanie po��cze� od klient�w
            cs.start();
            System.out.println("Naci�nij <Enter> aby zako�czy� serwer");
            System.in.read();
            // Koniec nas�uchu
            cs.stop();
        }
        catch(IOException e){
            System.err.println(e);
        }
    }

}
