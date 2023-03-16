
// A jeœli nie mamy w klasach pacjentów metod leczenia
// Dajemy zewnêtrzn¹ klasê dzia³aj¹c¹ na pacjentach
class Leczenie1 {

  public void lecz(Pacjent p) {
    if (p instanceof ChoryNaG³owê) System.out.println("Stosujê aspirynê");
    else if (p instanceof ChoryNa¯o³¹dek) System.out.println("Stosujê wêgiel");
    else if (p instanceof ChoryNaNogê) System.out.println("Zak³adam gips");
  }

}

// Bardziej elegancko = Visitor

interface Visitor {
  public void visit(ChoryNaG³owê p);
  public void visit(ChoryNa¯o³¹dek p);
  public void visit(ChoryNaNogê p);
}


class Leczenie2 implements Visitor {
  public void visit(ChoryNaG³owê p) {
    System.out.println(p);
    System.out.println("Stosujê aspirynê");
  }
  public void visit(ChoryNa¯o³¹dek p) {
    System.out.println(p);
    System.out.println("Stosujê wêgiel");
  }
  public void visit(ChoryNaNogê p) {
    System.out.println(p);
    System.out.println("Zak³adam gips");
  }

  public String toString() { return "Leczenie"; }
}

// dodajemy now¹ funkcjonalnoœæ (zewnêtrznie!)
// w klasach pacjentów nic nie musimy zmieniaæ

class Wypis implements Visitor {
  public void visit(ChoryNaG³owê p) {
    System.out.println(p);
    System.out.println("Do domu!");
  }
  public void visit(ChoryNa¯o³¹dek p) {
    System.out.println(p);
    System.out.println("Do domu, ale stosowaæ dietê");
  }
  public void visit(ChoryNaNogê p) {
    System.out.println(p);
    System.out.println("Nale¿y przewieŸæ do domu");
  }

  public String toString() { return "Wypisywanie ze szpitala"; }
}


