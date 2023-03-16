package demo3;
class Box {
  private Dimension dim;
  private String cont;

  public Box(Dimension d, String c) {
    dim = d;
    cont = c;
  }

  public String toString() {
     return "Pude³ko: " + dim + " Zawartoœæ: " + cont;
  }
}

public class BoxTest {

  public static void main(String[] args) {

    // Pobranie fabryki rozmiarów
    BoxDimensionFactory boxDimFac =  BoxDimensionFactory.getInstance();

    // na jakie pude³ka jest teraz zapotrzebowanie
    int[] potrzebne = { 10, 10, 10, 20, 30, 45, 20, 20, 20, 20, 10,
                        20, 50, 65, 50, 50, 60, 100, 50, 50, 50,
                      };

    // Kolejne pude³ka tworzymy na podstawie rozmiarów
    // uzyskanych z fabryki rozmiarów
    // nie wiemy i nie interesuje nas czy rozmiary to nowe obiekty
    // czy te¿ ju¿ u¿ywane przez inne pude³ka
    Box box = null;
    for (int i = 0; i < potrzebne.length; i++) {
      box = new Box(boxDimFac.makeDimension(potrzebne[i]), "Kwiaty");
      System.out.println(box);
    }
    
    System.out.println("Na " + potrzebne.length + " rozmiarów pude³ek\n" +
          "Utworzono nowych " + (potrzebne.length - boxDimFac.reusedCount()) +
          "\nPonownie u¿yto (przy wspóldzieleniu) " + boxDimFac.reusedCount()
          );
  }

}