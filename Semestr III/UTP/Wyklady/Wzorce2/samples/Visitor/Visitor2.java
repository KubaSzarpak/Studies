// Wady tradycyjnego visitora:
// a) klasy musz¹ byæ przygotowane (implementowaæ "Visitable" == accept)
// b) dodanie nowej klasy Visitable wymaga zmian we wszystkich dotychczasowych wizytorach

// Rozwi¹zania: refleksja (wada - efektywnoœæ)

import java.lang.reflect.*;
import javax.swing.*;
import java.awt.*;

interface DynamicVisitor {
  public void visit(Object o);
}

abstract class AbstractDynamicVisitor implements DynamicVisitor {
  public void visit(Object o) {
    // Klasa konkretnego wizytora
    Class visitorClass = getClass();
    // Klasa wizytowanego obiektu
    Class objectClass = o.getClass();
    // W konkretnym wizytorze szukamy metody visit dla danego obiektu
    try {
      Method visitMethod = visitorClass.getMethod("visit", objectClass);
      visitMethod.invoke(this,o);
    } catch (Exception exc) { defaultDispatch(o); }
  }

  // Jeœli dla danej klasy obiektu nie ma metody visit z takim argumentem
  public void defaultDispatch(Object o) {}
}


class Przydzia³DoOddzia³ów extends AbstractDynamicVisitor {
  public void visit(ChoryNaG³owê p) {
    System.out.println(p + " - na oddzia³ psychiatrii");
  }
  public void visit(ChoryNaNogê p) {
    System.out.println(p + " - ortopedia");
  }
  public void visit(ChoryNa¯o³¹dek p) {
    System.out.println(p + " - na oddzia³ zakaŸny");
  }
  public void defaultDispatch(Object o) {
    System.out.println(o + " - przypadek nierozpoznany");
  }
}



public class Visitor2 extends JFrame {

  public Visitor2() {
    java.util.List<Pacjent> lista = new java.util.ArrayList<Pacjent>();
    lista.add(new ChoryNaG³owê("Jan Kowalski"));
    lista.add(new ChoryNaG³owê("Stefan Kowalewski"));
    lista.add(new ChoryNaNogê("Janusz Malinowski"));
    lista.add(new ChoryNa¯o³¹dek("Adam Mickiewicz"));

    // Uwaga! Teraz nie musimy stosowaæ metody accept!!!
    DynamicVisitor dv = new Przydzia³DoOddzia³ów();
    for(Pacjent p : lista) dv.visit(p);

    // klasy nie musz¹ nic wiedzieæ o tym, ¿e s¹ wizytowane
    // czyli mo¿emy coœ robiæ "z zewn¹trz" zamkniêtym, gotowym klasom

    final DynamicVisitor guiChanger = new AbstractDynamicVisitor() {

      public void visit(JButton b) {
        b.setBackground(Color.yellow);
      }

      public void visit(JTextField tf) {
        tf.setBorder(BorderFactory.createLineBorder(Color.red, 3));
      }

    };

    setLayout(new FlowLayout());
    add(new JButton("Wypis"));
    add(new JTextField(20));
    add(new JButton("Ok"));

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setVisible(true);

    try { Thread.sleep(5000); } catch(Exception exc) {}

    SwingUtilities.invokeLater( new Runnable() {
      public void run() {
        Component[] clist = getContentPane().getComponents();
        for (Component c : clist) guiChanger.visit(c);
      }
    });
  }

  public static void main(String[] args) {
    Visitor2 visitor2 = new Visitor2();
  }


}