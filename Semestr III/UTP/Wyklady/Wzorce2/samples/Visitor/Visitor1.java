import java.util.*;

public class Visitor1 {

  public static void main(String[] args) {
    List<Pacjent> lista = new ArrayList<Pacjent>();
    lista.add(new ChoryNaG�ow�("Jan Kowalski"));
    lista.add(new ChoryNaG�ow�("Stefan Kowalewski"));
    lista.add(new ChoryNaNog�("Janusz Malinowski"));
    lista.add(new ChoryNa�o��dek("Adam Mickiewicz"));

    // Leczenie
    Visitor leczenie = new Leczenie2();
    for(Pacjent p : lista) p.accept(leczenie);

    // Wypisy
    Visitor wypis = new Wypis();
    for(Pacjent p : lista) p.accept(wypis);

    // Aha! Konkretna forma dzialania p.accept(v) polimorficznie
    // zale�y od klas dw�ch obiektow: pacjenta i visitora
    // To jest "double dispatching".


    wykonajOperacj�(leczenie, lista);
    wykonajOperacj�(wypis, lista);


  }


  static void wykonajOperacj�(Visitor v, List<Pacjent> lista) {
    System.out.println("Wykonuj� operacj� - " +  v);
    for(Pacjent p : lista) p.accept(v);
  }


}