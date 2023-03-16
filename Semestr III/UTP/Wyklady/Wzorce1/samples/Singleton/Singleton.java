package demo2;

public class Singleton {

  private static Singleton instance;
  private static Class klasa = Singleton.class;

  private String dane = "Dane obiektu";

  private Singleton() { // nie u¿ywany; zabrania instancjacji
  }

  public static synchronized Singleton getInstance() {
    if (instance == null) {
      try {
        ClassLoader classLoader = klasa.getClassLoader();
        Class kl = classLoader.loadClass(klasa.getName());
        instance = (Singleton) kl.newInstance();
      } catch (Exception exc) { exc.printStackTrace(); }
    }
    return instance;
  }

  public String toString() { return dane; }



  public static void main(String[] args) {
     Singleton sgl1 = Singleton.getInstance();
     Singleton sgl2 = Singleton.getInstance();
     System.out.println("Czy to jest singleton ? " + (sgl1 == sgl2));
     System.out.println(sgl1);
  }


}