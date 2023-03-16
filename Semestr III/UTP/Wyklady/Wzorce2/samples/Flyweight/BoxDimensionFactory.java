package demo3;

class BoxDimensionFactory {

  // czêsto wystêpuj¹ce szerokoœci pude³ek
  private int[] widths = { 10, 20, 30, 40, 50, 60, 70 };

  // Tablica rzomiarów pude³ek - do ponownego u¿ycia (wspó³dzielenia)
  private Dimension[] d = new Dimension[widths.length];

  private int reused;  // ile razy ponownie u¿yto gotowego rozmiaru

  // Singleton
  // --- odniesienie do jedynego obiektu fabryki
  private static BoxDimensionFactory bdf;

  // --- prywatny konstruktor
  private BoxDimensionFactory() {}

  // --- metoda zwracaj¹ca fabrykê
  public static BoxDimensionFactory getInstance() {
    if (bdf == null) bdf = new BoxDimensionFactory(); // je¿eli obiekt nie istnieje -stwórz
    return bdf;                                       // zwróæ jedyny obiekt klasy
  }

  // Metoda fabryczna
  // zwraca referencjê do obiekty klasy Dimension
  public Dimension makeDimension(int w) {
    for (int i=0; i < widths.length; i++)
      if (w == widths[i]) { // je¿eli czêsto wystêpuj¹cy rozmiar
         // je¿eli u¿ywany pierwszy raz - utwórz go i zapisz do tablicy
         if (d[i] == null) d[i] = new Dimension(w, 2*w);
         else reused++;  // je¿eli ju¿ by³ utworzony - zwiêksz licznik ponownego u¿ycia
         return d[i];    // zwróæ rozmiar - z tablicy "ponownego u¿ycia"
      }
    return new Dimension(w, 2*w);  // je¿eli jakiœ inny rozmiar - utwórz go i zwróæ
  }

  // Zwraca liczbê ponownego u¿ycia rozmiarów
  public int reusedCount() {
    return reused;
  }

}