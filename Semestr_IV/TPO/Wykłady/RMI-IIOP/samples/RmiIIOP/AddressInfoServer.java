import javax.naming.*;

public class AddressInfoServer {

    public static void main(String[] args) {
        try {
            // Utworzenie zdalnego obiektu    
            AddressInfo ref = 
                  new AddressInfo();


            Context ctx = new InitialContext();   
            ctx.rebind("AddressInfoService", ref );
            System.out.println("Start serwera");

         } catch (Exception exc) {
            exc.printStackTrace();
         } 
     }
}