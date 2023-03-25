
/**
 * Interfejs standardowego m-ziarna.
 * Deklaruje dwa atrybuty: 
 *      "Message" (zapis-odczyt) - jaki� tekst
 *      "Changes" (tylko odczyt) - liczba zmian tekstu
 * oraz operacj� reset(), kt�ra przywraca pierwotny stan obiektu.
 */

public interface AnotherStandardMBean {

    /**
     * Setter i getter dla atrybutu "Message"
     */
    public String getMessage();
    public void setMessage(String newMessage);

    /**
     * Getter dla atrybutu "Changes"
     */
    public int getChanges();

    /**
     * Operacja resetuj�ca obiekt.
     * Ma przywraca� domy�lne warto�ci atrybut�w.
     */
    public int reset();

}
