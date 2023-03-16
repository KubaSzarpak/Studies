// Wady tradycyjnego visitora:
// a) klasy musz� by� przygotowane (implementowa� "Visitable" == accept)
// b) dodanie nowej klasy Visitable wymaga zmian we wszystkich dotychczasowych wizytorach

// Rozwi�zania: refleksja (wada - efektywno��)

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

  // Je�li dla danej klasy obiektu nie ma metody visit z takim argumentem
  public void defaultDispatch(Object o) {}
}


class Przydzia�DoOddzia��w extends AbstractDynamicVisitor {
  public void visit(ChoryNaG�ow� p) {
    System.out.println(p + " - na oddzia� psychiatrii");
  }
  public void visit(ChoryNaNog� p) {
    System.out.println(p + " - ortopedia");
  }
  public void visit(ChoryNa�o��dek p) {
    System.out.println(p + " - na oddzia� zaka�ny");
  }
  public void defaultDispatch(Object o) {
    System.out.println(o + " - przypadek nierozpoznany");
  }
}



public class Visitor2 extends JFrame {

  public Visitor2() {
    java.util.List<Pacjent> lista = new java.util.ArrayList<Pacjent>();
    lista.add(new ChoryNaG�ow�("Jan Kowalski"));
    lista.add(new ChoryNaG�ow�("Stefan Kowalewski"));
    lista.add(new ChoryNaNog�("Janusz Malinowski"));
    lista.add(new ChoryNa�o��dek("Adam Mickiewicz"));

    // Uwaga! Teraz nie musimy stosowa� metody accept!!!
    DynamicVisitor dv = new Przydzia�DoOddzia��w();
    for(Pacjent p : lista) dv.visit(p);

    // klasy nie musz� nic wiedzie� o tym, �e s� wizytowane
    // czyli mo�emy co� robi� "z zewn�trz" zamkni�tym, gotowym klasom

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