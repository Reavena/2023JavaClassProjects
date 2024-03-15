package com.example.bustopaskola.Calculations;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class modelClass {
    private final SimpleIntegerProperty month;
    private final SimpleDoubleProperty monthlyPayment;
    private final SimpleDoubleProperty principal;
    private final SimpleDoubleProperty interest;
    private final SimpleDoubleProperty remainingBalance;

    public modelClass(int month, double monthlyPayment, double principal, double interest, double remainingBalance) {
        this.month = new SimpleIntegerProperty(month);
        this.monthlyPayment = new SimpleDoubleProperty(monthlyPayment);
        this.principal = new SimpleDoubleProperty(principal);
        this.interest = new SimpleDoubleProperty(interest);
        this.remainingBalance = new SimpleDoubleProperty(remainingBalance);
    }

    public int getMonth() {
        return month.get();
    }

    public double getMonthlyPayment() {
        return monthlyPayment.get();
    }

    public double getPrincipal() {
        return principal.get();
    }

    public double getInterest() {
        return interest.get();
    }

    public double getRemainingBalance() {
        return remainingBalance.get();
    }

    public void setMonth(int value) {
        month.set(value);
    }

    public void setMonthlyPayment(double value) {
        monthlyPayment.set(value);
    }

    public void setPrincipal(double value) {
        principal.set(value);
    }

    public void setInterest(double value) {
        interest.set(value);
    }

    public void setRemainingBalance(double value) {
        remainingBalance.set(value);
    }

    public SimpleIntegerProperty monthProperty() {
        return month;
    }

    public SimpleDoubleProperty monthlyPaymentProperty() {
        return monthlyPayment;
    }

    public SimpleDoubleProperty principalProperty() {
        return principal;
    }

    public SimpleDoubleProperty interestProperty() {
        return interest;
    }

    public SimpleDoubleProperty remainingBalanceProperty() {
        return remainingBalance;
    }
}
