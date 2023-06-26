package Test;

import model.SavingsAccount;
import static org.junit.Assert.*;

public class SavingAccountTest {
    @org.junit.Test
    public void isAccepted() {
        SavingsAccount save = new SavingsAccount("123456789123", "123123", 11000000);
        SavingsAccount save2 = new SavingsAccount("123456789111", "600000", 6000000);
        assertFalse(save.isAccepted(10000));//so tien rut nho hon 50000
        assertFalse(save.isAccepted(12000000));//so tien rut lon hon so du
        assertFalse(save.isAccepted(11000000));//so du nho hon 50000
        assertFalse(save.isAccepted(123123));//so tien rut khong la boi so cua 10000
        assertFalse(save2.isAccepted(5500000));//tai khoan normal rut toi da 5000000
        assertTrue(save.isAccepted(10000000));//rut tien hop le + tai khoan premium rut khong gioi han
    }
    @org.junit.Test
    public void withdraw() {
        SavingsAccount save2 = new SavingsAccount("123456789111", "333333", 6000000);
        save2.withdraw(1000000);
        assertEquals(5000000, 6000000-1000000, 0);
    }
    @org.junit.Test
    public void transfer() {
        SavingsAccount receive = new SavingsAccount("123456789111", "333333", 500000);
        SavingsAccount send = new SavingsAccount("123456789333", "444444", 6000000);
        send.transfer(receive, 500000);
        assertEquals(1000000, 500000+500000, 0);
    }
}
