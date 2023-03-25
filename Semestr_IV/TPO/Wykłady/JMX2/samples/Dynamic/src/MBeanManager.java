import java.io.*;
import javax.management.*;

/**
 * Metody operuj±ce na m-ziarnie wspólne dla agenta i klienta.
 * Klasy klienta i agenta dziedzicz± MBeanManager.
 */
public class MBeanManager {
    
    /**
     * Serwer m-ziaren.
     * Na lokalnej maszynie bêdzie reprezentowany przez obiekt typu MBeanServer.
     * Na zdalnej maszynie bêdzie to obiekt typu MBeanServerConnection.
     */
    protected MBeanServerConnection server;

    /**
     * Konstruktor.
     * Jako argument trzeba dostarczyæ odniesienie do m-serwera (MBeanServer)
     * albo nawi±zane po³±czenie (MBeanServerConnection)
     */
    public MBeanManager(MBeanServerConnection aMBeanServer){
        this.server = aMBeanServer;
    }

    /**
     * Metoda tworz±ca m-ziarno podanej klasy (za po¶rednictwem serwera).
     * Nazwa m-ziarna jako napis zawiera informacje o 
     *         klasie m-ziarna (type) i
     *         twórcy (creator) - agent czy klient.
     * Nowe m-ziarno jest automatycznie rejestrowane w serwerze.
     * Zwraca nazwê ObjectName dla utworzonego m-ziarna.
     */

    public ObjectName createMBean(String className, String creator) {
        ObjectName mbeanObjectName = null;
        try {
            String domain = "pl.edu.pjwstk.JMX";
            String mbNameStr = domain + ":type=" + className + ",creator=" + creator;
            mbeanObjectName =  ObjectName.getInstance(mbNameStr);
            server.createMBean(className, mbeanObjectName);
        }
        catch(JMException e){
            System.err.println(e);
        }
        catch(IOException e){
            System.err.println(e);
        }
        return mbeanObjectName;
    }


    /**
     * Wykonuje operacje na podanym m-ziarnie
     */
    public void manageMBean(ObjectName mbeanObjectName) {
        System.out.println("\nM-ziarno   \"" + mbeanObjectName + "\"\n");
        try {
            System.out.println("\tStan pocz±tkowy:");
            printAttributes(mbeanObjectName);
            Attribute attribute = new Attribute("Message","new Message");
            server.setAttribute(mbeanObjectName, attribute);
            System.out.println("\tPo zmianie atrybutów:");
            printAttributes(mbeanObjectName);
            int retval = (Integer)server.invoke(mbeanObjectName, 
                                                "reset", 
                                                new Object[0], 
                                                new String[0]);
            System.out.println("\tPo wykonaniu operacji reset(): ");
            System.out.println("\t\tWarto¶æ zwrócona= " + retval);
            printAttributes(mbeanObjectName);
        }
        catch(JMException e){
            System.err.println(e);
        }
        catch(IOException e){
            System.err.println(e);
        }
        System.out.println();
    }

    /**
     * Pomocnicza metoda wypisuj±ca atrybuty "Message" i "Changes"
     * podanego m-ziarna (liczymy na to, ¿e m-ziarno je ma :) 
     */

    public void printAttributes(ObjectName mbeanObjectName) {
        try {
            String message = (String) server.getAttribute(mbeanObjectName, "Message");
            System.out.println("\t\tAtrybut Message = \"" + message + "\"");
            Integer changes = (Integer) server.getAttribute(mbeanObjectName,"Changes");
            System.out.println("\t\tAtrybut Changes = " + changes);
        }
        catch(JMException e){
            System.err.println(e);
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
}
