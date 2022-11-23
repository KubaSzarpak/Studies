/**
 *
 *  @author Szarpak Jakub S25511
 *
 */

package utp5_2;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CustomersPurchaseSortFind {
    private List<Purchase> purchaseList = new ArrayList<>();
    public void readFile(String fname) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fname));
            String line = reader.readLine();
            while (line != null) {
                generatePurchase(Arrays.asList(line.split(";")));
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generatePurchase(List<String> list){
        List<String> nameAndUsername = Arrays.asList(list.get(1).split(" "));

        purchaseList.add(new Purchase(list.get(0), nameAndUsername.get(0), nameAndUsername.get(1), list.get(2), Double.parseDouble(list.get(3)), Double.parseDouble(list.get(4))));
    }

    public void showSortedBy(String arg) {
        System.out.println("\n" + arg);
        switch (arg) {
            case "Nazwiska": purchaseList.sort(Comparator.comparing(Purchase::getNazwisko).thenComparing(Purchase::getImie).thenComparing(Purchase::getId_klienta));
                for (Purchase item : purchaseList)
                    System.out.println(item);
                break;
            case "Koszty": purchaseList.sort(Comparator.comparingDouble(Purchase::getKoszt).reversed().thenComparing(Purchase::getId_klienta));
                for (Purchase item : purchaseList) {
                    System.out.print(item);
                    System.out.println("(koszt: " + item.getCena()*item.getZakupiona_ilosc() + ")");
                }
                break;
        }
    }

    public void showPurchaseFor(String id) {
        System.out.println("\nKlient " + id);
        for (Purchase item : purchaseList) {
            if (item.getId_klienta().equals(id))
                System.out.println(item);
        }
    }
}
