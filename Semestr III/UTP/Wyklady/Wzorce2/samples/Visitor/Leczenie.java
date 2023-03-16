
// A je�li nie mamy w klasach pacjent�w metod leczenia
// Dajemy zewn�trzn� klas� dzia�aj�c� na pacjentach
class Leczenie1 {

  public void lecz(Pacjent p) {
    if (p instanceof ChoryNaG�ow�) System.out.println("Stosuj� aspiryn�");
    else if (p instanceof ChoryNa�o��dek) System.out.println("Stosuj� w�giel");
    else if (p instanceof ChoryNaNog�) System.out.println("Zak�adam gips");
  }

}

// Bardziej elegancko = Visitor

interface Visitor {
  public void visit(ChoryNaG�ow� p);
  public void visit(ChoryNa�o��dek p);
  public void visit(ChoryNaNog� p);
}


class Leczenie2 implements Visitor {
  public void visit(ChoryNaG�ow� p) {
    System.out.println(p);
    System.out.println("Stosuj� aspiryn�");
  }
  public void visit(ChoryNa�o��dek p) {
    System.out.println(p);
    System.out.println("Stosuj� w�giel");
  }
  public void visit(ChoryNaNog� p) {
    System.out.println(p);
    System.out.println("Zak�adam gips");
  }

  public String toString() { return "Leczenie"; }
}

// dodajemy now� funkcjonalno�� (zewn�trznie!)
// w klasach pacjent�w nic nie musimy zmienia�

class Wypis implements Visitor {
  public void visit(ChoryNaG�ow� p) {
    System.out.println(p);
    System.out.println("Do domu!");
  }
  public void visit(ChoryNa�o��dek p) {
    System.out.println(p);
    System.out.println("Do domu, ale stosowa� diet�");
  }
  public void visit(ChoryNaNog� p) {
    System.out.println(p);
    System.out.println("Nale�y przewie�� do domu");
  }

  public String toString() { return "Wypisywanie ze szpitala"; }
}


