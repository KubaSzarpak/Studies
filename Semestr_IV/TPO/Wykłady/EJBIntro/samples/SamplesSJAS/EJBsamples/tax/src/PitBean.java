package pit;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.math.*;


public class PitBean implements SessionBean {

    private BigDecimal taxRate = new BigDecimal("0.14");

    public PitBean() {
    }

    public BigDecimal taxToPay(BigDecimal income) {
        BigDecimal result = income.multiply(taxRate);

        return result.setScale(2, BigDecimal.ROUND_UP);
    }


    // Metody interfejsu SessionBean

    public void ejbCreate() {
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void setSessionContext(SessionContext sc) {
    }
}

