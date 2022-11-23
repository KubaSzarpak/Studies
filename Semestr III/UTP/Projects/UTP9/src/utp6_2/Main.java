/**
 *
 *  @author Szarpak Jakub S25511
 *
 */

package utp6_2;


public class Main {
  public static void main(String[] args) {
    String dirName = System.getProperty("user.home")+"/UTP6dir";
    String resultFileName = "UTP6res.txt";
    Futil.processDir(dirName, resultFileName);
  }
}
