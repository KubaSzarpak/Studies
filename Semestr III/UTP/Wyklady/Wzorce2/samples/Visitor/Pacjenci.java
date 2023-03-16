abstract class Pacjent {
  protected String name;
  Pacjent(String s) {
    name = s;
  }

  // *** funkcjonalno�� zapisana w klasie
  public abstract String lecz();

  // *** ale mo�emy da� dowoln� funkcjonalno�� - z zewn�trz
  // --- trzyba tylko przygotowa� nasze klasy do "wizytacji"

  public abstract void accept(Visitor v);
}


class ChoryNaG�ow� extends Pacjent {
  private static final String opis = "Chory na g�ow�";
  ChoryNaG�ow�(String s) { super(s); }
  public String toString() { return name + " " + opis; }

  // ---- funkcjonalno�� zapisana w klasie

  public String lecz() {
    return "Stosuj� aspiryn�";
  }

  // -- a je�li potrzebujemy te� robi� inne rzeczy?
  // --- nie dajmy dok�adnej funkcjonalno�ci
  // zastosujmy Visitor
  public void accept(Visitor v) {
      v.visit(this);
  }

}

class ChoryNa�o��dek extends Pacjent {
  private static final String opis = "Chory na �o��dek";
  ChoryNa�o��dek(String s) { super(s); }
  public String toString() { return name + " " + opis; }

  public String lecz() {
    return "Stosuj� w�giel";
  }

  public void accept(Visitor v) {
      v.visit(this);
  }


}

class ChoryNaNog� extends Pacjent {
  private static final String opis = "Chory na nog�";
  ChoryNaNog�(String s) { super(s); }
  public String toString() { return name + " " + opis; }

  public String lecz() {
    return "Zak�adam gips";
  }

  public void accept(Visitor v) {
      v.visit(this);
  }

}