package Test;

import model.ASM2.Customer;
import model.DigitalBank;
import org.junit.Test;

import static org.junit.Assert.*;

public class DigitalBankTest {
    Customer customer = new Customer("FunIX", "123456789123");
    DigitalBank activeBank = new DigitalBank();
    @Test
    public void getCustomerById() throws Exception{
        activeBank.addCustomers(String.valueOf(customer));
        assertNotNull(activeBank.getCustomerById("123456789123"));
    }
}
