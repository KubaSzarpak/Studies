import java.util.*;

interface Component {
  void accept(Visitor v);
}

interface Visitor {
  void visit(Leaf l);
  void visit(Node n);
}


class Leaf implements Component {

  private String id;
  private int value;

  public Leaf(String s, int v) {
    id = s;
    value = v;
  }

  public int getValue() { return value; }

  public void setValue(int i) { value = i; }

  public String toString() { return id + " " + value; }

  public void accept(Visitor v) {
    v.visit(this);
  }
}

class Node extends LinkedList<Component> implements Component {
  private String id;

  public Node(String s) { id = s; }

  public String getId() { return id; }

  public void accept(Visitor v) {
    v.visit(this);
  }

}


class PrintVisitor implements Visitor {

  public void visit(Leaf l) { System.out.println(l); }

  public void visit(Node n) {
    System.out.println(n.getId());
    for(Component c : n) c.accept(this); // Kluczowe!
                                         // nie wolno: System.out.println(c);
  }
}

class IncreaseValueVisitor implements Visitor {

  public void visit(Leaf l) { l.setValue(l.getValue()+1); }

  public void visit(Node n) {
    for(Component c : n) c.accept(this);
  }

}


class Composite {

  public static void main(String args[]) {

    Node root = new Node("Zwierzêta");
    Node node;
    root.add(node = new Node("Psy"));
    node.add(new Leaf("Azor", 5));
    node.add(new Leaf("Aza", 7));
    root.add(node = new Node("Koty"));
    node.add(new Leaf("Pusia", 5));
    node.add(new Leaf("Mruczek", 10));

    root.accept(new IncreaseValueVisitor());
    root.accept(new PrintVisitor());
  }

}