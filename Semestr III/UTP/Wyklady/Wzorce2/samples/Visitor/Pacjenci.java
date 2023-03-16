abstract class Pacjent {
  protected String name;
  Pacjent(String s) {
    name = s;
  }

  // *** funkcjonalnoœæ zapisana w klasie
  public abstract String lecz();

  // *** ale mo¿emy daæ dowoln¹ funkcjonalnoœæ - z zewn¹trz
  // --- trzyba tylko przygotowaæ nasze klasy do "wizytacji"

  public abstract void accept(Visitor v);
}


class ChoryNaG³owê extends Pacjent {
  private static final String opis = "Chory na g³owê";
  ChoryNaG³owê(String s) { super(s); }
  public String toString() { return name + " " + opis; }

  // ---- funkcjonalnoœæ zapisana w klasie

  public String lecz() {
    return "Stosujê aspirynê";
  }

  // -- a jeœli potrzebujemy te¿ robiæ inne rzeczy?
  // --- nie dajmy dok³adnej funkcjonalnoœci
  // zastosujmy Visitor
  public void accept(Visitor v) {
      v.visit(this);
  }

}

class ChoryNa¯o³¹dek extends Pacjent {
  private static final String opis = "Chory na ¿o³¹dek";
  ChoryNa¯o³¹dek(String s) { super(s); }
  public String toString() { return name + " " + opis; }

  public String lecz() {
    return "Stosujê wêgiel";
  }

  public void accept(Visitor v) {
      v.visit(this);
  }


}

class ChoryNaNogê extends Pacjent {
  private static final String opis = "Chory na nogê";
  ChoryNaNogê(String s) { super(s); }
  public String toString() { return name + " " + opis; }

  public String lecz() {
    return "Zak³adam gips";
  }

  public void accept(Visitor v) {
      v.visit(this);
  }

}