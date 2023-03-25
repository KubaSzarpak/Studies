import javax.management.*;

/**
 * Implementacja standardowego m-ziarna AnotherStandardMBean
 * z mo¿liwo¶cia wysy³ania sygna³ów. W tym celu,
 * zamiast dziedziczyæ klasê NotificationBroadcasterSupport
 * implementuje interfejs NotificationEmitter deleguj±c wywo³ania jego metod
 * do obiektu NotificationBroadcasterSupport bêd±cego polem tej klasy.
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
        // zwiêkszamy licznik zmian
        changes++;
    }

    public int getChanges(){
        return changes;
    }

    public int reset(){
        // Tworzymy sygna³ nios±cy informacjê o zmianie atrybutu
        AttributeChangeNotification notification =
            new AttributeChangeNotification(this,
                                            ++resets,
                                            System.currentTimeMillis(),
                                            "Zresetowano m-ziarno klasy AnotherStandard",
                                            "Changes",
                                            "Integer",
                                            changes,
                                            0);
        notification.setUserData("Poprzednia warto¶æ atrybutu Message = \"" + message + "\"");
        message = "Standard";
        int oldValue = changes;
        changes = 0;
        // Wys³anie sygna³u zlecamy obiektowi wsparcia
        support.sendNotification(notification);
        return oldValue;
    }

    /*
     * Implementacje metod interfejsu NotificationEmitter
     */

    /**
     * Metoda zwracaj±ca informacje o sygna³ach emitowanych przez to m-ziarno.
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

    /*
     * Wywo³ania pozosta³ych metod interfejsu NotificationEmitter
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
     * Kolejny numer sygna³u wysy³anego po reset()
     */
    private long resets = 0;

    /**
     * Wsparcie dla wysy³ania sygna³ów.
     */
    private NotificationBroadcasterSupport support = new NotificationBroadcasterSupport();
}
