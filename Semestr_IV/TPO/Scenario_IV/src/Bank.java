import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final public class Bank {
    private final Map<String,String> credentials;

    public Bank(Map<String, String> credentials) {
        this.credentials = Map.copyOf(credentials);
    }

    public boolean isAdminPasswordCorrect(String password){
        return credentials.get("admin").equals(password);
    }
}

final class CredentialProvider{
    private final HashMap<String,String> credentials;

    public CredentialProvider() {
        this.credentials = new HashMap<>();
        credentials.put("admin", UUID.randomUUID().toString());
    }

    Map<String, String> provideCredentials(){
        return credentials;
    }
}