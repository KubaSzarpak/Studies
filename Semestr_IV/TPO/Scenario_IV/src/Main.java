import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        final CredentialProvider credentialProvider = new CredentialProvider();
        final Bank bank = new Bank(credentialProvider.provideCredentials());

        /*"Zaloguj się" do banku za pomocą metody "isAdminPasswordCorrect"*/

//        Set<String> set = credentialProvider.provideCredentials().keySet();
//        for (String s : set) {
            credentialProvider.provideCredentials().replace("admin", "?");
//        }
        /**/

        System.out.println(bank.isAdminPasswordCorrect("?"));
    }
}