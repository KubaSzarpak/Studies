import javax.management.*;

/**
 * Implementacja standardowego m-ziarna AnotherStandardMBean
 * z mo�liwo�cia wysy�ania sygna��w. W tym celu,
 * zamiast dziedziczy� klas� NotificationBroadcasterSupport
 * implementuje interfejs NotificationEmitter deleguj�c wywo�ania jego metod
 * do obiektu NotificationBroadcasterSupport b�d�cego polem tej klasy.
 */

public class AnotherStandard
    implements NotificationEmitter, AnotherStandardMBean {

    /*
     * Implementacje metod interfejsu m-ziarna AnotherStandardMBean
     */

    public String getMessage(){
        return message;
    }

    public void setMessage(String newMessage){
        message = newMessage;
        // zwi�kszamy licznik zmian
        changes++;
    }

    public int getChanges(){
        return changes;
    }

    public int reset(){
        // Tworzymy sygna� nios�cy informacj� o zmianie atrybutu
        AttributeChangeNotification notification =
            new AttributeChangeNotification(this,
                                            ++resets,
                                            System.currentTimeMillis(),
                                            "Zresetowano m-ziarno klasy AnotherStandard",
                                            "Changes",
                                            "Integer",
                                            changes,
                                            0);
        notification.setUserData("Poprzednia warto�� atrybutu Message = \"" + message + "\"");
        message = "Standard";
        int oldValue = changes;
        changes = 0;
        // Wys�anie sygna�u zlecamy obiektowi wsparcia
        support.sendNotification(notification);
        return oldValue;
    }

    /*
     * Implementacje metod interfejsu NotificationEmitter
     */

    /**
     * Metoda zwracaj�ca informacje o sygna�ach emitowanych przez to m-ziarno.
     * Narzucona przez interfejs NotificationBroadcaster.
     * Powinna by� zaimplementowana zawsze, kiedy wysy�amy jakie� sygna�y -
     * r�wnie� w przypadku dziedziczenia klasy NotificationBroadcasterSupport, 
     * kt�ra dostarcza jej pustej implementacji.
     */
    public MBeanNotificationInfo[] getNotificationInfo() {
        // Kod tej metody jest podobny w wi�kszo�ci prostych przypadk�w.
        // Zmianie ulega jedynie komunikat b�d�cy ostatnim argumentem konstruktora.
        return new MBeanNotificationInfo[] {
	    new MBeanNotificationInfo(
                new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE },
                AttributeChangeNotification.class.getName(),
                    "Sygna� emitowany po wywo�aniu reset()"
                )
	};
    }

    /*
     * Wywo�ania pozosta�ych metod interfejsu NotificationEmitter
     * delegujemy do obiektu support (klasy NotificationBroadcasterSupport)
     */

    public void addNotificationListener(NotificationListener listener, 
                                        NotificationFilter filter, 
                                        Object handback){
        support.addNotificationListener(listener, filter, handback);
    }

    public void removeNotificationListener(NotificationListener listener)
                throws ListenerNotFoundException { 
        support.removeNotificationListener(listener);
    }

    public void removeNotificationListener(NotificationListener listener, 
                                           NotificationFilter filter, 
                                           Object handback)
                throws ListenerNotFoundException { 
        support.removeNotificationListener(listener, filter, handback);
    }

    /*
     * Koniec metod narzuconych przez interfejsy
     */



    /*
     * Atrybuty m-ziarna
     */

    /**
     * Atrybut read-write (jest getter i setter)
     */
    private String message = "Standard";

    /**
     * Atrybut read-only (jest tylko getter)
     */
    private int changes = 0;


    /*
     * Inne prywatne pola
     */

    /**
     * Kolejny numer sygna�u wysy�anego po reset()
     */
    private long resets = 0;

    /**
     * Wsparcie dla wysy�ania sygna��w.
     */
    private NotificationBroadcasterSupport support = new NotificationBroadcasterSupport();
}
