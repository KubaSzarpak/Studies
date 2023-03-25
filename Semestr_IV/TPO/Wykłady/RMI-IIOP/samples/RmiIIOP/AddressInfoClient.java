import javax.rmi.*;
import javax.naming.*;
import javax.swing.*;

public class AddressInfoClient {

    public static void  main( String args[] ) {

        try {
            Context ctx = new InitialContext();

            Object objref = ctx.lookup("AddressInfoService");
            System.out.println(objref.getClass().getName());
 
            AddressInfoInterface aif; // uwaga: zawsze inerfejs!
            aif = (AddressInfoInterface) PortableRemoteObject.narrow(
                                   objref, AddressInfoInterface.class);

        // zdalne wywolanie metod
        String name = "Kowalski Jan";
        String adres = aif.getAddress(name);
        JOptionPane.showMessageDialog(null, name + " - adres: " + adres);


        } catch( Exception e ) {
            e.printStackTrace( );
        }
    }
}