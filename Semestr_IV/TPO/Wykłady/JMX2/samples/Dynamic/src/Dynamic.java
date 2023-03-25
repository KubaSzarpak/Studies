import java.util.Iterator;

import javax.management.DynamicMBean;
import javax.management.NotificationBroadcasterSupport;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeChangeNotification;

import javax.management.ReflectionException;
import javax.management.AttributeNotFoundException;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;

import javax.management.MBeanInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanParameterInfo;


/**
 * M-ziarno dynamiczne.
 */

/*
 * Dla uproszczenia pomijamy sprawdzanie poprawno¶ci argumentów (czy nie s± null)
 */

public class Dynamic
    extends NotificationBroadcasterSupport
    implements DynamicMBean {

    /*
     * Implementacje metod interfejsu DynamicMBean
     */
    

    /**
     * Zwraca warto¶æ atrybutu o nazwie attribute 
     */
    public Object getAttribute(String attribute) 
        throws AttributeNotFoundException {

        if (attribute.equals("Message")) {
            return getMessage();
        } 
        else if (attribute.equals("Changes")) {
            return getChanges();
        }
        // Nie ma takiego atrybutu
        throw new AttributeNotFoundException("Nie ma takiego atrybutu: " + attribute);
    }    


    /**
     * Zwraca warto¶ci atrybutów z tablicy attributes
     */
    public AttributeList getAttributes(String[] attributes) {

        AttributeList results = new AttributeList();
        
        for(String attr : attributes){
            try {        
                Object value = getAttribute(attr);     
                // Dodajemy, je¶li nie by³o wyj±tku
                results.add(new Attribute(attr, value));
            } catch (Exception e) {
                // Nie przekazujemy wyj±tków wo³aj±cemu
            }
        }
        // Na wynikowej li¶cie znajduj± siê te warto¶ci, które uda³o siê pobraæ
        return results;
    }


    /**
     * Ustala warto¶æ atrybutu o nazwie attribute 
     */
    public void setAttribute(Attribute attribute) 
        throws AttributeNotFoundException, InvalidAttributeValueException {

        String name = attribute.getName();
        Object value = attribute.getValue();

        if (name.equals("Message")) {

            // Czy mo¿na dokonaæ przypisania warto¶ci atrybutu?
            if (String.class.isAssignableFrom(value.getClass()))
                setMessage((String) value);
            else
                throw new InvalidAttributeValueException("Z³y typ warto¶ci atrybutu");
        }
        // Próba modyfikacji atrybutu read-only
        else if (name.equals("Changes")) {
            throw new AttributeNotFoundException("Atrybut tylko do odczytu");
        }
        else 
            throw new AttributeNotFoundException("Nieznany atrybut: " + name);
    }


    /**
     * Ustala warto¶ci atrybutów z listy attributes
     */
    public AttributeList setAttributes(AttributeList attributes) {

        AttributeList results = new AttributeList();

        for (Iterator i = attributes.iterator(); i.hasNext();) {
            Attribute attr = (Attribute) i.next();
            try {
                setAttribute(attr);
                // Je¶li siê uda³o to dodajemy do wynikowej listy
                results.add(attr);
            } catch(Exception e) {
                // Nie przekazujemy wyj±tków wo³aj±cemu
            }
        }
        return results;
    }

    /**
     * Wywo³uje metodê o nazwie operationName i sygnaturze signature
     * przekazuj±c parametry params i zwracaj±c wynik
     */
    public Object invoke(String operationName, Object params[], String signature[])
        throws ReflectionException {

        if (operationName.equals("reset"))
            return reset();
        else 
            // Nieznana operacja
            throw new ReflectionException(new NoSuchMethodException(operationName));
    }

    /**
     * Zwraca informacje o m-ziarnie
     */
    public MBeanInfo getMBeanInfo() {
        return mBeanInfo;
    }

    /*
     * Koniec metod interfejsu DynamicMBean
     */

    /**
     * Metoda zwracaj±ca informacjê o sygna³ach emitowanych przez to m-ziarno.
     * Narzucona przez interfejs NotificationBroadcaster.
     * Powinna byæ zaimplementowana zawsze, kiedy wysy³amy jakie¶ sygna³y -
     * równie¿ w przypadku dziedziczenia klasy NotificationBroadcasterSupport, 
     * która dostarcza jej pustej implementacji.
     */
    public MBeanNotificationInfo[] getNotificationInfo() {
        // Kod tej metody jest podobny w wiêkszo¶ci prostych przypadków.
        // Zmianie ulega jedynie komunikat bêd±cy ostatnim argumentem konstruktora.
        return new MBeanNotificationInfo[] {
	    new MBeanNotificationInfo(
                new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE },
                AttributeChangeNotification.class.getName(),
                    "Sygna³ emitowany po wywo³aniu reset()"
                )
	};
    }


    /**
     * Publiczny konstruktor
     */
    public Dynamic(){
        mBeanInfo = buildMBeanInfo();
    }


    /*
     * Inne metody publiczne
     */

    public String getMessage(){
        return message;
    }

    public void setMessage(String newMessage){
        message = newMessage;
        changes++;
    }

    public int getChanges(){
        return changes;
    }

    public int reset(){
        AttributeChangeNotification notification =
            new AttributeChangeNotification(this,
                                            ++resets,
                                            System.currentTimeMillis(),
                                            "Zresetowano m-ziarno klasy Dynamic",
                                            "Changes",
                                            "Integer",
                                            changes,
                                            0);
        notification.setUserData("Poprzednia warto¶æ atrybutu Message = \"" + message + "\"");
        message = "Dynamic";
        int oldValue = changes;
        changes = 0;
        sendNotification(notification);
        return oldValue;
    }

    /*
     * Metody robocze
     */

    /**
     * Tworzy informacjê o m-ziarnie zawart± w MBeanInfo
     */
    private MBeanInfo buildMBeanInfo() {

        MBeanAttributeInfo[] attributes = {
            new MBeanAttributeInfo(
                "Message", "java.lang.String", "Komunikat", true, true, false),
            new MBeanAttributeInfo(
                "Changes", "java.lang.Integer", "Licznik zmian komunikatu", true, false, false)
        };

        MBeanConstructorInfo[] constructors = {
            new MBeanConstructorInfo("Konstruktor", getClass().getConstructors()[0])
        };

        MBeanOperationInfo[] operations = {
            new MBeanOperationInfo("reset", 
                                   "Resetuje m-ziarno", 
                                   new MBeanParameterInfo[0], 
                                   "java.lang.Integer", 
                                   MBeanOperationInfo.ACTION)
        };

        MBeanNotificationInfo[] notifications = {
            new MBeanNotificationInfo(
                new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE },
                AttributeChangeNotification.class.getName(),
                "Sygna³ emitowany po wywo³aniu reset()")
        };

        return new MBeanInfo(this.getClass().getName(),
                             "Przyk³ad dynamicznego m-ziarna",
                             attributes,
                             constructors,
                             operations,
                             notifications);
    }


    /*
     * Prywatne atrybuty    
     */

    /**
     * Atrybut read-write (jest getter i setter)
     */
    private String message = "Dynamic";

    /**
     * Atrybut read-only (jest tylko getter)
     */
    private int changes = 0;


    /**
     * Kolejny numer sygna³u wysy³anego po reset()
     */
    private long resets = 0;

    /**
     * Informacja o dynamicznym m-ziarnie
     */
    MBeanInfo mBeanInfo;
}
