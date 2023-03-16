import java.util.*;

interface Component {
  void show();
}



class Leaf implements Component {

  private String id;
  private int value;

  public Leaf(String s, int v) {
    id = s;
    value = v;
  }

  public String toString() { return id + " " + value; }

  public void show() {
    System.out.println(this);
  }
}

class Node extends LinkedList<Component> implements Component {
  private String id;

  public Node(String s) { id = s; }

  public void show() {
    System.out.println(id);
    for(Component c : this ) c.show();
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

    root.show();
  }

}