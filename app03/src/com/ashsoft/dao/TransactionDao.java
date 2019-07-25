
package com.ashsoft.dao;

public interface TransactionDao {
    public String transferFunds(String fromAccount,int amount,String toAccount);
    public String deposit(String accNo,int dp_Amount);
    public String withDraw(String accNo, int wd_Amount);
}
