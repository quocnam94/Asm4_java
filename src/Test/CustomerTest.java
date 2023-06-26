package Test;
import model.ASM2.Account;
import model.ASM2.Customer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CustomerTest {
    Customer customer = new Customer("Nam", "123456789123");
    Customer customer2 = new Customer("FunIX", "123456123456");

    @Before
    public void setup(){
        customer.addAccount(new Account("111111", "11000000", 150000));
        customer2.addAccount(new Account("333333", "500000", 500000));
    }
    @Test
    public void getBalance() {
        double balance1 = customer.getAccountByAccountNumber("111111").getBalance();
        double balance2 = customer.getAccountByAccountNumber("222222").getBalance();
        assertEquals(customer.getSum(),balance1 + balance2, 0 );
    }
    @Test
    public void isAccountPremium(){
        assertTrue(customer.isPremium());
    }
    @Test
    public void isAccountPremium2(){
        assertTrue(customer2.isPremium());
    }
}