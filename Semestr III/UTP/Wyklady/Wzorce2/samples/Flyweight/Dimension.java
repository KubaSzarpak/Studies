package demo3;
final class Dimension {

  private int width;
  private int height;

  public Dimension(int w, int h) {
    width = w;
    height = h;
  }

  public int getWidth()  { return width; }
  public int getHeight() { return height; }

  public String toString() { return width + "x" + height; }

}