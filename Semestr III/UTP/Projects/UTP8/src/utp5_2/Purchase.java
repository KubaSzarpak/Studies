/**
 *
 *  @author Szarpak Jakub S25511
 *
 */

package utp5_2;


public class Purchase implements Comparable{
    private final String id_klienta;
    private final String nazwisko;
    private final String imie;
    private final String nazwa_towaru;
    private final double cena;
    private final double zakupiona_ilosc;

    public Purchase(String id_klienta, String nazwikso, String imie, String nazwa_towaru, double cena, double zakupiona_ilosc) {
        this.id_klienta = id_klienta;
        this.nazwisko = nazwikso;
        this.imie = imie;
        this.nazwa_towaru = nazwa_towaru;
        this.cena = cena;
        this.zakupiona_ilosc = zakupiona_ilosc;
    }

    public String getId_klienta() {
        return id_klienta;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwa_towaru() {
        return nazwa_towaru;
    }

    public double getCena() {
        return cena;
    }

    public double getZakupiona_ilosc() {
        return zakupiona_ilosc;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public double getKoszt(){
        return cena*zakupiona_ilosc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id_klienta).append(";").append(nazwisko).append(" ").append(imie).append(";").append(nazwa_towaru).append(";").append(cena).append(";").append(zakupiona_ilosc);
        return sb.toString();
    }
}
