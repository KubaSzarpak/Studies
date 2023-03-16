package demo3;

class BoxDimensionFactory {

  // cz�sto wyst�puj�ce szeroko�ci pude�ek
  private int[] widths = { 10, 20, 30, 40, 50, 60, 70 };

  // Tablica rzomiar�w pude�ek - do ponownego u�ycia (wsp�dzielenia)
  private Dimension[] d = new Dimension[widths.length];

  private int reused;  // ile razy ponownie u�yto gotowego rozmiaru

  // Singleton
  // --- odniesienie do jedynego obiektu fabryki
  private static BoxDimensionFactory bdf;

  // --- prywatny konstruktor
  private BoxDimensionFactory() {}

  // --- metoda zwracaj�ca fabryk�
  public static BoxDimensionFactory getInstance() {
    if (bdf == null) bdf = new BoxDimensionFactory(); // je�eli obiekt nie istnieje -stw�rz
    return bdf;                                       // zwr�� jedyny obiekt klasy
  }

  // Metoda fabryczna
  // zwraca referencj� do obiekty klasy Dimension
  public Dimension makeDimension(int w) {
    for (int i=0; i < widths.length; i++)
      if (w == widths[i]) { // je�eli cz�sto wyst�puj�cy rozmiar
         // je�eli u�ywany pierwszy raz - utw�rz go i zapisz do tablicy
         if (d[i] == null) d[i] = new Dimension(w, 2*w);
         else reused++;  // je�eli ju� by� utworzony - zwi�ksz licznik ponownego u�ycia
         return d[i];    // zwr�� rozmiar - z tablicy "ponownego u�ycia"
      }
    return new Dimension(w, 2*w);  // je�eli jaki� inny rozmiar - utw�rz go i zwr��
  }

  // Zwraca liczb� ponownego u�ycia rozmiar�w
  public int reusedCount() {
    return reused;
  }

}