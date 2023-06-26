package model;

public interface IWithdraw {
    void withdraw(double amount);
    boolean isAccepted(double amount);
}
