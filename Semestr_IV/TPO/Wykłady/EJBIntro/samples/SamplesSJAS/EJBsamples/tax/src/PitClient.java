import pit.Pit;
import pit.PitHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.math.BigDecimal;


public class PitClient {
    public static void main(String[] args) {
        try {
            Context initial = new InitialContext();
            Context myEnv = (Context) initial.lookup("java:comp/env");
            Object objref = myEnv.lookup("ejb/Pit");

            PitHome home =
                (PitHome) PortableRemoteObject.narrow(objref,
                    PitHome.class);

            Pit pit = home.create();

            BigDecimal income = new BigDecimal("100000.00");
            BigDecimal tax = pit.taxToPay(income);

            System.out.println("Podatek wynosi: " + tax);

            System.exit(0);

        } catch (Exception ex) {
            System.err.println("Caught an unexpected exception!");
            ex.printStackTrace();
        }
    }
}
