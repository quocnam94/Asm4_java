package model;

import model.ASM2.Account;

public interface ITransfer {
    void transfer(Account receiveAccount, double amount);
}
